package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.util.StringUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarMovimentacaoContaInvestigadoSimRule extends AbstractContaRule {
   public void validar() {
      String sql = "SELECT CONTA.LINHA_ARQUIVO,       AGENCIA.NUMERO_BANCO,       AGENCIA.NUMERO_AGENCIA,       CONTA.NUMERO_CONTA,      CONTA.TIPO_CONTA,      CONTA.MOVIMENTACAO_CONTA FROM AGENCIA INNER JOIN CONTA ON AGENCIA.CODIGO_CHAVE_AGENCIA = CONTA.CODIGO_CHAVE_AGENCIA  INNER JOIN TITULARIDADE TIT_EXT ON CONTA.CODIGO_CHAVE_CONTA = TIT_EXT.CODIGO_CHAVE_CONTA WHERE         TIT_EXT.PESSOA_INVESTIGADA = 1     AND CONTA.MOVIMENTACAO_CONTA NOT IN (1, 2)";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            String banco = StringUtil.completarZerosEsquerda(rs.getString("NUMERO_BANCO"), 3);
            String agencia = StringUtil.completarZerosEsquerda(rs.getString("NUMERO_AGENCIA"), 4);
            String conta = rs.getString("NUMERO_CONTA");
            String tipoConta = rs.getString("TIPO_CONTA");
            StringBuilder msg = new StringBuilder();
            msg.append("Conta com atributo MOVIMENTACAO_CONTA incorreto. Deve ser 1 ou 2, pois é um investigado. NUMERO_BANCO: ");
            msg.append(banco);
            msg.append(", NUMERO_AGENCIA: ");
            msg.append(agencia);
            msg.append(", NUMERO_CONTA: ");
            msg.append(conta);
            msg.append(", TIPO_CONTA: ");
            msg.append(tipoConta);
            msg.append(".");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }
}
