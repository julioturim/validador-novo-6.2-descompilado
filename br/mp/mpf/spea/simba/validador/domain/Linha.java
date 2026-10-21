package br.mp.mpf.spea.simba.validador.domain;

public abstract class Linha {
   protected long linha;

   public void clear() {
      this.linha = 0L;
   }

   public long getLinha() {
      return this.linha;
   }

   public void setLinha(long linha) {
      this.linha = linha;
   }
}
