package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.CpfCnpjUtil;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Gab112;
import br.mp.mpf.spea.simba.validador.enums.CamposGab112;
import br.mp.mpf.spea.simba.validador.enums.MercadoGab;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.enums.TipoPessoa;
import br.mp.mpf.spea.simba.validador.enums.UF;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoGab112Cmd extends AbstractCarregarArquivoCmd<Gab112> {
   @Inject
   private CarregarArquivoGab112Cmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return Gab112.class.getSimpleName();
   }

   protected String getNomeArquivo() {
      return NomeArquivoGab.GAB112.name();
   }

   protected Object[] getFieldEnums() {
      return CamposGab112.values();
   }

   protected int getQuantidadeColunas() {
      return CamposGab112.values().length - 1;
   }

   protected Gab112 createDomain() {
      return new Gab112();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabelaGab(this.con, this.getNomeTabela(), quantidadeLinha);
   }

   protected Gab112 convert(String[] dados, int numeroLinha) {
      ((Gab112)this.domain).setCodigoCliente(dados[0]);
      ((Gab112)this.domain).setDocumentoCliente(dados[1]);
      ((Gab112)this.domain).setDataCriacao(dados[2]);
      ((Gab112)this.domain).setDataAtualizacao(dados[3]);
      ((Gab112)this.domain).setDataNascimentoFundacao(dados[4]);
      ((Gab112)this.domain).setTipoPessoa(dados[5]);
      ((Gab112)this.domain).setNomeCliente(dados[6]);
      ((Gab112)this.domain).setDocumentoIdentificacao(dados[7]);
      ((Gab112)this.domain).setNomeConjuge(dados[8]);
      ((Gab112)this.domain).setCodigoClienteMercado(dados[9]);
      ((Gab112)this.domain).setEndereco(dados[10]);
      ((Gab112)this.domain).setNumeroComplemento(dados[11]);
      ((Gab112)this.domain).setBairro(dados[12]);
      ((Gab112)this.domain).setCidade(dados[13]);
      ((Gab112)this.domain).setEstado(dados[14]);
      ((Gab112)this.domain).setCep(dados[15]);
      ((Gab112)this.domain).setComplementoCep(dados[16]);
      ((Gab112)this.domain).setConstante0000a(dados[17]);
      ((Gab112)this.domain).setCodigoAtividade(dados[18]);
      ((Gab112)this.domain).setTelefone(dados[19]);
      ((Gab112)this.domain).setMercado(dados[20]);
      ((Gab112)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void setParametros(Gab112 gab112) throws SQLException, ParseException {
      this.setString(CamposGab112.CODIGO_DO_CLIENTE.getCodigoCampo(), gab112.getCodigoCliente());
      this.setString(CamposGab112.DOCUMENTO_DO_CLIENTE.getCodigoCampo(), gab112.getDocumentoCliente());
      this.setDate(CamposGab112.DATA_DA_CRIACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataCriacao()));
      this.setDate(CamposGab112.DATA_DA_ATUALIZACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataAtualizacao()));
      this.setDate(CamposGab112.DATA_NASCIMENTO_FUNDACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataNascimentoFundacao()));
      this.setString(CamposGab112.TIPO_DE_PESSOA.getCodigoCampo(), gab112.getTipoPessoa());
      this.setString(CamposGab112.NOME_DO_CLIENTE.getCodigoCampo(), gab112.getNomeCliente());
      this.setString(CamposGab112.DOCUMENTO_DE_IDENTIFICACAO.getCodigoCampo(), gab112.getDocumentoIdentificacao());
      this.setString(CamposGab112.NOME_DO_CONJUGE.getCodigoCampo(), gab112.getNomeConjuge());
      this.setString(CamposGab112.CODIGO_DO_CLIENTE_MERCADO.getCodigoCampo(), gab112.getCodigoClienteMercado());
      this.setString(CamposGab112.ENDERECO.getCodigoCampo(), gab112.getEndereco());
      this.setString(CamposGab112.NUMERO_E_COMPLEMENTO.getCodigoCampo(), gab112.getNumeroComplemento());
      this.setString(CamposGab112.BAIRRO.getCodigoCampo(), gab112.getBairro());
      this.setString(CamposGab112.CIDADE.getCodigoCampo(), gab112.getCidade());
      this.setString(CamposGab112.ESTADO.getCodigoCampo(), gab112.getEstado());
      this.setString(CamposGab112.CEP.getCodigoCampo(), gab112.getCep());
      this.setString(CamposGab112.COMPLEMENTO_DO_CEP.getCodigoCampo(), gab112.getComplementoCep());
      this.setString(CamposGab112.CONSTANTE_0000A.getCodigoCampo(), gab112.getConstante0000a());
      this.setString(CamposGab112.CODIGO_DA_ATIVIDADE.getCodigoCampo(), gab112.getCodigoAtividade());
      this.setString(CamposGab112.TELEFONE.getCodigoCampo(), gab112.getTelefone());
      this.setString(CamposGab112.MERCADO.getCodigoCampo(), gab112.getMercado());
      this.setLong(CamposGab112.LINHA_ARQUIVO.getCodigoCampo(), gab112.getLinha());
   }

   protected void validar(Gab112 gab112) {
      this.validarObrigatoriedade(gab112);
      this.validarData(gab112);
      this.validarNumero(gab112);
      this.validarDadoEsperado(gab112);
      this.validarCpfCnpj(gab112);
   }

   private void validarNumero(Gab112 gab112) {
      String erroNumber = "O dado informado não é um número. ";
      if (!Util.isNumber(gab112.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + "Campo: " + CamposGab112.CODIGO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112.DOCUMENTO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab112.getCodigoClienteMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112.CODIGO_DO_CLIENTE_MERCADO.toString()));
      }

      if (!Util.isNumber(gab112.getCodigoAtividade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112.CODIGO_DA_ATIVIDADE.toString()));
      }

   }

   private void validarObrigatoriedade(Gab112 gab112) {
      if (StringUtil.isEmpty(gab112.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CODIGO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DOCUMENTO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataCriacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DATA_DA_CRIACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataAtualizacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DATA_DA_ATUALIZACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataNascimentoFundacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DATA_NASCIMENTO_FUNDACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getTipoPessoa())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.TIPO_DE_PESSOA.toString())));
      }

      if (StringUtil.isEmpty(gab112.getNomeCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.NOME_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoIdentificacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DOCUMENTO_DE_IDENTIFICACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCodigoClienteMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CODIGO_DO_CLIENTE_MERCADO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getEndereco())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.ENDERECO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getBairro())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.BAIRRO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCidade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CIDADE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.DOCUMENTO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getEstado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.ESTADO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getComplementoCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.COMPLEMENTO_DO_CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getComplementoCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.COMPLEMENTO_DO_CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getConstante0000a())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CONSTANTE_0000A.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCodigoAtividade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.CODIGO_DA_ATIVIDADE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112.MERCADO.toString())));
      }

   }

   private void validarData(Gab112 gab112) {
      try {
         String erroData = "A data deve estar no formato AAAAMMDD e válida. ";
         if (Util.getDateGab(gab112.getDataCriacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112.DATA_DA_CRIACAO.toString()));
         }

         if (Util.getDateGab(gab112.getDataAtualizacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112.DATA_DA_ATUALIZACAO.toString()));
         }

         if (Util.getDateGab(gab112.getDataNascimentoFundacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112.DATA_NASCIMENTO_FUNDACAO.toString()));
         }

      } catch (Exception e) {
         throw new InfraestruturaException("Erro na formatação da data", e);
      }
   }

   private void validarDadoEsperado(Gab112 gab112) {
      if (!"0000A".equals(gab112.getConstante0000a())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Valor esperado para o campo " + CamposGab112.CONSTANTE_0000A.toString() + ": 0000A."));
      }

      try {
         TipoPessoa.valueOf(gab112.getTipoPessoa());
      } catch (Exception var5) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe F para pessoa física ou J para pessoa jurídica. Campo: " + CamposGab112.TIPO_DE_PESSOA.toString()));
      }

      try {
         MercadoGab.valueOf(gab112.getMercado());
      } catch (Exception var4) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe BOVESPA para mercado à vista, BMF para derivativos ou SWAP para balcão. Campo: " + CamposGab112.MERCADO.toString()));
      }

      try {
         UF.valueOf(gab112.getEstado());
      } catch (Exception var3) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "A sigla do campo estado está incorreta"));
      }

   }

   private void validarCpfCnpj(Gab112 gab112) {
      if (gab112.getDocumentoCliente().length() != CamposGab112.DOCUMENTO_DO_CLIENTE.getTamanhoCampo()) {
         ErrorReportCmd var10000 = this.errorReportCmd;
         long var10003 = gab112.getLinha();
         String var10004 = this.getNomeTabela();
         String var10005 = CamposGab112.DOCUMENTO_DO_CLIENTE.toString();
         var10000.report(new ErroValidacao(var10003, var10004, "O campo " + var10005 + " é obrigatório conter " + CamposGab112.DOCUMENTO_DO_CLIENTE.getTamanhoCampo() + "posições. Devendo ser completementado com zeros à esquerda."));
      }

      String cpfCnpjSemZerosEsquerda = StringUtil.removerZerosEsquerda(gab112.getDocumentoCliente());
      if (gab112.getTipoPessoa().equals(TipoPessoa.F.toString())) {
         if (!CpfCnpjUtil.validarCpf(cpfCnpjSemZerosEsquerda)) {
            ErrorReportCmd var3 = this.errorReportCmd;
            long var5 = gab112.getLinha();
            String var7 = this.getNomeTabela();
            String var9 = gab112.getTipoPessoa();
            var3.report(new ErroValidacao(var5, var7, "CPF inválido para o tipo de pessoa: " + var9 + ". Campo: " + CamposGab112.DOCUMENTO_DO_CLIENTE.toString()));
         }
      } else if (gab112.getTipoPessoa().equals(TipoPessoa.J.toString())) {
         if (!CpfCnpjUtil.validarCnpj(cpfCnpjSemZerosEsquerda)) {
            ErrorReportCmd var4 = this.errorReportCmd;
            long var6 = gab112.getLinha();
            String var8 = this.getNomeTabela();
            String var10 = gab112.getTipoPessoa();
            var4.report(new ErroValidacao(var6, var8, "CNPJ inválido para o tipo de pessoa: " + var10 + ". Campo: " + CamposGab112.DOCUMENTO_DO_CLIENTE.toString()));
         }
      } else {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe F para pessoa física ou J para pessoa jurídica. Campo: " + CamposGab112.TIPO_DE_PESSOA.toString()));
      }

   }

   private String erroObrigatoriedade(String campo) {
      return "O campo " + campo + " é obrigatório.";
   }

   protected CampoArquivo[] getCamposArquivo() {
      return null;
   }
}
