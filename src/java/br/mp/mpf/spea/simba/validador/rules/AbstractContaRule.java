package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.pgr.client.validacao.AbstractRule;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;

public abstract class AbstractContaRule extends AbstractRule {
   public AbstractContaRule() {
      this.nomeArquivo = NomeArquivo.CONTAS.toString();
   }
}
