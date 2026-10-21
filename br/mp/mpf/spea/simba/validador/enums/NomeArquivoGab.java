package br.mp.mpf.spea.simba.validador.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum NomeArquivoGab {
   GAB109,
   GAB112,
   GAB800;

   public static List<NomeArquivoGab> getNomesList() {
      return new ArrayList(EnumSet.allOf(NomeArquivoGab.class));
   }

   public static int getTamanhoLinha(NomeArquivoGab arquivosGab, int versaoLayoutGab) {
      switch (arquivosGab.ordinal()) {
         case 0 -> {
            return versaoLayoutGab == 2 ? CamposGab109Ver2.getTamanhoLinha() : CamposGab109.getTamanhoLinha();
         }
         case 1 -> {
            return versaoLayoutGab == 2 ? CamposGab112Ver2.getTamanhoLinha() : CamposGab112.getTamanhoLinha();
         }
         case 2 -> {
            return versaoLayoutGab == 2 ? CamposGab800Ver2.getTamanhoLinha() : CamposGab800.getTamanhoLinha();
         }
         default -> {
            return -1;
         }
      }
   }

   // $FF: synthetic method
   private static NomeArquivoGab[] $values() {
      return new NomeArquivoGab[]{GAB109, GAB112, GAB800};
   }
}
