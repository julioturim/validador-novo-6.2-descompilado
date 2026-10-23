package br.mp.mpf.spea.simba.validador.domain;

public class Gab109 extends Linha {
   private String dataMovimento;
   private String dataReferencia;
   private String codigoCliente;
   private String dataLiquidacao;
   private String descricaoLancamento;
   private String valorLancamento;
   private String naturezaLancamento;

   public String getDataMovimento() {
      return this.dataMovimento;
   }

   public void setDataMovimento(String dataMovimento) {
      this.dataMovimento = dataMovimento;
   }

   public String getDataReferencia() {
      return this.dataReferencia;
   }

   public void setDataReferencia(String dataReferencia) {
      this.dataReferencia = dataReferencia;
   }

   public String getCodigoCliente() {
      return this.codigoCliente;
   }

   public void setCodigoCliente(String codigoCliente) {
      this.codigoCliente = codigoCliente;
   }

   public String getDescricaoLancamento() {
      return this.descricaoLancamento;
   }

   public void setDescricaoLancamento(String descricaoLancamento) {
      this.descricaoLancamento = descricaoLancamento;
   }

   public String getDataLiquidacao() {
      return this.dataLiquidacao;
   }

   public String getValorLancamento() {
      return this.valorLancamento;
   }

   public String getNaturezaLancamento() {
      return this.naturezaLancamento;
   }

   public void setDataLiquidacao(String dataLiquidacao) {
      this.dataLiquidacao = dataLiquidacao;
   }

   public void setValorLancamento(String valorLancamento) {
      this.valorLancamento = valorLancamento;
   }

   public void setNaturezaLancamento(String naturezaLancamento) {
      this.naturezaLancamento = naturezaLancamento;
   }
}
