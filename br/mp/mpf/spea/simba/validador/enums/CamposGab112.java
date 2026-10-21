package br.mp.mpf.spea.simba.validador.enums;

public enum CamposGab112 {
   CODIGO_DO_CLIENTE(7, 1),
   DOCUMENTO_DO_CLIENTE(15, 2),
   DATA_DA_CRIACAO(8, 3),
   DATA_DA_ATUALIZACAO(8, 4),
   DATA_NASCIMENTO_FUNDACAO(8, 5),
   TIPO_DE_PESSOA(1, 6),
   NOME_DO_CLIENTE(60, 7),
   DOCUMENTO_DE_IDENTIFICACAO(10, 8),
   NOME_DO_CONJUGE(60, 9),
   CODIGO_DO_CLIENTE_MERCADO(7, 10),
   ENDERECO(30, 11),
   NUMERO_E_COMPLEMENTO(15, 12),
   BAIRRO(18, 13),
   CIDADE(18, 14),
   ESTADO(2, 15),
   CEP(5, 16),
   COMPLEMENTO_DO_CEP(3, 17),
   CONSTANTE_0000A(5, 18),
   CODIGO_DA_ATIVIDADE(3, 19),
   TELEFONE(14, 20),
   MERCADO(10, 21),
   LINHA_ARQUIVO(0, 22);

   private int tamanhoCampo;
   private int codigoCampo;

   private CamposGab112(int tamanho, int codigo) {
      this.tamanhoCampo = tamanho;
      this.codigoCampo = codigo;
   }

   public static int getTamanhoLinha() {
      int tamanhoLinha = 0;

      for(CamposGab112 camposGab112 : values()) {
         tamanhoLinha += camposGab112.getTamanhoCampo();
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
   private static CamposGab112[] $values() {
      return new CamposGab112[]{CODIGO_DO_CLIENTE, DOCUMENTO_DO_CLIENTE, DATA_DA_CRIACAO, DATA_DA_ATUALIZACAO, DATA_NASCIMENTO_FUNDACAO, TIPO_DE_PESSOA, NOME_DO_CLIENTE, DOCUMENTO_DE_IDENTIFICACAO, NOME_DO_CONJUGE, CODIGO_DO_CLIENTE_MERCADO, ENDERECO, NUMERO_E_COMPLEMENTO, BAIRRO, CIDADE, ESTADO, CEP, COMPLEMENTO_DO_CEP, CONSTANTE_0000A, CODIGO_DA_ATIVIDADE, TELEFONE, MERCADO, LINHA_ARQUIVO};
   }
}
