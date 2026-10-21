package br.mp.mpf.spea.simba.validador.to;

import java.util.Date;

public class ExtratoOrigemDestino {
   private int linhaArquivoExtrato;
   private int linhaArquivoOd;
   private int tipoLancamento;
   private Date dataLancamento;
   private Long valorLancamento;
   private String cpfCnpjPessoaOd;
   private String numeroBancoOd;
   private String numeroAgenciaOd;
   private String numeroContaOd;
   private String tipoPessoaOd;
   private boolean nomePessoaOdNulo;
   private boolean observacaoNaoCorrentistaOdNulo;
   private boolean observacaoPortadorOdNulo;

   public int getLinhaArquivoExtrato() {
      return this.linhaArquivoExtrato;
   }

   public void setLinhaArquivoExtrato(int linhaArquivoExtrato) {
      this.linhaArquivoExtrato = linhaArquivoExtrato;
   }

   public int getLinhaArquivoOd() {
      return this.linhaArquivoOd;
   }

   public void setLinhaArquivoOd(int linhaArquivoOd) {
      this.linhaArquivoOd = linhaArquivoOd;
   }

   public int getTipoLancamento() {
      return this.tipoLancamento;
   }

   public void setTipoLancamento(int tipoLancamento) {
      this.tipoLancamento = tipoLancamento;
   }

   public Date getDataLancamento() {
      return this.dataLancamento;
   }

   public void setDataLancamento(Date dataLancamento) {
      this.dataLancamento = dataLancamento;
   }

   public Long getValorLancamento() {
      return this.valorLancamento;
   }

   public void setValorLancamento(Long valorLancamento) {
      this.valorLancamento = valorLancamento;
   }

   public String getCpfCnpjPessoaOd() {
      return this.cpfCnpjPessoaOd;
   }

   public void setCpfCnpjPessoaOd(String cpfCnpjPessoaOd) {
      this.cpfCnpjPessoaOd = cpfCnpjPessoaOd;
   }

   public String getNumeroBancoOd() {
      return this.numeroBancoOd;
   }

   public void setNumeroBancoOd(String numeroBancoOd) {
      this.numeroBancoOd = numeroBancoOd;
   }

   public String getNumeroAgenciaOd() {
      return this.numeroAgenciaOd;
   }

   public void setNumeroAgenciaOd(String numeroAgenciaOd) {
      this.numeroAgenciaOd = numeroAgenciaOd;
   }

   public String getNumeroContaOd() {
      return this.numeroContaOd;
   }

   public void setNumeroContaOd(String numeroContaOd) {
      this.numeroContaOd = numeroContaOd;
   }

   public String getTipoPessoaOd() {
      return this.tipoPessoaOd;
   }

   public void setTipoPessoaOd(String tipoPessoaOd) {
      this.tipoPessoaOd = tipoPessoaOd;
   }

   public boolean isNomePessoaOdNulo() {
      return this.nomePessoaOdNulo;
   }

   public void setNomePessoaOdNulo(boolean nomePessoaOdNulo) {
      this.nomePessoaOdNulo = nomePessoaOdNulo;
   }

   public boolean isObservacaoNaoCorrentistaOdNulo() {
      return this.observacaoNaoCorrentistaOdNulo;
   }

   public void setObservacaoNaoCorrentistaOdNulo(boolean observacaoNaoCorrentistaOdNulo) {
      this.observacaoNaoCorrentistaOdNulo = observacaoNaoCorrentistaOdNulo;
   }

   public boolean isObservacaoPortadorOdNulo() {
      return this.observacaoPortadorOdNulo;
   }

   public void setObservacaoPortadorOdNulo(boolean observacaoPortadorOdNulo) {
      this.observacaoPortadorOdNulo = observacaoPortadorOdNulo;
   }
}
