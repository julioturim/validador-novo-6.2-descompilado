package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarInvestigadoPessoaInvestigada0Rule extends AbstractTitularesRule {
   public void validar() {
      String sql = "SELECT PESSOA.LINHA_ARQUIVO,      PESSOA.CPF_CNPJ_PESSOA FROM INVESTIGADO INNER JOIN PESSOA ON PESSOA.CODIGO_INVESTIGADO = INVESTIGADO.CODIGO_INVESTIGADO INNER JOIN TITULARIDADE ON PESSOA.CODIGO_CHAVE_PESSOA = TITULARIDADE.CODIGO_CHAVE_PESSOA WHERE       INVESTIGADO.TEVE_RELACIONAMENTO = 1   AND INVESTIGADO.TEVE_CONTA = 1   AND TITULARIDADE.PESSOA_INVESTIGADA = 0 ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O CPF_CNPJ ");
            msg.append(rs.getString("CPF_CNPJ_PESSOA"));
            msg.append(" cadastrado no atendimento como investigado, deve possuir o atributo PESSOA_INVESTIGADA = 1");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
