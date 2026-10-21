package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarAgenciaRepetidaRule extends AbstractAgenciaRule {
   public void validar() {
      String sql = "  SELECT RIGHT(CONCAT('000', NUMERO_BANCO), 3) AS NUM_BANCO,      RIGHT(CONCAT('0000', NUMERO_AGENCIA), 4) AS NUM_AGENCIA,      COUNT(*) QTD,      MAX(LINHA_ARQUIVO) LINHA_ARQUIVO FROM AGENCIA GROUP BY NUMERO_BANCO, NUMERO_AGENCIA HAVING COUNT(*) > 1 ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("Agência duplicada: NUMERO_BANCO: ");
            msg.append(rs.getString(1));
            msg.append(", NUMERO_AGENCIA: ");
            msg.append(rs.getString(2));
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
