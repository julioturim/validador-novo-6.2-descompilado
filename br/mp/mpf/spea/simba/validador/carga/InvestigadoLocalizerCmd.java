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
public class InvestigadoLocalizerCmd {
   private static final Logger log = LoggerFactory.getLogger(InvestigadoLocalizerCmd.class);
   private final PreparedStatement ps;
   private final String sql = "select CODIGO_INVESTIGADO from investigado where tipo_pessoa = ? and cpf_cnpj_pessoa = ?";
   private final LoadingCache<InvestigadoNaturalKey, Long> cache;

   @Inject
   public InvestigadoLocalizerCmd(SharedConnection sharedConnection) throws SQLException {
      this.ps = sharedConnection.get().prepareStatement("select CODIGO_INVESTIGADO from investigado where tipo_pessoa = ? and cpf_cnpj_pessoa = ?");
      this.cache = CacheBuilder.newBuilder().initialCapacity(2000).maximumSize(2000L).build(new CacheLoader<InvestigadoNaturalKey, Long>() {
         {
            Objects.requireNonNull(InvestigadoLocalizerCmd.this);
         }

         public Long load(InvestigadoNaturalKey key) throws Exception {
            return InvestigadoLocalizerCmd.this.findIdInvestigado(key);
         }
      });
   }

   public long findIdInvestigado(InvestigadoNaturalKey key) throws SQLException {
      this.ps.clearParameters();
      this.ps.setLong(1, (long)key.tipoPessoa);
      this.ps.setString(2, key.cpfCnpj);
      ResultSet rs;
      if (log.isTraceEnabled()) {
         log.trace("Localizando ID do Investigado - Tipo: {} CpfCnpj: {}", key.tipoPessoa, key.cpfCnpj);
         Stopwatch stopwatch = Stopwatch.createStarted();
         rs = this.ps.executeQuery();
         stopwatch.stop();
         log.trace("Investigado localizado em {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
      } else {
         rs = this.ps.executeQuery();
      }

      return rs.next() ? rs.getLong(1) : -1L;
   }

   public long findIdInvestigado(int tipoPessoa, String cpfCnpj) {
      InvestigadoNaturalKey naturalKey = new InvestigadoNaturalKey();
      naturalKey.tipoPessoa = tipoPessoa;
      naturalKey.cpfCnpj = cpfCnpj;

      try {
         return (Long)this.cache.get(naturalKey);
      } catch (ExecutionException e) {
         log.error("Erro ao localizar investigado.", e);
         return -1L;
      }
   }

   public class InvestigadoNaturalKey {
      public int tipoPessoa;
      public String cpfCnpj;

      public InvestigadoNaturalKey() {
         Objects.requireNonNull(InvestigadoLocalizerCmd.this);
         super();
      }

      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         result = 31 * result + (this.cpfCnpj == null ? 0 : this.cpfCnpj.hashCode());
         result = 31 * result + this.tipoPessoa;
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
            InvestigadoNaturalKey other = (InvestigadoNaturalKey)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
               return false;
            } else {
               if (this.cpfCnpj == null) {
                  if (other.cpfCnpj != null) {
                     return false;
                  }
               } else if (!this.cpfCnpj.equals(other.cpfCnpj)) {
                  return false;
               }

               return this.tipoPessoa == other.tipoPessoa;
            }
         }
      }

      private InvestigadoLocalizerCmd getOuterType() {
         return InvestigadoLocalizerCmd.this;
      }
   }
}
