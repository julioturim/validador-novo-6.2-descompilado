package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.CpfCnpjUtil;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Gab112Ver2;
import br.mp.mpf.spea.simba.validador.enums.CamposGab112Ver2;
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
public class CarregarArquivoGab112Ver2Cmd extends AbstractCarregarArquivoCmd<Gab112Ver2> {
   @Inject
   private CarregarArquivoGab112Ver2Cmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return Gab112Ver2.class.getSimpleName();
   }

   protected String getNomeArquivo() {
      return NomeArquivoGab.GAB112.name();
   }

   protected Object[] getFieldEnums() {
      return CamposGab112Ver2.values();
   }

   protected int getQuantidadeColunas() {
      return CamposGab112Ver2.values().length - 1;
   }

   protected Gab112Ver2 createDomain() {
      return new Gab112Ver2();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabelaGab(this.con, this.getNomeTabela(), quantidadeLinha);
   }

   protected Gab112Ver2 convert(String[] dados, int numeroLinha) {
      ((Gab112Ver2)this.domain).setCodigoCliente(dados[0]);
      ((Gab112Ver2)this.domain).setDocumentoCliente(dados[1]);
      ((Gab112Ver2)this.domain).setDataCriacao(dados[2]);
      ((Gab112Ver2)this.domain).setDataAtualizacao(dados[3]);
      ((Gab112Ver2)this.domain).setDataNascimentoFundacao(dados[4]);
      ((Gab112Ver2)this.domain).setTipoPessoa(dados[5]);
      ((Gab112Ver2)this.domain).setNomeCliente(dados[6]);
      ((Gab112Ver2)this.domain).setDocumentoIdentificacao(dados[7]);
      ((Gab112Ver2)this.domain).setNomeConjuge(dados[8]);
      ((Gab112Ver2)this.domain).setCodigoClienteMercado(dados[9]);
      ((Gab112Ver2)this.domain).setEndereco(dados[10]);
      ((Gab112Ver2)this.domain).setNumeroComplemento(dados[11]);
      ((Gab112Ver2)this.domain).setBairro(dados[12]);
      ((Gab112Ver2)this.domain).setCidade(dados[13]);
      ((Gab112Ver2)this.domain).setEstado(dados[14]);
      ((Gab112Ver2)this.domain).setCep(dados[15]);
      ((Gab112Ver2)this.domain).setComplementoCep(dados[16]);
      ((Gab112Ver2)this.domain).setConstante0000a(dados[17]);
      ((Gab112Ver2)this.domain).setCodigoAtividade(dados[18]);
      ((Gab112Ver2)this.domain).setDescricaoAtividade(dados[19]);
      ((Gab112Ver2)this.domain).setTelefone(dados[20]);
      ((Gab112Ver2)this.domain).setMercado(dados[21]);
      ((Gab112Ver2)this.domain).setEmail(dados[22]);
      ((Gab112Ver2)this.domain).setCelular(dados[23]);
      ((Gab112Ver2)this.domain).setMembroCompensacao(dados[24]);
      ((Gab112Ver2)this.domain).setDescricaoMembroCompensacao(dados[25]);
      ((Gab112Ver2)this.domain).setAgenteCustodia(dados[26]);
      ((Gab112Ver2)this.domain).setDescricaoAgenteCustodia(dados[27]);
      ((Gab112Ver2)this.domain).setCodigoCorretora(dados[28]);
      ((Gab112Ver2)this.domain).setDescricaoCodigoCorretora(dados[29]);
      ((Gab112Ver2)this.domain).setSituacaoCliente(dados[30]);
      ((Gab112Ver2)this.domain).setDataSituacao(dados[31]);
      ((Gab112Ver2)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void setParametros(Gab112Ver2 gab112) throws SQLException, ParseException {
      this.setString(CamposGab112Ver2.CODIGO_DO_CLIENTE.getCodigoCampo(), gab112.getCodigoCliente());
      this.setString(CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.getCodigoCampo(), gab112.getDocumentoCliente());
      this.setDate(CamposGab112Ver2.DATA_DA_CRIACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataCriacao()));
      this.setDate(CamposGab112Ver2.DATA_DA_ATUALIZACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataAtualizacao()));
      this.setDate(CamposGab112Ver2.DATA_NASCIMENTO_FUNDACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataNascimentoFundacao()));
      this.setString(CamposGab112Ver2.TIPO_DE_PESSOA.getCodigoCampo(), gab112.getTipoPessoa());
      this.setString(CamposGab112Ver2.NOME_DO_CLIENTE.getCodigoCampo(), gab112.getNomeCliente());
      this.setString(CamposGab112Ver2.DOCUMENTO_DE_IDENTIFICACAO.getCodigoCampo(), gab112.getDocumentoIdentificacao());
      this.setString(CamposGab112Ver2.NOME_DO_CONJUGE.getCodigoCampo(), gab112.getNomeConjuge());
      this.setString(CamposGab112Ver2.CODIGO_DO_CLIENTE_MERCADO.getCodigoCampo(), gab112.getCodigoClienteMercado());
      this.setString(CamposGab112Ver2.ENDERECO.getCodigoCampo(), gab112.getEndereco());
      this.setString(CamposGab112Ver2.NUMERO_E_COMPLEMENTO.getCodigoCampo(), gab112.getNumeroComplemento());
      this.setString(CamposGab112Ver2.BAIRRO.getCodigoCampo(), gab112.getBairro());
      this.setString(CamposGab112Ver2.CIDADE.getCodigoCampo(), gab112.getCidade());
      this.setString(CamposGab112Ver2.ESTADO.getCodigoCampo(), gab112.getEstado());
      this.setString(CamposGab112Ver2.CEP.getCodigoCampo(), gab112.getCep());
      this.setString(CamposGab112Ver2.COMPLEMENTO_DO_CEP.getCodigoCampo(), gab112.getComplementoCep());
      this.setString(CamposGab112Ver2.CONSTANTE_0000A.getCodigoCampo(), gab112.getConstante0000a());
      this.setString(CamposGab112Ver2.CODIGO_DA_ATIVIDADE.getCodigoCampo(), gab112.getCodigoAtividade());
      this.setString(CamposGab112Ver2.DESCRICAO_DA_ATIVIDADE.getCodigoCampo(), gab112.getDescricaoAtividade());
      this.setString(CamposGab112Ver2.TELEFONE.getCodigoCampo(), gab112.getTelefone());
      this.setString(CamposGab112Ver2.MERCADO.getCodigoCampo(), gab112.getMercado());
      this.setString(CamposGab112Ver2.EMAIL.getCodigoCampo(), gab112.getEmail());
      this.setString(CamposGab112Ver2.CELULAR.getCodigoCampo(), gab112.getCelular());
      this.setString(CamposGab112Ver2.MEMBRO_COMPENSACAO.getCodigoCampo(), gab112.getMembroCompensacao());
      this.setString(CamposGab112Ver2.DESCRICAO_MEMBRO_COMPENSACAO.getCodigoCampo(), gab112.getDescricaoMembroCompensacao());
      this.setString(CamposGab112Ver2.AGENTE_CUSTODIA.getCodigoCampo(), gab112.getAgenteCustodia());
      this.setString(CamposGab112Ver2.DESCRICAO_AGENTE_CUSTODIA.getCodigoCampo(), gab112.getDescricaoAgenteCustodia());
      this.setString(CamposGab112Ver2.CODIGO_CORRETORA.getCodigoCampo(), gab112.getCodigoCorretora());
      this.setString(CamposGab112Ver2.DESCRICAO_CODIGO_CORRETORA.getCodigoCampo(), gab112.getDescricaoCodigoCorretora());
      this.setString(CamposGab112Ver2.SITUACAO_CLIENTE.getCodigoCampo(), gab112.getSituacaoCliente());
      this.setDate(CamposGab112Ver2.DATA_SITUACAO.getCodigoCampo(), Util.getDateGabSql(gab112.getDataSituacao()));
      this.setLong(CamposGab112Ver2.LINHA_ARQUIVO.getCodigoCampo(), gab112.getLinha());
   }

   protected void validar(Gab112Ver2 gab112) {
      this.validarObrigatoriedade(gab112);
      this.validarData(gab112);
      this.validarNumero(gab112);
      this.validarDadoEsperado(gab112);
      this.validarCpfCnpj(gab112);
   }

   private void validarNumero(Gab112Ver2 gab112) {
      String erroNumber = "O dado informado não é um número. ";
      if (!Util.isNumber(gab112.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + "Campo: " + CamposGab112Ver2.CODIGO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString()));
      }

      if (!Util.isNumber(gab112.getCodigoClienteMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112Ver2.CODIGO_DO_CLIENTE_MERCADO.toString()));
      }

      if (!Util.isNumber(gab112.getCodigoAtividade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroNumber + " Campo " + CamposGab112Ver2.CODIGO_DA_ATIVIDADE.toString()));
      }

   }

   private void validarObrigatoriedade(Gab112Ver2 gab112) {
      if (StringUtil.isEmpty(gab112.getCodigoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CODIGO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataCriacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DATA_DA_CRIACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataAtualizacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DATA_DA_ATUALIZACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDataNascimentoFundacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DATA_NASCIMENTO_FUNDACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getTipoPessoa())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.TIPO_DE_PESSOA.toString())));
      }

      if (StringUtil.isEmpty(gab112.getNomeCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.NOME_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoIdentificacao())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DOCUMENTO_DE_IDENTIFICACAO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCodigoClienteMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CODIGO_DO_CLIENTE_MERCADO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getEndereco())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.ENDERECO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getBairro())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.BAIRRO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCidade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CIDADE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getDocumentoCliente())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getEstado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.ESTADO.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getComplementoCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.COMPLEMENTO_DO_CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getComplementoCep())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.COMPLEMENTO_DO_CEP.toString())));
      }

      if (StringUtil.isEmpty(gab112.getConstante0000a())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CONSTANTE_0000A.toString())));
      }

      if (StringUtil.isEmpty(gab112.getCodigoAtividade())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.CODIGO_DA_ATIVIDADE.toString())));
      }

      if (StringUtil.isEmpty(gab112.getMercado())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), this.erroObrigatoriedade(CamposGab112Ver2.MERCADO.toString())));
      }

   }

   private void validarData(Gab112Ver2 gab112) {
      try {
         String erroData = "A data deve estar no formato AAAAMMDD e válida. ";
         if (Util.getDateGab(gab112.getDataCriacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112Ver2.DATA_DA_CRIACAO.toString()));
         }

         if (Util.getDateGab(gab112.getDataAtualizacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112Ver2.DATA_DA_ATUALIZACAO.toString()));
         }

         if (Util.getDateGab(gab112.getDataNascimentoFundacao()) == null) {
            this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), erroData + " Campo: " + CamposGab112Ver2.DATA_NASCIMENTO_FUNDACAO.toString()));
         }

      } catch (Exception e) {
         throw new InfraestruturaException("Erro na formatação da data", e);
      }
   }

   private void validarDadoEsperado(Gab112Ver2 gab112) {
      if (!"0000A".equals(gab112.getConstante0000a())) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Valor esperado para o campo " + CamposGab112Ver2.CONSTANTE_0000A.toString() + ": 0000A."));
      }

      try {
         TipoPessoa.valueOf(gab112.getTipoPessoa());
      } catch (Exception var5) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe F para pessoa física ou J para pessoa jurídica. Campo: " + CamposGab112Ver2.TIPO_DE_PESSOA.toString()));
      }

      try {
         MercadoGab.valueOf(gab112.getMercado());
      } catch (Exception var4) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe BOVESPA para mercado à vista, BMF para derivativos ou SWAP para balcão. Campo: " + CamposGab112Ver2.MERCADO.toString()));
      }

      try {
         UF.valueOf(gab112.getEstado());
      } catch (Exception var3) {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "A sigla do campo estado está incorreta"));
      }

   }

   private void validarCpfCnpj(Gab112Ver2 gab112) {
      if (gab112.getDocumentoCliente().length() != CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.getTamanhoCampo()) {
         ErrorReportCmd var10000 = this.errorReportCmd;
         long var10003 = gab112.getLinha();
         String var10004 = this.getNomeTabela();
         String var10005 = CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString();
         var10000.report(new ErroValidacao(var10003, var10004, "O campo " + var10005 + " é obrigatório conter " + CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.getTamanhoCampo() + "posições. Devendo ser completementado com zeros à esquerda."));
      }

      String cpfCnpjSemZerosEsquerda = StringUtil.removerZerosEsquerda(gab112.getDocumentoCliente());
      if (gab112.getTipoPessoa().equals(TipoPessoa.F.toString())) {
         if (!CpfCnpjUtil.validarCpf(cpfCnpjSemZerosEsquerda)) {
            ErrorReportCmd var3 = this.errorReportCmd;
            long var5 = gab112.getLinha();
            String var7 = this.getNomeTabela();
            String var9 = gab112.getTipoPessoa();
            var3.report(new ErroValidacao(var5, var7, "CPF inválido para o tipo de pessoa: " + var9 + ". Campo: " + CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString()));
         }
      } else if (gab112.getTipoPessoa().equals(TipoPessoa.J.toString())) {
         if (!CpfCnpjUtil.validarCnpj(cpfCnpjSemZerosEsquerda)) {
            ErrorReportCmd var4 = this.errorReportCmd;
            long var6 = gab112.getLinha();
            String var8 = this.getNomeTabela();
            String var10 = gab112.getTipoPessoa();
            var4.report(new ErroValidacao(var6, var8, "CNPJ inválido para o tipo de pessoa: " + var10 + ". Campo: " + CamposGab112Ver2.DOCUMENTO_DO_CLIENTE.toString()));
         }
      } else {
         this.errorReportCmd.report(new ErroValidacao(gab112.getLinha(), this.getNomeTabela(), "Informe F para pessoa física ou J para pessoa jurídica. Campo: " + CamposGab112Ver2.TIPO_DE_PESSOA.toString()));
      }

   }

   private String erroObrigatoriedade(String campo) {
      return "O campo " + campo + " é obrigatório.";
   }

   protected CampoArquivo[] getCamposArquivo() {
      return null;
   }
}
