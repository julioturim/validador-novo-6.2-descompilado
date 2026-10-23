package br.mp.mpf.spea.simba.validador.domain;

public class Extrato extends Linha {
   private long codigoChave;
   private long idConta;
   private String data;
   private String numeroDocumento;
   private String descricao;
   private int tipoLancamento;
   private long valor;
   private String naturezaLancamento;
   private long valorSaldo;
   private String naturezaSaldo;
   private String localTransacao;

   public void clear() {
      super.clear();
      this.codigoChave = 0L;
      this.idConta = 0L;
      this.data = null;
      this.numeroDocumento = null;
      this.descricao = null;
      this.tipoLancamento = 0;
      this.valor = 0L;
      this.naturezaLancamento = null;
      this.valorSaldo = 0L;
      this.naturezaSaldo = null;
      this.localTransacao = null;
   }

   public long getIdConta() {
      return this.idConta;
   }

   public void setIdConta(long idConta) {
      this.idConta = idConta;
   }

   public String getData() {
      return this.data;
   }

   public void setData(String data) {
      this.data = data;
   }

   public String getNumeroDocumento() {
      return this.numeroDocumento;
   }

   public void setNumeroDocumento(String numeroDocumento) {
      this.numeroDocumento = numeroDocumento;
   }

   public String getDescricao() {
      return this.descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public int getTipoLancamento() {
      return this.tipoLancamento;
   }

   public void setTipoLancamento(int tipoLancamento) {
      this.tipoLancamento = tipoLancamento;
   }

   public long getValor() {
      return this.valor;
   }

   public void setValor(long valor) {
      this.valor = valor;
   }

   public String getNaturezaLancamento() {
      return this.naturezaLancamento;
   }

   public void setNaturezaLancamento(String naturezaLancamento) {
      this.naturezaLancamento = naturezaLancamento;
   }

   public long getValorSaldo() {
      return this.valorSaldo;
   }

   public void setValorSaldo(long valorSaldo) {
      this.valorSaldo = valorSaldo;
   }

   public String getNaturezaSaldo() {
      return this.naturezaSaldo;
   }

   public void setNaturezaSaldo(String naturezaSaldo) {
      this.naturezaSaldo = naturezaSaldo;
   }

   public String getLocalTransacao() {
      return this.localTransacao;
   }

   public void setLocalTransacao(String localTransacao) {
      this.localTransacao = localTransacao;
   }

   public long getCodigoChave() {
      return this.codigoChave;
   }

   public void setCodigoChave(long codigoChave) {
      this.codigoChave = codigoChave;
   }
}
