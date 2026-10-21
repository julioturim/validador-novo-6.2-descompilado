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
public class ContaLocalizerCmd {
   private static final Logger log = LoggerFactory.getLogger(ContaLocalizerCmd.class);
   private final PreparedStatement ps;
   private final String sql = "select CODIGO_CHAVE_CONTA from conta where CODIGO_CHAVE_AGENCIA = ? and NUMERO_CONTA = ? and TIPO_CONTA = ?";
   private final LoadingCache<ContaNaturalKey, Long> cache;
   private final AgenciaLocalizerCmd agenciaLocalizer;

   @Inject
   public ContaLocalizerCmd(AgenciaLocalizerCmd agenciaLocalizer, SharedConnection sharedConnection) throws SQLException {
      this.agenciaLocalizer = agenciaLocalizer;
      this.ps = sharedConnection.get().prepareStatement("select CODIGO_CHAVE_CONTA from conta where CODIGO_CHAVE_AGENCIA = ? and NUMERO_CONTA = ? and TIPO_CONTA = ?");
      this.cache = CacheBuilder.newBuilder().initialCapacity(2000).maximumSize(2000L).build(new CacheLoader<ContaNaturalKey, Long>() {
         {
            Objects.requireNonNull(ContaLocalizerCmd.this);
         }

         public Long load(ContaNaturalKey key) throws Exception {
            return ContaLocalizerCmd.this.findIdConta(key);
         }
      });
   }

   public long findIdConta(ContaNaturalKey key) throws SQLException {
      long idAgencia = this.agenciaLocalizer.findIdAgencia(key.numeroBanco, key.numeroAgencia);
      if (idAgencia == -1L) {
         log.error("Agencia não localizada: Banco: {} Agencia: {}", new Object[]{key.numeroBanco, key.numeroAgencia});
         return -1L;
      } else {
         this.ps.clearParameters();
         this.ps.setLong(1, idAgencia);
         this.ps.setString(2, key.numeroConta);
         this.ps.setInt(3, key.tipoConta);
         ResultSet rs;
         if (log.isTraceEnabled()) {
            log.trace("Localizando ID da Conta - Banco: {} Agencia: {} Conta: {} TipoConta: {}", key.asObjectArray());
            Stopwatch stopwatch = Stopwatch.createStarted();
            rs = this.ps.executeQuery();
            stopwatch.stop();
            log.trace("Conta localizada em {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
         } else {
            rs = this.ps.executeQuery();
         }

         return rs.next() ? rs.getLong(1) : -1L;
      }
   }

   public long findIdConta(String numeroBanco, String numeroAgencia, String numeroConta, String tipoConta) {
      ContaNaturalKey naturalKey = new ContaNaturalKey();
      naturalKey.numeroBanco = Integer.valueOf(numeroBanco);
      naturalKey.numeroAgencia = Integer.valueOf(numeroAgencia);
      naturalKey.numeroConta = numeroConta;
      naturalKey.tipoConta = Integer.valueOf(tipoConta);

      try {
         return (Long)this.cache.get(naturalKey);
      } catch (ExecutionException e) {
         log.error("Erro ao localizar conta.", e);
         return -1L;
      }
   }

   public class ContaNaturalKey {
      public int numeroBanco;
      public int numeroAgencia;
      public String numeroConta;
      public int tipoConta;

      public ContaNaturalKey() {
         Objects.requireNonNull(ContaLocalizerCmd.this);
         super();
      }

      public Object[] asObjectArray() {
         return new Object[]{this.numeroBanco, this.numeroAgencia, this.numeroConta, this.tipoConta};
      }

      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         result = 31 * result + this.numeroAgencia;
         result = 31 * result + this.numeroBanco;
         result = 31 * result + (this.numeroConta == null ? 0 : this.numeroConta.hashCode());
         result = 31 * result + this.tipoConta;
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
            ContaNaturalKey other = (ContaNaturalKey)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
               return false;
            } else if (this.numeroAgencia != other.numeroAgencia) {
               return false;
            } else if (this.numeroBanco != other.numeroBanco) {
               return false;
            } else {
               if (this.numeroConta == null) {
                  if (other.numeroConta != null) {
                     return false;
                  }
               } else if (!this.numeroConta.equals(other.numeroConta)) {
                  return false;
               }

               return this.tipoConta == other.tipoConta;
            }
         }
      }

      private ContaLocalizerCmd getOuterType() {
         return ContaLocalizerCmd.this;
      }
   }
}
