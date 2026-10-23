package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.pgr.client.validacao.AbstractRule;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;

public abstract class AbstractGab109Rule extends AbstractRule {
   public AbstractGab109Rule() {
      this.nomeArquivo = NomeArquivoGab.GAB109.toString();
   }
}
