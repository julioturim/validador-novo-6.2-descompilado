package br.mp.mpf.spea.simba.validador.rules;

import org.springframework.stereotype.Component;

@Component
public class CnabRule {
   private static final int CNAB_201 = 201;
   private static final int CNAB_223 = 223;
   private static final int CNAB_101 = 101;
   private static final int CNAB_127 = 127;
   private static final int CNAB_116 = 116;
   private static final int CNAB_999 = 999;

   public boolean validarCnabCredito(int cnab) {
      return (cnab < 201 || cnab > 223) && cnab != 999;
   }

   public boolean validarCnabDebito(int cnab) {
      return (cnab < 101 || cnab > 127 || cnab == 116) && cnab != 999;
   }

   public boolean validarCnabCreditoDebito(int cnab) {
      return (cnab < 101 || cnab > 127 && cnab < 201 || cnab > 223 || cnab == 116) && cnab != 999;
   }
}
