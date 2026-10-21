package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.spea.simba.validador.to.ExtratoOrigemDestino;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarIdentificacaoTedDocTransfEleitoralRule extends AbstractOrigemDestinoRule {
   public void validar() {
      String sql = "SELECT OD.LINHA_ARQUIVO,\n       EXTRATO.LINHA_ARQUIVO,\n       EXTRATO.DESCRICAO_LANCAMENTO,\n       EXTRATO.TIPO_LANCAMENTO\nFROM ORIGEM_DESTINO OD\nINNER JOIN EXTRATO ON EXTRATO.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO\nWHERE\n    EXTRATO.TIPO_LANCAMENTO IN (117, 120, 209, 213)\n    AND (\n        OD.CPF_CNPJ_PESSOA_OD IS NULL\n        OR OD.NUMERO_BANCO_OD IS NULL\n        OR OD.NUMERO_AGENCIA_OD IS NULL\n        OR OD.NUMERO_CONTA_OD IS NULL\n        OR OD.NUMERO_BANCO_OD = 0\n        OR OD.NUMERO_AGENCIA_OD = 0\n        OR OD.NUMERO_CONTA_OD = REPEAT('0', LENGTH(OD.NUMERO_CONTA_OD))\n    )\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido de transferências no mesmo banco ou ");
            msg.append("transferências interbancárias (TED/DOC) nos campos CPF_CNPJ_OD, NUMERO_BANCO_OD, ");
            msg.append("NUMERO_AGENCIA_OD e NUMERO_CONTA_OD.");
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
      if ((tipoLancamento == 117 || tipoLancamento == 120 || tipoLancamento == 209 || tipoLancamento == 213) && (extratoOrigemDestino.getCpfCnpjPessoaOd() == null || extratoOrigemDestino.getNumeroBancoOd() == null || extratoOrigemDestino.getNumeroAgenciaOd() == null || extratoOrigemDestino.getNumeroContaOd() == null || extratoOrigemDestino.getNumeroBancoOd().matches("0{1,}") || extratoOrigemDestino.getNumeroAgenciaOd().matches("0{1,}") || extratoOrigemDestino.getNumeroContaOd().matches("0{1,}"))) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         StringBuilder msg = new StringBuilder();
         msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido de transferências no mesmo banco ou ");
         msg.append("transferências interbancárias (TED/DOC) nos campos CPF_CNPJ_OD, NUMERO_BANCO_OD, ");
         msg.append("NUMERO_AGENCIA_OD e NUMERO_CONTA_OD.");
         this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
      }

   }
}
