package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarCasoPossuiInvestigadoComRelacionamentoRule {
   @Inject
   protected SharedConnection sharedConnection;

   public boolean validar() {
      String sql = "select count(*) from investigado where teve_relacionamento = 1;";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         rs.next();
         int qntInvestigadosComRelacionamento = rs.getInt(1);
         return qntInvestigadosComRelacionamento > 0;
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
