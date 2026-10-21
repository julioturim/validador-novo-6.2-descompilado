package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarInvestigadoCadastroNaoArquivoSimRule extends AbstractTitularesRule {
   public void validar() {
      String sql = "SELECT PESSOA.LINHA_ARQUIVO,      CPF_CNPJ_PESSOA FROM PESSOA INNER JOIN TITULARIDADE ON           PESSOA.CODIGO_CHAVE_PESSOA = TITULARIDADE.CODIGO_CHAVE_PESSOA       AND PESSOA_INVESTIGADA = 1       AND RIGHT(CONCAT('00000000000000', CPF_CNPJ_PESSOA), 14) NOT IN           (               SELECT RIGHT(CONCAT('00000000000000', CPF_CNPJ_PESSOA), 14)                FROM INVESTIGADO                WHERE                     TEVE_RELACIONAMENTO = 1                 AND TEVE_CONTA = 1           )";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O CPF_CNPJ ");
            msg.append(rs.getString("CPF_CNPJ_PESSOA"));
            msg.append(" informado como PESSOA_INVESTIGADA = 1 não foi cadastrado no atendimento.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
