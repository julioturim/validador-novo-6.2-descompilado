package br.mp.mpf.spea.simba.validador.domain;

public class Titularidade extends Linha {
   private long idPessoa;
   private long idConta;
   private String tipo;
   private String pessoaInvestigada;
   private String dataInicioRelacionamento;
   private String dataFimRelacionamento;

   public void clear() {
      super.clear();
      this.idPessoa = 0L;
      this.idConta = 0L;
      this.tipo = null;
      this.pessoaInvestigada = null;
      this.dataInicioRelacionamento = null;
      this.dataFimRelacionamento = null;
   }

   public String toString() {
      return this.idPessoa != 0L && this.idConta != 0L && this.tipo != null && this.pessoaInvestigada != null ? "Titularidade - IdPessoa: " + this.idPessoa + " IdConta: " + this.idConta + " Tipo: " + this.tipo + " Investigada: " + this.pessoaInvestigada : super.toString();
   }

   public long getIdPessoa() {
      return this.idPessoa;
   }

   public void setIdPessoa(long idPessoa) {
      this.idPessoa = idPessoa;
   }

   public long getIdConta() {
      return this.idConta;
   }

   public void setIdConta(long idConta) {
      this.idConta = idConta;
   }

   public String getTipo() {
      return this.tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getPessoaInvestigada() {
      return this.pessoaInvestigada;
   }

   public void setPessoaInvestigada(String pessoaInvestigada) {
      this.pessoaInvestigada = pessoaInvestigada;
   }

   public String getDataInicioRelacionamento() {
      return this.dataInicioRelacionamento;
   }

   public void setDataInicioRelacionamento(String dataInicioRelacionamento) {
      this.dataInicioRelacionamento = dataInicioRelacionamento;
   }

   public String getDataFimRelacionamento() {
      return this.dataFimRelacionamento;
   }

   public void setDataFimRelacionamento(String dataFimRelacionamento) {
      this.dataFimRelacionamento = dataFimRelacionamento;
   }
}
