package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import com.google.common.base.Stopwatch;
import jakarta.inject.Inject;
import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CarregarArquivoTitularesCmd implements HasCargaInfo {
   private static final Logger log = LoggerFactory.getLogger(CarregarArquivoTitularesCmd.class);
   private CarregarTabelaPessoaCmd carregarTabelaPessoaCmd;
   private CarregarTabelaTitularidadeCmd carregarTabelaTitularidadeCmd;

   @Inject
   private CarregarArquivoTitularesCmd(CarregarTabelaPessoaCmd carregarTabelaPessoaCmd, CarregarTabelaTitularidadeCmd carregarTabelaTitularidadeCmd) {
      this.carregarTabelaPessoaCmd = carregarTabelaPessoaCmd;
      this.carregarTabelaTitularidadeCmd = carregarTabelaTitularidadeCmd;
   }

   public void carregar(File file, Desempenho desempenho, Map<String, Long> investigados) throws SQLException, ParseException {
      log.debug("Iniciando carga. Tamanho: {} Arquivo: {}", file.length(), file);
      Stopwatch cargaStopwatch = Stopwatch.createStarted();
      this.carregarTabelaPessoaCmd.setInvestigados(investigados);
      this.carregarTabelaPessoaCmd.carregar(file, desempenho, false, 0);
      if (this.getCargaInfo().isTemErro()) {
         log.debug("Carga cancelada devido a erro(s) anteriores.");
      } else {
         this.carregarTabelaTitularidadeCmd.carregar(file, desempenho, false, 0);
         cargaStopwatch.stop();
         log.debug("Carga realizada em {} segundos", cargaStopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000L);
      }
   }

   public CargaInfo getCargaInfo() {
      return this.carregarTabelaPessoaCmd.getCargaInfo() != null ? this.carregarTabelaPessoaCmd.getCargaInfo() : this.carregarTabelaTitularidadeCmd.getCargaInfo();
   }

   public void setCargaInfo(CargaInfo cargaInfo) {
      this.carregarTabelaPessoaCmd.setCargaInfo(cargaInfo);
      this.carregarTabelaTitularidadeCmd.setCargaInfo(cargaInfo);
   }
}
