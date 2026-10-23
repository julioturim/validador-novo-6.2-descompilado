package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import br.mp.mpf.spea.simba.validador.domain.ArquivosGab;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import com.google.common.base.Stopwatch;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarGab {
   private static final Logger log = LoggerFactory.getLogger(CarregarGab.class);
   @Inject
   private CarregarArquivoGab109Cmd arquivoGab109;
   @Inject
   private CarregarArquivoGab112Cmd arquivoGab112;
   @Inject
   private CarregarArquivoGab800Cmd arquivoGab800;
   @Inject
   private SharedConnection sharedConnection;
   @Inject
   private ApagarTabelasCmd apagarTabelasCmd;
   @Inject
   private Configurador configurador;
   private Desempenho desempenho;

   public void carregarGab(AtendimentoGab atendimento, ArquivosGab arquivos) {
      log.debug("Inicializando carga dos arquivos");
      CargaInfo cargaInfo = new CargaInfo();
      cargaInfo.setAtendimentoGab(atendimento);
      this.arquivoGab109.setCargaInfo(cargaInfo);
      this.arquivoGab112.setCargaInfo(cargaInfo);
      this.arquivoGab800.setCargaInfo(cargaInfo);

      try {
         this.apagarTabelasCmd.apagarGab();
         Connection con = this.sharedConnection.get();
         con.setAutoCommit(false);
         Stopwatch stopwatch = Stopwatch.createStarted();
         boolean isCorretora = true;
         log.debug("Chamando carga do arquivo GAB109");
         this.desempenho.tarefaBackgroundGab.startLoad(NomeArquivoGab.GAB109);
         this.arquivoGab109.carregar(arquivos.getGab109(), this.desempenho, isCorretora, this.configurador.getVersaoLayoutGab());
         this.temErroNaCarga(cargaInfo, NomeArquivoGab.GAB109);
         log.debug("Chamando carga do arquivo GAB112");
         this.desempenho.tarefaBackgroundGab.startLoad(NomeArquivoGab.GAB112);
         this.arquivoGab112.carregar(arquivos.getGab112(), this.desempenho, isCorretora, this.configurador.getVersaoLayoutGab());
         this.temErroNaCarga(cargaInfo, NomeArquivoGab.GAB112);
         log.debug("Chamando carga do arquivo GAB800");
         this.desempenho.tarefaBackgroundGab.startLoad(NomeArquivoGab.GAB800);
         this.arquivoGab800.carregar(arquivos.getGab800(), this.desempenho, isCorretora, this.configurador.getVersaoLayoutGab());
         this.temErroNaCarga(cargaInfo, NomeArquivoGab.GAB800);
         stopwatch.stop();
         log.debug("Carga de todos os arquivos realizada em {} segundos", stopwatch.elapsed(TimeUnit.SECONDS));
      } catch (NegocioException e) {
         log.error("Erro de negócio na carga.");

         try {
            this.sharedConnection.get().commit();
         } catch (SQLException var19) {
            log.error("Erro no commit da conexão com o banco.", e);
         }
      } catch (Exception var21) {
         log.error("Erro na carga.", var21);

         try {
            this.sharedConnection.get().commit();
         } catch (SQLException var18) {
            log.error("Erro no rollback da conexão com o banco.", var21);
         }

         throw new InfraestruturaException(var21);
      } finally {
         try {
            this.sharedConnection.get().setAutoCommit(false);
            log.debug("Encerrando carga.");
         } catch (Exception e) {
            log.error("Erro ao fechar a conexão na carga.", e);
         }

      }

   }

   private void temErroNaCarga(CargaInfo cargaInfo, NomeArquivoGab nomeArquivo) {
      if (cargaInfo.isTemErro()) {
         this.desempenho.tarefaBackgroundGab.setFormatoValido(nomeArquivo, false);
         throw new NegocioException("A carga foi interrompida devido a erros anteriores.");
      } else {
         this.desempenho.tarefaBackgroundGab.setFormatoValido(nomeArquivo, true);
      }
   }

   public Desempenho getDesempenho() {
      return this.desempenho;
   }

   public void setDesempenho(Desempenho desempenho) {
      this.desempenho = desempenho;
   }
}
