package br.mp.mpf.spea.simba.validador.domain;

import br.mp.mpf.pgr.util.StringUtil;

public class Pessoa extends Linha {
   private Long codigoInvestigado;
   private String tipo;
   private String cpfCnpj;
   private String nome;
   private String nomeDocIdentificacao;
   private String numeroDocIdentificacao;
   private String enderecoLogradouro;
   private String enderecoCidade;
   private String enderecoUf;
   private String enderecoPais;
   private String enderecoCep;
   private String telefone;
   private Double valorRenda;
   private String dataAtualizacaoRenda;

   public void clear() {
      super.clear();
      this.tipo = null;
      this.cpfCnpj = null;
      this.nome = null;
      this.nomeDocIdentificacao = null;
      this.numeroDocIdentificacao = null;
      this.enderecoLogradouro = null;
      this.enderecoCidade = null;
      this.enderecoUf = null;
      this.enderecoPais = null;
      this.enderecoCep = null;
      this.telefone = null;
      this.valorRenda = null;
      this.dataAtualizacaoRenda = null;
      this.codigoInvestigado = null;
   }

   public String toString() {
      return !StringUtil.isEmpty(this.cpfCnpj) && !StringUtil.isEmpty(this.nome) ? "CPF/CNPJ: " + this.cpfCnpj + " Nome: " + this.nome : super.toString();
   }

   public String getTipo() {
      return this.tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getCpfCnpj() {
      return this.cpfCnpj;
   }

   public void setCpfCnpj(String cpfCnpj) {
      this.cpfCnpj = cpfCnpj;
   }

   public String getNome() {
      return this.nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getNomeDocIdentificacao() {
      return this.nomeDocIdentificacao;
   }

   public void setNomeDocIdentificacao(String nomeDocIdentificacao) {
      this.nomeDocIdentificacao = nomeDocIdentificacao;
   }

   public String getNumeroDocIdentificacao() {
      return this.numeroDocIdentificacao;
   }

   public void setNumeroDocIdentificacao(String numeroDocIdentificacao) {
      this.numeroDocIdentificacao = numeroDocIdentificacao;
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

   public String getTelefone() {
      return this.telefone;
   }

   public void setTelefone(String telefone) {
      this.telefone = telefone;
   }

   public Double getValorRenda() {
      return this.valorRenda;
   }

   public void setValorRenda(Double valorRenda) {
      this.valorRenda = valorRenda;
   }

   public String getDataAtualizacaoRenda() {
      return this.dataAtualizacaoRenda;
   }

   public void setDataAtualizacaoRenda(String dataAtualizacaoRenda) {
      this.dataAtualizacaoRenda = dataAtualizacaoRenda;
   }

   public Long getCodigoInvestigado() {
      return this.codigoInvestigado;
   }

   public void setCodigoInvestigado(Long codigoInvestigado) {
      this.codigoInvestigado = codigoInvestigado;
   }
}
