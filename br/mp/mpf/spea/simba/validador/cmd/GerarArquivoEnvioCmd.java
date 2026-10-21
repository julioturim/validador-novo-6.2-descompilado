package br.mp.mpf.spea.simba.validador.cmd;

import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.enums.TipoAtendimento;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import jakarta.inject.Inject;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class GerarArquivoEnvioCmd {
   @Inject
   private Context context;
   @Inject
   private GetAtendimentoCmd getAtendimentoCmd;
   @Inject
   private AtualizarDataGeracaoAtendimentoCmd atualizarDataGeracaoAtendimentoCmd;
   @Inject
   private GerarArquivoAtendimentoCmd gerarArquivoAtendimentoCmd;
   @Inject
   private VerificarHashArquivosBancarioCmd verificarHashArquivosBancarioCmd;
   @Inject
   private VerificarHashArquivosCorretoraCmd verificarHashArquivosCorretoraCmd;
   @Inject
   private GerarArquivoInvestigadosCmd gerarArquivoInvestigadosCmd;
   @Inject
   private CompactarArquivosParaEnvioCmd compactarArquivosParaEnvioCmd;

   public void gerar(TipoAtendimento tipoAtendimento) {
      String numeroCaso = this.context.getNumeroCaso();
      Atendimento atendimento = this.getAtendimentoCmd.get(numeroCaso);
      this.atualizarDataGeracaoAtendimentoCmd.atualizar(atendimento, LocalDate.now());
      if (tipoAtendimento == TipoAtendimento.BANCARIO) {
         this.gerarBancario();
      } else if (tipoAtendimento == TipoAtendimento.CORRETORA) {
         this.gerarCorretora();
      }

   }

   private void gerarBancario() {
      this.verificarHashArquivosBancarioCmd.verificar();
      this.gerarArquivoAtendimentoCmd.gerar(TipoAtendimento.BANCARIO);
      this.gerarArquivoInvestigadosCmd.gerar();
      this.compactarArquivosParaEnvioCmd.compactar(TipoAtendimento.BANCARIO);
   }

   private void gerarCorretora() {
      this.verificarHashArquivosCorretoraCmd.verificar();
      this.gerarArquivoAtendimentoCmd.gerar(TipoAtendimento.CORRETORA);
      this.gerarArquivoInvestigadosCmd.gerar();
      this.compactarArquivosParaEnvioCmd.compactar(TipoAtendimento.CORRETORA);
   }
}
