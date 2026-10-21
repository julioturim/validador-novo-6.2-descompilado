package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.jdbc.ConnectionManager;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class InserirAtendimentoCmd {
   @Inject
   private Configurador configurador;
   @Inject
   private ConnectionManager connectionManager;

   public void inserir(Atendimento atendimento) {
      String sql = "insert into atendimento (  NUMERO_CASO               ,  CNPJ         \t           ,  NUMERO_BANCO              ,  NOME_BANCO                ,  NOME_BANCO_ATENDIMENTO    ,  DATA_GERACAO_ARQUIVO      ,  NOME_FUNC_RESPONSAVEL     ,  TELEFONE_FUNC_RESPONSAVEL ,  EMAIL_FUNC_RESPONSAVEL    ,  DATA_INICIO_AFASTAMENTO   ,  DATA_FIM_AFASTAMENTO      ,  NUMERO_CORREIO_BACEN      ,  NUMERO_OFICIO             ,  DATA_OFICIO               ,  NUMERO_PROCESSO           ,  NOME_TRIBUNAL             ,  NUMERO_VARA               ,  NOME_MAGISTRADO           ,  CARGO_MAGISTRADO          ,  DESCRICAO_CONTEUDO        ,  VERSAO_VALIDADOR          ) values (   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,   ? )";
      Connection connection = this.connectionManager.get();

      try {
         connection.createStatement().execute("delete from investigado");
         connection.createStatement().execute("delete from atendimento");
         PreparedStatement stmt = connection.prepareStatement(sql);
         int i = 1;
         stmt.setString(i++, atendimento.getNumeroCaso());
         stmt.setString(i++, atendimento.getCnpj());
         if (atendimento.getNumeroBanco() != null) {
            stmt.setInt(i++, atendimento.getNumeroBanco());
         } else {
            stmt.setNull(i++, 4);
         }

         stmt.setString(i++, atendimento.getNomeBanco());
         stmt.setString(i++, atendimento.getNomeBancoAtendimento());
         if (Objects.nonNull(atendimento.getDataGeracaoArquivo())) {
            stmt.setDate(i++, Date.valueOf(atendimento.getDataGeracaoArquivo()));
         } else {
            stmt.setNull(i++, 91);
         }

         stmt.setString(i++, atendimento.getNomeFuncionarioResponsavel());
         stmt.setString(i++, atendimento.getTelefoneFuncionarioResponsavel());
         stmt.setString(i++, atendimento.getEmailFuncionarioResponsavel());
         stmt.setDate(i++, Date.valueOf(atendimento.getDataInicioAfastamento()));
         stmt.setDate(i++, Date.valueOf(atendimento.getDataFimAfastamento()));
         stmt.setString(i++, atendimento.getNumeroCorreioBacen());
         stmt.setString(i++, atendimento.getNumeroOficio());
         if (Objects.nonNull(atendimento.getDataOficio())) {
            stmt.setDate(i++, Date.valueOf(atendimento.getDataOficio()));
         } else {
            stmt.setNull(i++, 91);
         }

         stmt.setString(i++, atendimento.getNumeroProcesso());
         stmt.setString(i++, atendimento.getNomeTribunal());
         stmt.setString(i++, atendimento.getNumeroVara());
         stmt.setString(i++, atendimento.getNomeMagistrado());
         stmt.setString(i++, atendimento.getCargoMagistrado());
         stmt.setString(i++, atendimento.getDescricaoConteudo());
         stmt.setString(i++, this.configurador.getVersao());
         stmt.execute();
         sql = "select codigo_chave_atendimento from atendimento where numero_caso = '" + atendimento.getNumeroCaso() + "'";
         ResultSet rs = connection.createStatement().executeQuery(sql);
         if (!rs.next()) {
            throw new InfraestruturaException("Erro ao recuperar chave do atendimento do caso " + atendimento.getNumeroCaso());
         } else {
            atendimento.setId(rs.getLong("CODIGO_CHAVE_ATENDIMENTO"));
            atendimento.setVersaoValidador(this.configurador.getVersao());
         }
      } catch (Exception e) {
         try {
            connection.rollback();
         } catch (SQLException e1) {
            throw new InfraestruturaException("Erro ao executar rollback", e1);
         }

         throw new InfraestruturaException("Erro ao inserir atendimento", e);
      }
   }
}
