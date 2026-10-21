package br.mp.mpf.spea.simba.validador.enums;

public enum CamposGab800 {
   CODIGO_DO_CLIENTE(7, 1),
   NOME_DO_CLIENTE(60, 2),
   ENDERECO(52, 3),
   ENDERECO_COMPLEMENTO(47, 4),
   DATA_DE_REFERENCIA(8, 5),
   DATA_DE_LIQUIDACAO(8, 6),
   DESCRICAO_DO_LANCAMENTO(80, 7),
   SALDO_INICIAL(17, 8),
   NATUREZA_DO_SALDO_INICIAL(1, 9),
   VALOR_DO_LANCAMENTO(17, 10),
   NATUREZA_DO_LANCAMENTO(1, 11),
   LINHA_ARQUIVO(0, 12);

   private int tamanhoCampo;
   private int codigoCampo;

   private CamposGab800(int tamanho, int codigo) {
      this.tamanhoCampo = tamanho;
      this.codigoCampo = codigo;
   }

   public static int getTamanhoLinha() {
      int tamanhoLinha = 0;

      for(CamposGab800 camposGab800 : values()) {
         tamanhoLinha += camposGab800.getTamanhoCampo();
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
   private static CamposGab800[] $values() {
      return new CamposGab800[]{CODIGO_DO_CLIENTE, NOME_DO_CLIENTE, ENDERECO, ENDERECO_COMPLEMENTO, DATA_DE_REFERENCIA, DATA_DE_LIQUIDACAO, DESCRICAO_DO_LANCAMENTO, SALDO_INICIAL, NATUREZA_DO_SALDO_INICIAL, VALOR_DO_LANCAMENTO, NATUREZA_DO_LANCAMENTO, LINHA_ARQUIVO};
   }
}
