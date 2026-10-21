package br.mp.mpf.spea.simba.validador.domain;

public class OrigemDestino extends Linha {
   private long idExtrato;
   private long valorOperacao;
   private String numeroDocumento;
   private String codigoBarras;
   private String nomeEndossanteCheque;
   private String docEndossanteCheque;
   private int situacaoIdentificacao = 0;
   private String observacao;
   private String numeroBanco;
   private Integer numeroAgencia;
   private String numeroConta;
   private String tipoConta;
   private String tipoPessoa;
   private String cpfCnpj;
   private String nomePessoa;
   private String nomeDocumentoIdentificacao;
   private String numeroDocumentoIdentificacao;
   private long codigoChave;
   private long codigoChaveExtrato;

   public void clear() {
      super.clear();
      this.idExtrato = 0L;
      this.valorOperacao = 0L;
      this.numeroDocumento = null;
      this.codigoBarras = null;
      this.nomeEndossanteCheque = null;
      this.docEndossanteCheque = null;
      this.situacaoIdentificacao = 0;
      this.observacao = null;
      this.numeroBanco = null;
      this.numeroAgencia = null;
      this.numeroConta = null;
      this.tipoConta = null;
      this.tipoPessoa = null;
      this.cpfCnpj = null;
      this.nomePessoa = null;
      this.nomeDocumentoIdentificacao = null;
      this.numeroDocumentoIdentificacao = null;
      this.codigoChave = 0L;
      this.codigoChaveExtrato = 0L;
   }

   public long getIdExtrato() {
      return this.idExtrato;
   }

   public void setIdExtrato(long idExtrato) {
      this.idExtrato = idExtrato;
   }

   public long getValorOperacao() {
      return this.valorOperacao;
   }

   public void setValorOperacao(long valorOperacao) {
      this.valorOperacao = valorOperacao;
   }

   public String getNumeroDocumento() {
      return this.numeroDocumento;
   }

   public void setNumeroDocumento(String numeroDocumento) {
      this.numeroDocumento = numeroDocumento;
   }

   public String getCodigoBarras() {
      return this.codigoBarras;
   }

   public void setCodigoBarras(String codigoBarras) {
      this.codigoBarras = codigoBarras;
   }

   public String getNomeEndossanteCheque() {
      return this.nomeEndossanteCheque;
   }

   public void setNomeEndossanteCheque(String nomeEndossanteCheque) {
      this.nomeEndossanteCheque = nomeEndossanteCheque;
   }

   public String getDocEndossanteCheque() {
      return this.docEndossanteCheque;
   }

   public void setDocEndossanteCheque(String docEndossanteCheque) {
      this.docEndossanteCheque = docEndossanteCheque;
   }

   public int getSituacaoIdentificacao() {
      return this.situacaoIdentificacao;
   }

   public void setSituacaoIdentificacao(int situacaoIdentificacao) {
      this.situacaoIdentificacao = situacaoIdentificacao;
   }

   public String getObservacao() {
      return this.observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public String getNumeroBanco() {
      return this.numeroBanco;
   }

   public void setNumeroBanco(String numeroBanco) {
      this.numeroBanco = numeroBanco;
   }

   public Integer getNumeroAgencia() {
      return this.numeroAgencia;
   }

   public void setNumeroAgencia(Integer numeroAgencia) {
      this.numeroAgencia = numeroAgencia;
   }

   public String getNumeroConta() {
      return this.numeroConta;
   }

   public void setNumeroConta(String numeroConta) {
      this.numeroConta = numeroConta;
   }

   public String getTipoConta() {
      return this.tipoConta;
   }

   public void setTipoConta(String tipoConta) {
      this.tipoConta = tipoConta;
   }

   public String getTipoPessoa() {
      return this.tipoPessoa;
   }

   public void setTipoPessoa(String tipoPessoa) {
      this.tipoPessoa = tipoPessoa;
   }

   public String getCpfCnpj() {
      return this.cpfCnpj;
   }

   public void setCpfCnpj(String cpfCnpj) {
      this.cpfCnpj = cpfCnpj;
   }

   public String getNomePessoa() {
      return this.nomePessoa;
   }

   public void setNomePessoa(String nomePessoa) {
      this.nomePessoa = nomePessoa;
   }

   public String getNomeDocumentoIdentificacao() {
      return this.nomeDocumentoIdentificacao;
   }

   public void setNomeDocumentoIdentificacao(String nomeDocumentoIdentificacao) {
      this.nomeDocumentoIdentificacao = nomeDocumentoIdentificacao;
   }

   public String getNumeroDocumentoIdentificacao() {
      return this.numeroDocumentoIdentificacao;
   }

   public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
      this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
   }

   public long getCodigoChave() {
      return this.codigoChave;
   }

   public void setCodigoChave(long codigoChave) {
      this.codigoChave = codigoChave;
   }

   public long getCodigoChaveExtrato() {
      return this.codigoChaveExtrato;
   }

   public void setCodigoChaveExtrato(long codigoChaveExtrato) {
      this.codigoChaveExtrato = codigoChaveExtrato;
   }
}
