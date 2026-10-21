package br.mp.mpf.spea.simba.validador.cmd;

import DAO.Mapa;
import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoBancarioCmd;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import jakarta.inject.Inject;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerificarHashArquivosBancarioCmd {
   private static final Logger log = LoggerFactory.getLogger(VerificarHashArquivosBancarioCmd.class);
   @Inject
   private Context context;
   @Inject
   private Controlador controlador;
   @Inject
   private GetAtendimentoBancarioCmd getAtendimentoBancarioCmd;

   public void verificar() {
      String numeroCaso = this.context.getNumeroCaso();
      Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
      AtendimentoBancario atendimentoBancario = this.getAtendimentoBancarioCmd.get(numeroCaso);
      String hashArquivo1 = atendimentoBancario.getHashArquivo1();
      String hashArquivo2 = atendimentoBancario.getHashArquivo2();
      String hashArquivo3 = atendimentoBancario.getHashArquivo3();
      String hashArquivo4 = atendimentoBancario.getHashArquivo4();
      String hashArquivo5 = atendimentoBancario.getHashArquivo5();
      this.verificarArquivo(1, hashArquivo1, computador);
      this.verificarArquivo(2, hashArquivo2, computador);
      this.verificarArquivo(3, hashArquivo3, computador);
      this.verificarArquivo(4, hashArquivo4, computador);
      this.verificarArquivo(5, hashArquivo5, computador);
   }

   private void verificarArquivo(int indiceArquivo, String hashBanco, Computador computador) {
      String numeroCaso = this.context.getNumeroCaso();
      File pastaArqTxt = this.context.getPastaArqTxt();
      String nomeArquivo = numeroCaso + "_" + Mapa.getNomesTabelasA()[indiceArquivo] + ".txt";
      File arquivo = new File(pastaArqTxt, nomeArquivo);
      String hashCalculado = (new Arquivo(arquivo)).obterHashArquivo(computador);
      if (!hashBanco.equalsIgnoreCase(hashCalculado)) {
         StringBuilder msg = new StringBuilder();
         msg.append("O arquivo ");
         msg.append(arquivo.getAbsolutePath());
         msg.append(" não está íntegro, favor reiniciar validação.");
         log.error("{}", msg);
         throw new NegocioException(msg.toString());
      }
   }
}
