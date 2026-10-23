package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.enums.TipoAtendimento;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

@Component
public class CompactarArquivosParaEnvioCmd {
   @Inject
   private Context context;

   public void compactar(TipoAtendimento tipoAtendimento) {
      Arquivo obj = new Arquivo(this.context.getPastaArqTxt());
      String nomeArqZip = Util.tiraBarra(this.context.getNumeroCaso()) + ".zip";
      File pastaEnvio = this.context.getPastaEnvio();
      obj.zipArquivos(pastaEnvio.getAbsolutePath(), nomeArqZip);
      this.gerarArquivoTipoAtendimento(new File(pastaEnvio, nomeArqZip), tipoAtendimento);
   }

   private void gerarArquivoTipoAtendimento(File arquivoAtendimento, TipoAtendimento tipoAtendimento) {
      String nomeArquivoAtendimento = FilenameUtils.getBaseName(arquivoAtendimento.getName());
      nomeArquivoAtendimento = nomeArquivoAtendimento + "-tipo-atendimento.txt";
      File pastaArquivoAtendimento = arquivoAtendimento.getParentFile();
      File arquivoTipoAtendimento = new File(pastaArquivoAtendimento, nomeArquivoAtendimento);

      try {
         Files.asCharSink(arquivoTipoAtendimento, StandardCharsets.ISO_8859_1, new FileWriteMode[0]).write(tipoAtendimento.getCodigo());
      } catch (IOException e) {
         throw new InfraestruturaException("Erro ao gerar o arquivo tipo_atendimento: " + arquivoAtendimento.getAbsolutePath(), e);
      }
   }
}
