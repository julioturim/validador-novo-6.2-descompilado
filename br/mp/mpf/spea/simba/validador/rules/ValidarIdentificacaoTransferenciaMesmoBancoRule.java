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
public class ValidarIdentificacaoTransferenciaMesmoBancoRule extends AbstractOrigemDestinoRule {
   private Date dataLimite = (new SimpleDateFormat("dd-MM-yyyy")).parse("01-01-2006");

   public ValidarIdentificacaoTransferenciaMesmoBancoRule() throws ParseException {
   }

   public void validar() {
      String sql = "SELECT OD.LINHA_ARQUIVO,\n       E.LINHA_ARQUIVO,\n       E.DESCRICAO_LANCAMENTO,\n       E.TIPO_LANCAMENTO\nFROM EXTRATO E\nINNER JOIN ORIGEM_DESTINO OD ON E.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO\nWHERE\n    E.TIPO_LANCAMENTO IN (117, 213)\n    AND E.DATA_LANCAMENTO >= '2006-01-01'\n    AND E.VALOR_LANCAMENTO >= 1000000\n    AND (\n        OD.CPF_CNPJ_PESSOA_OD IS NULL\n        OR OD.NOME_PESSOA_OD IS NULL\n        OR OD.NUMERO_BANCO_OD IS NULL\n        OR OD.NUMERO_AGENCIA_OD IS NULL\n        OR OD.NUMERO_CONTA_OD IS NULL\n        OR OD.NUMERO_BANCO_OD = 0\n        OR OD.NUMERO_AGENCIA_OD = 0\n        OR OD.NUMERO_CONTA_OD = REPEAT('0', LENGTH(OD.NUMERO_CONTA_OD))\n    )\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            String msg = "É OBRIGATÓRIA a identicação do depositante/favorecido de Transferências entre Contas nos campos CPF_CNPJ_OD, NOME_PESSOA_OD, NUMERO_BANCO_OD, NUMERO_AGENCIA_OD e NUMERO_CONTA_OD.";
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public void validar(ExtratoOrigemDestino extratoOrigemDestino) {
      int tipoLancamento = extratoOrigemDestino.getTipoLancamento();
      if ((tipoLancamento == 117 || tipoLancamento == 213) && extratoOrigemDestino.getValorLancamento() >= 1000000L && extratoOrigemDestino.getDataLancamento().after(this.dataLimite) && (extratoOrigemDestino.getCpfCnpjPessoaOd() == null || extratoOrigemDestino.isNomePessoaOdNulo() || extratoOrigemDestino.getNumeroBancoOd() == null || extratoOrigemDestino.getNumeroAgenciaOd() == null || extratoOrigemDestino.getNumeroContaOd() == null || extratoOrigemDestino.getNumeroBancoOd().matches("0{1,}") || extratoOrigemDestino.getNumeroAgenciaOd().matches("0{1,}") || extratoOrigemDestino.getNumeroContaOd().matches("0{1,}"))) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         StringBuilder msg = new StringBuilder();
         msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido ");
         msg.append("de Transferências entre Contas nos campos CPF_CNPJ_OD, ");
         msg.append("NOME_PESSOA_OD, NUMERO_BANCO_OD, NUMERO_AGENCIA_OD e NUMERO_CONTA_OD.");
         this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
      }

   }
}
