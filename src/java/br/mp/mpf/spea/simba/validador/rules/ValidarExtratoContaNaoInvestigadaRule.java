package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarExtratoContaNaoInvestigadaRule extends AbstractExtratoRule {
   public void validar() {
      String sql = "SELECT E.LINHA_ARQUIVO\nFROM CONTA C\nINNER JOIN EXTRATO E ON E.CODIGO_CHAVE_CONTA = C.CODIGO_CHAVE_CONTA\nWHERE C.MOVIMENTACAO_CONTA = 3\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("No arquivo CONTAS, se o campo MOVIMENTAÇÃO DA CONTA estiver com o parâmetro \"3\"(conta não investigada) não pode haver nenhum registro para essa conta no arquivo EXTRATO");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
