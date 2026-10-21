package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Gab800;
import br.mp.mpf.spea.simba.validador.enums.CamposGab800;
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
public class CarregarArquivoGab800Cmd extends AbstractCarregarArquivoCmd<Gab800> {
   @Inject
   private CarregarArquivoGab800Cmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return Gab800.class.getSimpleName();
   }

   protected String getNomeArquivo() {
      return NomeArquivoGab.GAB800.name();
   }

   protected Object[] getFieldEnums() {
      return CamposGab800.values();
   }

   protected int getQuantidadeColunas() {
      return CamposGab800.values().length - 1;
   }

   protected Gab800 createDomain() {
      return new Gab800();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabelaGab(this.con, this.getNomeTabela(), quantidadeLinha);
   }

   protected Gab800 convert(String[] dados, int numeroLinha) {
      ((Gab800)this.domain).setCodigoCliente(dados[0]);
      ((Gab800)this.domain).setNomeCliente(dados[1]);
      ((Gab800)this.domain).setEndereco(dados[2]);
      ((Gab800)this.domain).setEnderecoComplemento(dados[3]);
      ((Gab800)this.domain).setDataReferencia(dados[4]);
      ((Gab800)this.domain).setDataLiquidacao(dados[5]);
      ((Gab800)this.domain).setDescricaoLancamento(dados[6]);
      ((Gab800)this.domain).setSaldoInicial(dados[7]);
      ((Gab800)this.domain).setNaturezaSaldoInicial(dados[8]);
      ((Gab800)this.domain).setValorLancamento(dados[9]);
      ((Gab800)this.domain).setNaturezaLancamento(dados[10]);
      ((Gab800)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void setParametros(Gab800 gab800) throws SQLException, ParseException {
      this.setString(CamposGab800.CODIGO_DO_CLIENTE.getCodigoCampo(), gab800.getCodigoCliente());
      this.setString(CamposGab800.NOME_DO_CLIENTE.getCodigoCampo(), gab800.getNomeCliente());
      this.setString(CamposGab800.ENDERECO.getCodigoCampo(), gab800.getEndereco());
      this.setString(CamposGab800.ENDERECO_COMPLEMENTO.getCodigoCampo(), gab800.getEnderecoComplemento());
      this.setDate(CamposGab800.DATA_DE_REFERENCIA.getCodigoCampo(), Util.getDateGabSql(gab800.getDataReferencia()));
      this.setDate(CamposGab800.DATA_DE_LIQUIDACAO.getCodigoCampo(), Util.getDateGabSql(gab800.getDataLiquidacao()));
      this.setString(CamposGab800.DESCRICAO_DO_LANCAMENTO.getCodigoCampo(), gab800.getDescricaoLancamento());
      this.setString(CamposGab800.SALDO_INICIAL.getCodigoCampo(), gab800.getSaldoInicial());
      this.setString(CamposGab800.NATUREZA_DO_SALDO_INICIAL.getCodigoCampo(), gab800.getNaturezaSaldoInicial());
      this.setString(CamposGab800.VALOR_DO_LANCAMENTO.getCodigoCampo(), gab800.getValorLancamento());
      this.setString(CamposGab800.NATUREZA_DO_LANCAMENTO.getCodigoCampo(), gab800.getNaturezaLancamento());
      this.setLong(CamposGab800.LINHA_ARQUIVO.getCodigoCampo(), gab800.getLinha());
   }

   protected void validar(Gab800 gab800) {
      this.validarObrigatoriedade(gab800);
      this.validarData(gab800);
      this.validarNumero(gab800);
      this.validarDadoEsperado(gab800);
   }

   private void validarNumero(Gab800 gab800) {
      String erroNumber = "O dado informado não é um número. ";
      if (!Util.isNumber(gab800.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), erroNumber + "Campo: " + CamposGab800.CODIGO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab800.getValorLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab800.VALOR_DO_LANCAMENTO.toString()));
      }

      if (!Util.isNumber(gab800.getSaldoInicial())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab800.SALDO_INICIAL.toString()));
      }

   }

   private void validarObrigatoriedade(Gab800 gab800) {
      if (StringUtil.isEmpty(gab800.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.CODIGO_DO_CLIENTE.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getDescricaoLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.DESCRICAO_DO_LANCAMENTO.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getValorLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.VALOR_DO_LANCAMENTO.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getNomeCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.NOME_DO_CLIENTE.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getDataLiquidacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.DATA_DE_LIQUIDACAO.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getEndereco())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.ENDERECO.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getDataReferencia())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.DATA_DE_REFERENCIA.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getSaldoInicial())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.SALDO_INICIAL.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getNaturezaSaldoInicial())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.NATUREZA_DO_SALDO_INICIAL.toString() + " é obrigatório."));
      }

      if (StringUtil.isEmpty(gab800.getNaturezaLancamento())) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "O campo " + CamposGab800.NATUREZA_DO_LANCAMENTO.toString() + " é obrigatório."));
      }

   }

   private void validarData(Gab800 gab800) {
      try {
         String erroData = "A data deve estar no formato AAAAMMDD e válida. ";
         if (Util.getDateGab(gab800.getDataReferencia()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab800.DATA_DE_REFERENCIA.toString()));
         }

         if (Util.getDateGab(gab800.getDataLiquidacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab800.DATA_DE_LIQUIDACAO.toString()));
         }

      } catch (Exception e) {
         throw new InfraestruturaException("Erro na formatação da data", e);
      }
   }

   private void validarDadoEsperado(Gab800 gab800) {
      try {
         TipoLancamento.valueOf(gab800.getNaturezaSaldoInicial());
      } catch (Exception var4) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "Informe C para crédito ou D para débito. Campo: " + CamposGab800.NATUREZA_DO_SALDO_INICIAL.toString()));
      }

      try {
         TipoLancamento.valueOf(gab800.getNaturezaLancamento());
      } catch (Exception var3) {
         this.errorReportCmd.report(new ErroValidacao(gab800.getLinha(), this.getNomeTabela(), "Informe C para crédito ou D para débito. Campo: " + CamposGab800.NATUREZA_DO_LANCAMENTO.toString()));
      }

   }

   protected CampoArquivo[] getCamposArquivo() {
      return null;
   }
}
