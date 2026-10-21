package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarDataAberturaRelacionamentoTitularidadeRule extends AbstractTitularesRule {
   public void validar() {
      String sql = "   SELECT TITULARIDADE.LINHA_ARQUIVO,         CONTA.DATA_ABERTURA_CONTA FROM TITULARIDADE INNER JOIN CONTA ON TITULARIDADE.CODIGO_CHAVE_CONTA = CONTA.CODIGO_CHAVE_CONTA WHERE      TITULARIDADE.DATA_INICIO_RELACIONAMENTO < CONTA.DATA_ABERTURA_CONTA   OR TITULARIDADE.DATA_FIM_RELACIONAMENTO < CONTA.DATA_ABERTURA_CONTA ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("A DATA_INICIO_RELACIONAMENTO ou DATA_FIM_RELACIONAMENTO está menor que a DATA_ABERTURA_CONTA (");
            msg.append(rs.getString("DATA_ABERTURA_CONTA"));
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
