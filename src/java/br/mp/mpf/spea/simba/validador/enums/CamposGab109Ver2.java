package br.mp.mpf.spea.simba.validador.enums;

public enum CamposGab109Ver2 {
   DATA_DO_MOVIMENTO(8, 1),
   DATA_DE_REFERENCIA(8, 2),
   CODIGO_DO_CLIENTE(9, 3),
   DATA_DE_LIQUIDACAO(8, 4),
   DESCRICAO_DO_LANCAMENTO(80, 5),
   VALOR_DO_LANCAMENTO(15, 6),
   NATUREZA_LANCAMENTO(1, 7),
   CHAVE_OD(20, 8),
   BANCO_ORIGEM(8, 9),
   AGENCIA_ORIGEM(5, 10),
   CONTA_ORIGEM(15, 11),
   CPF_CNPJ_ORIGEM(14, 12),
   BANCO_DESTINO(8, 13),
   AGENCIA_DESTINO(5, 14),
   CONTA_DESTINO(15, 15),
   CPF_CNPJ_DESTINO(14, 16),
   OBSERVACAO(100, 17),
   LINHA_ARQUIVO(0, 18);

   private int tamanhoCampo;
   private int codigoCampo;

   private CamposGab109Ver2(int tamanho, int codigo) {
      this.tamanhoCampo = tamanho;
      this.codigoCampo = codigo;
   }

   public static int getTamanhoLinha() {
      int tamanhoLinha = 0;

      for(CamposGab109Ver2 camposGab109 : values()) {
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
   private static CamposGab109Ver2[] $values() {
      return new CamposGab109Ver2[]{DATA_DO_MOVIMENTO, DATA_DE_REFERENCIA, CODIGO_DO_CLIENTE, DATA_DE_LIQUIDACAO, DESCRICAO_DO_LANCAMENTO, VALOR_DO_LANCAMENTO, NATUREZA_LANCAMENTO, CHAVE_OD, BANCO_ORIGEM, AGENCIA_ORIGEM, CONTA_ORIGEM, CPF_CNPJ_ORIGEM, BANCO_DESTINO, AGENCIA_DESTINO, CONTA_DESTINO, CPF_CNPJ_DESTINO, OBSERVACAO, LINHA_ARQUIVO};
   }
}
