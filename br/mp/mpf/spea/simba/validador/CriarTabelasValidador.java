package br.mp.mpf.spea.simba.validador;

import DAO.Mapa;
import DAO.MapaGab;
import br.mp.mpf.pgr.client.jdbc.CriarTabelasCmd;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CriarTabelasValidador extends CriarTabelasCmd {
   private static final Logger log = LoggerFactory.getLogger(CriarTabelasValidador.class);

   protected void executeStatements() throws SQLException {
      this.execute("SET PROPERTY \"sql.enforce_strict_size\" true");
      this.execute(Mapa.getSqlCriaTabelaSistema());
      log.debug("Criando tabela SISTEMA. Versão: {}", this.configurador.getVersao());
      this.execute("INSERT INTO SISTEMA(VERSAO) VALUES ('" + this.configurador.getVersao() + "')");
      this.execute(Mapa.getSqlCriaTabelaAtendimento());
      this.execute(Mapa.getSqlCriaTabelaInvestigado());
      this.execute(Mapa.getSqlCriaTabelaAgencia());
      this.execute(Mapa.getSqlCriaTabelaConta());
      this.execute(Mapa.getSqlCriaTabelaExtrato());
      this.execute(Mapa.getSqlCriaTabelaPessoa());
      this.execute(Mapa.getSqlCriaTabelaTitularidade());
      this.execute(Mapa.getSqlCriaTabelaOrigemDestino());
      this.execute(MapaGab.getSqlCriaTabelaGab109());
      this.execute(MapaGab.getSqlCriaTabelaGab112());
      this.execute(MapaGab.getSqlCriaTabelaGab800());
      this.execute(MapaGab.getSqlCriaTabelaGab109Ver2());
      this.execute(MapaGab.getSqlCriaTabelaGab112Ver2());
      this.execute(MapaGab.getSqlCriaTabelaGab800Ver2());
      this.execute("CREATE UNIQUE INDEX IDX_ATENDIMENTO ON ATENDIMENTO (NUMERO_CASO)");
      this.execute("CREATE INDEX IDX_AGENCIA ON AGENCIA (NUMERO_BANCO, NUMERO_AGENCIA)");
      this.execute("CREATE INDEX IDX_PESSOA ON PESSOA (TIPO_PESSOA, CPF_CNPJ_PESSOA, NOME_PESSOA, NOME_DOC_IDENTIFICACAO, NUMERO_DOC_IDENTIFICACAO)");
      this.execute("CREATE INDEX IDX_PESSOA_LINHA ON PESSOA (LINHA_ARQUIVO)");
      this.execute("CREATE INDEX IDX_CONTA ON CONTA (CODIGO_CHAVE_AGENCIA, NUMERO_CONTA, TIPO_CONTA)");
      this.execute("CREATE INDEX IDX_EXTRATO_1 ON EXTRATO (TIPO_LANCAMENTO, VALOR_LANCAMENTO)");
      this.execute("CREATE INDEX IDX_EXTRATO_2 ON EXTRATO (TIPO_LANCAMENTO, DATA_LANCAMENTO)");
      this.execute("CREATE INDEX IDX_EXTRATO_3 ON EXTRATO (DATA_LANCAMENTO)");
   }

   public String getTabelaAtendimento() {
      return Mapa.getNomesTabelasA()[0];
   }
}
