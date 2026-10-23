package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarDadosObrigatoriosTitularRule extends AbstractTitularesRule {
   public void validar() {
      String sql = "  SELECT LINHA_ARQUIVO FROM PESSOA INNER JOIN TITULARIDADE ON PESSOA.CODIGO_CHAVE_PESSOA=TITULARIDADE.CODIGO_CHAVE_PESSOA WHERE PESSOA_INVESTIGADA = 1       AND TIPO_TITULAR = 'T'       AND (            NOME_DOC_IDENTIFICACAO IS NULL          OR NUMERO_DOC_IDENTIFICACAO IS NULL          OR ENDERECO_LOGRADOURO IS NULL          OR ENDERECO_CIDADE IS NULL          OR ENDERECO_UF IS NULL          OR ENDERECO_PAIS IS NULL          OR ENDERECO_CEP IS NULL          OR TELEFONE_PESSOA IS NULL      )";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("O titular de conta investigada deve ter os seguintes dados preenchidos: TIPO_PESSOA, CPF_CNPJ, NOME_PESSOA, NOME_DOC_IDENTIFICACAO, ");
            msg.append("NUMERO_DOC_IDENTIFICACAO, ENDERECO_LOGRADOURO, CIDADE, UF, PAIS, CEP, TELEFONE_PESSOA");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
