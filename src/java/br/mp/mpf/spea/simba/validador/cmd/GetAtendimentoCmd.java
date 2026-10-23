package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class GetAtendimentoCmd {
   @Inject
   private SharedConnection sharedConnection;

   public Atendimento get(String numeroCaso) {
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
            Atendimento atendimento = new Atendimento();
            atendimento.setId(rs.getLong("CODIGO_CHAVE_ATENDIMENTO"));
            atendimento.setNumeroCaso(rs.getString("NUMERO_CASO"));
            if (Objects.nonNull(rs.getObject("NUMERO_BANCO"))) {
               atendimento.setNumeroBanco(rs.getInt("NUMERO_BANCO"));
            }

            atendimento.setCnpj(rs.getString("CNPJ"));
            atendimento.setNomeBanco(rs.getString("NOME_BANCO"));
            atendimento.setNomeBancoAtendimento(rs.getString("NOME_BANCO_ATENDIMENTO"));
            Date dataGeracaoArquivo = rs.getDate("DATA_GERACAO_ARQUIVO");
            if (dataGeracaoArquivo != null) {
               atendimento.setDataGeracaoArquivo((new java.sql.Date(dataGeracaoArquivo.getTime())).toLocalDate());
            }

            atendimento.setNomeFuncionarioResponsavel(rs.getString("NOME_FUNC_RESPONSAVEL"));
            atendimento.setTelefoneFuncionarioResponsavel(rs.getString("TELEFONE_FUNC_RESPONSAVEL"));
            atendimento.setEmailFuncionarioResponsavel(rs.getString("EMAIL_FUNC_RESPONSAVEL"));
            Date dataInicioAfastamento = rs.getDate("DATA_INICIO_AFASTAMENTO");
            if (dataInicioAfastamento != null) {
               atendimento.setDataInicioAfastamento((new java.sql.Date(dataInicioAfastamento.getTime())).toLocalDate());
            }

            Date dataFimAfastamento = rs.getDate("DATA_FIM_AFASTAMENTO");
            if (dataFimAfastamento != null) {
               atendimento.setDataFimAfastamento((new java.sql.Date(dataFimAfastamento.getTime())).toLocalDate());
            }

            atendimento.setNumeroCorreioBacen(rs.getString("NUMERO_CORREIO_BACEN"));
            atendimento.setNumeroOficio(rs.getString("NUMERO_OFICIO"));
            Date dataOficio = rs.getDate("DATA_OFICIO");
            if (dataOficio != null) {
               atendimento.setDataOficio((new java.sql.Date(dataOficio.getTime())).toLocalDate());
            }

            atendimento.setNumeroProcesso(rs.getString("NUMERO_PROCESSO"));
            atendimento.setNomeTribunal(rs.getString("NOME_TRIBUNAL"));
            atendimento.setNumeroVara(rs.getString("NUMERO_VARA"));
            atendimento.setNomeMagistrado(rs.getString("NOME_MAGISTRADO"));
            atendimento.setCargoMagistrado(rs.getString("CARGO_MAGISTRADO"));
            atendimento.setDescricaoConteudo(rs.getString("DESCRICAO_CONTEUDO"));
            atendimento.setVersaoValidador(rs.getString("VERSAO_VALIDADOR"));
            return atendimento;
         }
      } catch (SQLException e) {
         throw new InfraestruturaException("Erro ao recuperar dados do atendimento.", e);
      }
   }
}
