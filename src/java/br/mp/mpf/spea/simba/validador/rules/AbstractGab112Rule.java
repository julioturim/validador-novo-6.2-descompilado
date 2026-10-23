package br.mp.mpf.spea.simba.validador.rules;

import br.mp.mpf.pgr.client.validacao.AbstractRule;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;

public abstract class AbstractGab112Rule extends AbstractRule {
   public AbstractGab112Rule() {
      this.nomeArquivo = NomeArquivoGab.GAB112.toString();
   }
}
