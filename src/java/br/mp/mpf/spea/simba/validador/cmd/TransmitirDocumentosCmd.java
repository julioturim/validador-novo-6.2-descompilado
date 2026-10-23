package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.Certificado;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.cmd.GetDadosUsuarioChavesAsbCmd;
import br.mp.mpf.pgr.client.cmd.MostrarErroComunicacaoComputadorCmd;
import br.mp.mpf.pgr.client.cmd.SolicitarCodigoAutenticacaoCmd;
import br.mp.mpf.pgr.client.http.PostToUrl;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.to.Versao;
import br.mp.mpf.pgr.filefilter.PdfFileFilter;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.domain.Investigado;
import br.mp.mpf.spea.simba.validador.to.Documento;
import br.mp.mpf.spea.simba.validador.to.TransmissaoDocumentoRequestTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import jakarta.inject.Inject;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.stereotype.Component;

@Component
public class TransmitirDocumentosCmd {
   private static final Logger log = LoggerFactory.getLogger(TransmitirDocumentosCmd.class);
   private static final String servletUrl = "documentos";
   private static final String[] VERSAO_ALGORITMO = new String[]{"1", "SHA-512"};
   public static final int ETAPA_AUTENTICACAO = 1;
   public static final int ETAPA_EMPACOTAMENTO = 2;
   public static final int ETAPA_TRANSMISSAO = 3;
   public static final int ETAPA_AGUARDANDO_RESPOSTA = 4;
   public static final int ETAPA_CONCLUIDO = 5;
   private static final String chavePastaUltimoComprovanteSalvoKey = "pasta.ultimo.comprovante.salvo";
   public static final FileFilter pdfFileFilter = new PdfFileFilter();
   private static final String DATE_FORMAT = "dd/MM/yyyy";
   private static final String ATENDIMENTO_DATE_FORMAT = "yyyy-MM-dd";
   @Inject
   private Context context;
   @Inject
   private Config config;
   @Inject
   private SolicitarCodigoAutenticacaoCmd solicitarCodigoAutenticacaoCmd;
   @Inject
   private Controlador controlador;
   @Inject
   private MostrarErroComunicacaoComputadorCmd mostrarErroComunicacaoComputadorCmd;
   @Inject
   private GetDadosUsuarioChavesAsbCmd getDadosUsuarioChavesAsbCmd;
   @Inject
   private PostToUrl postToUrl;

   public void transmitir(TransmissaoDocumentoRequestTO request, BiConsumer<Integer, Integer> progressConsumer) {
      progressConsumer.accept(1, 0);
      Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
      int codigoComputador = computador.getCodigo_computador();
      progressConsumer.accept(1, 10);
      Certificado certificado = this.context.getCertificado();
      String chavePublicaCliente = Util.arrayDeBytes2String(certificado.getPublicaEncriptada("1234567890123456"));
      String assinatura = certificado.getAssinatura(codigoComputador);
      progressConsumer.accept(1, 20);
      String codigoAutenticacao = this.solicitarCodigoAutenticacaoCmd.get(computador, chavePublicaCliente, assinatura);
      progressConsumer.accept(1, 90);
      codigoAutenticacao = this.validarCodigoAutenticacao(computador, codigoAutenticacao);
      if (codigoAutenticacao == null) {
         throw new NegocioException("Chave não localizada no computador para autenticação");
      } else {
         String codigoAssinado;
         try {
            codigoAssinado = Util.arrayDeBytes2String(certificado.Assinar(codigoAutenticacao.getBytes()));
         } catch (Exception e) {
            throw new InfraestruturaException("Erro ao assinar código de autenticação", e);
         }

         progressConsumer.accept(1, 100);
         Map<String, String> dadosUsuario = this.getDadosUsuarioChavesAsbCmd.get(certificado);
         List<String[]> parametros = this.montarParametros(dadosUsuario, chavePublicaCliente, assinatura, codigoAssinado, computador);
         progressConsumer.accept(2, 5);
         Map<String, String> dadosAtendimento = this.montarDadosAtendimento(request);
         progressConsumer.accept(2, 10);
         File pastaEnvioDocumentos = this.pastaEnvioDocumentos();
         File arquivoAtendimento = new File(pastaEnvioDocumentos, "atendimento-" + this.context.getNumeroCaso() + ".txt");
         this.gerarArquivoAtendimento(request, progressConsumer, dadosAtendimento, arquivoAtendimento);
         File hashFile = this.gerarArquivoHash(pastaEnvioDocumentos, arquivoAtendimento);
         progressConsumer.accept(2, 55);
         File zipFile = this.gerarArquivoZip(pastaEnvioDocumentos, arquivoAtendimento, progressConsumer);
         long tamanhoZipFile = zipFile.length();
         parametros.add(new String[]{"tamanhoZip", String.valueOf(tamanhoZipFile)});
         progressConsumer.accept(2, 100);
         List<String> arquivos = new ArrayList(2);
         arquivos.add(zipFile.getAbsolutePath());
         arquivos.add(hashFile.getAbsolutePath());

         try {
            ConstructorArgumentValues.ValueHolder respostaStartTime = new ConstructorArgumentValues.ValueHolder((Object)null);
            ConstructorArgumentValues.ValueHolder respostaProgressThread = new ConstructorArgumentValues.ValueHolder((Object)null);
            ConstructorArgumentValues.ValueHolder stopRespostaProgressThread = new ConstructorArgumentValues.ValueHolder(false);
            long envioStartTime = System.currentTimeMillis();
            String resposta = this.postToUrl.post(computador, "documentos", parametros, arquivos, (progress) -> {
               if (progress < 100) {
                  progressConsumer.accept(3, progress);
               } else if (respostaProgressThread.getValue() == null) {
                  long current = System.currentTimeMillis();
                  long deltaEnvio = current - envioStartTime;
                  log.debug("Tempo de envio: {} ms", deltaEnvio);
                  respostaStartTime.setValue(current);
                  Thread thread = new Thread(() -> {
                     while(!(Boolean)stopRespostaProgressThread.getValue()) {
                        long elapsed = System.currentTimeMillis() - (Long)respostaStartTime.getValue();
                        double delta = (double)elapsed / (double)60000.0F * (double)100.0F;
                        int p = Long.valueOf(Math.round(delta)).intValue();
                        int progressResposta = p < 97 ? p : 97;
                        progressConsumer.accept(4, progressResposta);

                        try {
                           Thread.sleep(100L);
                        } catch (InterruptedException var10) {
                        }
                     }

                  });
                  respostaProgressThread.setValue(thread);
                  thread.start();
               }

            }, false);
            long elapsed = System.currentTimeMillis() - (Long)respostaStartTime.getValue();
            log.debug("Tempo de resposta: {} ms", elapsed);
            stopRespostaProgressThread.setValue(true);
            progressConsumer.accept(5, 100);
            if (resposta == null) {
               throw new NegocioException("Erro na transmissão: não houve resposta do servidor.");
            }

            if (resposta.indexOf("<erro>") != -1) {
               String msg = resposta.split("<erro>")[1].split("</erro>")[0];
               throw new NegocioException("Erro na transmissão: " + msg);
            }

            StringReader reader = new StringReader(resposta);
            BufferedReader b = new BufferedReader(reader);
            String linha = b.readLine();
            if (linha == null) {
               throw new InfraestruturaException("Resposta inválida. <dados> não localizado.");
            }

            linha = b.readLine();
            if (linha == null) {
               throw new InfraestruturaException("Resposta inválida. <entrega> não localizado.");
            }

            String entrega = linha.split("<entrega>")[1].split("</entrega>")[0];
            linha = b.readLine();
            if (linha == null) {
               throw new InfraestruturaException("Resposta inválida. <dataHora> não localizado.");
            }

            String dataHora = linha.split("<dataHora>")[1].split("</dataHora>")[0];
            linha = b.readLine();
            if (linha == null) {
               throw new InfraestruturaException("Resposta inválida. Linha em branco.");
            }

            StringBuilder msg = new StringBuilder();
            msg.append("Transmissão concluída com sucesso!\n\n<b>Caso:</b> ");
            msg.append(request.getAtendimento().getNumeroCaso());
            msg.append("\n<b>Código do Comprovante:</b> ");
            msg.append(entrega);
            msg.append("\n<b>Data e Hora:</b> ");
            msg.append(dataHora);
            ViewUtil.showInfoDialog(msg.toString());
            linha = b.readLine();
            linha = b.readLine();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteArrayOutputStream outer;
            try {
               while(linha != null && !"".equals(linha)) {
                  byte[] decodeBase64 = Base64.getDecoder().decode(linha.getBytes(StandardCharsets.UTF_8));
                  out.write(decodeBase64);
                  linha = b.readLine();
               }

               outer = out;
            } catch (Throwable var40) {
               try {
                  out.close();
               } catch (Throwable var38) {
                  var40.addSuppressed(var38);
               }

               throw var40;
            }

            out.close();
            EventQueue.invokeLater(() -> this.salvarComprovante(request, entrega, outer));
         } catch (NegocioException e) {
            throw e;
         } catch (Exception e) {
            throw new InfraestruturaException("Erro na transmissão de documentos", e);
         }

         progressConsumer.accept(0, 0);
      }
   }

   private Map<String, String> montarDadosAtendimento(TransmissaoDocumentoRequestTO request) {
      Gson gson = (new GsonBuilder()).setDateFormat("").registerTypeAdapter(TransmitirDocumentosCmd.LocalDateConverter.LOCAL_DATE_TYPE, new LocalDateConverter("yyyy-MM-dd")).create();
      JsonElement atendimentoElement = gson.toJsonTree(request.getAtendimento());
      Map<String, String> dadosAtendimento = new LinkedHashMap();
      JsonObject atendimentoJsonObject = atendimentoElement.getAsJsonObject();

      for(String key : atendimentoJsonObject.keySet()) {
         if (!"id".equals(key)) {
            JsonElement element = atendimentoJsonObject.get(key);
            String chave = "atendimento." + key;
            if (element.isJsonNull()) {
               dadosAtendimento.put(chave, (Object)null);
            } else if (element.isJsonPrimitive()) {
               JsonPrimitive primitive = (JsonPrimitive)element;
               if (primitive.isString()) {
                  byte[] stringBytes = primitive.getAsString().getBytes(StandardCharsets.UTF_8);
                  String stringBase64 = Base64.getEncoder().encodeToString(stringBytes);
                  dadosAtendimento.put(chave, stringBase64);
               } else {
                  dadosAtendimento.put(chave, primitive.getAsString());
               }
            }
         }
      }

      dadosAtendimento.put("observacao", Base64.getEncoder().encodeToString(request.getObservacao().getBytes(StandardCharsets.UTF_8)));
      dadosAtendimento.put("totalArquivos", String.valueOf(request.getTotalArquivos()));
      dadosAtendimento.put("totalBytes", String.valueOf(request.getTotalBytes()));
      return dadosAtendimento;
   }

   private Map<String, String> montarDadosInvestigado(int ordem, Investigado investigado) {
      Gson gson = (new GsonBuilder()).setDateFormat("").registerTypeAdapter(TransmitirDocumentosCmd.LocalDateConverter.LOCAL_DATE_TYPE, new LocalDateConverter("yyyy-MM-dd")).create();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      JsonElement investigadoElement = gson.toJsonTree(investigado);
      Map<String, String> dadosInvestigado = new LinkedHashMap();
      JsonObject investigadoJsonObject = investigadoElement.getAsJsonObject();

      for(String key : investigadoJsonObject.keySet()) {
         if (!"codigo".equals(key) && !"linha".equals(key)) {
            JsonElement element = investigadoJsonObject.get(key);
            String chave = "investigado[" + ordem + "]." + key;
            if ("tipoPessoa".equals(key)) {
               String tipoPessoa = investigado.getTipoPessoa().getCodigo();
               byte[] stringBytes = tipoPessoa.getBytes(StandardCharsets.UTF_8);
               tipoPessoa = Base64.getEncoder().encodeToString(stringBytes);
               dadosInvestigado.put(chave, tipoPessoa);
            } else if ("dataInicioAfastamento".equals(key)) {
               String value = investigado.getDataInicioAfastamento().format(formatter);
               byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
               value = Base64.getEncoder().encodeToString(stringBytes);
               dadosInvestigado.put(chave, value);
            } else if ("dataFimAfastamento".equals(key)) {
               String value = investigado.getDataFimAfastamento().format(formatter);
               byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
               value = Base64.getEncoder().encodeToString(stringBytes);
               dadosInvestigado.put(chave, value);
            } else if (element.isJsonNull()) {
               dadosInvestigado.put(chave, (Object)null);
            } else if (element.isJsonPrimitive()) {
               JsonPrimitive primitive = (JsonPrimitive)element;
               if (primitive.isString()) {
                  byte[] stringBytes = primitive.getAsString().getBytes(StandardCharsets.UTF_8);
                  String stringBase64 = Base64.getEncoder().encodeToString(stringBytes);
                  dadosInvestigado.put(chave, stringBase64);
               } else {
                  dadosInvestigado.put(chave, primitive.getAsString());
               }
            }
         }
      }

      return dadosInvestigado;
   }

   private String validarCodigoAutenticacao(Computador computador, String codigoAutenticacao) {
      if (codigoAutenticacao == null) {
         return null;
      } else if (codigoAutenticacao.indexOf("<erro>") != -1) {
         String erro = codigoAutenticacao.split("<erro>")[1].split("</erro>")[0];
         this.mostrarErroComunicacaoComputadorCmd.mostrar(computador, erro);
         return null;
      } else if (codigoAutenticacao.indexOf("<numero>") == -1) {
         this.mostrarErroComunicacaoComputadorCmd.mostrar(computador, "Código de autenticação não localizado.");
         return null;
      } else {
         codigoAutenticacao = codigoAutenticacao.trim().split("<numero>")[1];
         codigoAutenticacao = codigoAutenticacao.trim().split("</numero>")[0];
         return codigoAutenticacao;
      }
   }

   private List<String[]> montarParametros(Map<String, String> dadosUsuario, String chavePublicaCliente, String assinatura, String codigoAssinado, Computador computador) {
      List<String[]> parametros = new ArrayList();
      parametros.add(new String[]{"assinatura", assinatura});
      parametros.add(new String[]{"chavePublica", chavePublicaCliente});
      parametros.add(new String[]{"codigo", codigoAssinado});
      parametros.add(new String[]{"numeroCaso", this.context.getNumeroCaso()});
      this.setParametro(parametros, "CPF", dadosUsuario);
      this.setParametro(parametros, "rg", dadosUsuario);
      this.setParametro(parametros, "cargo", dadosUsuario);
      this.setParametro(parametros, "setor", dadosUsuario);
      this.setParametro(parametros, "nomeCompleto", dadosUsuario);
      this.setParametro(parametros, "ddd", dadosUsuario);
      this.setParametro(parametros, "email", dadosUsuario);
      if (dadosUsuario.get("numeroBanco") != null) {
         this.setParametro(parametros, "numeroBanco", dadosUsuario);
      }

      if (computador.getVersaoReceptor().isVersaoMaiorOuIgual("4.0.0")) {
         String telefone = ((String)dadosUsuario.get("telefone")).trim();
         String ramal = ((String)dadosUsuario.get("ramal")).trim();
         parametros.add(new String[]{"telefone", String.format("%s r:%s", telefone, ramal)});
      } else {
         this.setParametro(parametros, "ramal", dadosUsuario);
         this.setParametro(parametros, "telefone", dadosUsuario);
      }

      Versao versao = computador.getVersaoReceptor();
      boolean suportaTransmissaoDocumentos = versao.suportaTransmissaoDocumentos(computador);
      boolean suportaCorretora = versao.suportaCorretora(computador);
      if (suportaTransmissaoDocumentos) {
         if (dadosUsuario.get("cnpj") != null) {
            this.setParametro(parametros, "cnpj", dadosUsuario);
         }
      } else if (suportaCorretora && dadosUsuario.get("cnpj") != null) {
         parametros.add(new String[]{"cnpjCorretora", ((String)dadosUsuario.get("cnpj")).trim()});
      }

      this.setParametro(parametros, "nomeBanco", dadosUsuario);
      return parametros;
   }

   private void setParametro(List<String[]> parametros, String key, Map<String, String> dadosUsuario) {
      parametros.add(new String[]{key, ((String)dadosUsuario.get(key)).trim()});
   }

   private File pastaEnvioDocumentos() {
      File pastaEnvioDocumentos = this.context.getPastaEnvioDocumentos();
      if (pastaEnvioDocumentos.exists()) {
         try {
            FileUtils.deleteDirectory(pastaEnvioDocumentos);
         } catch (IOException var3) {
            throw new InfraestruturaException("Erro ao excluir pastaEnvioDocumentos");
         }
      }

      pastaEnvioDocumentos.mkdirs();
      return pastaEnvioDocumentos;
   }

   private void gerarArquivoAtendimento(TransmissaoDocumentoRequestTO request, BiConsumer<Integer, Integer> progressConsumer, Map<String, String> dadosAtendimento, File arquivoAtendimento) {
      try {
         OutputStream out = new BufferedOutputStream(new FileOutputStream(arquivoAtendimento));

         try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), 65536);

            try {
               String var10001 = VERSAO_ALGORITMO[0];
               writer.append(var10001 + " " + VERSAO_ALGORITMO[1]);
               this.gravarDadosAtendimento(dadosAtendimento, writer);
               this.gravarInvestigados(request.getInvestigados(), writer);
               this.gravarDocumentos(request, progressConsumer, writer);
            } catch (Throwable var11) {
               try {
                  writer.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            writer.close();
         } catch (Throwable var12) {
            try {
               out.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         out.close();
      } catch (Exception e) {
         throw new InfraestruturaException("Erro ao empacotar documentos para transmissão", e);
      }
   }

   private void gravarDadosAtendimento(Map<String, String> dadosAtendimento, BufferedWriter writer) throws IOException {
      writer.newLine();

      for(String key : dadosAtendimento.keySet()) {
         writer.append(key);
         writer.append("=");
         writer.append((CharSequence)dadosAtendimento.get(key));
         writer.newLine();
      }

   }

   private void gravarDocumentos(TransmissaoDocumentoRequestTO request, BiConsumer<Integer, Integer> progressConsumer, BufferedWriter writer) throws IOException {
      float totalBytes = (float)request.getTotalBytes();
      float bytesReads = 0.0F;

      for(Documento documento : request.getDocumentos()) {
         writer.newLine();
         writer.append(documento.getTipo().name());
         writer.newLine();
         writer.append(documento.getNome());
         writer.newLine();
         writer.append(documento.getHash());
         writer.newLine();
         writer.append(documento.getMimeType());
         writer.newLine();
         if (StringUtil.isNotEmpty(documento.getObservacao())) {
            byte[] observacaoBytes = documento.getObservacao().getBytes(StandardCharsets.UTF_8);
            String observacaoBase64 = Base64.getEncoder().encodeToString(observacaoBytes);
            writer.append(observacaoBase64);
         }

         writer.newLine();
         writer.append(String.valueOf(documento.getTamanho()));
         writer.newLine();
         InputStream in = new BufferedInputStream(new FileInputStream(documento.getArquivo()));

         try {
            byte[] buffer = new byte[57];

            int reads;
            while((reads = in.read(buffer)) != -1) {
               byte[] chunked;
               if (reads == 57) {
                  chunked = Base64.getEncoder().encode(buffer);
               } else {
                  chunked = Base64.getEncoder().encode(Arrays.copyOf(buffer, reads));
               }

               String temp = new String(chunked);
               writer.write(temp);
               writer.newLine();
               bytesReads += (float)reads;
               int proporcao = (int)Math.floor((double)(bytesReads * 100.0F / totalBytes) * 0.4);
               progressConsumer.accept(2, 10 + proporcao);
            }
         } catch (Throwable var16) {
            try {
               in.close();
            } catch (Throwable var15) {
               var16.addSuppressed(var15);
            }

            throw var16;
         }

         in.close();
      }

   }

   private void gravarInvestigados(List<Investigado> investigados, BufferedWriter writer) throws IOException {
      int i = 1;

      for(Investigado investigado : investigados) {
         Map<String, String> dadosInvestigado = this.montarDadosInvestigado(i++, investigado);
         writer.newLine();

         for(String key : dadosInvestigado.keySet()) {
            writer.append(key);
            writer.append("=");
            writer.append((CharSequence)dadosInvestigado.get(key));
            writer.newLine();
         }
      }

      writer.newLine();
   }

   private File gerarArquivoHash(File pastaEnvioDocumentos, File arquivoAtendimento) {
      String hash;
      try {
         FileInputStream in = new FileInputStream(arquivoAtendimento);

         try {
            hash = DigestUtils.sha512Hex(in);
         } catch (Throwable var9) {
            try {
               in.close();
            } catch (Throwable var7) {
               var9.addSuppressed(var7);
            }

            throw var9;
         }

         in.close();
      } catch (Exception e) {
         throw new InfraestruturaException("Erro ao gerar hash do pacote de documentos para transmissão", e);
      }

      File hashFile = new File(pastaEnvioDocumentos, arquivoAtendimento.getName() + ".hash");

      try {
         Files.write(hashFile.toPath(), hash.getBytes(StandardCharsets.UTF_8), new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW});
         return hashFile;
      } catch (Exception e) {
         throw new InfraestruturaException("Erro ao gravar arquivo de hash do pacote de documentos para transmissão", e);
      }
   }

   private File gerarArquivoZip(File pastaEnvioDocumentos, File arquivoAtendimento, BiConsumer<Integer, Integer> progressConsumer) {
      File zipFile = new File(pastaEnvioDocumentos, arquivoAtendimento.getName() + ".zip");

      try {
         ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile), StandardCharsets.UTF_8);

         try {
            FileInputStream in = new FileInputStream(arquivoAtendimento);

            try {
               ZipEntry entry = new ZipEntry(arquivoAtendimento.getName());
               zos.putNextEntry(entry);
               float totalBytes = (float)arquivoAtendimento.length();
               float bytesReads = 0.0F;
               byte[] buffer = new byte[65536];

               int reads;
               while((reads = in.read(buffer)) != -1) {
                  zos.write(buffer, 0, reads);
                  bytesReads += (float)reads;
                  int proporcao = (int)Math.floor((double)(bytesReads * 100.0F / totalBytes) * 0.45);
                  progressConsumer.accept(2, 55 + proporcao);
               }

               zos.closeEntry();
            } catch (Throwable var15) {
               try {
                  in.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }

               throw var15;
            }

            in.close();
         } catch (Throwable var16) {
            try {
               zos.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }

            throw var16;
         }

         zos.close();
         return zipFile;
      } catch (Exception e) {
         throw new InfraestruturaException("Erro ao compactar pacote de documentos para transmissão", e);
      }
   }

   protected void salvarComprovante(TransmissaoDocumentoRequestTO request, String entrega, ByteArrayOutputStream baos) {
      JFileChooser fc = new JFileChooser();
      String var10003 = request.getAtendimento().getNumeroCaso();
      fc.setSelectedFile(new File(var10003 + "-comprovante-" + entrega + ".pdf"));
      fc.setCurrentDirectory(this.getPastaUltimoComprovanteSalvo());
      fc.setMultiSelectionEnabled(false);
      Action details = fc.getActionMap().get("viewTypeDetails");
      details.actionPerformed((ActionEvent)null);
      SwingUtilities.invokeLater(() -> this.configChoosableFileFilterForSave(fc));
      if (fc.showSaveDialog((java.awt.Component)null) == 0) {
         File comprovanteFile = fc.getSelectedFile();

         try {
            OutputStream out = new FileOutputStream(comprovanteFile);

            try {
               out.write(baos.toByteArray());
            } catch (Throwable var11) {
               try {
                  out.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            out.close();
         } catch (Exception e) {
            throw new NegocioException("Erro ao salvar comprovante.", e);
         }

         this.setPastaUltimoComprovanteSalvo(comprovanteFile);
      }
   }

   private void configChoosableFileFilterForSave(JFileChooser fc) {
      FileFilter[] choosableFileFilters = fc.getChoosableFileFilters();
      Arrays.stream(choosableFileFilters).forEach((filter) -> fc.removeChoosableFileFilter(filter));
      fc.addChoosableFileFilter(pdfFileFilter);
      Arrays.stream(choosableFileFilters).forEach((filter) -> fc.addChoosableFileFilter(filter));
   }

   private File getPastaUltimoComprovanteSalvo() {
      if (this.config.contains("pasta.ultimo.comprovante.salvo")) {
         String path = this.config.get("pasta.ultimo.comprovante.salvo");
         File arquivoChave = new File(path);
         if (arquivoChave.exists()) {
            return arquivoChave.getAbsoluteFile();
         }
      }

      return null;
   }

   protected void setPastaUltimoComprovanteSalvo(File arquivo) {
      if (arquivo != null && arquivo.exists()) {
         try {
            this.config.set("pasta.ultimo.comprovante.salvo", arquivo.getCanonicalPath());
         } catch (IOException e) {
            log.warn("Erro ao gravar último comprovante salvo", e);
         }

      }
   }

   private static class LocalDateType extends TypeToken<LocalDate> {
      private static final long serialVersionUID = -7957438672922196707L;
   }

   private static class LocalDateConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
      public static final Type LOCAL_DATE_TYPE = (new LocalDateType()).getType();
      private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.of("pt", "BR"));

      public LocalDateConverter() {
      }

      public LocalDateConverter(String pattern) {
         this.formatter = DateTimeFormatter.ofPattern(pattern, Locale.of("pt", "BR"));
      }

      public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         return (LocalDate)this.formatter.parse(json.getAsString(), LocalDate::from);
      }

      public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive(this.formatter.format(src));
      }
   }
}
