package br.mp.mpf.spea.simba.validador.domain;

import java.io.File;

public class Arquivos {
   private File atendimento;
   private File agencias;
   private File contas;
   private File extrato;
   private File origemDestino;
   private File titulares;
   private File investigados;

   public File getAgencias() {
      return this.agencias;
   }

   public void setAgencias(File agencias) {
      this.agencias = agencias;
   }

   public File getContas() {
      return this.contas;
   }

   public void setContas(File contas) {
      this.contas = contas;
   }

   public File getExtrato() {
      return this.extrato;
   }

   public void setExtrato(File extrato) {
      this.extrato = extrato;
   }

   public File getOrigemDestino() {
      return this.origemDestino;
   }

   public void setOrigemDestino(File origemDestino) {
      this.origemDestino = origemDestino;
   }

   public File getTitulares() {
      return this.titulares;
   }

   public void setTitulares(File titulares) {
      this.titulares = titulares;
   }

   public File getAtendimento() {
      return this.atendimento;
   }

   public void setAtendimento(File atendimento) {
      this.atendimento = atendimento;
   }

   public File getInvestigados() {
      return this.investigados;
   }

   public void setInvestigados(File investigados) {
      this.investigados = investigados;
   }
}
