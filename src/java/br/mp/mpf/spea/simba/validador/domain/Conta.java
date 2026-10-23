package br.mp.mpf.spea.simba.validador.domain;

import br.mp.mpf.pgr.util.StringUtil;

public class Conta extends Linha {
   private long idAgencia;
   private String numeroBanco;
   private String numeroAgencia;
   private String numeroConta;
   private String tipoConta;
   private String dataAberturaConta;
   private String dataFechamentoConta;
   private String movimentacaoConta;

   public void clear() {
      super.clear();
      this.idAgencia = 0L;
      this.numeroBanco = null;
      this.numeroAgencia = null;
      this.numeroConta = null;
      this.tipoConta = null;
      this.dataAberturaConta = null;
      this.dataFechamentoConta = null;
      this.movimentacaoConta = null;
   }

   public String toString() {
      return !StringUtil.isEmpty(this.numeroBanco) && !StringUtil.isEmpty(this.numeroAgencia) && !StringUtil.isEmpty(this.numeroConta) ? String.format("Banco: % Agencia: % Conta %", this.numeroBanco, this.numeroAgencia, this.numeroConta) : super.toString();
   }

   public String getNumeroBanco() {
      return this.numeroBanco;
   }

   public void setNumeroBanco(String numeroBanco) {
      this.numeroBanco = numeroBanco;
   }

   public String getNumeroAgencia() {
      return this.numeroAgencia;
   }

   public void setNumeroAgencia(String numeroAgencia) {
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

   public String getDataAberturaConta() {
      return this.dataAberturaConta;
   }

   public void setDataAberturaConta(String dataAberturaConta) {
      this.dataAberturaConta = dataAberturaConta;
   }

   public String getDataFechamentoConta() {
      return this.dataFechamentoConta;
   }

   public void setDataFechamentoConta(String dataFechamentoConta) {
      this.dataFechamentoConta = dataFechamentoConta;
   }

   public String getMovimentacaoConta() {
      return this.movimentacaoConta;
   }

   public void setMovimentacaoConta(String movimentacaoConta) {
      this.movimentacaoConta = movimentacaoConta;
   }

   public long getIdAgencia() {
      return this.idAgencia;
   }

   public void setIdAgencia(long idAgencia) {
      this.idAgencia = idAgencia;
   }
}
