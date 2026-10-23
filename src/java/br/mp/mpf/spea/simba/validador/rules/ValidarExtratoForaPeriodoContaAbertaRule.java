package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class ValidarExtratoForaPeriodoContaAbertaRule extends AbstractExtratoRule {
   public void validar() {
      String sql = "      SELECT EXTRATO.LINHA_ARQUIVO,\n             EXTRATO.DATA_LANCAMENTO,\n             CONTA.DATA_ABERTURA_CONTA,\n             CONTA.DATA_ENCERRAMENTO_CONTA\n      FROM EXTRATO\n      INNER JOIN CONTA ON EXTRATO.CODIGO_CHAVE_CONTA = CONTA.CODIGO_CHAVE_CONTA\n      WHERE EXTRATO.DATA_LANCAMENTO < CONTA.DATA_ABERTURA_CONTA\n         OR EXTRATO.DATA_LANCAMENTO > CONTA.DATA_ENCERRAMENTO_CONTA\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            Date dataLancamento = rs.getDate("DATA_LANCAMENTO");
            Date dataAberturaConta = rs.getDate("DATA_ABERTURA_CONTA");
            Date dataEncerramentoConta = rs.getDate("DATA_ENCERRAMENTO_CONTA");
            StringBuilder msg = new StringBuilder();
            msg.append("A DATA_LANCAMENTO (");
            msg.append(sdf.format(dataLancamento));
            msg.append(") está fora do período de DATA_ABERTURA_CONTA (");
            msg.append(sdf.format(dataAberturaConta));
            if (dataEncerramentoConta != null) {
               msg.append(") e DATA_ENCERRAMENTO_CONTA (");
               msg.append(sdf.format(dataEncerramentoConta));
            } else {
               msg.append(") e sem DATA_ENCERRAMENTO_CONTA");
            }

            msg.append(").");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
