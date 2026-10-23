package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.domain.Arquivos;
import java.io.File;
import org.springframework.stereotype.Component;

@Component
public class GerarHashArquivosCmd {
   public void gerar(ControladorValidador controlador, Computador computador, Arquivos a) {
      this.gerar(controlador, computador, 1, a.getAgencias());
      this.gerar(controlador, computador, 2, a.getContas());
      this.gerar(controlador, computador, 3, a.getTitulares());
      this.gerar(controlador, computador, 4, a.getExtrato());
      this.gerar(controlador, computador, 5, a.getOrigemDestino());
   }

   protected void gerar(ControladorValidador controlador, Computador computador, int codArquivo, File arq) {
      Arquivo arquivo = new Arquivo(arq);
      String hash = arquivo.obterHashArquivo(computador);
      controlador.gravarHashArquivo(codArquivo, hash);
   }
}
