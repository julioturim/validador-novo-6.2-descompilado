package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.enums.ArquivoTransmissaoPermitido;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerificarArquivosPermitidosTransmissaoCmd {
   private static final Logger log = LoggerFactory.getLogger(VerificarArquivosPermitidosTransmissaoCmd.class);
   private static final long MAX_ZIP_FILE_SIZE = 104857600L;

   public List<File> verificar(File[] selectedFiles) {
      ArrayList<File> arquivosRecusados = new ArrayList();

      for(File file : selectedFiles) {
         try {
            String mimeType = ArquivoTransmissaoPermitido.mimeType(file.getName());
            if (ArquivoTransmissaoPermitido.ZIP.getMimeType().equals(mimeType)) {
               boolean valido = this.verificarZip(file);
               if (!valido) {
                  arquivosRecusados.add(file);
               }
            } else if (!ArquivoTransmissaoPermitido.contains(mimeType)) {
               arquivosRecusados.add(file);
            }
         } catch (Exception e) {
            throw new InfraestruturaException("Erro ao verificar arquivo " + file.getName(), e);
         }
      }

      return arquivosRecusados;
   }

   private boolean verificarZip(File file) {
      try {
         FileInputStream in = new FileInputStream(file);

         boolean var16;
         label89: {
            label88: {
               boolean var8;
               label87: {
                  boolean var14;
                  try {
                     ZipInputStream zis;
                     label84: {
                        label83: {
                           label94: {
                              zis = new ZipInputStream(in);

                              try {
                                 while(true) {
                                    LocalFileHeader entry;
                                    if ((entry = zis.getNextEntry()) == null) {
                                       var14 = true;
                                       break label84;
                                    }

                                    if (!entry.isDirectory()) {
                                       String mimeType = ArquivoTransmissaoPermitido.mimeType(entry.getFileName());
                                       if (!ArquivoTransmissaoPermitido.contains(mimeType)) {
                                          var16 = false;
                                          break label83;
                                       }

                                       if (ArquivoTransmissaoPermitido.ZIP.getMimeType().equals(mimeType)) {
                                          var16 = false;
                                          break label94;
                                       }

                                       long fileSize = entry.getUncompressedSize();
                                       if (fileSize > 104857600L) {
                                          log.warn("Arquivo interno de zip maior que limite máximo permitido.");
                                          log.warn("Arquivo zip: {}", file.getName());
                                          log.warn("Entry: {}", entry.getFileName());
                                          log.warn("Tamanho: {}", fileSize);
                                          var8 = false;
                                          break;
                                       }
                                    }
                                 }
                              } catch (Throwable var11) {
                                 try {
                                    zis.close();
                                 } catch (Throwable var10) {
                                    var11.addSuppressed(var10);
                                 }

                                 throw var11;
                              }

                              zis.close();
                              break label87;
                           }

                           zis.close();
                           break label88;
                        }

                        zis.close();
                        break label89;
                     }

                     zis.close();
                  } catch (Throwable var12) {
                     try {
                        in.close();
                     } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                     }

                     throw var12;
                  }

                  in.close();
                  return var14;
               }

               in.close();
               return var8;
            }

            in.close();
            return var16;
         }

         in.close();
         return var16;
      } catch (Exception e) {
         throw new InfraestruturaException("Erro ao verificar arquivo zip " + file.getName(), e);
      }
   }
}
