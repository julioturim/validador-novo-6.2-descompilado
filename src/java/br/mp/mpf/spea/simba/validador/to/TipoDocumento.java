package br.mp.mpf.spea.simba.validador.to;

import java.util.Arrays;

public enum TipoDocumento {
   EXTRATO_CARTAO_CREDITO("Extrato de Cartão de Crédito"),
   EXTRATO_INVESTIMENTO("Extrato de Investimento"),
   CARTAO_ASSINATURA("Cartão de Assinatura"),
   PROCURACAO("Procuração"),
   DADO_CADASTRAL("Dado Cadastral"),
   EXTRATO_FINANCIAMENTO("Extrato de financiamento"),
   OFICIO_CARTA_RESPOSTA("Ofício/Carta de resposta"),
   COMPROVANTE_TRASMISSAO_SIMBA("Comprovante de transmissão do SIMBA"),
   COPIA_MICROFILMAGEM_CHEQUE("Cópia/Microfilmagem de cheque"),
   OFICIO_JUDICIAL_QUEBRA_SIGILO("Ofício judicial da quebra de sigilo bancário"),
   COMPROVANTE_MENSAL_APLICACAO_RESGATE("Comprovante mensal de aplicação e resgate"),
   EXTRATO_CONTA_CAPITAL("Extrato de conta capital"),
   EXTRATO_FUNDO_INVESTIMENTO("Extrato de fundo de investimento"),
   COPIA_CONTRATO("Cópia de contrato"),
   EXTRATO_CAPITALIZACAO("Extrato de capitalização"),
   EXTRATO_CARTAO_PRE_PAGO("Extrato de Cartão Pré-Pago"),
   EXTRATO_PREVIDENCIA_PRIVADA("Extrato de previdência privada"),
   OUTROS("Outros");

   private String descricao;

   public static TipoDocumento[] tipos() {
      TipoDocumento[] tipos = values();
      Arrays.sort(tipos, (o1, o2) -> o1.descricao.compareTo(o2.descricao));
      return tipos;
   }

   public String toString() {
      return this.descricao;
   }

   private TipoDocumento(String descricao) {
      this.descricao = descricao;
   }

   public String getDescricao() {
      return this.descricao;
   }

   // $FF: synthetic method
   private static TipoDocumento[] $values() {
      return new TipoDocumento[]{EXTRATO_CARTAO_CREDITO, EXTRATO_INVESTIMENTO, CARTAO_ASSINATURA, PROCURACAO, DADO_CADASTRAL, EXTRATO_FINANCIAMENTO, OFICIO_CARTA_RESPOSTA, COMPROVANTE_TRASMISSAO_SIMBA, COPIA_MICROFILMAGEM_CHEQUE, OFICIO_JUDICIAL_QUEBRA_SIGILO, COMPROVANTE_MENSAL_APLICACAO_RESGATE, EXTRATO_CONTA_CAPITAL, EXTRATO_FUNDO_INVESTIMENTO, COPIA_CONTRATO, EXTRATO_CAPITALIZACAO, EXTRATO_CARTAO_PRE_PAGO, EXTRATO_PREVIDENCIA_PRIVADA, OUTROS};
   }
}
