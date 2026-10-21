package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarExtratoSemOrigemDestinoRule extends AbstractExtratoRule {
   public void validar() {
      String sql = "SELECT EXTRATO.LINHA_ARQUIVO,\n       EXTRATO.CODIGO_CHAVE_EXTRATO\nFROM EXTRATO\nLEFT JOIN ORIGEM_DESTINO ON EXTRATO.CODIGO_CHAVE_EXTRATO = ORIGEM_DESTINO.CODIGO_CHAVE_EXTRATO\nWHERE ORIGEM_DESTINO.CODIGO_CHAVE_EXTRATO IS NULL\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("Lançamento em EXTRATO sem correspondente em ORIGEM_DESTINO. CODIGO_CHAVE_EXTRATO: ");
            msg.append(rs.getLong("CODIGO_CHAVE_EXTRATO"));
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
