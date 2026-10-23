package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApagarTabelasCmd {
   private static final Logger log = LoggerFactory.getLogger(ApagarTabelasCmd.class);
   @Inject
   private SharedConnection sharedConnection;

   public void apagar() throws SQLException {
      log.debug("Apagando tabelas...");
      Connection cn = this.sharedConnection.get();
      Statement st = cn.createStatement();
      String[] tabelas = new String[]{"TITULARIDADE", "PESSOA", "ORIGEM_DESTINO", "EXTRATO", "CONTA", "AGENCIA"};

      for(String tabela : tabelas) {
         st.execute("truncate table " + tabela);
      }

   }

   public void apagarGab() throws SQLException {
      log.debug("Apagando tabelas gab...");
      Connection cn = this.sharedConnection.get();
      Statement st = cn.createStatement();
      st.execute("truncate table gab109");
      st.execute("truncate table gab112");
      st.execute("truncate table gab800");
      st.execute("truncate table gab109ver2");
      st.execute("truncate table gab112ver2");
      st.execute("truncate table gab800ver2");
   }
}
