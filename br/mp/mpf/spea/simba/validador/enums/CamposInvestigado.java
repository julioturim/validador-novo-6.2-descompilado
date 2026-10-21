package br.mp.mpf.spea.simba.validador.enums;

public enum CamposInvestigado {
   CODIGO_INVESTIGADO,
   TIPO_PESSOA,
   CPF_CNPJ_PESSOA,
   NOME_PESSOA,
   TEVE_RELACIONAMENTO,
   TEVE_CONTA,
   TEVE_BDV,
   OBSERVACAO,
   DATA_INICIO_AFASTAMENTO,
   DATA_FIM_AFASTAMENTO;

   public static String[] getTodosCampos() {
      String[] camposA = new String[values().length];
      int i = 0;

      for(CamposInvestigado campo : values()) {
         camposA[i++] = campo.toString();
      }

      return camposA;
   }

   public static String[] getCamposArquivo() {
      String[] camposA = new String[values().length - 1];
      int i = 0;

      for(CamposInvestigado campo : values()) {
         if (campo != CODIGO_INVESTIGADO) {
            camposA[i++] = campo.toString();
         }
      }

      return camposA;
   }

   // $FF: synthetic method
   private static CamposInvestigado[] $values() {
      return new CamposInvestigado[]{CODIGO_INVESTIGADO, TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, TEVE_RELACIONAMENTO, TEVE_CONTA, TEVE_BDV, OBSERVACAO, DATA_INICIO_AFASTAMENTO, DATA_FIM_AFASTAMENTO};
   }
}
