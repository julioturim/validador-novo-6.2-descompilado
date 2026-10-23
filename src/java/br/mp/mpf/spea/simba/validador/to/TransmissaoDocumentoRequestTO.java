package br.mp.mpf.spea.simba.validador.to;

import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import br.mp.mpf.spea.simba.validador.domain.Investigado;
import java.util.List;

public class TransmissaoDocumentoRequestTO {
   private Atendimento atendimento;
   private String observacao;
   private Integer totalArquivos;
   private Long totalBytes;
   private List<Documento> documentos;
   private List<Investigado> investigados;

   public Atendimento getAtendimento() {
      return this.atendimento;
   }

   public void setAtendimento(Atendimento atendimento) {
      this.atendimento = atendimento;
   }

   public String getObservacao() {
      return this.observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public Integer getTotalArquivos() {
      return this.totalArquivos;
   }

   public void setTotalArquivos(Integer totalArquivos) {
      this.totalArquivos = totalArquivos;
   }

   public Long getTotalBytes() {
      return this.totalBytes;
   }

   public void setTotalBytes(Long totalBytes) {
      this.totalBytes = totalBytes;
   }

   public List<Documento> getDocumentos() {
      return this.documentos;
   }

   public void setDocumentos(List<Documento> documentos) {
      this.documentos = documentos;
   }

   public List<Investigado> getInvestigados() {
      return this.investigados;
   }

   public void setInvestigados(List<Investigado> investigados) {
      this.investigados = investigados;
   }
}
