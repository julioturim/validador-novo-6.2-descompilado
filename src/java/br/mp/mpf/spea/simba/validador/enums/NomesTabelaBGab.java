package br.mp.mpf.spea.simba.validador.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum NomesTabelaBGab {
   ATENDIMENTO,
   GAB109,
   GAB112,
   GAB800,
   INVESTIGADO;

   public static List<NomesTabelaBGab> getCamposList() {
      return new ArrayList(EnumSet.allOf(NomesTabelaBGab.class));
   }

   public static String[] getCamposArray() {
      return (String[])getCamposList().toArray(new String[0]);
   }

   // $FF: synthetic method
   private static NomesTabelaBGab[] $values() {
      return new NomesTabelaBGab[]{ATENDIMENTO, GAB109, GAB112, GAB800, INVESTIGADO};
   }
}
