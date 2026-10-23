package br.mp.mpf.spea.simba.validador.domain;

import java.time.LocalDate;

public class Atendimento {
   private Long id;
   private String numeroCaso;
   private String cnpj;
   private Integer numeroBanco;
   private String nomeBanco;
   private String nomeBancoAtendimento;
   private LocalDate dataGeracaoArquivo;
   private String nomeFuncionarioResponsavel;
   private String telefoneFuncionarioResponsavel;
   private String emailFuncionarioResponsavel;
   private LocalDate dataInicioAfastamento;
   private LocalDate dataFimAfastamento;
   private String numeroCorreioBacen;
   private String numeroOficio;
   private LocalDate dataOficio;
   private String numeroProcesso;
   private String nomeTribunal;
   private String numeroVara;
   private String nomeMagistrado;
   private String cargoMagistrado;
   private String descricaoConteudo;
   private String versaoValidador;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNumeroCaso() {
      return this.numeroCaso;
   }

   public void setNumeroCaso(String numeroCaso) {
      this.numeroCaso = numeroCaso;
   }

   public Integer getNumeroBanco() {
      return this.numeroBanco;
   }

   public void setNumeroBanco(Integer numeroBanco) {
      this.numeroBanco = numeroBanco;
   }

   public String getNomeBanco() {
      return this.nomeBanco;
   }

   public void setNomeBanco(String nomeBanco) {
      this.nomeBanco = nomeBanco;
   }

   public String getNomeBancoAtendimento() {
      return this.nomeBancoAtendimento;
   }

   public void setNomeBancoAtendimento(String nomeBancoAtendimento) {
      this.nomeBancoAtendimento = nomeBancoAtendimento;
   }

   public LocalDate getDataGeracaoArquivo() {
      return this.dataGeracaoArquivo;
   }

   public void setDataGeracaoArquivo(LocalDate dataGeracaoArquivo) {
      this.dataGeracaoArquivo = dataGeracaoArquivo;
   }

   public String getNomeFuncionarioResponsavel() {
      return this.nomeFuncionarioResponsavel;
   }

   public void setNomeFuncionarioResponsavel(String nomeFuncionarioResponsavel) {
      this.nomeFuncionarioResponsavel = nomeFuncionarioResponsavel;
   }

   public String getTelefoneFuncionarioResponsavel() {
      return this.telefoneFuncionarioResponsavel;
   }

   public void setTelefoneFuncionarioResponsavel(String telefoneFuncionarioResponsavel) {
      this.telefoneFuncionarioResponsavel = telefoneFuncionarioResponsavel;
   }

   public String getEmailFuncionarioResponsavel() {
      return this.emailFuncionarioResponsavel;
   }

   public void setEmailFuncionarioResponsavel(String emailFuncionarioResponsavel) {
      this.emailFuncionarioResponsavel = emailFuncionarioResponsavel;
   }

   public LocalDate getDataInicioAfastamento() {
      return this.dataInicioAfastamento;
   }

   public void setDataInicioAfastamento(LocalDate dataInicioAfastamento) {
      this.dataInicioAfastamento = dataInicioAfastamento;
   }

   public LocalDate getDataFimAfastamento() {
      return this.dataFimAfastamento;
   }

   public void setDataFimAfastamento(LocalDate dataFimAfastamento) {
      this.dataFimAfastamento = dataFimAfastamento;
   }

   public String getNumeroCorreioBacen() {
      return this.numeroCorreioBacen;
   }

   public void setNumeroCorreioBacen(String numeroCorreioBacen) {
      this.numeroCorreioBacen = numeroCorreioBacen;
   }

   public String getNumeroOficio() {
      return this.numeroOficio;
   }

   public void setNumeroOficio(String numeroOficio) {
      this.numeroOficio = numeroOficio;
   }

   public LocalDate getDataOficio() {
      return this.dataOficio;
   }

   public void setDataOficio(LocalDate dataOficio) {
      this.dataOficio = dataOficio;
   }

   public String getNumeroProcesso() {
      return this.numeroProcesso;
   }

   public void setNumeroProcesso(String numeroProcesso) {
      this.numeroProcesso = numeroProcesso;
   }

   public String getNomeTribunal() {
      return this.nomeTribunal;
   }

   public void setNomeTribunal(String nomeTribunal) {
      this.nomeTribunal = nomeTribunal;
   }

   public String getNumeroVara() {
      return this.numeroVara;
   }

   public void setNumeroVara(String numeroVara) {
      this.numeroVara = numeroVara;
   }

   public String getNomeMagistrado() {
      return this.nomeMagistrado;
   }

   public void setNomeMagistrado(String nomeMagistrado) {
      this.nomeMagistrado = nomeMagistrado;
   }

   public String getCargoMagistrado() {
      return this.cargoMagistrado;
   }

   public void setCargoMagistrado(String cargoMagistrado) {
      this.cargoMagistrado = cargoMagistrado;
   }

   public String getDescricaoConteudo() {
      return this.descricaoConteudo;
   }

   public void setDescricaoConteudo(String descricaoConteudo) {
      this.descricaoConteudo = descricaoConteudo;
   }

   public String getVersaoValidador() {
      return this.versaoValidador;
   }

   public void setVersaoValidador(String versaoValidador) {
      this.versaoValidador = versaoValidador;
   }

   public String getCnpj() {
      return this.cnpj;
   }

   public void setCnpj(String cnpj) {
      this.cnpj = cnpj;
   }
}
