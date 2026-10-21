package br.mp.mpf.spea.simba.validador.enums;

public enum TipoConta {
   CONTA_CORRENTE("1", "Conta Corrente"),
   POUPANCA("2", "Poupança"),
   INVESTIMENTO("3", "Investimento"),
   OUTROS("4", "Outros");

   private String codigo;
   private String descricao;

   private TipoConta(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
   }

   public static TipoConta get(String codigo) {
      TipoConta[] values = values();

      for(TipoConta tipoConta : values) {
         if (tipoConta.getCodigo().equals(codigo)) {
            return tipoConta;
         }
      }

      return null;
   }

   public String getCodigo() {
      return this.codigo;
   }

   public int getCodigoInt() {
      return Integer.valueOf(this.codigo);
   }

   public String getDescricao() {
      return this.descricao;
   }

   // $FF: synthetic method
   private static TipoConta[] $values() {
      return new TipoConta[]{CONTA_CORRENTE, POUPANCA, INVESTIMENTO, OUTROS};
   }
}
