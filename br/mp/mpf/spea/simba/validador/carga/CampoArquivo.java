package br.mp.mpf.spea.simba.validador.carga;

public class CampoArquivo {
   private String nome;
   private TipoCampoArquivo tipo;
   private Integer maxLength;

   public CampoArquivo(String nome, TipoCampoArquivo tipo, Integer maxLength) {
      this.nome = nome;
      this.tipo = tipo;
      this.maxLength = maxLength;
   }

   public static CampoArquivo from(String nome, TipoCampoArquivo tipo, Integer maxLength) {
      return new CampoArquivo(nome, tipo, maxLength);
   }

   public String getNome() {
      return this.nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public Integer getMaxLength() {
      return this.maxLength;
   }

   public void setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
   }

   public TipoCampoArquivo getTipo() {
      return this.tipo;
   }

   public void setTipo(TipoCampoArquivo tipo) {
      this.tipo = tipo;
   }
}
