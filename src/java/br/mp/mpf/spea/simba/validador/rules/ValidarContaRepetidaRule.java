package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.validacao.AbstractRule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarContaRepetidaRule extends AbstractRule {
   public void validar() {
      String sql = "  SELECT CODIGO_CHAVE_AGENCIA,       NUMERO_CONTA,       TIPO_CONTA,       COUNT(*) QTD,       MAX(LINHA_ARQUIVO) LINHA_ARQUIVO FROM CONTA GROUP BY CODIGO_CHAVE_AGENCIA, NUMERO_CONTA, TIPO_CONTA HAVING COUNT(*) > 1";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         String sqlAgencia = "  SELECT NUMERO_BANCO, NUMERO_AGENCIA FROM AGENCIA WHERE CODIGO_CHAVE_AGENCIA = ?";
         PreparedStatement ps = cn.prepareStatement(sqlAgencia);

         while(rs.next()) {
            ps.clearParameters();
            ps.setLong(1, rs.getLong("CODIGO_CHAVE_AGENCIA"));
            ResultSet rsAgencia = ps.executeQuery();
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("Conta duplicada - Linha: ");
            msg.append(linhaArquivo);
            if (rsAgencia.next()) {
               msg.append(", NUMERO_AGENCIA: ");
               msg.append(rsAgencia.getString("NUMERO_AGENCIA"));
            }

            msg.append(", NUMERO_CONTA: ");
            msg.append(rs.getString("NUMERO_CONTA"));
            msg.append(", TIPO_CONTA: ");
            msg.append(rs.getString("TIPO_CONTA"));
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
            rsAgencia.close();
         }

         ps.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
