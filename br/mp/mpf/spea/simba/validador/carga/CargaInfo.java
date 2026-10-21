package br.mp.mpf.spea.simba.validador.carga;

import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;

public class CargaInfo {
   private AtendimentoBancario atendimentoBancario;
   private AtendimentoGab atendimentoGab;
   private boolean temErro = false;

   public AtendimentoBancario getAtendimentoBancario() {
      return this.atendimentoBancario;
   }

   public void setAtendimentoBancario(AtendimentoBancario atendimentoBancario) {
      this.atendimentoBancario = atendimentoBancario;
   }

   public AtendimentoGab getAtendimentoGab() {
      return this.atendimentoGab;
   }

   public void setAtendimentoGab(AtendimentoGab atendimentoGab) {
      this.atendimentoGab = atendimentoGab;
   }

   public boolean isTemErro() {
      return this.temErro;
   }

   public void setTemErro(boolean temErro) {
      this.temErro = temErro;
   }
}
