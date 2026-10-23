package br.mp.mpf.spea.simba.validador.enums;

public enum SimNao {
   NAO("0", "Não"),
   SIM("1", "Sim");

   private String codigo;
   private String descricao;

   private SimNao(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
   }

   public static SimNao get(String codigo) {
      SimNao[] values = values();

      for(SimNao simNao : values) {
         if (simNao.getCodigo().equals(codigo)) {
            return simNao;
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
   private static SimNao[] $values() {
      return new SimNao[]{NAO, SIM};
   }
}
