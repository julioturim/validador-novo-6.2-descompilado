package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarContaSemMovimentacao extends AbstractContaRule {
   public void validar() {
      String sql = "  SELECT CONTA.LINHA_ARQUIVO,      AGENCIA.NUMERO_BANCO,      AGENCIA.NUMERO_AGENCIA,       CONTA.NUMERO_CONTA,      CONTA.TIPO_CONTA FROM AGENCIA INNER JOIN CONTA ON AGENCIA.CODIGO_CHAVE_AGENCIA = CONTA.CODIGO_CHAVE_AGENCIA  WHERE        MOVIMENTACAO_CONTA IN (1,2) ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         if (!rs.next()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Atendimento sem nenhuma movimentação de conta.");
            this.errorReportCmd.report(new ErroValidacao(0L, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
