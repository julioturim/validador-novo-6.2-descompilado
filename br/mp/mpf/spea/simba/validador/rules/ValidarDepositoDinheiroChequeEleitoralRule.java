package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.spea.simba.validador.to.ExtratoOrigemDestino;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

@Component
public class ValidarDepositoDinheiroChequeEleitoralRule extends AbstractOrigemDestinoRule {
   public void validar() {
      String sql = "  SELECT OD.LINHA_ARQUIVO FROM EXTRATO E INNER JOIN ORIGEM_DESTINO OD ON E.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO WHERE       E.TIPO_LANCAMENTO = 201   AND E.VALOR_LANCAMENTO >= 10000   AND OD.CPF_CNPJ_PESSOA_OD IS NULL ";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            String msg = "É OBRIGATÓRIA a identicação do depositante/favorecido de depósito em cheque e/ou dinheiro no campo CPF_CNPJ_OD.";
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public void validar(ExtratoOrigemDestino extratoOrigemDestino) {
      int tipoLancamento = extratoOrigemDestino.getTipoLancamento();
      if (tipoLancamento == 201 && extratoOrigemDestino.getCpfCnpjPessoaOd() != null && extratoOrigemDestino.getValorLancamento() >= 10000L) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         String msg = "É OBRIGATÓRIA a identicação do depositante/favorecido de depósito em cheque e/ou dinheiro no campo CPF_CNPJ_OD.";
         this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg));
      }

   }
}
