package br.mp.mpf.spea.simba.validador.domain;

import br.mp.mpf.spea.simba.validador.enums.SimNao;
import br.mp.mpf.spea.simba.validador.enums.TipoPessoaOrigemDestino;
import java.time.LocalDate;

public class Investigado extends Linha {
   private Long codigo;
   private String cpfCnpj;
   private String nomePessoa;
   private String observacao;
   private LocalDate dataInicioAfastamento;
   private LocalDate dataFimAfastamento;
   private TipoPessoaOrigemDestino tipoPessoa;
   private SimNao teveRelacionamento;
   private SimNao teveConta;
   private SimNao teveBdv;

   public void clear() {
      super.clear();
      this.codigo = null;
      this.cpfCnpj = null;
      this.nomePessoa = null;
      this.observacao = null;
      this.dataInicioAfastamento = null;
      this.dataFimAfastamento = null;
      this.tipoPessoa = null;
      this.teveRelacionamento = null;
      this.teveConta = null;
      this.teveBdv = null;
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

   public String getObservacao() {
      return this.observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
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

   public TipoPessoaOrigemDestino getTipoPessoa() {
      return this.tipoPessoa;
   }

   public void setTipoPessoa(TipoPessoaOrigemDestino tipoPessoa) {
      this.tipoPessoa = tipoPessoa;
   }

   public SimNao getTeveRelacionamento() {
      return this.teveRelacionamento;
   }

   public void setTeveRelacionamento(SimNao teveRelacionamento) {
      this.teveRelacionamento = teveRelacionamento;
   }

   public SimNao getTeveConta() {
      return this.teveConta;
   }

   public void setTeveConta(SimNao teveConta) {
      this.teveConta = teveConta;
   }

   public SimNao getTeveBdv() {
      return this.teveBdv;
   }

   public void setTeveBdv(SimNao teveBdv) {
      this.teveBdv = teveBdv;
   }

   public Long getCodigo() {
      return this.codigo;
   }

   public void setCodigo(Long codigo) {
      this.codigo = codigo;
   }
}
