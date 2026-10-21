package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.domain.ArquivosGab;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import java.io.File;
import org.springframework.stereotype.Component;

@Component
public class GerarHashArquivosGabCmd {
   public void gerar(ControladorValidador controlador, Computador computador, ArquivosGab a) {
      this.gerar(controlador, computador, NomeArquivoGab.GAB109, a.getGab109());
      this.gerar(controlador, computador, NomeArquivoGab.GAB112, a.getGab112());
      this.gerar(controlador, computador, NomeArquivoGab.GAB800, a.getGab800());
   }

   protected void gerar(ControladorValidador controlador, Computador computador, NomeArquivoGab nome, File arq) {
      Arquivo arquivo = new Arquivo(arq);
      String hash = arquivo.obterHashArquivo(computador);
      controlador.gravarHashArquivoGab(nome, hash);
   }
}
