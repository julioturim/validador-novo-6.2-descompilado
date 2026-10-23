package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.CpfCnpjUtil;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Pessoa;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarTabelaPessoaCmd extends AbstractCarregarArquivoCmd<Pessoa> {
   private Map<String, Long> investigados;

   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("NUMERO_BANCO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NUMERO_CONTA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("TIPO_CONTA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("TIPO_TITULAR", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("PESSOA_INVESTIGADA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("TIPO_PESSOA_TITULAR", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("CPF_CNPJ_TITULAR", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NOME_TITULAR", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("NOME_DOC_IDENTIFICACAO", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("NUMERO_DOC_IDENTIFICACAO", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("ENDERECO_LOGRADOURO", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("ENDERECO_CIDADE", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_UF", TipoCampoArquivo.TEXTO, 2), CampoArquivo.from("ENDERECO_PAIS", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_CEP", TipoCampoArquivo.TEXTO, 8), CampoArquivo.from("TELEFONE_PESSOA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("VALOR_RENDA", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("DATA_ATUALIZACAO_RENDA", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_INICIO_RELACIONAMENTO", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_FIM_RELACIONAMENTO", TipoCampoArquivo.DATA, 8)};
   }

   @Inject
   public CarregarTabelaPessoaCmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return "PESSOA";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.TITULARES.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarTabelaPessoaCmd.CamposPessoa.values();
   }

   protected int getQuantidadeColunas() {
      return 21;
   }

   protected Pessoa createDomain() {
      return new Pessoa();
   }

   protected Pessoa convert(String[] dados, int numeroLinha) throws SQLException {
      ((Pessoa)this.domain).clear();
      ((Pessoa)this.domain).setTipo(dados[6]);
      ((Pessoa)this.domain).setCpfCnpj(dados[7].toUpperCase());
      ((Pessoa)this.domain).setNome(dados[8]);
      ((Pessoa)this.domain).setNomeDocIdentificacao(dados[9]);
      ((Pessoa)this.domain).setNumeroDocIdentificacao(dados[10]);
      ((Pessoa)this.domain).setEnderecoLogradouro(dados[11]);
      ((Pessoa)this.domain).setEnderecoCidade(dados[12]);
      ((Pessoa)this.domain).setEnderecoUf(dados[13]);
      ((Pessoa)this.domain).setEnderecoPais(dados[14]);
      ((Pessoa)this.domain).setEnderecoCep(dados[15]);
      ((Pessoa)this.domain).setTelefone(dados[16]);

      try {
         ((Pessoa)this.domain).setValorRenda(StringUtil.isEmpty(dados[17]) ? null : Double.valueOf(dados[17]));
      } catch (NumberFormatException var4) {
         throw new NegocioException("Valor numérico inválido no campo Valor Renda: " + dados[17]);
      }

      ((Pessoa)this.domain).setDataAtualizacaoRenda(dados[18]);
      ((Pessoa)this.domain).setLinha((long)numeroLinha);
      Long idInvestigado = (Long)this.investigados.get(StringUtil.completarZerosEsquerda(((Pessoa)this.domain).getCpfCnpj(), 14));
      if (idInvestigado != null) {
         ((Pessoa)this.domain).setCodigoInvestigado(idInvestigado);
      }

      return this.domain;
   }

   protected void setParametros(Pessoa pessoa) throws SQLException, ParseException {
      this.setInt(CarregarTabelaPessoaCmd.CamposPessoa.TIPO_PESSOA.ordinal(), pessoa.getTipo());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.CPF_CNPJ_PESSOA.ordinal(), pessoa.getCpfCnpj());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.NOME_PESSOA.ordinal(), pessoa.getNome());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.NOME_DOC_IDENTIFICACAO.ordinal(), pessoa.getNomeDocIdentificacao());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.NUMERO_DOC_IDENTIFICACAO.ordinal(), pessoa.getNumeroDocIdentificacao());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.ENDERECO_LOGRADOURO.ordinal(), pessoa.getEnderecoLogradouro());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.ENDERECO_CIDADE.ordinal(), pessoa.getEnderecoCidade());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.ENDERECO_UF.ordinal(), pessoa.getEnderecoUf());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.ENDERECO_PAIS.ordinal(), pessoa.getEnderecoPais());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.ENDERECO_CEP.ordinal(), pessoa.getEnderecoCep());
      this.setString(CarregarTabelaPessoaCmd.CamposPessoa.TELEFONE_PESSOA.ordinal(), pessoa.getTelefone());
      this.setDouble(CarregarTabelaPessoaCmd.CamposPessoa.VALOR_RENDA.ordinal(), pessoa.getValorRenda());
      this.ps.setDate(CarregarTabelaPessoaCmd.CamposPessoa.DATA_ATUALIZACAO_RENDA.ordinal(), Util.getDate(pessoa.getDataAtualizacaoRenda()));
      this.setLong(CarregarTabelaPessoaCmd.CamposPessoa.LINHA_ARQUIVO.ordinal(), pessoa.getLinha());
      this.setLong(CarregarTabelaPessoaCmd.CamposPessoa.CODIGO_INVESTIGADO.ordinal(), pessoa.getCodigoInvestigado());
   }

   protected void validar(Pessoa pessoa) {
      this.validarCamposObrigatorios(pessoa);
      this.validarDataAtualizacaoRenda(pessoa);
      this.validarCpfCnpj(pessoa);
      this.validarTipoPessoa(pessoa);
   }

   protected void validarCamposObrigatorios(Pessoa pessoa) {
      if (StringUtil.isEmpty(pessoa.getTipo()) || StringUtil.isEmpty(pessoa.getCpfCnpj()) || StringUtil.isEmpty(pessoa.getNome())) {
         String camposObrigatorios = "TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, PESSOA_INVESTIGADA, DATA_INICIO_RELACIONAMENTO";
         this.errorReportCmd.report(new ErroValidacao(pessoa.getLinha(), NomeArquivo.TITULARES.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
      }

   }

   protected void validarCpfCnpj(Pessoa pessoa) {
      if ("1".equals(pessoa.getTipo())) {
         if (!CpfCnpjUtil.validarCpf(StringUtil.removerZerosEsquerda(pessoa.getCpfCnpj()))) {
            this.errorReportCmd.report(new ErroValidacao(pessoa.getLinha(), NomeArquivo.TITULARES.toString(), "O CPF_CNPJ_PESSOA: " + pessoa.getCpfCnpj() + " está inválido."));
         }
      } else if ("2".equals(pessoa.getTipo()) && !CpfCnpjUtil.validarCnpj(pessoa.getCpfCnpj())) {
         this.errorReportCmd.report(new ErroValidacao(pessoa.getLinha(), NomeArquivo.TITULARES.toString(), "O CPF_CNPJ_PESSOA: " + pessoa.getCpfCnpj() + " está inválido."));
      }

   }

   protected void validarTipoPessoa(Pessoa pessoa) {
      if (!"1".equals(pessoa.getTipo()) && !"2".equals(pessoa.getTipo())) {
         this.errorReportCmd.report(new ErroValidacao(pessoa.getLinha(), NomeArquivo.TITULARES.toString(), "Tipo de pessoa inválido. Valores possíveis: 1 e 2."));
      }
   }

   public void setInvestigados(Map<String, Long> investigados) {
      this.investigados = investigados;
   }

   protected void validarDataAtualizacaoRenda(Pessoa pessoa) {
      if (!StringUtil.isEmpty(pessoa.getDataAtualizacaoRenda()) && !br.mp.mpf.asspa.client.Util.verificaData(pessoa.getDataAtualizacaoRenda())) {
         this.errorReportCmd.report(new ErroValidacao(pessoa.getLinha(), NomeArquivo.EXTRATO.toString(), "A DATA_ATUALIZACAO_RENDA (" + pessoa.getDataAtualizacaoRenda() + ") é inválida ou o ano é anterior a 1900."));
      }
   }

   public static enum CamposPessoa {
      CAMPO_NAO_UTILIZADO,
      CODIGO_INVESTIGADO,
      TIPO_PESSOA,
      CPF_CNPJ_PESSOA,
      NOME_PESSOA,
      NOME_DOC_IDENTIFICACAO,
      NUMERO_DOC_IDENTIFICACAO,
      ENDERECO_LOGRADOURO,
      ENDERECO_CIDADE,
      ENDERECO_UF,
      ENDERECO_PAIS,
      ENDERECO_CEP,
      TELEFONE_PESSOA,
      VALOR_RENDA,
      DATA_ATUALIZACAO_RENDA,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposPessoa[] $values() {
         return new CamposPessoa[]{CAMPO_NAO_UTILIZADO, CODIGO_INVESTIGADO, TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, NOME_DOC_IDENTIFICACAO, NUMERO_DOC_IDENTIFICACAO, ENDERECO_LOGRADOURO, ENDERECO_CIDADE, ENDERECO_UF, ENDERECO_PAIS, ENDERECO_CEP, TELEFONE_PESSOA, VALOR_RENDA, DATA_ATUALIZACAO_RENDA, LINHA_ARQUIVO};
      }
   }
}
