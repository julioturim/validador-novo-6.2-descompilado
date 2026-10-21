package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarGab112NaoGab109SimRule extends AbstractGab109Rule {
   @Inject
   private Configurador configurador;

   public void validar() {
      String nomeTabelaGab109 = "";
      String nomeTabelaGab112 = "";
      switch (this.configurador.getVersaoLayoutGab()) {
         case 1:
            nomeTabelaGab109 = "GAB109";
            nomeTabelaGab112 = "GAB112";
            break;
         case 2:
            nomeTabelaGab109 = "GAB109VER2";
            nomeTabelaGab112 = "GAB112VER2";
      }

      String sql = "select linha_arquivo, codigo_do_cliente from " + nomeTabelaGab109 + " where codigo_do_cliente not in (select " + nomeTabelaGab112 + ".codigo_do_cliente from " + nomeTabelaGab112 + ")";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O cliente com o código ");
            msg.append(rs.getString("codigo_do_cliente"));
            msg.append(" não foi informado no arquivo gab112.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
