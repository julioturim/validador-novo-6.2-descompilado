package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Gab109Ver2;
import br.mp.mpf.spea.simba.validador.enums.CamposGab109Ver2;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.enums.TipoLancamento;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoGab109Ver2Cmd extends AbstractCarregarArquivoCmd<Gab109Ver2> {
   @Inject
   public CarregarArquivoGab109Ver2Cmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return Gab109Ver2.class.getSimpleName();
   }

   protected String getNomeArquivo() {
      return NomeArquivoGab.GAB109.name();
   }

   protected Object[] getFieldEnums() {
      return CamposGab109Ver2.values();
   }

   protected int getQuantidadeColunas() {
      return CamposGab109Ver2.values().length - 1;
   }

   protected Gab109Ver2 createDomain() {
      return new Gab109Ver2();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabelaGab(this.con, this.getNomeTabela(), quantidadeLinha);
   }

   protected Gab109Ver2 convert(String[] dados, int numeroLinha) {
      ((Gab109Ver2)this.domain).setDataMovimento(dados[0]);
      ((Gab109Ver2)this.domain).setDataReferencia(dados[1]);
      ((Gab109Ver2)this.domain).setCodigoCliente(dados[2]);
      ((Gab109Ver2)this.domain).setDataLiquidacao(dados[3]);
      ((Gab109Ver2)this.domain).setDescricaoLancamento(dados[4]);
      ((Gab109Ver2)this.domain).setValorLancamento(dados[5]);
      ((Gab109Ver2)this.domain).setNaturezaLancamento(dados[6]);
      ((Gab109Ver2)this.domain).setChaveOD(dados[7]);
      ((Gab109Ver2)this.domain).setBancoOrigem(dados[8]);
      ((Gab109Ver2)this.domain).setAgenciaOrigem(dados[9]);
      ((Gab109Ver2)this.domain).setContaOrigem(dados[10]);
      ((Gab109Ver2)this.domain).setCpfCnpjOrigem(dados[11]);
      ((Gab109Ver2)this.domain).setBancoDestino(dados[12]);
      ((Gab109Ver2)this.domain).setAgenciaDestino(dados[13]);
      ((Gab109Ver2)this.domain).setContaDestino(dados[14]);
      ((Gab109Ver2)this.domain).setCpfCnpjDestino(dados[15]);
      ((Gab109Ver2)this.domain).setObservacao(dados[16]);
      ((Gab109Ver2)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void validar(Gab109Ver2 gab109) {
      this.validarObrigatoriedade(gab109);
      this.validarData(gab109);
      this.validarNumero(gab109);
      this.validarDadoEsperado(gab109);
   }

   protected void setParametros(Gab109Ver2 gab109) throws SQLException, ParseException {
      this.setDate(CamposGab109Ver2.DATA_DO_MOVIMENTO.getCodigoCampo(), Util.getDateGabSql(gab109.getDataMovimento()));
      this.setDate(CamposGab109Ver2.DATA_DE_REFERENCIA.getCodigoCampo(), Util.getDateGabSql(gab109.getDataReferencia()));
      this.setString(CamposGab109Ver2.CODIGO_DO_CLIENTE.getCodigoCampo(), gab109.getCodigoCliente());
      this.setDate(CamposGab109Ver2.DATA_DE_LIQUIDACAO.getCodigoCampo(), Util.getDateGabSql(gab109.getDataLiquidacao()));
      this.setString(CamposGab109Ver2.DESCRICAO_DO_LANCAMENTO.getCodigoCampo(), gab109.getDescricaoLancamento());
      this.setString(CamposGab109Ver2.VALOR_DO_LANCAMENTO.getCodigoCampo(), gab109.getValorLancamento());
      this.setString(CamposGab109Ver2.NATUREZA_LANCAMENTO.getCodigoCampo(), gab109.getNaturezaLancamento());
      this.setString(CamposGab109Ver2.CHAVE_OD.getCodigoCampo(), gab109.getChaveOD());
      this.setString(CamposGab109Ver2.BANCO_ORIGEM.getCodigoCampo(), gab109.getBancoOrigem());
      this.setString(CamposGab109Ver2.AGENCIA_ORIGEM.getCodigoCampo(), gab109.getAgenciaOrigem());
      this.setString(CamposGab109Ver2.CONTA_ORIGEM.getCodigoCampo(), gab109.getContaOrigem());
      this.setString(CamposGab109Ver2.CPF_CNPJ_ORIGEM.getCodigoCampo(), gab109.getCpfCnpjOrigem());
      this.setString(CamposGab109Ver2.BANCO_DESTINO.getCodigoCampo(), gab109.getBancoDestino());
      this.setString(CamposGab109Ver2.AGENCIA_DESTINO.getCodigoCampo(), gab109.getAgenciaDestino());
      this.setString(CamposGab109Ver2.CONTA_DESTINO.getCodigoCampo(), gab109.getContaDestino());
      this.setString(CamposGab109Ver2.CPF_CNPJ_DESTINO.getCodigoCampo(), gab109.getCpfCnpjDestino());
      this.setString(CamposGab109Ver2.OBSERVACAO.getCodigoCampo(), gab109.getObservacao());
      this.setLong(CamposGab109Ver2.LINHA_ARQUIVO.getCodigoCampo(), gab109.getLinha());
   }

   private void validarNumero(Gab109Ver2 gab109) {
      String erroNumber = "O dado informado não é um número. ";
      if (!Util.isNumber(gab109.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), erroNumber + "Campo: " + CamposGab109Ver2.CODIGO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab109.getValorLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab109Ver2.VALOR_DO_LANCAMENTO.toString()));
      }

   }

   private void validarObrigatoriedade(Gab109Ver2 gab109) {
      if (StringUtil.isEmpty(gab109.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.CODIGO_DO_CLIENTE)));
      }

      if (StringUtil.isEmpty(gab109.getDescricaoLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.DESCRICAO_DO_LANCAMENTO)));
      }

      if (StringUtil.isEmpty(gab109.getValorLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.VALOR_DO_LANCAMENTO)));
      }

      if (StringUtil.isEmpty(gab109.getNaturezaLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.NATUREZA_LANCAMENTO)));
      }

      if (StringUtil.isEmpty(gab109.getDataLiquidacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.DATA_DE_LIQUIDACAO)));
      }

      if (StringUtil.isEmpty(gab109.getDataMovimento())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.DATA_DO_MOVIMENTO)));
      }

      if (StringUtil.isEmpty(gab109.getDataReferencia())) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab109Ver2.DATA_DE_REFERENCIA)));
      }

   }

   private void validarData(Gab109Ver2 gab109) {
      try {
         if (Util.getDateGab(gab109.getDataMovimento()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroData(CamposGab109Ver2.DATA_DO_MOVIMENTO)));
         } else if (Util.getDateGab(gab109.getDataReferencia()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroData(CamposGab109Ver2.DATA_DE_REFERENCIA)));
         } else if (Util.getDateGab(gab109.getDataLiquidacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), this.erroData(CamposGab109Ver2.DATA_DE_LIQUIDACAO)));
         }

      } catch (Exception e) {
         throw new InfraestruturaException("Erro na formatação da data", e);
      }
   }

   private void validarDadoEsperado(Gab109Ver2 gab109) {
      try {
         TipoLancamento.valueOf(gab109.getNaturezaLancamento());
      } catch (Exception var3) {
         this.errorReportCmd.report(new ErroValidacao(gab109.getLinha(), this.getNomeTabela(), "Informe C para crédito ou D para débito. Campo: " + CamposGab109Ver2.NATUREZA_LANCAMENTO.toString()));
      }

   }

   private String erroObrigatoriedade(CamposGab109Ver2 camposGab109) {
      return "O campo " + camposGab109.toString() + " é obrigatório.";
   }

   private String erroData(CamposGab109Ver2 camposGab109) {
      return "O campo " + String.valueOf(camposGab109) + " está incorreto. A data deve estar no formato AAAAMMDD e válida.";
   }

   protected CampoArquivo[] getCamposArquivo() {
      return null;
   }
}
