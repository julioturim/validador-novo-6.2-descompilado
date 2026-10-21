package br.mp.mpf.spea.simba.validador.domain;

import br.mp.mpf.pgr.util.StringUtil;

public class Agencia extends Linha {
   private String numeroBanco;
   private String numeroAgencia;
   private String nomeAgencia;
   private String enderecoLogradouro;
   private String enderecoCidade;
   private String enderecoUf;
   private String enderecoPais;
   private String enderecoCep;
   private String telefoneAgencia;
   private String dataAberturaAgencia;
   private String dataFechamentoAgencia;

   public String toString() {
      return !StringUtil.isEmpty(this.numeroBanco) && !StringUtil.isEmpty(this.numeroAgencia) ? "Banco: " + this.numeroBanco + " Agencia: " + this.numeroAgencia : super.toString();
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

   public String getNomeAgencia() {
      return this.nomeAgencia;
   }

   public void setNomeAgencia(String nomeAgencia) {
      this.nomeAgencia = nomeAgencia;
   }

   public String getEnderecoLogradouro() {
      return this.enderecoLogradouro;
   }

   public void setEnderecoLogradouro(String enderecoLogradouro) {
      this.enderecoLogradouro = enderecoLogradouro;
   }

   public String getEnderecoCidade() {
      return this.enderecoCidade;
   }

   public void setEnderecoCidade(String enderecoCidade) {
      this.enderecoCidade = enderecoCidade;
   }

   public String getEnderecoUf() {
      return this.enderecoUf;
   }

   public void setEnderecoUf(String enderecoUf) {
      this.enderecoUf = enderecoUf;
   }

   public String getEnderecoPais() {
      return this.enderecoPais;
   }

   public void setEnderecoPais(String enderecoPais) {
      this.enderecoPais = enderecoPais;
   }

   public String getEnderecoCep() {
      return this.enderecoCep;
   }

   public void setEnderecoCep(String enderecoCep) {
      this.enderecoCep = enderecoCep;
   }

   public String getTelefoneAgencia() {
      return this.telefoneAgencia;
   }

   public void setTelefoneAgencia(String telefoneAgencia) {
      this.telefoneAgencia = telefoneAgencia;
   }

   public String getDataAberturaAgencia() {
      return this.dataAberturaAgencia;
   }

   public void setDataAberturaAgencia(String dataAberturaAgencia) {
      this.dataAberturaAgencia = dataAberturaAgencia;
   }

   public String getDataFechamentoAgencia() {
      return this.dataFechamentoAgencia;
   }

   public void setDataFechamentoAgencia(String dataFechamentoAgencia) {
      this.dataFechamentoAgencia = dataFechamentoAgencia;
   }
}
