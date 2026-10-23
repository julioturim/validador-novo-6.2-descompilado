package br.mp.mpf.spea.simba.validador;

import br.mp.mpf.pgr.client.Configurador;
import java.io.File;

public class ConfiguradorValidador extends Configurador {
   public String getVersao() {
      return "6.2.6";
   }

   public String getCodigoPrograma() {
      return "VBMPF";
   }

   public String getNomePrograma() {
      return "Validador";
   }

   public void setPastaDadosValidador(File pastaDadosValidador) {
   }
}
