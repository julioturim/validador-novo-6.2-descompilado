package br.mp.mpf.spea.simba.validador.enums;

public enum CamposGab109 {
   DATA_DO_MOVIMENTO(8, 1),
   DATA_DE_REFERENCIA(8, 2),
   CODIGO_DO_CLIENTE(7, 3),
   DATA_DE_LIQUIDACAO(8, 4),
   DESCRICAO_DO_LANCAMENTO(80, 5),
   VALOR_DO_LANCAMENTO(15, 6),
   NATUREZA_LANCAMENTO(1, 7),
   LINHA_ARQUIVO(0, 8);

   private int tamanhoCampo;
   private int codigoCampo;

   private CamposGab109(int tamanho, int codigo) {
      this.tamanhoCampo = tamanho;
      this.codigoCampo = codigo;
   }

   public static int getTamanhoLinha() {
      int tamanhoLinha = 0;

      for(CamposGab109 camposGab109 : values()) {
         tamanhoLinha += camposGab109.getTamanhoCampo();
      }

      return tamanhoLinha;
   }

   public int getTamanhoCampo() {
      return this.tamanhoCampo;
   }

   public int getCodigoCampo() {
      return this.codigoCampo;
   }

   // $FF: synthetic method
   private static CamposGab109[] $values() {
      return new CamposGab109[]{DATA_DO_MOVIMENTO, DATA_DE_REFERENCIA, CODIGO_DO_CLIENTE, DATA_DE_LIQUIDACAO, DESCRICAO_DO_LANCAMENTO, VALOR_DO_LANCAMENTO, NATUREZA_LANCAMENTO, LINHA_ARQUIVO};
   }
}
