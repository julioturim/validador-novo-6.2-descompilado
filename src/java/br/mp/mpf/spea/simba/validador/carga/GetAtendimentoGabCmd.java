package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class GetAtendimentoGabCmd {
   @Inject
   private SharedConnection sharedConnection;

   public AtendimentoGab get(String numeroCaso) {
      try {
         String sql = "select * from atendimento where numero_caso = ?";
         Connection cn = this.sharedConnection.get();
         PreparedStatement stmt = cn.prepareStatement(sql);
         stmt.setString(1, numeroCaso);
         ResultSet rs = stmt.executeQuery();
         if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
         } else {
            AtendimentoGab atendimentoGab = new AtendimentoGab();
            atendimentoGab.setId(rs.getLong("CODIGO_CHAVE_ATENDIMENTO"));
            atendimentoGab.setNumeroCaso(rs.getString("NUMERO_CASO"));
            atendimentoGab.setNumeroBanco(rs.getInt("NUMERO_BANCO"));
            atendimentoGab.setCnpj(rs.getString("CNPJ"));
            atendimentoGab.setNomeBanco(rs.getString("NOME_BANCO"));
            atendimentoGab.setNomeBancoAtendimento(rs.getString("NOME_BANCO_ATENDIMENTO"));
            Date dataGeracaoArquivo = rs.getDate("DATA_GERACAO_ARQUIVO");
            if (dataGeracaoArquivo != null) {
               atendimentoGab.setDataGeracaoArquivo((new java.sql.Date(dataGeracaoArquivo.getTime())).toLocalDate());
            }

            atendimentoGab.setNomeFuncionarioResponsavel(rs.getString("NOME_FUNC_RESPONSAVEL"));
            atendimentoGab.setTelefoneFuncionarioResponsavel(rs.getString("TELEFONE_FUNC_RESPONSAVEL"));
            atendimentoGab.setEmailFuncionarioResponsavel(rs.getString("EMAIL_FUNC_RESPONSAVEL"));
            Date dataInicioAfastamento = rs.getDate("DATA_INICIO_AFASTAMENTO");
            if (dataInicioAfastamento != null) {
               atendimentoGab.setDataInicioAfastamento((new java.sql.Date(dataInicioAfastamento.getTime())).toLocalDate());
            }

            Date dataFimAfastamento = rs.getDate("DATA_FIM_AFASTAMENTO");
            if (dataFimAfastamento != null) {
               atendimentoGab.setDataFimAfastamento((new java.sql.Date(dataFimAfastamento.getTime())).toLocalDate());
            }

            atendimentoGab.setNumeroCorreioBacen(rs.getString("NUMERO_CORREIO_BACEN"));
            atendimentoGab.setNumeroOficio(rs.getString("NUMERO_OFICIO"));
            Date dataOficio = rs.getDate("DATA_OFICIO");
            if (dataOficio != null) {
               atendimentoGab.setDataOficio((new java.sql.Date(dataOficio.getTime())).toLocalDate());
            }

            atendimentoGab.setNumeroProcesso(rs.getString("NUMERO_PROCESSO"));
            atendimentoGab.setNomeTribunal(rs.getString("NOME_TRIBUNAL"));
            atendimentoGab.setNumeroVara(rs.getString("NUMERO_VARA"));
            atendimentoGab.setNomeMagistrado(rs.getString("NOME_MAGISTRADO"));
            atendimentoGab.setCargoMagistrado(rs.getString("CARGO_MAGISTRADO"));
            atendimentoGab.setDescricaoConteudo(rs.getString("DESCRICAO_CONTEUDO"));
            atendimentoGab.setHashArquivoGab109(rs.getString("HASH_ARQUIVO_GAB109"));
            atendimentoGab.setHashArquivoGab112(rs.getString("HASH_ARQUIVO_GAB112"));
            atendimentoGab.setHashArquivoGab800(rs.getString("HASH_ARQUIVO_GAB800"));
            atendimentoGab.setLinhasArquivoGab109(rs.getLong("LINHAS_ARQUIVO_Gab109"));
            atendimentoGab.setLinhasArquivoGab112(rs.getLong("LINHAS_ARQUIVO_Gab112"));
            atendimentoGab.setLinhasArquivoGab800(rs.getLong("LINHAS_ARQUIVO_Gab800"));
            atendimentoGab.setVersaoValidador(rs.getString("VERSAO_VALIDADOR"));
            return atendimentoGab;
         }
      } catch (SQLException e) {
         throw new InfraestruturaException("Erro ao recuperar dados do atendimentoGab.", e);
      }
   }
}
