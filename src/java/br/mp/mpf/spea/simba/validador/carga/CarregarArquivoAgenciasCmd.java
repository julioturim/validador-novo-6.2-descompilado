package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Agencia;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.enums.UF;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoAgenciasCmd extends AbstractCarregarArquivoCmd<Agencia> {
   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("NUMERO_BANCO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NOME_AGENCIA", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("ENDERECO_LOGRADOURO", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("ENDERECO_CIDADE", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_UF", TipoCampoArquivo.TEXTO, 2), CampoArquivo.from("ENDERECO_PAIS", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_CEP", TipoCampoArquivo.TEXTO, 8), CampoArquivo.from("TELEFONE_AGENCIA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("DATA_ABERTURA_AGENCIA", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_FECHAMENTO_AGENCIA", TipoCampoArquivo.DATA, 8)};
   }

   @Inject
   private CarregarArquivoAgenciasCmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return "AGENCIA";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.AGENCIAS.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarArquivoAgenciasCmd.CamposAgencia.values();
   }

   protected int getQuantidadeColunas() {
      return 11;
   }

   protected Agencia createDomain() {
      return new Agencia();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabela(this.con, "AGENCIAS", quantidadeLinha);
   }

   protected Agencia convert(String[] dados, int numeroLinha) {
      ((Agencia)this.domain).setNumeroBanco(dados[0]);
      ((Agencia)this.domain).setNumeroAgencia(dados[1]);
      ((Agencia)this.domain).setNomeAgencia(dados[2]);
      ((Agencia)this.domain).setEnderecoLogradouro(dados[3]);
      ((Agencia)this.domain).setEnderecoCidade(dados[4]);
      ((Agencia)this.domain).setEnderecoUf(dados[5]);
      ((Agencia)this.domain).setEnderecoPais(dados[6]);
      ((Agencia)this.domain).setEnderecoCep(dados[7]);
      ((Agencia)this.domain).setTelefoneAgencia(dados[8]);
      ((Agencia)this.domain).setDataAberturaAgencia(dados[9]);
      ((Agencia)this.domain).setDataFechamentoAgencia(dados[10]);
      ((Agencia)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void setParametros(Agencia agencia) throws SQLException, ParseException {
      this.setInt(CarregarArquivoAgenciasCmd.CamposAgencia.NUMERO_AGENCIA.ordinal(), agencia.getNumeroAgencia());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.NOME_AGENCIA.ordinal(), agencia.getNomeAgencia());
      this.setInt(CarregarArquivoAgenciasCmd.CamposAgencia.NUMERO_BANCO.ordinal(), agencia.getNumeroBanco());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.ENDERECO_LOGRADOURO.ordinal(), agencia.getEnderecoLogradouro());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.ENDERECO_CIDADE.ordinal(), agencia.getEnderecoCidade());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.ENDERECO_UF.ordinal(), agencia.getEnderecoUf());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.ENDERECO_PAIS.ordinal(), agencia.getEnderecoPais());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.ENDERECO_CEP.ordinal(), agencia.getEnderecoCep());
      this.setString(CarregarArquivoAgenciasCmd.CamposAgencia.TELEFONE_AGENCIA.ordinal(), agencia.getTelefoneAgencia());
      this.ps.setDate(CarregarArquivoAgenciasCmd.CamposAgencia.DATA_ABERTURA_AGENCIA.ordinal(), Util.getDate(agencia.getDataAberturaAgencia()));
      this.ps.setDate(CarregarArquivoAgenciasCmd.CamposAgencia.DATA_FECHAMENTO_AGENCIA.ordinal(), Util.getDate(agencia.getDataFechamentoAgencia()));
      this.setLong(CarregarArquivoAgenciasCmd.CamposAgencia.LINHA_ARQUIVO.ordinal(), agencia.getLinha());
   }

   protected void validar(Agencia agencia) {
      this.validarCamposObrigatorios(agencia);
      this.validarDataAberturaAgencia(agencia);
      this.validarDataFechamentoAgencia(agencia);
      this.validarNumeroBanco(agencia);
      this.validarDatas(agencia);
      this.validarUf(agencia);
   }

   protected void validarCamposObrigatorios(Agencia agencia) {
      if (this.context.isEleitoral()) {
         this.validarCamposObrigatoriosEleitoral(agencia);
      } else {
         if (StringUtil.isEmpty(agencia.getNumeroBanco()) || StringUtil.isEmpty(agencia.getNumeroAgencia()) || StringUtil.isEmpty(agencia.getNomeAgencia()) || StringUtil.isEmpty(agencia.getEnderecoLogradouro()) || StringUtil.isEmpty(agencia.getEnderecoCidade()) || StringUtil.isEmpty(agencia.getEnderecoUf()) || StringUtil.isEmpty(agencia.getEnderecoPais()) || StringUtil.isEmpty(agencia.getEnderecoCep()) || StringUtil.isEmpty(agencia.getTelefoneAgencia())) {
            String camposObrigatorios = "NUMERO_BANCO, NUMERO_AGENCIA, NOME_AGENCIA, ENDERECO_LOGRADOURO, ENDERECO_CIDADE, ENDERECO_UF, ENDERECO_PAIS, ENDERECO_CEP, TELEFONE_AGENCIA";
            this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
         }

      }
   }

   protected void validarCamposObrigatoriosEleitoral(Agencia agencia) {
      if (StringUtil.isEmpty(agencia.getNumeroBanco()) || StringUtil.isEmpty(agencia.getNumeroAgencia()) || StringUtil.isEmpty(agencia.getNomeAgencia())) {
         String camposObrigatorios = "NUMERO_BANCO, NUMERO_AGENCIA, NOME_AGENCIA";
         this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
      }

   }

   protected void validarNumeroBanco(Agencia agencia) {
      int numeroBanco = this.cargaInfo.getAtendimentoBancario().getNumeroBanco();
      if (numeroBanco != Integer.valueOf(agencia.getNumeroBanco())) {
         this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "NUMERO_BANCO diferente do informado em atendimento."));
      }

   }

   protected void validarDatas(Agencia agencia) {
      if (!this.context.isEleitoral()) {
         if (!StringUtil.isEmpty(agencia.getDataFechamentoAgencia())) {
            if (StringUtil.isEmpty(agencia.getDataAberturaAgencia())) {
               this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "DATA_FECHAMENTO_AGENCIA informada, mas não há DATA_ABERTURA_AGENCIA."));
            } else {
               try {
                  Date dataAberturaAgencia = this.sdf.parse(agencia.getDataAberturaAgencia());
                  Date dataFechamentoAgencia = this.sdf.parse(agencia.getDataFechamentoAgencia());
                  if (dataFechamentoAgencia.before(dataAberturaAgencia)) {
                     this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "DATA_ABERTURA_AGENCIA posterior à DATA_FECHAMENTO_AGENCIA."));
                  }
               } catch (ParseException var4) {
                  System.out.println();
                  this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "Data inválida nos campos DATA_ABERTURA_AGENCIA e/ou DATA_FECHAMENTO_AGENCIA."));
               }
            }
         }

      }
   }

   protected void validarUf(Agencia agencia) {
      if (!this.context.isEleitoral()) {
         if (UF.get(agencia.getEnderecoUf()) == null && !"--".equals(agencia.getEnderecoUf())) {
            this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "ENDERECO_UF:" + agencia.getEnderecoUf() + " não é uma sigla válida."));
         }

      }
   }

   protected void validarDataAberturaAgencia(Agencia agencia) {
      if (!StringUtil.isEmpty(agencia.getDataAberturaAgencia()) && !br.mp.mpf.asspa.client.Util.verificaData(agencia.getDataAberturaAgencia())) {
         this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "A DATA_ABERTURA_AGENCIA (" + agencia.getDataAberturaAgencia() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   protected void validarDataFechamentoAgencia(Agencia agencia) {
      if (!StringUtil.isEmpty(agencia.getDataFechamentoAgencia()) && !br.mp.mpf.asspa.client.Util.verificaData(agencia.getDataFechamentoAgencia())) {
         this.errorReportCmd.report(new ErroValidacao(agencia.getLinha(), NomeArquivo.AGENCIAS.toString(), "A DATA_FECHAMENTO_AGENCIA (" + agencia.getDataFechamentoAgencia() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   public static enum CamposAgencia {
      CAMPO_NAO_UTILIZADO,
      NUMERO_BANCO,
      NUMERO_AGENCIA,
      NOME_AGENCIA,
      ENDERECO_LOGRADOURO,
      ENDERECO_CIDADE,
      ENDERECO_UF,
      ENDERECO_PAIS,
      ENDERECO_CEP,
      TELEFONE_AGENCIA,
      DATA_ABERTURA_AGENCIA,
      DATA_FECHAMENTO_AGENCIA,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposAgencia[] $values() {
         return new CamposAgencia[]{CAMPO_NAO_UTILIZADO, NUMERO_BANCO, NUMERO_AGENCIA, NOME_AGENCIA, ENDERECO_LOGRADOURO, ENDERECO_CIDADE, ENDERECO_UF, ENDERECO_PAIS, ENDERECO_CEP, TELEFONE_AGENCIA, DATA_ABERTURA_AGENCIA, DATA_FECHAMENTO_AGENCIA, LINHA_ARQUIVO};
      }
   }
}
