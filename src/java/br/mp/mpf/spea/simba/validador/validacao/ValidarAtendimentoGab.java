package br.mp.mpf.spea.simba.validador.validacao;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.rules.InvestigadoGabNenhumRelacionamentoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarGab112InvestigadoNaoInformadoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarGab112InvestigadoSemRelacionamentoRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarGab112NaoGab109SimRule;
import br.mp.mpf.spea.simba.validador.rules.ValidarGab112NaoGab800SimRule;
import jakarta.inject.Inject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import telas.frmSelecionaDiretorioGab;

@Component
@Scope("prototype")
public class ValidarAtendimentoGab {
   @Inject
   private ValidarGab112InvestigadoSemRelacionamentoRule validarGab112InvestigadoSemRelacionamentoRule;
   @Inject
   private ValidarGab112InvestigadoNaoInformadoRule validarGab112InvestigadoNaoInformadoRule;
   @Inject
   private ValidarGab112NaoGab109SimRule validarGab112NaoGab109SimRule;
   @Inject
   private ValidarGab112NaoGab800SimRule validarGab112NaoGab800SimRule;
   @Inject
   private InvestigadoGabNenhumRelacionamentoRule investigadoGabNenhumRelacionamentoRule;
   @Inject
   private ErrorReportCmd errorReportCmd;

   public boolean validarArquivoGab112(frmSelecionaDiretorioGab.ViewGab viewGab) {
      try {
         viewGab.startValidation(NomeArquivoGab.GAB112);
         this.investigadoGabNenhumRelacionamentoRule.validar();
         this.validarGab112InvestigadoSemRelacionamentoRule.validar();
         this.validarGab112InvestigadoNaoInformadoRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }

   public boolean validarArquivoGab109(frmSelecionaDiretorioGab.ViewGab viewGab) {
      try {
         viewGab.startValidation(NomeArquivoGab.GAB109);
         this.validarGab112NaoGab109SimRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }

   public boolean validarArquivoGab800(frmSelecionaDiretorioGab.ViewGab viewGab) {
      try {
         viewGab.startValidation(NomeArquivoGab.GAB800);
         this.validarGab112NaoGab800SimRule.validar();
         return !this.errorReportCmd.hasErros();
      } catch (NegocioException var3) {
         return false;
      }
   }
}
