package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.pgr.client.validacao.AbstractRule;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;

public abstract class AbstractOrigemDestinoRule extends AbstractRule {
   public AbstractOrigemDestinoRule() {
      this.nomeArquivo = NomeArquivo.ORIGEM_DESTINO.toString();
   }
}
