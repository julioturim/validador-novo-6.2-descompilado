package br.mp.mpf.spea.simba.validador.to;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

public class Documento {
   private TipoDocumento tipo;
   private File arquivo;
   private String nome;
   private long tamanho;
   private String hash;
   private String observacao;
   private String mimeType;
   private boolean hashDuplicado;

   public Documento(File file) {
      if (file != null && file.exists()) {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         PrintStream ps = new PrintStream(out);
         ps.append(file.getName());
         String fileNameWithCorrectEncoding = out.toString();
         File tempDir = this.getTempDir();
         File tempFile = new File(tempDir, fileNameWithCorrectEncoding);

         try {
            Files.asCharSink(tempFile, StandardCharsets.UTF_8, new FileWriteMode[0]).write("teste");
         } catch (IOException e) {
            throw new NegocioException("Nome inválido do arquivo " + fileNameWithCorrectEncoding, e);
         } finally {
            try {
               FileUtils.deleteDirectory(tempDir);
            } catch (IOException e) {
               throw new InfraestruturaException("Erro ao apagar pasta temporária " + tempDir.getAbsolutePath(), e);
            }
         }

         this.arquivo = file;
         this.nome = fileNameWithCorrectEncoding;
      } else {
         throw new InfraestruturaException("Arquivo é obrigatório.");
      }
   }

   private File getTempDir() {
      try {
         return java.nio.file.Files.createTempDirectory("simba").toFile();
      } catch (IOException e) {
         throw new InfraestruturaException("Erro ao criar pasta temporária", e);
      }
   }

   public String toString() {
      return this.nome;
   }

   public TipoDocumento getTipo() {
      return this.tipo;
   }

   public void setTipo(TipoDocumento tipo) {
      this.tipo = tipo;
   }

   public File getArquivo() {
      return this.arquivo;
   }

   public String getNome() {
      return this.nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public long getTamanho() {
      return this.tamanho;
   }

   public void setTamanho(long tamanho) {
      this.tamanho = tamanho;
   }

   public String getHash() {
      return this.hash;
   }

   public void setHash(String hash) {
      this.hash = hash;
   }

   public String getObservacao() {
      return this.observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
   }

   public int hashCode() {
      int prime = 31;
      int result = 1;
      result = 31 * result + (this.arquivo == null ? 0 : this.arquivo.hashCode());
      result = 31 * result + (this.hash == null ? 0 : this.hash.hashCode());
      result = 31 * result + (this.nome == null ? 0 : this.nome.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Documento other = (Documento)obj;
         if (this.arquivo == null) {
            if (other.arquivo != null) {
               return false;
            }
         } else if (!this.arquivo.equals(other.arquivo)) {
            return false;
         }

         if (this.hash == null) {
            if (other.hash != null) {
               return false;
            }
         } else if (!this.hash.equals(other.hash)) {
            return false;
         }

         if (this.nome == null) {
            if (other.nome != null) {
               return false;
            }
         } else if (!this.nome.equals(other.nome)) {
            return false;
         }

         return true;
      }
   }

   public boolean isHashDuplicado() {
      return this.hashDuplicado;
   }

   public void setHashDuplicado(boolean hashDuplicado) {
      this.hashDuplicado = hashDuplicado;
   }
}
