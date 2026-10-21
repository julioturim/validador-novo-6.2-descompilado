package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.spea.simba.validador.to.ExtratoOrigemDestino;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class ValidarIdentificacaoTransacaoEmEspecieRule extends AbstractOrigemDestinoRule {
   private Date dataLimite20130701 = (new SimpleDateFormat("dd-MM-yyyy")).parse("01-07-2013");
   private Date dataLimite20130102 = (new SimpleDateFormat("dd-MM-yyyy")).parse("02-01-2013");
   private Date dataLimite20120701 = (new SimpleDateFormat("dd-MM-yyyy")).parse("01-07-2013");

   public ValidarIdentificacaoTransacaoEmEspecieRule() throws ParseException {
   }

   public void validar() {
      String sql = "  SELECT OD.LINHA_ARQUIVO FROM EXTRATO E INNER JOIN ORIGEM_DESTINO OD ON E.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO WHERE        E.TIPO_LANCAMENTO IN (123, 220) AND        (             (                  E.DATA_LANCAMENTO >= '2013-07-01' AND                  E.VALOR_LANCAMENTO >= 1000000             ) OR             (                  E.DATA_LANCAMENTO >= '2013-01-02' AND                  E.VALOR_LANCAMENTO >= 5000000             ) OR             (                  E.DATA_LANCAMENTO >= '2012-07-01' AND                  E.VALOR_LANCAMENTO >= 10000000             )        ) AND        (             OD.NOME_PESSOA_OD IS NULL AND             (                  OD.OBSERVACAO_OD IS NULL OR                  UCASE(OD.OBSERVACAO_OD) <> '099 - NEGATIVA DE IDENTIFICACAO DO PORTADOR'             )        )";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            StringBuilder msg = new StringBuilder();
            msg.append("É obrigatória a identificação do depositante/favorecido de transações em espécie, campo ");
            msg.append("NOME_PESSOA_OD. Caso o depositante/favorecido recusou se identificar, ");
            msg.append("o campo OBSERVACAO_OD deve conter o seguinte valor: '099 - NEGATIVA DE IDENTIFICACAO DO PORTADOR'.");
            this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public void validar(ExtratoOrigemDestino extratoOrigemDestino) {
      int tipoLancamento = extratoOrigemDestino.getTipoLancamento();
      if ((tipoLancamento == 123 || tipoLancamento == 220) && (extratoOrigemDestino.getDataLancamento().after(this.dataLimite20130701) && extratoOrigemDestino.getValorLancamento() >= 1000000L || extratoOrigemDestino.getDataLancamento().after(this.dataLimite20130102) && extratoOrigemDestino.getValorLancamento() >= 5000000L || extratoOrigemDestino.getDataLancamento().after(this.dataLimite20120701) && extratoOrigemDestino.getValorLancamento() >= 10000000L) && extratoOrigemDestino.isNomePessoaOdNulo() && extratoOrigemDestino.isObservacaoPortadorOdNulo()) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         StringBuilder msg = new StringBuilder();
         msg.append("É OBRIGATÓRIA a identicação do depositante/favorecido ");
         msg.append("de Transferências entre Contas nos campos CPF_CNPJ_OD, ");
         msg.append("NOME_PESSOA_OD, NUMERO_BANCO_OD, NUMERO_AGENCIA_OD e NUMERO_CONTA_OD.");
         this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
      }

   }
}
