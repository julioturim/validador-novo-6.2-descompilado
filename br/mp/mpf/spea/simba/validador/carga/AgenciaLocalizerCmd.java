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
public class AgenciaLocalizerCmd {
   private static final Logger log = LoggerFactory.getLogger(AgenciaLocalizerCmd.class);
   private final PreparedStatement ps;
   private final String sql = "select CODIGO_CHAVE_AGENCIA from agencia where numero_banco = ? and numero_agencia = ?";
   private final LoadingCache<AgenciaNaturalKey, Long> cache;

   @Inject
   public AgenciaLocalizerCmd(SharedConnection sharedConnection) throws SQLException {
      this.ps = sharedConnection.get().prepareStatement("select CODIGO_CHAVE_AGENCIA from agencia where numero_banco = ? and numero_agencia = ?");
      this.cache = CacheBuilder.newBuilder().initialCapacity(2000).maximumSize(2000L).build(new CacheLoader<AgenciaNaturalKey, Long>() {
         {
            Objects.requireNonNull(AgenciaLocalizerCmd.this);
         }

         public Long load(AgenciaNaturalKey key) throws Exception {
            return AgenciaLocalizerCmd.this.findIdAgencia(key);
         }
      });
   }

   public long findIdAgencia(AgenciaNaturalKey key) throws SQLException {
      this.ps.clearParameters();
      this.ps.setInt(1, key.numeroBanco);
      this.ps.setInt(2, key.numeroAgencia);
      ResultSet rs;
      if (log.isTraceEnabled()) {
         log.trace("Localizando ID da Agencia - Banco: {} Agencia: {}", key.asObjectArray());
         Stopwatch stopwatch = Stopwatch.createStarted();
         rs = this.ps.executeQuery();
         stopwatch.stop();
         log.trace("Agencia localizada em {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
      } else {
         rs = this.ps.executeQuery();
      }

      return rs.next() ? rs.getLong(1) : -1L;
   }

   public long findIdAgencia(String numeroBanco, String numeroAgencia) throws SQLException {
      return this.findIdAgencia(Integer.valueOf(numeroBanco), Integer.valueOf(numeroAgencia));
   }

   public long findIdAgencia(int numeroBanco, int numeroAgencia) {
      AgenciaNaturalKey naturalKey = new AgenciaNaturalKey();
      naturalKey.numeroBanco = Integer.valueOf(numeroBanco);
      naturalKey.numeroAgencia = Integer.valueOf(numeroAgencia);

      try {
         return (Long)this.cache.get(naturalKey);
      } catch (ExecutionException e) {
         log.error("Erro ao localizar agencia.", e);
         return -1L;
      }
   }

   public class AgenciaNaturalKey {
      public int numeroBanco;
      public int numeroAgencia;

      public AgenciaNaturalKey() {
         Objects.requireNonNull(AgenciaLocalizerCmd.this);
         super();
      }

      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         result = 31 * result + this.numeroAgencia;
         result = 31 * result + this.numeroBanco;
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
            AgenciaNaturalKey other = (AgenciaNaturalKey)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
               return false;
            } else if (this.numeroAgencia != other.numeroAgencia) {
               return false;
            } else {
               return this.numeroBanco == other.numeroBanco;
            }
         }
      }

      private AgenciaLocalizerCmd getOuterType() {
         return AgenciaLocalizerCmd.this;
      }

      public Object[] asObjectArray() {
         return new Object[]{this.numeroBanco, this.numeroAgencia};
      }
   }
}
