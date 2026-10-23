package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import br.mp.mpf.spea.simba.validador.domain.Arquivos;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import com.google.common.base.Stopwatch;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Carregar {
   private static final Logger log = LoggerFactory.getLogger(Carregar.class);
   @Inject
   private CarregarArquivoAgenciasCmd agenciasCmd;
   @Inject
   private CarregarArquivoContasCmd contasCmd;
   @Inject
   private CarregarArquivoTitularesCmd titularesCmd;
   @Inject
   private CarregarArquivoExtratoCmd extratoCmd;
   @Inject
   private CarregarArquivoOrigemDestinoCmd origemDestinoCmd;
   @Inject
   private SharedConnection sharedConnection;
   @Inject
   private ApagarTabelasCmd apagarTabelasCmd;
   @Inject
   private GetInvestigadosHashTableCmd getInvestigadosHashTableCmd;
   private Desempenho desempenho;

   public void carregar(AtendimentoBancario atendimentoBancario, Arquivos arquivos) {
      log.debug("Inicializando carga dos arquivos");
      CargaInfo cargaInfo = new CargaInfo();
      cargaInfo.setAtendimentoBancario(atendimentoBancario);
      this.agenciasCmd.setCargaInfo(cargaInfo);
      this.contasCmd.setCargaInfo(cargaInfo);
      this.titularesCmd.setCargaInfo(cargaInfo);
      this.extratoCmd.setCargaInfo(cargaInfo);
      this.origemDestinoCmd.setCargaInfo(cargaInfo);

      try {
         this.apagarTabelasCmd.apagar();
         Connection con = this.sharedConnection.get();
         con.setAutoCommit(false);
         Stopwatch stopwatch = Stopwatch.createStarted();
         Map<String, Long> investigados = this.getInvestigadosHashTableCmd.get();
         boolean naoEhCorretora = false;
         log.debug("Chamando carga do arquivo AGENCIAS");
         this.desempenho.tarefaBackground.startLoad(NomeArquivo.AGENCIAS);
         this.agenciasCmd.carregar(arquivos.getAgencias(), this.desempenho, naoEhCorretora, 0);
         this.temErroNaCarga(cargaInfo, NomeArquivo.AGENCIAS);
         log.debug("Chamando carga do arquivo CONTAS");
         this.desempenho.tarefaBackground.startLoad(NomeArquivo.CONTAS);
         this.contasCmd.carregar(arquivos.getContas(), this.desempenho, naoEhCorretora, 0);
         this.temErroNaCarga(cargaInfo, NomeArquivo.CONTAS);
         log.debug("Chamando carga do arquivo TITULARES");
         this.desempenho.tarefaBackground.startLoad(NomeArquivo.TITULARES);
         this.titularesCmd.carregar(arquivos.getTitulares(), this.desempenho, investigados);
         this.temErroNaCarga(cargaInfo, NomeArquivo.TITULARES);
         log.debug("Chamando carga do arquivo EXTRATO");
         this.desempenho.tarefaBackground.startLoad(NomeArquivo.EXTRATO);
         this.extratoCmd.carregar(arquivos.getExtrato(), this.desempenho, naoEhCorretora, 0);
         this.temErroNaCarga(cargaInfo, NomeArquivo.EXTRATO);
         log.debug("Chamando carga do arquivo ORIGEM_DESTINO");
         this.desempenho.tarefaBackground.startLoad(NomeArquivo.ORIGEM_DESTINO);
         this.origemDestinoCmd.carregar(arquivos.getOrigemDestino(), this.desempenho, naoEhCorretora, 0);
         this.temErroNaCarga(cargaInfo, NomeArquivo.ORIGEM_DESTINO);
         stopwatch.stop();
         log.debug("Carga de todos os arquivos realizada em {} segundos", stopwatch.elapsed(TimeUnit.SECONDS));
      } catch (NegocioException e) {
         log.error("Erro de negócio na carga.");

         try {
            this.sharedConnection.get().commit();
         } catch (SQLException var20) {
            log.error("Erro no commit da conexão com o banco.", e);
         }
      } catch (Exception var22) {
         log.error("Erro na carga.", var22);

         try {
            this.sharedConnection.get().commit();
         } catch (SQLException var19) {
            log.error("Erro no rollback da conexão com o banco.", var22);
         }

         throw new InfraestruturaException(var22);
      } finally {
         try {
            this.sharedConnection.get().setAutoCommit(false);
            log.debug("Encerrando carga.");
         } catch (Exception e) {
            log.error("Erro ao fechar a conexão na carga.", e);
         }

      }

   }

   private void temErroNaCarga(CargaInfo cargaInfo, NomeArquivo nomeArquivo) {
      if (cargaInfo.isTemErro()) {
         this.desempenho.tarefaBackground.setFormatoValido(nomeArquivo, false);
         throw new NegocioException("A carga foi interrompida devido a erros anteriores.");
      } else {
         this.desempenho.tarefaBackground.setFormatoValido(nomeArquivo, true);
      }
   }

   public Desempenho getDesempenho() {
      return this.desempenho;
   }

   public void setDesempenho(Desempenho desempenho) {
      this.desempenho = desempenho;
   }
}
