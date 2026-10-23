package br.mp.mpf.spea.simba.validador.enums;

public enum TipoPessoaOrigemDestino {
   PF("1", "Pessoa Física"),
   PJ("2", "Pessoa Jurídica");

   private String codigo;
   private String descricao;

   private TipoPessoaOrigemDestino(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
   }

   public static TipoPessoaOrigemDestino get(String codigo) {
      TipoPessoaOrigemDestino[] values = values();

      for(TipoPessoaOrigemDestino tipoPessoa : values) {
         if (tipoPessoa.getCodigo().equals(codigo)) {
            return tipoPessoa;
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
   private static TipoPessoaOrigemDestino[] $values() {
      return new TipoPessoaOrigemDestino[]{PF, PJ};
   }
}
