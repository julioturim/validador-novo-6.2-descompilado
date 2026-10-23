package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PessoaLocalizerCmd {
   private static final Logger log = LoggerFactory.getLogger(PessoaLocalizerCmd.class);
   private final PreparedStatement ps;
   private final String sql = "select CODIGO_CHAVE_PESSOA from pessoa where linha_arquivo = ?";
   private final LoadingCache<PessoaNaturalKey, Long> cache;

   @Inject
   public PessoaLocalizerCmd(SharedConnection sharedConnection) throws SQLException {
      this.ps = sharedConnection.get().prepareStatement("select CODIGO_CHAVE_PESSOA from pessoa where linha_arquivo = ?");
      this.cache = CacheBuilder.newBuilder().initialCapacity(2000).maximumSize(2000L).build(new CacheLoader<PessoaNaturalKey, Long>() {
         {
            Objects.requireNonNull(PessoaLocalizerCmd.this);
         }

         public Long load(PessoaNaturalKey key) throws Exception {
            return PessoaLocalizerCmd.this.findIdPessoa(key);
         }
      });
   }

   public long findIdPessoa(PessoaNaturalKey key) throws SQLException {
      this.ps.clearParameters();
      this.ps.setLong(1, key.numeroLinha);
      ResultSet rs;
      if (log.isTraceEnabled()) {
         log.trace("Localizando ID da Pessoa - Linha: {}", key.numeroLinha);
         Stopwatch stopwatch = Stopwatch.createStarted();
         rs = this.ps.executeQuery();
         stopwatch.stop();
         log.trace("Agencia localizada em {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
      } else {
         rs = this.ps.executeQuery();
      }

      return rs.next() ? rs.getLong(1) : -1L;
   }

   public long findIdPessoa(int numeroLinha) {
      PessoaNaturalKey naturalKey = new PessoaNaturalKey();
      naturalKey.numeroLinha = (long)numeroLinha;

      try {
         return (Long)this.cache.get(naturalKey);
      } catch (ExecutionException e) {
         log.error("Erro ao localizar pessoa.", e);
         return -1L;
      }
   }

   public class PessoaNaturalKey {
      public long numeroLinha;

      public PessoaNaturalKey() {
         Objects.requireNonNull(PessoaLocalizerCmd.this);
         super();
      }

      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         result = 31 * result + (int)(this.numeroLinha ^ this.numeroLinha >>> 32);
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
            PessoaNaturalKey other = (PessoaNaturalKey)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
               return false;
            } else {
               return this.numeroLinha == other.numeroLinha;
            }
         }
      }

      private PessoaLocalizerCmd getOuterType() {
         return PessoaLocalizerCmd.this;
      }
   }
}
