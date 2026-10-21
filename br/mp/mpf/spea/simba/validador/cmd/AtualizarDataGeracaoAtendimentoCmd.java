package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.ConnectionManager;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class AtualizarDataGeracaoAtendimentoCmd {
   @Inject
   private ConnectionManager connectionManager;

   public void atualizar(Atendimento atendimento, LocalDate data) {
      String sql = "update atendimento set data_geracao_arquivo = ?";
      Connection connection = this.connectionManager.get();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);
         stmt.setDate(1, Date.valueOf(data));
         stmt.execute();
      } catch (Exception e) {
         try {
            connection.rollback();
         } catch (SQLException var7) {
            throw new InfraestruturaException("Erro durante rollback", e);
         }
      }

   }
}
