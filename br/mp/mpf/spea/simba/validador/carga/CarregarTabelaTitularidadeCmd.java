package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.Titularidade;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.util.Util;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarTabelaTitularidadeCmd extends AbstractCarregarArquivoCmd<Titularidade> {
   private final ContaLocalizerCmd contaLocalizer;
   private final PessoaLocalizerCmd pessoaLocalizer;

   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("NUMERO_BANCO", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NUMERO_CONTA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("TIPO_CONTA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("TIPO_TITULAR", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("PESSOA_INVESTIGADA", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("TIPO_PESSOA_TITULAR", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("CPF_CNPJ_TITULAR", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NOME_TITULAR", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("NOME_DOC_IDENTIFICACAO", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("NUMERO_DOC_IDENTIFICACAO", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("ENDERECO_LOGRADOURO", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("ENDERECO_CIDADE", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_UF", TipoCampoArquivo.TEXTO, 2), CampoArquivo.from("ENDERECO_PAIS", TipoCampoArquivo.TEXTO, 40), CampoArquivo.from("ENDERECO_CEP", TipoCampoArquivo.TEXTO, 8), CampoArquivo.from("TELEFONE_PESSOA", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("VALOR_RENDA", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("DATA_ATUALIZACAO_RENDA", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_INICIO_RELACIONAMENTO", TipoCampoArquivo.DATA, 8), CampoArquivo.from("DATA_FIM_RELACIONAMENTO", TipoCampoArquivo.DATA, 8)};
   }

   @Inject
   public CarregarTabelaTitularidadeCmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd, ContaLocalizerCmd contaLocalizer, PessoaLocalizerCmd pessoaLocalizer) throws SQLException {
      super(sharedConnection, errorReportCmd);
      this.contaLocalizer = contaLocalizer;
      this.pessoaLocalizer = pessoaLocalizer;
   }

   protected String getNomeTabela() {
      return "TITULARIDADE";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.TITULARES.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarTabelaTitularidadeCmd.CamposTitularidade.values();
   }

   protected int getQuantidadeColunas() {
      return 21;
   }

   protected Titularidade createDomain() {
      return new Titularidade();
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      super.atualizaQuantidadeLinhaTabela(this.con, "TITULARES", quantidadeLinha);
   }

   protected Titularidade convert(String[] dados, int numeroLinha) throws SQLException {
      ((Titularidade)this.domain).clear();
      ((Titularidade)this.domain).setTipo(dados[4]);
      ((Titularidade)this.domain).setPessoaInvestigada(dados[5]);
      ((Titularidade)this.domain).setDataInicioRelacionamento(dados[19]);
      ((Titularidade)this.domain).setDataFimRelacionamento(dados[20]);
      ((Titularidade)this.domain).setLinha((long)numeroLinha);
      String numeroBanco = dados[0];
      String numeroAgencia = dados[1];
      String numeroConta = dados[2];
      String tipoConta = dados[3];
      if ("".equals(numeroBanco)) {
         throw new NegocioException("O código COMPE da instituição financeira não foi informado");
      } else if ("".equals(numeroAgencia)) {
         throw new NegocioException("O número da agência não foi informado");
      } else if ("".equals(numeroConta)) {
         throw new NegocioException("O número da conta não foi informado");
      } else if ("".equals(tipoConta)) {
         throw new NegocioException("O tipo de conta não foi informado");
      } else {
         long idConta = this.contaLocalizer.findIdConta(numeroBanco, numeroAgencia, numeroConta, tipoConta);
         if (idConta == -1L) {
            throw new NegocioException("Conta não localizada. Banco: " + numeroBanco + " Agencia: " + numeroAgencia + " Conta: " + numeroConta + " TipoConta: " + tipoConta);
         } else {
            ((Titularidade)this.domain).setIdConta(idConta);
            long idPessoa = this.pessoaLocalizer.findIdPessoa(numeroLinha);
            if (idPessoa == -1L) {
               throw new NegocioException("Pessoa não localizada. Linha: " + numeroLinha);
            } else {
               ((Titularidade)this.domain).setIdPessoa(idPessoa);
               return this.domain;
            }
         }
      }
   }

   protected void setParametros(Titularidade titularidade) throws SQLException, ParseException {
      this.setLong(CarregarTabelaTitularidadeCmd.CamposTitularidade.CODIGO_CHAVE_PESSOA.ordinal(), titularidade.getIdPessoa());
      this.setLong(CarregarTabelaTitularidadeCmd.CamposTitularidade.CODIGO_CHAVE_CONTA.ordinal(), titularidade.getIdConta());
      this.setString(CarregarTabelaTitularidadeCmd.CamposTitularidade.TIPO_TITULAR.ordinal(), titularidade.getTipo());
      this.setInt(CarregarTabelaTitularidadeCmd.CamposTitularidade.PESSOA_INVESTIGADA.ordinal(), titularidade.getPessoaInvestigada());
      this.ps.setDate(CarregarTabelaTitularidadeCmd.CamposTitularidade.DATA_INICIO_RELACIONAMENTO.ordinal(), Util.getDate(titularidade.getDataInicioRelacionamento()));
      this.ps.setDate(CarregarTabelaTitularidadeCmd.CamposTitularidade.DATA_FIM_RELACIONAMENTO.ordinal(), Util.getDate(titularidade.getDataFimRelacionamento()));
      this.setLong(CarregarTabelaTitularidadeCmd.CamposTitularidade.LINHA_ARQUIVO.ordinal(), titularidade.getLinha());
   }

   protected void validar(Titularidade titularidade) {
      this.validarCamposObrigatorios(titularidade);
      this.validarDataInicioRelacionamento(titularidade);
      this.validarDataFimRelacionamento(titularidade);
      this.validarTipoTitular(titularidade);
      this.validarPessoaInvestigada(titularidade);
      this.validarDatasRelacionamento(titularidade);
   }

   protected void validarCamposObrigatorios(Titularidade titularidade) {
      if (this.context.isEleitoral()) {
         this.validarCamposObrigatoriosEleitoral(titularidade);
      } else {
         if (StringUtil.isEmpty(titularidade.getTipo()) || StringUtil.isEmpty(titularidade.getDataInicioRelacionamento()) || StringUtil.isEmpty(titularidade.getPessoaInvestigada())) {
            String camposObrigatorios = "TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, PESSOA_INVESTIGADA, DATA_INICIO_RELACIONAMENTO";
            this.errorReportCmd.report(new ErroValidacao(titularidade.getLinha(), NomeArquivo.TITULARES.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
         }

      }
   }

   protected void validarCamposObrigatoriosEleitoral(Titularidade titularidade) {
      if (StringUtil.isEmpty(titularidade.getTipo()) || StringUtil.isEmpty(titularidade.getPessoaInvestigada()) || StringUtil.isEmpty(titularidade.getDataInicioRelacionamento())) {
         String camposObrigatorios = "TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, PESSOA_INVESTIGADA, DATA_INICIO_RELACIONAMENTO";
         this.errorReportCmd.report(new ErroValidacao(titularidade.getLinha(), NomeArquivo.TITULARES.toString(), "Um dos campos obrigatórios (" + camposObrigatorios + ") não está preenchido."));
      }

   }

   protected void validarTipoTitular(Titularidade titularidade) {
      if (!StringUtil.isEmpty(titularidade.getTipo())) {
         String tipoTitular = titularidade.getTipo();
         if (!tipoTitular.matches("[T1-9RLPO]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(titularidade.getLinha(), NomeArquivo.TITULARES.toString(), "Tipo de titular inválido. Valores possíveis: T, 1 até 9, R, L, P e O."));
         }
      }

   }

   protected void validarPessoaInvestigada(Titularidade titularidade) {
      if (!StringUtil.isEmpty(titularidade.getPessoaInvestigada())) {
         String tipoTitular = titularidade.getPessoaInvestigada();
         if (!tipoTitular.matches("[01]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(titularidade.getLinha(), NomeArquivo.TITULARES.toString(), "Tipo de conta investigada inválido. Valores possíveis: 0 e 1."));
         }
      }

   }

   protected void validarDatasRelacionamento(Titularidade titularidade) {
      Date dataInicioRelacionamento = null;
      Date dataFimRelacionamento = null;

      try {
         dataInicioRelacionamento = this.sdf.parse(titularidade.getDataInicioRelacionamento());
      } catch (ParseException var5) {
         throw new NegocioException("A DATA_INICIO_RELACIONAMENTO é inválida: " + titularidade.getDataInicioRelacionamento());
      }

      try {
         if (!StringUtil.isNotEmpty(titularidade.getDataFimRelacionamento())) {
            return;
         }

         dataFimRelacionamento = this.sdf.parse(titularidade.getDataFimRelacionamento());
      } catch (ParseException var6) {
         throw new NegocioException("A DATA_FIM_RELACIONAMENTO é inválida: " + titularidade.getDataFimRelacionamento());
      }

      if (dataFimRelacionamento != null && dataInicioRelacionamento != null && dataFimRelacionamento.before(dataInicioRelacionamento)) {
         ErrorReportCmd var10000 = this.errorReportCmd;
         long var10003 = titularidade.getLinha();
         String var10004 = NomeArquivo.TITULARES.toString();
         String var10005 = titularidade.getDataInicioRelacionamento();
         var10000.report(new ErroValidacao(var10003, var10004, "A DATA_INICIO_RELACIONAMENTO (" + var10005 + ") é posterior a DATA_FIM_RELACIONAMENTO (" + titularidade.getDataFimRelacionamento() + ")."));
      }

   }

   protected void validarDataInicioRelacionamento(Titularidade titularidade) {
      if (!StringUtil.isEmpty(titularidade.getDataInicioRelacionamento()) && !br.mp.mpf.asspa.client.Util.verificaData(titularidade.getDataInicioRelacionamento())) {
         throw new NegocioException("A DATA_INICIO_RELACIONAMENTO (" + titularidade.getDataInicioRelacionamento() + ") é inválida ou o ano é anterior a 1900.");
      }
   }

   protected void validarDataFimRelacionamento(Titularidade titularidade) {
      if (StringUtil.isNotEmpty(titularidade.getDataFimRelacionamento()) && !br.mp.mpf.asspa.client.Util.verificaData(titularidade.getDataFimRelacionamento())) {
         throw new NegocioException("A DATA_FIM_RELACIONAMENTO (" + titularidade.getDataFimRelacionamento() + ") é inválida ou o ano é anterior a 1900.");
      }
   }

   public static enum CamposTitularidade {
      CAMPO_NAO_UTILIZADO,
      CODIGO_CHAVE_PESSOA,
      CODIGO_CHAVE_CONTA,
      TIPO_TITULAR,
      PESSOA_INVESTIGADA,
      DATA_INICIO_RELACIONAMENTO,
      DATA_FIM_RELACIONAMENTO,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposTitularidade[] $values() {
         return new CamposTitularidade[]{CAMPO_NAO_UTILIZADO, CODIGO_CHAVE_PESSOA, CODIGO_CHAVE_CONTA, TIPO_TITULAR, PESSOA_INVESTIGADA, DATA_INICIO_RELACIONAMENTO, DATA_FIM_RELACIONAMENTO, LINHA_ARQUIVO};
      }
   }
}
