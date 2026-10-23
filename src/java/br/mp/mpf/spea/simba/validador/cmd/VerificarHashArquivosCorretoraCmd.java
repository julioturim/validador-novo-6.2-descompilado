package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoGabCmd;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;
import br.mp.mpf.spea.simba.validador.enums.NomesTabelaAGab;
import jakarta.inject.Inject;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerificarHashArquivosCorretoraCmd {
   private static final Logger log = LoggerFactory.getLogger(VerificarHashArquivosBancarioCmd.class);
   @Inject
   private Context context;
   @Inject
   private Controlador controlador;
   @Inject
   private GetAtendimentoGabCmd getAtendimentoGabCmd;

   public void verificar() {
      String numeroCaso = this.context.getNumeroCaso();
      Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
      AtendimentoGab atendimentoGab = this.getAtendimentoGabCmd.get(numeroCaso);
      String hashArquivoGab109 = atendimentoGab.getHashArquivoGab109();
      String hashArquivoGab112 = atendimentoGab.getHashArquivoGab112();
      String hashArquivoGab800 = atendimentoGab.getHashArquivoGab800();
      this.verificarArquivo(NomesTabelaAGab.GAB109, hashArquivoGab109, computador);
      this.verificarArquivo(NomesTabelaAGab.GAB112, hashArquivoGab112, computador);
      this.verificarArquivo(NomesTabelaAGab.GAB800, hashArquivoGab800, computador);
   }

   private void verificarArquivo(NomesTabelaAGab nomeTabelaA, String hashBanco, Computador computador) {
      String numeroCaso = this.context.getNumeroCaso();
      File pastaArqTxt = this.context.getPastaArqTxt();
      String nomeArquivo = numeroCaso + "_" + nomeTabelaA.name() + ".txt";
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
