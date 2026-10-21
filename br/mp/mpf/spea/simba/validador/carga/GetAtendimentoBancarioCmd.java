package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class GetAtendimentoBancarioCmd {
   @Inject
   private SharedConnection sharedConnection;

   public AtendimentoBancario get(String numeroCaso) {
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
            AtendimentoBancario atendimentoBancario = new AtendimentoBancario();
            atendimentoBancario.setId(rs.getLong("CODIGO_CHAVE_ATENDIMENTO"));
            atendimentoBancario.setNumeroCaso(rs.getString("NUMERO_CASO"));
            atendimentoBancario.setNumeroBanco(rs.getInt("NUMERO_BANCO"));
            atendimentoBancario.setCnpj(rs.getString("CNPJ"));
            atendimentoBancario.setNomeBanco(rs.getString("NOME_BANCO"));
            atendimentoBancario.setNomeBancoAtendimento(rs.getString("NOME_BANCO_ATENDIMENTO"));
            Date dataGeracaoArquivo = rs.getDate("DATA_GERACAO_ARQUIVO");
            if (dataGeracaoArquivo != null) {
               atendimentoBancario.setDataGeracaoArquivo((new java.sql.Date(dataGeracaoArquivo.getTime())).toLocalDate());
            }

            atendimentoBancario.setNomeFuncionarioResponsavel(rs.getString("NOME_FUNC_RESPONSAVEL"));
            atendimentoBancario.setTelefoneFuncionarioResponsavel(rs.getString("TELEFONE_FUNC_RESPONSAVEL"));
            atendimentoBancario.setEmailFuncionarioResponsavel(rs.getString("EMAIL_FUNC_RESPONSAVEL"));
            Date dataInicioAfastamento = rs.getDate("DATA_INICIO_AFASTAMENTO");
            if (dataInicioAfastamento != null) {
               atendimentoBancario.setDataInicioAfastamento((new java.sql.Date(dataInicioAfastamento.getTime())).toLocalDate());
            }

            Date dataFimAfastamento = rs.getDate("DATA_FIM_AFASTAMENTO");
            if (dataFimAfastamento != null) {
               atendimentoBancario.setDataFimAfastamento((new java.sql.Date(dataFimAfastamento.getTime())).toLocalDate());
            }

            atendimentoBancario.setNumeroCorreioBacen(rs.getString("NUMERO_CORREIO_BACEN"));
            atendimentoBancario.setNumeroOficio(rs.getString("NUMERO_OFICIO"));
            Date dataOficio = rs.getDate("DATA_OFICIO");
            if (dataOficio != null) {
               atendimentoBancario.setDataOficio((new java.sql.Date(dataOficio.getTime())).toLocalDate());
            }

            atendimentoBancario.setNumeroProcesso(rs.getString("NUMERO_PROCESSO"));
            atendimentoBancario.setNomeTribunal(rs.getString("NOME_TRIBUNAL"));
            atendimentoBancario.setNumeroVara(rs.getString("NUMERO_VARA"));
            atendimentoBancario.setNomeMagistrado(rs.getString("NOME_MAGISTRADO"));
            atendimentoBancario.setCargoMagistrado(rs.getString("CARGO_MAGISTRADO"));
            atendimentoBancario.setDescricaoConteudo(rs.getString("DESCRICAO_CONTEUDO"));
            atendimentoBancario.setHashArquivo1(rs.getString("HASH_ARQUIVO_1"));
            atendimentoBancario.setHashArquivo2(rs.getString("HASH_ARQUIVO_2"));
            atendimentoBancario.setHashArquivo3(rs.getString("HASH_ARQUIVO_3"));
            atendimentoBancario.setHashArquivo4(rs.getString("HASH_ARQUIVO_4"));
            atendimentoBancario.setHashArquivo5(rs.getString("HASH_ARQUIVO_5"));
            atendimentoBancario.setLinhasArquivo1(rs.getLong("LINHAS_ARQUIVO_1"));
            atendimentoBancario.setLinhasArquivo2(rs.getLong("LINHAS_ARQUIVO_2"));
            atendimentoBancario.setLinhasArquivo3(rs.getLong("LINHAS_ARQUIVO_3"));
            atendimentoBancario.setLinhasArquivo4(rs.getLong("LINHAS_ARQUIVO_4"));
            atendimentoBancario.setLinhasArquivo5(rs.getLong("LINHAS_ARQUIVO_5"));
            atendimentoBancario.setVersaoValidador(rs.getString("VERSAO_VALIDADOR"));
            return atendimentoBancario;
         }
      } catch (SQLException e) {
         throw new InfraestruturaException("Erro ao recuperar dados do atendimento.", e);
      }
   }
}
