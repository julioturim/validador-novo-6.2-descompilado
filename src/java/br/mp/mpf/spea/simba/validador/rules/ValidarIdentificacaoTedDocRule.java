package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.spea.simba.validador.to.ExtratoOrigemDestino;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class ValidarIdentificacaoTedDocRule extends AbstractOrigemDestinoRule {
   private Date dataLimite = (new SimpleDateFormat("dd-MM-yyyy")).parse("01-01-2006");

   public ValidarIdentificacaoTedDocRule() throws ParseException {
   }

   public void validar() {
      String sql = "SELECT OD.LINHA_ARQUIVO, EXTRATO.LINHA_ARQUIVO,\n       EXTRATO.DESCRICAO_LANCAMENTO,\n       EXTRATO.TIPO_LANCAMENTO\nFROM EXTRATO\nINNER JOIN ORIGEM_DESTINO OD ON EXTRATO.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO\nWHERE\n      EXTRATO.TIPO_LANCAMENTO IN (120, 209) AND\n      EXTRATO.DATA_LANCAMENTO >= '2006-01-01'  AND\n      EXTRATO.VALOR_LANCAMENTO >= 1000000  AND\n      (\n            (\n                  OD.CPF_CNPJ_PESSOA_OD IS NULL OR\n                  OD.NOME_PESSOA_OD IS NULL OR\n                  OD.NUMERO_BANCO_OD IS NULL OR\n                  OD.NUMERO_AGENCIA_OD IS NULL OR\n                  OD.NUMERO_CONTA_OD IS NULL OR\n                  OD.NUMERO_BANCO_OD = 0 OR\n                  OD.NUMERO_AGENCIA_OD = 0 OR\n                  OD.NUMERO_CONTA_OD = REPEAT('0', LENGTH( CONCAT(OD.NUMERO_CONTA_OD,'') ))\n            )\n            AND\n            (\n                  (OD.NUMERO_AGENCIA_OD IS NULL OR OD.NUMERO_AGENCIA_OD <> 9999) OR\n                  (OD.NUMERO_CONTA_OD IS NULL OR OD.NUMERO_CONTA_OD <> '99999999999999999999') OR\n                  (\n                      OD.OBSERVACAO_OD IS NULL OR\n                      UCASE(OD.OBSERVACAO_OD) <> 'NAO-CORRENTISTA'\n                  )\n            )\n      )\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido de Transferências Interbancária (TED, DOC) nos ");
            msg.append("campos CPF_CNPJ_OD, NOME_PESSOA_OD, NUMERO_BANCO_OD, NUMERO_AGENCIA_OD e NUMERO_CONTA_OD. ");
            msg.append("Caso o depositante/favorecido não possua conta no banco, os campos NUMERO_AGENCIA_OD, NUMERO_CONTA_OD e OBSERVACAO_OD ");
            msg.append("devem possuir, respectivamente, os seguintes valores: 9999, 99999999999999999999 e NAO-CORRENTISTA.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public void validar(ExtratoOrigemDestino extratoOrigemDestino) {
      int tipoLancamento = extratoOrigemDestino.getTipoLancamento();
      if ((tipoLancamento == 120 || tipoLancamento == 209) && extratoOrigemDestino.getValorLancamento() >= 1000000L && extratoOrigemDestino.getDataLancamento().after(this.dataLimite) && (extratoOrigemDestino.getCpfCnpjPessoaOd() == null || extratoOrigemDestino.isNomePessoaOdNulo() || extratoOrigemDestino.getNumeroBancoOd() == null || extratoOrigemDestino.getNumeroAgenciaOd() == null || extratoOrigemDestino.getNumeroContaOd() == null || extratoOrigemDestino.getNumeroBancoOd().matches("0{1,}") || extratoOrigemDestino.getNumeroAgenciaOd().matches("0{1,}") || extratoOrigemDestino.getNumeroContaOd().matches("0{1,}")) && (extratoOrigemDestino.getNumeroAgenciaOd() == null || extratoOrigemDestino.getNumeroContaOd() == null || !extratoOrigemDestino.getNumeroAgenciaOd().equals("9999") || !extratoOrigemDestino.getNumeroContaOd().equals("99999999999999999999") || extratoOrigemDestino.isObservacaoNaoCorrentistaOdNulo())) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         StringBuilder msg = new StringBuilder();
         msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido de ");
         msg.append("Transferências Interbancária (TED, DOC) nos ");
         msg.append("campos CPF_CNPJ_OD, NOME_PESSOA_OD, NUMERO_BANCO_OD, ");
         msg.append("NUMERO_AGENCIA_OD e NUMERO_CONTA_OD. ");
         msg.append("Caso o depositante/favorecido não possua conta no banco, ");
         msg.append("os campos NUMERO_AGENCIA_OD, NUMERO_CONTA_OD e OBSERVACAO_OD ");
         msg.append("devem possuir, respectivamente, os seguintes valores: 9999, ");
         msg.append("99999999999999999999 e NAO-CORRENTISTA.");
         this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
      }

   }
}
