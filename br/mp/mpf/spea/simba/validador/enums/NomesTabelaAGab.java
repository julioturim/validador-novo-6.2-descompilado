package br.mp.mpf.spea.simba.validador.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum NomesTabelaAGab {
   ATENDIMENTO,
   GAB109,
   GAB112,
   GAB800;

   public static List<NomesTabelaAGab> getCamposList() {
      return new ArrayList(EnumSet.allOf(NomesTabelaAGab.class));
   }

   public static String[] getStringArray() {
      String[] camposA = new String[values().length];
      int i = 0;

      for(NomesTabelaAGab campo : values()) {
         camposA[i++] = campo.toString();
      }

      return camposA;
   }

   // $FF: synthetic method
   private static NomesTabelaAGab[] $values() {
      return new NomesTabelaAGab[]{ATENDIMENTO, GAB109, GAB112, GAB800};
   }
}
