package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarGab112InvestigadoNaoInformadoRule extends AbstractGab112Rule {
   @Inject
   private Configurador configurador;

   public void validar() {
      String nomeTabelaGab112 = "";
      switch (this.configurador.getVersaoLayoutGab()) {
         case 1 -> nomeTabelaGab112 = "GAB112";
         case 2 -> nomeTabelaGab112 = "GAB112VER2";
      }

      String sql = "select cpf_cnpj_pessoa\nfrom investigado\nwhere ltrim(cpf_cnpj_pessoa, '0') not in (\n\tselect ltrim(documento_do_cliente, '0')\n\tfrom nomeTabelaGab112\n) and teve_relacionamento = 1\n".replace("nomeTabelaGab112", nomeTabelaGab112);

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            StringBuilder msg = new StringBuilder();
            msg.append("O investigado com CPF/CNPJ ");
            msg.append(rs.getString("cpf_cnpj_pessoa"));
            msg.append(" não foi encontrado no arquivo cadastral(gab112).");
            this.errorReportCmd.report(new ErroValidacao(0L, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
