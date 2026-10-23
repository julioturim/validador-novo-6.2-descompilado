package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarValorExtratoOrigemDestinoRule extends AbstractExtratoRule {
   public void validar() {
      String sql = "WITH VALOR_OD AS (\n    SELECT\n        E.CODIGO_CHAVE_EXTRATO,\n        SUM(OD.VALOR_OPERACAO) SOMA_VALOR_OD\n    FROM EXTRATO E\n    INNER JOIN ORIGEM_DESTINO OD ON E.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO\n    GROUP BY E.CODIGO_CHAVE_EXTRATO\n)\nSELECT\n    V.CODIGO_CHAVE_EXTRATO,\n    E.LINHA_ARQUIVO,\n    E.VALOR_LANCAMENTO,\n    V.SOMA_VALOR_OD\nFROM VALOR_OD V\nJOIN EXTRATO E ON E.CODIGO_CHAVE_EXTRATO = V.CODIGO_CHAVE_EXTRATO\nWHERE E.VALOR_LANCAMENTO <> V.SOMA_VALOR_OD\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O Valor do lancamento em EXTRATO (CODIGO_CHAVE: ");
            msg.append(rs.getLong("CODIGO_CHAVE_EXTRATO"));
            msg.append(") não é igual ao somatório dos registros correspondentes em ORIGEM_DESTINO.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
