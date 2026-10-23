package br.mp.mpf.spea.simba.validador;

import br.mp.mpf.asspa.client.BaseMain;
import java.awt.Dimension;
import telas.frmIdentificaAtendimento;

public class Main extends BaseMain {
   public static void main(String[] args) {
      Main main = new Main();
      main.init(args);
      main.nextForm = frmIdentificaAtendimento.class;
      main.startThread(main);
   }

   protected String getArquivoSpring() {
      return "validador-spring.xml";
   }

   public String getSplashScreenImagePath() {
      return "/telas/recursos/validador-splash.jpg";
   }

   public Dimension getSplashScreenDimension() {
      return new Dimension(353, 215);
   }
}
