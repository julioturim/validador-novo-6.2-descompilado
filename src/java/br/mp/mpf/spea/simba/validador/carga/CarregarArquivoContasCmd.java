package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Conta;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoContasCmd extends AbstractCarregarArquivoCmd<Conta> {
   private final AgenciaLocalizerCmd agenciaLocalizer;

   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("NUMERO_BANCO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NUMERO_CONTA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("TIPO_CONTA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("DATA_ABERTURA_CONTA", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_ENCERRAMENTO_CONTA", TipoCampoArquivo.DATA, 8), CampoArquivo.from("MOVIMENTACAO_CONTA", TipoCampoArquivo.NUMERO, 1)};
   }

   @Inject
   public CarregarArquivoContasCmd(AgenciaLocalizerCmd agenciaLocalizer, SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
      this.agenciaLocalizer = agenciaLocalizer;
   }

   protected String getNomeTabela() {
      return "CONTA";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.CONTAS.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarArquivoContasCmd.CamposConta.values();
   }

   protected int getQuantidadeColunas() {
      return 7;
   }

   protected Conta createDomain() {
      return new Conta();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabela(this.con, "CONTAS", quantidadeLinha);
   }

   protected Conta convert(String[] dados, int numeroLinha) throws SQLException {
      ((Conta)this.domain).clear();
      ((Conta)this.domain).setNumeroBanco(dados[0]);
      ((Conta)this.domain).setNumeroAgencia(dados[1]);
      ((Conta)this.domain).setNumeroConta(dados[2]);
      ((Conta)this.domain).setTipoConta(dados[3]);
      ((Conta)this.domain).setDataAberturaConta(dados[4]);
      ((Conta)this.domain).setDataFechamentoConta(dados[5]);
      ((Conta)this.domain).setMovimentacaoConta(dados[6]);
      ((Conta)this.domain).setLinha((long)numeroLinha);
      long idAgencia = this.agenciaLocalizer.findIdAgencia(((Conta)this.domain).getNumeroBanco(), ((Conta)this.domain).getNumeroAgencia());
      if (idAgencia == -1L) {
         String var10002 = ((Conta)this.domain).getNumeroBanco();
         throw new NegocioException("Agencia não localizada. Banco: " + var10002 + " Agencia: " + ((Conta)this.domain).getNumeroAgencia());
      } else {
         ((Conta)this.domain).setIdAgencia(idAgencia);
         return this.domain;
      }
   }

   protected void setParametros(Conta conta) throws SQLException, ParseException {
      this.setLong(CarregarArquivoContasCmd.CamposConta.CODIGO_CHAVE_AGENCIA.ordinal(), conta.getIdAgencia());
      this.setInt(CarregarArquivoContasCmd.CamposConta.TIPO_CONTA.ordinal(), conta.getTipoConta());
      this.setString(CarregarArquivoContasCmd.CamposConta.NUMERO_CONTA.ordinal(), conta.getNumeroConta());
      this.setDate(CarregarArquivoContasCmd.CamposConta.DATA_ABERTURA_CONTA.ordinal(), Util.getDate(conta.getDataAberturaConta()));
      this.setDate(CarregarArquivoContasCmd.CamposConta.DATA_ENCERRAMENTO_CONTA.ordinal(), Util.getDate(conta.getDataFechamentoConta()));
      this.setInt(CarregarArquivoContasCmd.CamposConta.MOVIMENTACAO_CONTA.ordinal(), conta.getMovimentacaoConta());
      this.setLong(CarregarArquivoContasCmd.CamposConta.LINHA_ARQUIVO.ordinal(), conta.getLinha());
   }

   protected void validar(Conta conta) {
      this.validarCamposObrigatorios(conta);
      this.validarDataAberturaConta(conta);
      this.validarDataEncerramentoConta(conta);
      this.validarTipoConta(conta);
      this.validarMovimentacaoConta(conta);
   }

   protected void validarCamposObrigatorios(Conta conta) {
      if (StringUtil.isEmpty(conta.getNumeroBanco()) || StringUtil.isEmpty(conta.getNumeroConta()) || StringUtil.isEmpty(conta.getTipoConta()) || StringUtil.isEmpty(conta.getMovimentacaoConta())) {
         String camposObrigatorios = "NUMERO_CONTA, TIPO_CONTA, MOVIMENTACAO_CONTA";
         this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
      }

   }

   protected void validarTipoConta(Conta conta) {
      try {
         int tipoConta = Integer.valueOf(conta.getTipoConta());
         if (tipoConta < 1 || tipoConta > 4) {
            this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "Tipo de conta inválido. Valores possíveis: 1, 2, 3 e 4."));
         }
      } catch (NumberFormatException var3) {
         this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "Tipo de conta inválido. Valores possíveis: 1, 2, 3 e 4."));
      }

   }

   protected void validarMovimentacaoConta(Conta conta) {
      try {
         int movimentacaoConta = Integer.valueOf(conta.getMovimentacaoConta());
         if (movimentacaoConta < 1 || movimentacaoConta > 3) {
            this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "Movimentação de Conta inválido. Valores possíveis: 1, 2 e 3"));
         }
      } catch (NumberFormatException var3) {
         this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "Movimentação de Conta inválido. Valores possíveis: 1, 2 e 3"));
      }

   }

   protected void validarDataAberturaConta(Conta conta) {
      if (!StringUtil.isEmpty(conta.getDataAberturaConta()) && !br.mp.mpf.asspa.client.Util.verificaData(conta.getDataAberturaConta())) {
         this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "A DATA_ABERTURA_CONTA (" + conta.getDataAberturaConta() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   protected void validarDataEncerramentoConta(Conta conta) {
      if (!StringUtil.isEmpty(conta.getDataFechamentoConta()) && !br.mp.mpf.asspa.client.Util.verificaData(conta.getDataFechamentoConta())) {
         this.errorReportCmd.report(new ErroValidacao(conta.getLinha(), NomeArquivo.CONTAS.toString(), "A DATA_ENCERRAMENTO_CONTA (" + conta.getDataFechamentoConta() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   public static enum CamposConta {
      CAMPO_NAO_UTILIZADO,
      CODIGO_CHAVE_AGENCIA,
      TIPO_CONTA,
      NUMERO_CONTA,
      DATA_ABERTURA_CONTA,
      DATA_ENCERRAMENTO_CONTA,
      MOVIMENTACAO_CONTA,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposConta[] $values() {
         return new CamposConta[]{CAMPO_NAO_UTILIZADO, CODIGO_CHAVE_AGENCIA, TIPO_CONTA, NUMERO_CONTA, DATA_ABERTURA_CONTA, DATA_ENCERRAMENTO_CONTA, MOVIMENTACAO_CONTA, LINHA_ARQUIVO};
      }
   }
}
