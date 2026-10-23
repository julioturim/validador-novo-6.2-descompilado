package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.pgr.client.validacao.AbstractRule;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;

public abstract class AbstractTitularesRule extends AbstractRule {
   public AbstractTitularesRule() {
      this.nomeArquivo = NomeArquivo.TITULARES.toString();
   }
}
