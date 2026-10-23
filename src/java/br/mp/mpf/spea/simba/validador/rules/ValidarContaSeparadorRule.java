package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarContaSeparadorRule extends AbstractContaRule {
   public void validar() {
      String sql = "  SELECT NUMERO_CONTA, LINHA_ARQUIVO FROM CONTA";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            String numeroConta = rs.getString("NUMERO_CONTA");
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            if (numeroConta.contains(".") || numeroConta.contains("/") || numeroConta.contains("\\") || numeroConta.contains("-")) {
               StringBuilder msg = new StringBuilder();
               msg.append("Conta com separador: ");
               msg.append(numeroConta);
               this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
            }
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
