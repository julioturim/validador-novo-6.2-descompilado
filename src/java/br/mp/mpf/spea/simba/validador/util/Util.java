package br.mp.mpf.spea.simba.validador.util;

import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.enums.CamposInvestigado;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Util {
   public static Date getDateGab(String data) throws ParseException {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      if (StringUtil.isNotEmpty(data)) {
         Date ret = sdf.parse(data.trim());
         if (sdf.format(ret).equals(data.trim())) {
            return ret;
         }
      }

      return null;
   }

   public static java.sql.Date getDateGabSql(String data) throws ParseException {
      return new java.sql.Date(getDateGab(data).getTime());
   }

   public static java.sql.Date getDate(String data) throws ParseException {
      if (StringUtil.isEmpty(data)) {
         return null;
      } else {
         SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
         Date date = sdf.parse(data);
         return new java.sql.Date(date.getTime());
      }
   }

   public static String[] tabSeparatedStringToArray(String text) {
      String[] dados = text.split("(?=\t)");

      for(int j = 0; j < dados.length; ++j) {
         dados[j] = dados[j].trim();
      }

      return dados;
   }

   public static String toCsv(String[] array) {
      if (array == null) {
         return null;
      } else if (array.length == 0) {
         return "";
      } else {
         StringBuilder csv = new StringBuilder();
         csv.append(array[0]);

         for(int i = 1; i < array.length; ++i) {
            csv.append(", ");
            csv.append(array[i]);
         }

         return csv.toString();
      }
   }

   public static String arrayParaCSV(Set<CamposInvestigado> set) {
      StringBuilder listaCSV = new StringBuilder();
      boolean primeiro = true;

      for(CamposInvestigado key : set) {
         if (primeiro) {
            listaCSV.append(key);
            primeiro = false;
         } else {
            listaCSV.append(", " + String.valueOf(key));
         }
      }

      return listaCSV.toString();
   }

   public static boolean isNumber(String texto) {
      try {
         Long.parseLong(texto);
         new BigInteger(texto);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }
}
