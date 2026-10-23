package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.util.CpfCnpjUtil;
import br.mp.mpf.pgr.util.StringUtil;
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
public class ValidarCpfCnpjTransferenciaTedDocOrigemDestinoRule extends AbstractOrigemDestinoRule {
   private Date dataLimite = (new SimpleDateFormat("dd-MM-yyyy")).parse("31-12-2005");

   public ValidarCpfCnpjTransferenciaTedDocOrigemDestinoRule() throws ParseException {
   }

   public void validar() {
      String sql = "SELECT OD.LINHA_ARQUIVO,\n       OD.CPF_CNPJ_PESSOA_OD,\n       OD.TIPO_PESSOA_OD\nFROM EXTRATO E\nINNER JOIN ORIGEM_DESTINO OD ON E.CODIGO_CHAVE_EXTRATO = OD.CODIGO_CHAVE_EXTRATO\nWHERE\n       E.TIPO_LANCAMENTO IN (117, 120, 209, 213)\n     AND E.DATA_LANCAMENTO >= '2006-01-01'\n     AND OD.CPF_CNPJ_PESSOA_OD IS NOT NULL\n";

      try {
         Connection cn = this.sharedConnection.get();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery(sql);

         while(rs.next()) {
            int linhaArquivo = rs.getInt("LINHA_ARQUIVO");
            String tipoPessoa = rs.getString("TIPO_PESSOA_OD");
            String cpfCnpj = rs.getString("CPF_CNPJ_PESSOA_OD");
            if ("1".equals(tipoPessoa)) {
               if (!CpfCnpjUtil.validarCpf(cpfCnpj)) {
                  this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
               }
            } else if ("2".equals(tipoPessoa)) {
               cpfCnpj = StringUtil.completarZerosEsquerda(cpfCnpj, 14);
               if (!CpfCnpjUtil.validarCnpj(cpfCnpj)) {
                  this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
               }
            } else {
               cpfCnpj = StringUtil.completarZerosEsquerda(cpfCnpj, 14);
               if (!CpfCnpjUtil.validarCnpj(cpfCnpj)) {
                  cpfCnpj = cpfCnpj.substring(3);
                  if (!CpfCnpjUtil.validarCpf(cpfCnpj)) {
                     this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
                  }
               }
            }
         }

         rs.close();
         stmt.close();
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public void validar(ExtratoOrigemDestino extratoOrigemDestino) {
      int tipoLancamento = extratoOrigemDestino.getTipoLancamento();
      if ((tipoLancamento == 117 || tipoLancamento == 120 || tipoLancamento == 209 || tipoLancamento == 213) && extratoOrigemDestino.getDataLancamento().after(this.dataLimite) && extratoOrigemDestino.getCpfCnpjPessoaOd() == null) {
         int linhaArquivo = extratoOrigemDestino.getLinhaArquivoOd();
         String cpfCnpj = extratoOrigemDestino.getCpfCnpjPessoaOd();
         String tipoPessoa = extratoOrigemDestino.getTipoPessoaOd();
         if ("1".equals(tipoPessoa)) {
            if (!CpfCnpjUtil.validarCpf(cpfCnpj)) {
               this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
            }
         } else if ("2".equals(tipoPessoa)) {
            cpfCnpj = StringUtil.completarZerosEsquerda(cpfCnpj, 14);
            if (!CpfCnpjUtil.validarCnpj(cpfCnpj)) {
               this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
            }
         } else {
            cpfCnpj = StringUtil.completarZerosEsquerda(cpfCnpj, 14);
            if (!CpfCnpjUtil.validarCnpj(cpfCnpj)) {
               cpfCnpj = cpfCnpj.substring(3);
               if (!CpfCnpjUtil.validarCpf(cpfCnpj)) {
                  this.reportarErro(linhaArquivo, tipoPessoa, cpfCnpj);
               }
            }
         }
      }

   }

   private void reportarErro(int linhaArquivo, String tipoPessoa, String cpfCnpj) {
      StringBuilder msg = new StringBuilder();
      msg.append("O campo CPF_CNPJ_PESSOA_OD com o valor ");
      msg.append(cpfCnpj);
      if (StringUtil.isEmpty(tipoPessoa)) {
         msg.append(" é inválido.");
      } else {
         msg.append(" é inválido para TIPO_PESSOA_OD = ");
         msg.append(tipoPessoa);
         msg.append(" (");
         msg.append("1".equals(tipoPessoa) ? "Pessoa Física" : "Pessoa Jurídica");
         msg.append(").");
      }

      this.errorReportCmd.report(new ErroValidacao((long)linhaArquivo, this.nomeArquivo, msg.toString()));
   }
}
