package br.mp.mpf.spea.simba.validador.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TipoTitularidade {
   public static TipoTitularidade TITULAR = new TipoTitularidade("T", "Titular");
   public static TipoTitularidade CO_TITULAR_1 = new TipoTitularidade("1", "1o. Co-titular");
   public static TipoTitularidade CO_TITULAR_2 = new TipoTitularidade("2", "2o. Co-titular");
   public static TipoTitularidade CO_TITULAR_3 = new TipoTitularidade("3", "3o. Co-titular");
   public static TipoTitularidade CO_TITULAR_4 = new TipoTitularidade("4", "4o. Co-titular");
   public static TipoTitularidade REPRESENTANTE = new TipoTitularidade("R", "Representante");
   public static TipoTitularidade REPRESENTANTE_LEGAL = new TipoTitularidade("L", "Representante Legal");
   public static TipoTitularidade PROCURADOR = new TipoTitularidade("P", "Procurador");
   public static TipoTitularidade OUTROS = new TipoTitularidade("O", "Outros");
   private static Map<String, TipoTitularidade> map;
   private static final Pattern digitoRegex = Pattern.compile("[0-9]+");
   private String codigo;
   private String descricao;
   private int numeroCotitular;

   private TipoTitularidade(String codigo, String descricao) {
      this.codigo = codigo;
      this.descricao = descricao;
      if (digitoRegex.matcher(codigo).matches()) {
         this.numeroCotitular = Integer.valueOf(codigo);
      }

   }

   public static TipoTitularidade get(String codigo) {
      codigo = codigo.toUpperCase();
      Map<String, TipoTitularidade> map = getMap();
      if (map.containsKey(codigo)) {
         return (TipoTitularidade)map.get(codigo);
      } else {
         return digitoRegex.matcher(codigo).matches() ? new TipoTitularidade(codigo, codigo + "o. Co-titular") : null;
      }
   }

   private static Map<String, TipoTitularidade> getMap() {
      if (map == null) {
         map = new HashMap();
         map.put(TITULAR.getCodigo(), TITULAR);
         map.put(CO_TITULAR_1.getCodigo(), CO_TITULAR_1);
         map.put(CO_TITULAR_2.getCodigo(), CO_TITULAR_2);
         map.put(CO_TITULAR_3.getCodigo(), CO_TITULAR_3);
         map.put(CO_TITULAR_4.getCodigo(), CO_TITULAR_4);
         map.put(REPRESENTANTE.getCodigo(), REPRESENTANTE);
         map.put(REPRESENTANTE_LEGAL.getCodigo(), REPRESENTANTE_LEGAL);
         map.put(PROCURADOR.getCodigo(), PROCURADOR);
         map.put(OUTROS.getCodigo(), OUTROS);
      }

      return map;
   }

   public String getCodigo() {
      return this.codigo;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public int getNumeroCotitular() {
      return this.numeroCotitular;
   }
}
