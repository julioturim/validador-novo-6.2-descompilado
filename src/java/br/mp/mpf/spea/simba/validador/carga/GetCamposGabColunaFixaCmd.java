package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.spea.simba.validador.domain.Gab109;
import br.mp.mpf.spea.simba.validador.domain.Gab112;
import br.mp.mpf.spea.simba.validador.enums.CamposGab109;
import br.mp.mpf.spea.simba.validador.enums.CamposGab109Ver2;
import br.mp.mpf.spea.simba.validador.enums.CamposGab112;
import br.mp.mpf.spea.simba.validador.enums.CamposGab112Ver2;
import br.mp.mpf.spea.simba.validador.enums.CamposGab800;
import br.mp.mpf.spea.simba.validador.enums.CamposGab800Ver2;
import jakarta.inject.Inject;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class GetCamposGabColunaFixaCmd {
   @Inject
   private Configurador validador;

   public String[] getCampos(String nomeCampo, String linha) {
      if (nomeCampo.equalsIgnoreCase(Gab109.class.getSimpleName())) {
         return this.getGab109(linha);
      } else {
         return nomeCampo.equalsIgnoreCase(Gab112.class.getSimpleName()) ? this.getGab112(linha) : this.getGab800(linha);
      }
   }

   public String[] getGab109(String currentLine) {
      ArrayList<String> camposLinha = new ArrayList();
      switch (this.validador.getVersaoLayoutGab()) {
         case 1:
            for(int i = 0; i < CamposGab109.values().length - 1; ++i) {
               CamposGab109 camposGab109 = CamposGab109.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab109.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab109.getTamanhoCampo(), currentLine.length());
            }
            break;
         case 2:
            for(int i = 0; i < CamposGab109Ver2.values().length - 1; ++i) {
               CamposGab109Ver2 camposGab109 = CamposGab109Ver2.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab109.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab109.getTamanhoCampo(), currentLine.length());
            }
      }

      String[] campos = (String[])camposLinha.toArray(new String[camposLinha.size()]);
      return campos;
   }

   public String[] getGab112(String currentLine) {
      ArrayList<String> camposLinha = new ArrayList();
      switch (this.validador.getVersaoLayoutGab()) {
         case 1:
            for(int i = 0; i < CamposGab112.values().length - 1; ++i) {
               CamposGab112 camposGab112 = CamposGab112.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab112.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab112.getTamanhoCampo(), currentLine.length());
            }
            break;
         case 2:
            for(int i = 0; i < CamposGab112Ver2.values().length - 1; ++i) {
               CamposGab112Ver2 camposGab112 = CamposGab112Ver2.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab112.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab112.getTamanhoCampo(), currentLine.length());
            }
      }

      String[] campos = (String[])camposLinha.toArray(new String[camposLinha.size()]);
      return campos;
   }

   public String[] getGab800(String currentLine) {
      ArrayList<String> camposLinha = new ArrayList();
      switch (this.validador.getVersaoLayoutGab()) {
         case 1:
            for(int i = 0; i < CamposGab800.values().length - 1; ++i) {
               CamposGab800 camposGab800 = CamposGab800.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab800.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab800.getTamanhoCampo(), currentLine.length());
            }
            break;
         case 2:
            for(int i = 0; i < CamposGab800Ver2.values().length - 1; ++i) {
               CamposGab800Ver2 camposGab800 = CamposGab800Ver2.values()[i];
               camposLinha.add(currentLine.substring(0, camposGab800.getTamanhoCampo()).trim());
               currentLine = currentLine.substring(camposGab800.getTamanhoCampo(), currentLine.length());
            }
      }

      String[] campos = (String[])camposLinha.toArray(new String[camposLinha.size()]);
      return campos;
   }
}
