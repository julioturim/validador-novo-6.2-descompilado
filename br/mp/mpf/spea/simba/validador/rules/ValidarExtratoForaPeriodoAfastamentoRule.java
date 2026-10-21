package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class ValidarExtratoForaPeriodoAfastamentoRule extends AbstractExtratoRule {
   public void validar() {
      String sql = "      WITH INV AS (\n          SELECT\n              T.CODIGO_CHAVE_CONTA,\n              I.DATA_INICIO_AFASTAMENTO,\n              I.DATA_FIM_AFASTAMENTO,\n              I.CPF_CNPJ_PESSOA\n          FROM TITULARIDADE T\n          INNER JOIN PESSOA P ON T.CODIGO_CHAVE_PESSOA = P.CODIGO_CHAVE_PESSOA\n          INNER JOIN INVESTIGADO I ON I.CODIGO_INVESTIGADO = P.CODIGO_INVESTIGADO\n          WHERE T.PESSOA_INVESTIGADA = 1\n      )\n      SELECT E.LINHA_ARQUIVO,\n             E.DATA_LANCAMENTO,\n             INV.DATA_INICIO_AFASTAMENTO,\n             INV.DATA_FIM_AFASTAMENTO,\n             INV.CPF_CNPJ_PESSOA\n      FROM EXTRATO E\n      INNER JOIN INV ON E.CODIGO_CHAVE_CONTA = INV.CODIGO_CHAVE_CONTA\n      WHERE E.DATA_LANCAMENTO < INV.DATA_INICIO_AFASTAMENTO OR\n            E.DATA_LANCAMENTO > INV.DATA_FIM_AFASTAMENTO\n";

      try {
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            Date dataLancamento = rs.getDate("DATA_LANCAMENTO");
            Date dataInicioAfastamento = rs.getDate("DATA_INICIO_AFASTAMENTO");
            Date dataFimAfastamento = rs.getDate("DATA_FIM_AFASTAMENTO");
            String cpfCnpj = rs.getString("CPF_CNPJ_PESSOA");
            StringBuilder msg = new StringBuilder();
            msg.append("Lançamento em ");
            msg.append(sdf.format(dataLancamento));
            msg.append(" fora do período de afastamento (");
            msg.append(sdf.format(dataInicioAfastamento));
            msg.append(" a ");
            msg.append(sdf.format(dataFimAfastamento));
            msg.append(") do investigado: CPF_CNPJ (");
            msg.append(cpfCnpj);
            msg.append(")");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
