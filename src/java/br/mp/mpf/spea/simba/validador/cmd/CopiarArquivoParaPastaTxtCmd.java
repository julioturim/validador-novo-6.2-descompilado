package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.spea.simba.validador.domain.Arquivos;
import br.mp.mpf.spea.simba.validador.domain.ArquivosGab;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CopiarArquivoParaPastaTxtCmd {
   private static final Logger log = LoggerFactory.getLogger(CopiarArquivoParaPastaTxtCmd.class);
   @Inject
   private Configurador configurador;

   public void copiar(String numeroCaso, Arquivos a) {
      File pastaDadosValidador = this.configurador.getPastaDadosValidador();
      File pastaCaso = new File(pastaDadosValidador, numeroCaso);
      File pastaArqtxt = new File(pastaCaso, "arqtxt");
      Arquivo.apaga(pastaArqtxt);
      pastaArqtxt.mkdirs();
      this.copy(a.getAgencias(), pastaArqtxt);
      this.copy(a.getContas(), pastaArqtxt);
      this.copy(a.getTitulares(), pastaArqtxt);
      this.copy(a.getExtrato(), pastaArqtxt);
      this.copy(a.getOrigemDestino(), pastaArqtxt);
   }

   public void copiarGab(String numeroCaso, ArquivosGab a) {
      File pastaDadosValidador = this.configurador.getPastaDadosValidador();
      File pastaCaso = new File(pastaDadosValidador, numeroCaso);
      File pastaArqtxt = new File(pastaCaso, "arqtxt");
      Arquivo.apaga(pastaArqtxt);
      pastaArqtxt.mkdirs();
      this.copy(a.getGab109(), pastaArqtxt);
      this.copy(a.getGab112(), pastaArqtxt);
      this.copy(a.getGab800(), pastaArqtxt);
   }

   protected void copy(File from, File toDir) {
      log.debug("Copiando arquivo {} para pasta {}", from.getAbsolutePath(), toDir.getAbsolutePath());
      File to = new File(toDir, from.getName());
      String var10003 = FilenameUtils.getBaseName(to.getName());
      to = new File(toDir, var10003 + "." + FilenameUtils.getExtension(to.getName()).toLowerCase());

      try {
         if (to.exists()) {
            to.delete();
         }

         FileUtils.copyFile(from, to);
      } catch (IOException e) {
         throw new InfraestruturaException(e);
      }
   }
}
