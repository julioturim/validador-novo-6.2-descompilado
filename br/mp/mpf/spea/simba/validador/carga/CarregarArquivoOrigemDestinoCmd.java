package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.domain.OrigemDestino;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import jakarta.inject.Inject;
import java.sql.SQLException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoOrigemDestinoCmd extends AbstractCarregarArquivoCmd<OrigemDestino> {
   @Inject
   private Context context;
   @Inject
   private Controlador controlador;

   protected CampoArquivo[] getCamposArquivo() {
      return new CampoArquivo[]{CampoArquivo.from("CODIGO_CHAVE_OD", TipoCampoArquivo.NUMERO, 18), CampoArquivo.from("CODIGO_CHAVE_EXTRATO", TipoCampoArquivo.NUMERO, 18), CampoArquivo.from("VALOR_TRANSACAO", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NUMERO_DOCUMENTO_TRANSACAO", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("NUMERO_BANCO_OD", TipoCampoArquivo.NUMERO, 3), CampoArquivo.from("NUMERO_AGENCIA_OD", TipoCampoArquivo.NUMERO, 4), CampoArquivo.from("NUMERO_CONTA_OD", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("TIPO_CONTA_OD", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("TIPO_PESSOA_OD", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("CPF_CNPJ_OD", TipoCampoArquivo.NUMERO, 14), CampoArquivo.from("NOME_PESSOA_OD", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("NOME_DOC_IDENTIFICACAO_OD", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("NUMERO_DOC_IDENTIFICACAO_OD", TipoCampoArquivo.TEXTO, 20), CampoArquivo.from("CODIGO_DE_BARRAS", TipoCampoArquivo.TEXTO, 100), CampoArquivo.from("NOME_ENDOSSANTE_CHEQUE", TipoCampoArquivo.TEXTO, 80), CampoArquivo.from("DOC_ENDOSSANTE_CHEQUE", TipoCampoArquivo.TEXTO, 50), CampoArquivo.from("SITUACAO_IDENTIFICACAO", TipoCampoArquivo.NUMERO, 1), CampoArquivo.from("OBSERVACÃO", TipoCampoArquivo.TEXTO, 250)};
   }

   @Inject
   public CarregarArquivoOrigemDestinoCmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      super(sharedConnection, errorReportCmd);
   }

   protected String getNomeTabela() {
      return "ORIGEM_DESTINO";
   }

   protected String getNomeArquivo() {
      return NomeArquivo.ORIGEM_DESTINO.name();
   }

   protected Object[] getFieldEnums() {
      return CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.values();
   }

   protected int getQuantidadeColunas() {
      return 18;
   }

   protected OrigemDestino createDomain() {
      return new OrigemDestino();
   }

   protected OrigemDestino convert(String[] dados, int numeroLinha) {
      ((OrigemDestino)this.domain).clear();
      ((OrigemDestino)this.domain).setCodigoChave(Long.valueOf(dados[0]));
      ((OrigemDestino)this.domain).setCodigoChaveExtrato(this.getLong(dados[1], numeroLinha, "CODIGO_CHAVE_EXTRATO"));
      ((OrigemDestino)this.domain).setValorOperacao(this.getLong(dados[2], numeroLinha, "VALOR_OPERACAO"));
      ((OrigemDestino)this.domain).setNumeroDocumento(dados[3]);
      ((OrigemDestino)this.domain).setNumeroBanco(dados[4]);
      if (!StringUtil.isEmpty(dados[5])) {
         ((OrigemDestino)this.domain).setNumeroAgencia(this.getInt(dados[5], numeroLinha, "NUMERO_AGENCIA"));
      }

      ((OrigemDestino)this.domain).setNumeroConta(dados[6]);
      ((OrigemDestino)this.domain).setTipoConta(dados[7]);
      ((OrigemDestino)this.domain).setTipoPessoa(dados[8]);
      ((OrigemDestino)this.domain).setCpfCnpj(dados[9]);
      ((OrigemDestino)this.domain).setNomePessoa(dados[10]);
      ((OrigemDestino)this.domain).setNomeDocumentoIdentificacao(dados[11]);
      ((OrigemDestino)this.domain).setNumeroDocumentoIdentificacao(dados[12]);
      ((OrigemDestino)this.domain).setCodigoBarras(dados[13]);
      ((OrigemDestino)this.domain).setNomeEndossanteCheque(dados[14]);
      ((OrigemDestino)this.domain).setDocEndossanteCheque(dados[15]);
      ((OrigemDestino)this.domain).setSituacaoIdentificacao(this.getInt(dados[16], numeroLinha, "SITUACAO_IDENTIFICACAO"));
      ((OrigemDestino)this.domain).setObservacao(dados[17]);
      ((OrigemDestino)this.domain).setLinha((long)numeroLinha);
      return this.domain;
   }

   protected void setParametros(OrigemDestino origemDestino) throws SQLException {
      this.setLong(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.CODIGO_CHAVE_OD.ordinal(), origemDestino.getCodigoChave());
      this.setLong(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.CODIGO_CHAVE_EXTRATO.ordinal(), origemDestino.getCodigoChaveExtrato());
      this.setLong(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.VALOR_OPERACAO.ordinal(), origemDestino.getValorOperacao());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NUMERO_DOCUMENTO.ordinal(), origemDestino.getNumeroDocumento());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.CODIGO_DE_BARRAS.ordinal(), origemDestino.getCodigoBarras());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NOME_ENDOSSANTE_CHEQUE.ordinal(), origemDestino.getNomeEndossanteCheque());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.DOC_ENDOSSANTE_CHEQUE.ordinal(), origemDestino.getDocEndossanteCheque());
      this.setInt(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.SITUACAO_IDENTIFICACAO.ordinal(), origemDestino.getSituacaoIdentificacao());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.OBSERVACAO_OD.ordinal(), origemDestino.getObservacao());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NUMERO_BANCO_OD.ordinal(), origemDestino.getNumeroBanco());
      this.setInt(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NUMERO_AGENCIA_OD.ordinal(), origemDestino.getNumeroAgencia());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NUMERO_CONTA_OD.ordinal(), origemDestino.getNumeroConta());
      this.setInt(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.TIPO_CONTA_OD.ordinal(), origemDestino.getTipoConta());
      this.setInt(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.TIPO_PESSOA_OD.ordinal(), origemDestino.getTipoPessoa());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.CPF_CNPJ_PESSOA_OD.ordinal(), origemDestino.getCpfCnpj());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NOME_PESSOA_OD.ordinal(), origemDestino.getNomePessoa());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NOME_DOC_IDENTIFICACAO_OD.ordinal(), origemDestino.getNomeDocumentoIdentificacao());
      this.setString(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.NUMERO_DOC_IDENTIFICACAO_OD.ordinal(), origemDestino.getNumeroDocumentoIdentificacao());
      this.setLong(CarregarArquivoOrigemDestinoCmd.CamposOrigemDestino.LINHA_ARQUIVO.ordinal(), origemDestino.getLinha());
   }

   protected void validar(OrigemDestino origemDestino) {
      this.validarTipoConta(origemDestino);
      this.validarTipoPessoa(origemDestino);
      this.validarSituacaoIdentificao(origemDestino);
      this.validarNumeroBanco(origemDestino);
   }

   protected void validarNumeroBanco(OrigemDestino origemDestino) {
      if (!StringUtil.isEmpty(origemDestino.getNumeroBanco())) {
         if (origemDestino.getNumeroBanco().length() > 3) {
            Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
            if (!computador.getVersaoReceptor().suportaCNPJComoNumeroBanco(computador)) {
               this.errorReportCmd.report(new ErroValidacao(origemDestino.getLinha(), NomeArquivo.ORIGEM_DESTINO.toString(), "Número Banco inválido. O mesmo deve ser composto de apenas 3 dígitos (COMPE)."));
            } else if (origemDestino.getNumeroBanco().length() != 14) {
               this.errorReportCmd.report(new ErroValidacao(origemDestino.getLinha(), NomeArquivo.ORIGEM_DESTINO.toString(), "Número Banco inválido. O mesmo deve ser composto de apenas 3 dígitos (COMPE) ou 14 dígitos(CNPJ)."));
            }
         }
      }
   }

   protected void validarTipoConta(OrigemDestino origemDestino) {
      if (!StringUtil.isEmpty(origemDestino.getTipoPessoa())) {
         if (!origemDestino.getTipoPessoa().matches("[12]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(origemDestino.getLinha(), NomeArquivo.ORIGEM_DESTINO.toString(), "TIPO_CONTA_OD inválido. Valores possíveis: 1, 2, 3 ou 4."));
         }

      }
   }

   protected void validarTipoPessoa(OrigemDestino origemDestino) {
      if (!StringUtil.isEmpty(origemDestino.getTipoPessoa())) {
         if (!origemDestino.getTipoPessoa().matches("[12]{1}")) {
            this.errorReportCmd.report(new ErroValidacao(origemDestino.getLinha(), NomeArquivo.ORIGEM_DESTINO.toString(), "TIPO_PESSOA_OD inválido. Valores possíveis: 1 ou 2."));
         }

      }
   }

   protected void validarSituacaoIdentificao(OrigemDestino origemDestino) {
      if (origemDestino.getSituacaoIdentificacao() != 0) {
         this.errorReportCmd.report(new ErroValidacao(origemDestino.getLinha(), NomeArquivo.ORIGEM_DESTINO.toString(), "SITUACAO_IDENTIFICACAO inválido. Valor possível: 0."));
      }

   }

   public static enum CamposOrigemDestino {
      CAMPO_NAO_UTILIZADO,
      CODIGO_CHAVE_OD,
      CODIGO_CHAVE_EXTRATO,
      VALOR_OPERACAO,
      NUMERO_DOCUMENTO,
      NUMERO_BANCO_OD,
      NUMERO_AGENCIA_OD,
      NUMERO_CONTA_OD,
      TIPO_CONTA_OD,
      TIPO_PESSOA_OD,
      CPF_CNPJ_PESSOA_OD,
      NOME_PESSOA_OD,
      NOME_DOC_IDENTIFICACAO_OD,
      NUMERO_DOC_IDENTIFICACAO_OD,
      CODIGO_DE_BARRAS,
      NOME_ENDOSSANTE_CHEQUE,
      DOC_ENDOSSANTE_CHEQUE,
      SITUACAO_IDENTIFICACAO,
      OBSERVACAO_OD,
      LINHA_ARQUIVO;

      // $FF: synthetic method
      private static CamposOrigemDestino[] $values() {
         return new CamposOrigemDestino[]{CAMPO_NAO_UTILIZADO, CODIGO_CHAVE_OD, CODIGO_CHAVE_EXTRATO, VALOR_OPERACAO, NUMERO_DOCUMENTO, NUMERO_BANCO_OD, NUMERO_AGENCIA_OD, NUMERO_CONTA_OD, TIPO_CONTA_OD, TIPO_PESSOA_OD, CPF_CNPJ_PESSOA_OD, NOME_PESSOA_OD, NOME_DOC_IDENTIFICACAO_OD, NUMERO_DOC_IDENTIFICACAO_OD, CODIGO_DE_BARRAS, NOME_ENDOSSANTE_CHEQUE, DOC_ENDOSSANTE_CHEQUE, SITUACAO_IDENTIFICACAO, OBSERVACAO_OD, LINHA_ARQUIVO};
      }
   }
}
