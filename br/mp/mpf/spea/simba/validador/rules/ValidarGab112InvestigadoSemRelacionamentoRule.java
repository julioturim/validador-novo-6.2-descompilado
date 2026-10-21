package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.spea.simba.validador.enums.CamposGab112;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarGab112InvestigadoSemRelacionamentoRule extends AbstractGab112Rule {
   @Inject
   private Configurador configurador;

   public void validar() {
      String nomeTabelaGab112 = "";
      switch (this.configurador.getVersaoLayoutGab()) {
         case 1 -> nomeTabelaGab112 = "GAB112";
         case 2 -> nomeTabelaGab112 = "GAB112VER2";
      }

      String sql = "select linha_arquivo, DOCUMENTO_DO_CLIENTE\nfrom nomeTabelaGab112\nwhere ltrim(DOCUMENTO_DO_CLIENTE, '0') not in (\n\tselect ltrim(cpf_cnpj_pessoa, '0')\n\tfrom investigado\n)\n".replace("nomeTabelaGab112", nomeTabelaGab112);

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O CPF/CNPJ ");
            msg.append(rs.getString(CamposGab112.DOCUMENTO_DO_CLIENTE.toString()));
            msg.append(" não teve relacionamento com esta instituição no período do afastamento de sigilo.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
