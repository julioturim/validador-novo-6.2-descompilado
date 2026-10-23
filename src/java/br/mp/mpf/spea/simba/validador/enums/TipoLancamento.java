package br.mp.mpf.spea.simba.validador.enums;

public enum TipoLancamento {
   C("Crédito"),
   D("Débito");

   private String descricao;

   private TipoLancamento(String descricao) {
      this.descricao = descricao;
   }

   public String getDescricao() {
      return this.descricao;
   }

   // $FF: synthetic method
   private static TipoLancamento[] $values() {
      return new TipoLancamento[]{C, D};
   }
}
