package br.mp.mpf.spea.simba.validador.enums;

public enum TipoPessoa {
   F("Pessoa Física"),
   J("Pessoa Jurídica");

   private String descricao;

   private TipoPessoa(String descricao) {
      this.descricao = descricao;
   }

   public String getDescricao() {
      return this.descricao;
   }

   // $FF: synthetic method
   private static TipoPessoa[] $values() {
      return new TipoPessoa[]{F, J};
   }
}
