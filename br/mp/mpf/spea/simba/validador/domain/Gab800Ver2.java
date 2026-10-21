package br.mp.mpf.spea.simba.validador.domain;

public class Gab800Ver2 extends Linha {
   private String codigoCliente;
   private String nomeCliente;
   private String endereco;
   private String enderecoComplemento;
   private String dataReferencia;
   private String dataLiquidacao;
   private String descricaoLancamento;
   private String saldoInicial;
   private String naturezaSaldoInicial;
   private String valorLancamento;
   private String naturezaLancamento;
   private String chaveOD;

   public String getCodigoCliente() {
      return this.codigoCliente;
   }

   public String getNomeCliente() {
      return this.nomeCliente;
   }

   public String getEndereco() {
      return this.endereco;
   }

   public String getEnderecoComplemento() {
      return this.enderecoComplemento;
   }

   public String getDataReferencia() {
      return this.dataReferencia;
   }

   public String getDataLiquidacao() {
      return this.dataLiquidacao;
   }

   public String getDescricaoLancamento() {
      return this.descricaoLancamento;
   }

   public String getSaldoInicial() {
      return this.saldoInicial;
   }

   public String getNaturezaSaldoInicial() {
      return this.naturezaSaldoInicial;
   }

   public String getValorLancamento() {
      return this.valorLancamento;
   }

   public String getNaturezaLancamento() {
      return this.naturezaLancamento;
   }

   public void setCodigoCliente(String codigoCliente) {
      this.codigoCliente = codigoCliente;
   }

   public void setNomeCliente(String nomeCliente) {
      this.nomeCliente = nomeCliente;
   }

   public void setEndereco(String endereco) {
      this.endereco = endereco;
   }

   public void setEnderecoComplemento(String enderecoComplemento) {
      this.enderecoComplemento = enderecoComplemento;
   }

   public void setDataReferencia(String dataReferencia) {
      this.dataReferencia = dataReferencia;
   }

   public void setDataLiquidacao(String dataLiquidacao) {
      this.dataLiquidacao = dataLiquidacao;
   }

   public void setDescricaoLancamento(String descricaoLancamento) {
      this.descricaoLancamento = descricaoLancamento;
   }

   public void setSaldoInicial(String saldoInicial) {
      this.saldoInicial = saldoInicial;
   }

   public void setNaturezaSaldoInicial(String naturezaSaldoInicial) {
      this.naturezaSaldoInicial = naturezaSaldoInicial;
   }

   public void setValorLancamento(String valorLancamento) {
      this.valorLancamento = valorLancamento;
   }

   public void setNaturezaLancamento(String naturezaLancamento) {
      this.naturezaLancamento = naturezaLancamento;
   }

   public String getChaveOD() {
      return this.chaveOD;
   }

   public void setChaveOD(String chaveOD) {
      this.chaveOD = chaveOD;
   }
}
