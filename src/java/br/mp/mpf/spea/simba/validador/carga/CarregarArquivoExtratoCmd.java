package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Extrato;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.rules.CnabRule;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoExtratoCmd extends AbstractCarregarArquivoCmd<Extrato> {
   private final ContaLocalizerCmd contaLocalizer;
   private CnabRule cnabRule;

   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("CODIGO_CHAVE_EXTRATO", TipoCampoArquivo.NUMERO, 18), CampoArquivo.from("NUMERO_BANCO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NUMERO_CONTA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("TIPO_CONTA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("DATA_LANCAMENTO", TipoCampoArquivo.DATA, 8), CampoArquivo.from("NUMERO_DOCUMENTO", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("DESCRICAO_LANCAMENTO", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("TIPO_LANCAMENTO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("VALOR_LANCAMENTO", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NATUREZA_LANCAMENTO", TipoCampoArquivo.TEXTO, 1), CampoArquivo.from("VALOR_SALDO", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NATUREZA_SALDO", TipoCampoArquivo.TEXTO, 1), CampoArquivo.from("LOCAL_TRANSACAO", TipoCampoArquivo.TEXTO, 80)};
   }

   @Inject
   public CarregarArquivoExtratoCmd(ContaLocalizerCmd contaLocalizer, SharedConnection sharedConnection, ErrorReportCmd errorReportCmd, CnabRule cnabRule) throws SQLException {
      super(sharedConnection, errorReportCmd);
      this.contaLocalizer = contaLocalizer;
      this.cnabRule = cnabRule;
   }

   protected String getNomeTabela() {
      return "EXTRATO";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.EXTRATO.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarArquivoExtratoCmd.CamposExtrato.values();
   }

   protected int getQuantidadeColunas() {
      return 14;
   }

   protected Extrato createDomain() {
      return new Extrato();
   }

   protected Extrato convert(String[] dados, int numeroLinha) {
      ((Extrato)this.domain).clear();
      ((Extrato)this.domain).setCodigoChave(this.getLong(dados[0], numeroLinha, "CODIGO_CHAVE_EXTRATO"));
      ((Extrato)this.domain).setData(dados[5]);
      ((Extrato)this.domain).setNumeroDocumento(dados[6]);
      ((Extrato)this.domain).setDescricao(dados[7]);
      ((Extrato)this.domain).setTipoLancamento(this.getInt(dados[8], numeroLinha, "TIPO_LANCAMENTO"));
      ((Extrato)this.domain).setValor(this.getLong(dados[9], numeroLinha, "VALOR_LANCAMENTO"));
      ((Extrato)this.domain).setNaturezaLancamento(dados[10]);
      ((Extrato)this.domain).setValorSaldo(this.getLong(dados[11], numeroLinha, "VALOR_SALDO"));
      ((Extrato)this.domain).setNaturezaSaldo(dados[12]);
      ((Extrato)this.domain).setLocalTransacao(dados[13]);
      ((Extrato)this.domain).setLinha((long)numeroLinha);
      long idConta = this.contaLocalizer.findIdConta(dados[1], dados[2], dados[3], dados[4]);
      ((Extrato)this.domain).setIdConta(idConta);
      return this.domain;
   }

   protected void setParametros(Extrato extrato) throws SQLException, ParseException {
      this.setLong(CarregarArquivoExtratoCmd.CamposExtrato.CODIGO_CHAVE_EXTRATO.ordinal(), extrato.getCodigoChave());
      this.setLong(CarregarArquivoExtratoCmd.CamposExtrato.CODIGO_CHAVE_CONTA.ordinal(), extrato.getIdConta());
      this.setDate(CarregarArquivoExtratoCmd.CamposExtrato.DATA_LANCAMENTO.ordinal(), Util.getDate(extrato.getData()));
      this.setString(CarregarArquivoExtratoCmd.CamposExtrato.NUMERO_DOCUMENTO.ordinal(), extrato.getNumeroDocumento());
      this.setString(CarregarArquivoExtratoCmd.CamposExtrato.DESCRICAO_LANCAMENTO.ordinal(), extrato.getDescricao());
      this.setInt(CarregarArquivoExtratoCmd.CamposExtrato.TIPO_LANCAMENTO.ordinal(), extrato.getTipoLancamento());
      this.setLong(CarregarArquivoExtratoCmd.CamposExtrato.VALOR_LANCAMENTO.ordinal(), extrato.getValor());
      this.setString(CarregarArquivoExtratoCmd.CamposExtrato.NATUREZA_LANCAMENTO.ordinal(), extrato.getNaturezaLancamento());
      this.setLong(CarregarArquivoExtratoCmd.CamposExtrato.VALOR_SALDO.ordinal(), extrato.getValorSaldo());
      this.setString(CarregarArquivoExtratoCmd.CamposExtrato.NATUREZA_SALDO.ordinal(), extrato.getNaturezaSaldo());
      this.setString(CarregarArquivoExtratoCmd.CamposExtrato.LOCAL_TRANSACAO.ordinal(), extrato.getLocalTransacao());
      this.setLong(CarregarArquivoExtratoCmd.CamposExtrato.LINHA_ARQUIVO.ordinal(), extrato.getLinha());
   }

   protected void validar(Extrato extrato) {
      this.validarIdConta(extrato);
      this.validarCamposObrigatorios(extrato);
      this.validarDataLancamento(extrato);
      this.validarNaturezaLancamento(extrato);
      this.validarNaturezaSaldo(extrato);
      this.validarTipoLancamentoCredito(extrato);
      this.validarTipoLancamentoDebito(extrato);
      this.validarTipoLancamentoAsterisco(extrato);
      this.validarLancamentoSaldoNegativo(extrato);
   }

   protected void validarIdConta(Extrato extrato) {
      if (extrato.getIdConta() <= 0L) {
         this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "Conta não localizada no arquivo de CONTAS."));
      }

   }

   protected void validarCamposObrigatorios(Extrato extrato) {
      if (StringUtil.isEmpty(extrato.getData()) || StringUtil.isEmpty(extrato.getNumeroDocumento()) || StringUtil.isEmpty(extrato.getDescricao()) || extrato.getNaturezaLancamento() == null || extrato.getNaturezaSaldo() == null) {
         String camposObrigatorios = "DATA_LANCAMENTO, NUMERO_DOCUMENTO, DESCRICAO_LANCAMENTO, TIPO_LANCAMENTO, VALOR_LANCAMENTO, NATUREZA_LANCAMENTO, VALOR_SALDO, NATUREZA_SALDO";
         this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
      }

   }

   protected void validarNaturezaLancamento(Extrato extrato) {
      if (extrato.getNaturezaLancamento() != null) {
         if (!extrato.getNaturezaLancamento().matches("[CD\\*]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "Natureza de Lancamento inválida. Valores possíveis: C, D e *(asterisco)."));
         }

      }
   }

   protected void validarNaturezaSaldo(Extrato extrato) {
      if (extrato.getNaturezaSaldo() != null) {
         if (!extrato.getNaturezaSaldo().matches("[CD]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "Natureza de saldo inválida. Valores possíveis: C e D."));
         }

      }
   }

   protected void validarTipoLancamentoCredito(Extrato extrato) {
      if ("C".equals(extrato.getNaturezaLancamento())) {
         int tipoLancamento = extrato.getTipoLancamento();
         if (this.cnabRule.validarCnabCredito(tipoLancamento)) {
            this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "TIPO_LANCAMENTO inválido para NATUREZA_LANCAMENTO='C'. Veja tabela CNAB-240."));
         }
      }

   }

   protected void validarTipoLancamentoDebito(Extrato extrato) {
      if ("D".equals(extrato.getNaturezaLancamento())) {
         int tipoLancamento = extrato.getTipoLancamento();
         if (this.cnabRule.validarCnabDebito(tipoLancamento)) {
            this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "TIPO_LANCAMENTO inválido para NATUREZA_LANCAMENTO='D'. Veja tabela CNAB-240."));
         }
      }

   }

   protected void validarTipoLancamentoAsterisco(Extrato extrato) {
      if ("*".equals(extrato.getNaturezaLancamento())) {
         int tipoLancamento = extrato.getTipoLancamento();
         if (this.cnabRule.validarCnabCreditoDebito(tipoLancamento)) {
            this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "TIPO_LANCAMENTO inválido para NATUREZA_LANCAMENTO='*'. Veja tabela CNAB-240."));
         }
      }

   }

   protected void validarLancamentoSaldoNegativo(Extrato extrato) {
      if (extrato.getValor() < 0L || extrato.getValorSaldo() < 0L) {
         this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "VALOR_LANCAMENTO e VALOR_SALDO não podem ser negativos."));
      }

   }

   protected void validarDataLancamento(Extrato extrato) {
      if (!StringUtil.isEmpty(extrato.getData()) && !br.mp.mpf.asspa.client.Util.verificaData(extrato.getData())) {
         this.errorReportCmd.report(new ErroValidacao(extrato.getLinha(), NomeArquivo.EXTRATO.toString(), "A DATA_LANCAMENTO (" + extrato.getData() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   public static enum CamposExtrato {
      CAMPO_NAO_UTILIZADO,
      CODIGO_CHAVE_EXTRATO,
      CODIGO_CHAVE_CONTA,
      DATA_LANCAMENTO,
      NUMERO_DOCUMENTO,
      DESCRICAO_LANCAMENTO,
      TIPO_LANCAMENTO,
      VALOR_LANCAMENTO,
      NATUREZA_LANCAMENTO,
      VALOR_SALDO,
      NATUREZA_SALDO,
      LOCAL_TRANSACAO,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposExtrato[] $values() {
         return new CamposExtrato[]{CAMPO_NAO_UTILIZADO, CODIGO_CHAVE_EXTRATO, CODIGO_CHAVE_CONTA, DATA_LANCAMENTO, NUMERO_DOCUMENTO, DESCRICAO_LANCAMENTO, TIPO_LANCAMENTO, VALOR_LANCAMENTO, NATUREZA_LANCAMENTO, VALOR_SALDO, NATUREZA_SALDO, LOCAL_TRANSACAO, LINHA_ARQUIVO};
      }
   }
}
