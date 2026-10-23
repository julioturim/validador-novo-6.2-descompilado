package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.spea.simba.validador.domain.Investigado;
import jakarta.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GerarArquivoInvestigadosCmd {
   private static Logger log = LoggerFactory.getLogger(GerarArquivoInvestigadosCmd.class);
   @Inject
   private Context context;
   @Inject
   private GetInvestigadosCmd getInvestigadosCmd;

   public void gerar() {
      String numeroCaso = this.context.getNumeroCaso();
      File pastaArqTxt = this.context.getPastaArqTxt();
      List<Investigado> investigados = this.getInvestigadosCmd.get();
      File arquivoInvestigado = new File(pastaArqTxt, numeroCaso + "_INVESTIGADO.txt");
      if (arquivoInvestigado.exists()) {
         arquivoInvestigado.delete();
      }

      try {
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoInvestigado), "ISO-8859-1"));

         try {
            for(Investigado investigado : investigados) {
               String linha = this.getLinhaInvestigado(investigado);
               out.write(linha);
            }
         } catch (Throwable var10) {
            try {
               out.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         out.close();
      } catch (IOException e) {
         log.error("Erro ao gerar arquivo de investigado", e);
      }

   }

   private String getLinhaInvestigado(Investigado investigado) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
      String separador = "\t";
      String texto = "";
      texto = texto + investigado.getTipoPessoa().getCodigo();
      texto = texto + separador + investigado.getCpfCnpj();
      texto = texto + separador + investigado.getNomePessoa();
      texto = texto + separador + investigado.getTeveRelacionamento().getCodigo();
      texto = texto + separador + investigado.getTeveConta().getCodigo();
      texto = texto + separador + investigado.getTeveBdv().getCodigo();
      texto = texto + separador + investigado.getObservacao();
      texto = texto + separador + investigado.getDataInicioAfastamento().format(formatter);
      texto = texto + separador + investigado.getDataFimAfastamento().format(formatter);
      texto = texto + "\r\n";
      return texto;
   }
}
