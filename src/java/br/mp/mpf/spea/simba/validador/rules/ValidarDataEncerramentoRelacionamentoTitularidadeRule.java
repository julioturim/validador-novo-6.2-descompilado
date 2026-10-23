package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarDataEncerramentoRelacionamentoTitularidadeRule extends AbstractTitularesRule {
   public void validar() {
      String sql = "  SELECT TITULARIDADE.LINHA_ARQUIVO,        CONTA.DATA_ENCERRAMENTO_CONTA FROM TITULARIDADE INNER JOIN CONTA ON TITULARIDADE.CODIGO_CHAVE_CONTA = CONTA.CODIGO_CHAVE_CONTA WHERE      TITULARIDADE.DATA_INICIO_RELACIONAMENTO > CONTA.DATA_ENCERRAMENTO_CONTA   OR TITULARIDADE.DATA_FIM_RELACIONAMENTO > CONTA.DATA_ENCERRAMENTO_CONTA ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("A DATA_INICIO_RELACIONAMENTO ou DATA_FIM_RELACIONAMENTO está maior que a DATA_ENCERRAMENTO_CONTA (");
            msg.append(rs.getString("DATA_ENCERRAMENTO_CONTA"));
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
