package DAO;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.ConnectionManager;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BancoHSQLDB {
   private static final Logger log = LoggerFactory.getLogger(BancoHSQLDB.class);
   @Inject
   private ConnectionManager conexao;

   public String getVersaoSistema() {
      String sql = "SELECT VERSAO FROM SISTEMA";

      try {
         ResultSet rs = this.executaQuery(sql);
         return rs.next() ? rs.getString(1) : null;
      } catch (SQLException e) {
         throw new InfraestruturaException(e);
      }
   }

   public ResultSet executaQuery(String sql) {
      if (!this.conexao.isOpened()) {
         throw new InfraestruturaException("Banco HSQLDB não está inicializado");
      } else {
         log.trace("BancoHSQLDB: executaquery: SQL: {}", sql);

         try {
            return this.conexao.get().prepareStatement(sql).executeQuery();
         } catch (SQLException e) {
            log.error("Erro ao executar o sql: " + sql, e);
            return null;
         }
      }
   }

   public boolean executaUpdate(String sql) {
      if (!this.conexao.isOpened()) {
         throw new InfraestruturaException("Banco HSQLDB não está inicializado");
      } else {
         log.trace("BancoHSQLDB: executaUpdate: SQL: " + sql);

         try {
            this.conexao.get().prepareStatement(sql).executeUpdate();
            return true;
         } catch (SQLException e) {
            log.error("Erro ao executar comando sql: " + sql, e);
            return false;
         }
      }
   }

   public boolean gravarHashArquivo(int codArquivo, String sHash) {
      String var10000 = String.valueOf(codArquivo);
      String strSQL = "UPDATE ATENDIMENTO SET HASH_ARQUIVO_" + var10000 + " ='" + sHash + "'";
      return this.executaUpdate(strSQL);
   }

   public boolean gravarHashArquivoGab(NomeArquivoGab nome, String sHash) {
      String var10000 = String.valueOf(nome);
      String strSQL = "UPDATE ATENDIMENTO SET HASH_ARQUIVO_" + var10000 + " ='" + sHash + "'";
      return this.executaUpdate(strSQL);
   }

   public boolean atualizaQtdLinhasArquivo(int codArquivo, int qtdLinhas) {
      String var10001 = String.valueOf(codArquivo);
      return this.executaUpdate("UPDATE ATENDIMENTO SET LINHAS_ARQUIVO_" + var10001 + " = " + String.valueOf(qtdLinhas));
   }
}
