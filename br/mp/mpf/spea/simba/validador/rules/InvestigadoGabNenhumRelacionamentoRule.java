package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.validacao.AbstractRule;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class InvestigadoGabNenhumRelacionamentoRule extends AbstractRule {
   public void validar() {
      String sql = "select distinct (select count(*) from investigado where teve_relacionamento != 1) qntsemrelacionamento, (select count(*) from investigado) qntinvestigados from investigado;";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         rs.next();
         int qntInvestigadosSemRelacionamento = rs.getInt("qntsemrelacionamento");
         int qntInvestigados = rs.getInt("qntinvestigados");
         if (qntInvestigados == qntInvestigadosSemRelacionamento) {
            StringBuilder msg = new StringBuilder();
            msg.append("Não foi encontrado relacionamento no arquivo investigado");
            this.errorReportCmd.report(new ErroValidacao(0L, "Investigado", msg.toString()));
         }

      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
