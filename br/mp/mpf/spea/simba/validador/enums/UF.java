package br.mp.mpf.spea.simba.validador.enums;

public enum UF {
   AC("Acre"),
   AL("Alagoas"),
   AP("Amapá"),
   AM("Amazonas"),
   BA("Bahia"),
   CE("Ceará"),
   DF("Distrito Federal"),
   ES("Espírito Santo"),
   GO("Goiás"),
   MA("Maranhão"),
   MT("Mato Grosso"),
   MS("Mato Grosso do Sul"),
   MG("Minas Gerais"),
   PA("Pará"),
   PB("Paraíba"),
   PR("Paraná"),
   PE("Pernambuco"),
   PI("Piauí"),
   RJ("Rio de Janeiro"),
   RN("Rio Grande do Norte"),
   RS("Rio Grande do Sul"),
   RO("Rondônia"),
   RR("Roraima"),
   SC("Santa Catarina"),
   SP("São Paulo"),
   SE("Sergipe"),
   TO("Tocantins");

   private String descricao;

   private UF(String descricao) {
      this.descricao = descricao;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public static UF get(String texto) {
      if (texto == null) {
         return null;
      } else {
         UF[] ufs = values();

         for(UF uf : ufs) {
            if (texto.equals(uf.toString())) {
               return uf;
            }
         }

         return null;
      }
   }

   // $FF: synthetic method
   private static UF[] $values() {
      return new UF[]{AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO};
   }
}
