package br.mp.mpf.spea.simba.validador.enums;

public enum TipoPessoaInvestigada {
   NAO_INVESTIGADA("0", "Não teve o sigilo afastado"),
   INVESTIGADA("1", "Teve o sigilo afastado");

   private String codigo;
   private String descricao;

   private TipoPessoaInvestigada(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
   }

   public static TipoPessoaInvestigada get(String codigo) {
      TipoPessoaInvestigada[] values = values();

      for(TipoPessoaInvestigada tipoPessoa : values) {
         if (tipoPessoa.getCodigo().equals(codigo)) {
            return tipoPessoa;
         }
      }

      return null;
   }

   public String getCodigo() {
      return this.codigo;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public boolean isInvestigada() {
      return INVESTIGADA.getCodigo().equals(this.codigo);
   }

   // $FF: synthetic method
   private static TipoPessoaInvestigada[] $values() {
      return new TipoPessoaInvestigada[]{NAO_INVESTIGADA, INVESTIGADA};
   }
}
