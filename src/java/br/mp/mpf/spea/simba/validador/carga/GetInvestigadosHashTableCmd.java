package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetInvestigadosHashTableCmd {
   @Inject
   private SharedConnection sharedConnection;
   private Map<String, Long> map;

   public Map<String, Long> get() {
      if (this.map != null) {
         return this.map;
      } else {
         this.map = new HashMap(1000);

         try {
            Connection cn = this.sharedConnection.get();
            Statement stmt = cn.createStatement();
            ResultSet rs = stmt.executeQuery("select CODIGO_INVESTIGADO, CPF_CNPJ_PESSOA from investigado");

            while(rs.next()) {
               String cpfCnpjPessoa = rs.getString(2);
               cpfCnpjPessoa = StringUtil.completarZerosEsquerda(cpfCnpjPessoa, 14);
               long codigoInvestigado = rs.getLong(1);
               this.map.put(cpfCnpjPessoa, codigoInvestigado);
            }

            return this.map;
         } catch (SQLException e) {
            throw new InfraestruturaException(e);
         }
      }
   }
}
