package br.mp.mpf.spea.simba.validador.cmd;

import DAO.Mapa;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.to.Versao;
import br.mp.mpf.pgr.enums.TipoAtendimento;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoBancarioCmd;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoGabCmd;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;
import br.mp.mpf.spea.simba.validador.enums.CamposAtendimentoBancario;
import br.mp.mpf.spea.simba.validador.enums.CamposAtendimentoComum;
import br.mp.mpf.spea.simba.validador.enums.CamposAtendimentoGab;
import jakarta.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class GerarArquivoAtendimentoCmd {
   @Inject
   private GetAtendimentoBancarioCmd getAtendimentoBancarioCmd;
   @Inject
   private GetAtendimentoGabCmd getAtendimentoGabCmd;
   @Inject
   private Controlador controlador;
   @Inject
   private Configurador configurador;
   @Inject
   private Context context;

   public void gerar(TipoAtendimento tipoAtendimento) {
      switch (tipoAtendimento) {
         case BANCARIO -> this.gerarBanco();
         case CORRETORA -> this.gerarCorretora();
         default -> throw new InfraestruturaException("TipoAtendimento " + tipoAtendimento.name() + " não implementado");
      }

   }

   private void gerarBanco() {
      String numeroCaso = this.context.getNumeroCaso();
      File arquivoAtendimento = this.getArquivo(numeroCaso);
      AtendimentoBancario atendimento = this.getAtendimentoBancarioCmd.get(numeroCaso);

      try {
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoAtendimento), "ISO-8859-1"));

         try {
            this.gravarCamposComuns(out, atendimento, TipoAtendimento.BANCARIO);
            this.gravarCamposBanco(out, atendimento);
         } catch (Throwable var8) {
            try {
               out.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         out.close();
      } catch (IOException e) {
         throw new InfraestruturaException("Erro ao gerar o arquivo tabular: " + arquivoAtendimento.getAbsolutePath(), e);
      }
   }

   private void gerarCorretora() {
      String numeroCaso = this.context.getNumeroCaso();
      File arquivoAtendimento = this.getArquivo(numeroCaso);
      AtendimentoGab atendimento = this.getAtendimentoGabCmd.get(numeroCaso);

      try {
         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivoAtendimento), "ISO-8859-1"));

         try {
            this.gravarCamposComuns(out, atendimento, TipoAtendimento.CORRETORA);
            this.gravarCamposCorretora(out, atendimento);
         } catch (Throwable var8) {
            try {
               out.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         out.close();
      } catch (IOException e) {
         throw new InfraestruturaException("Erro ao gerar o arquivo tabular: " + arquivoAtendimento.getAbsolutePath(), e);
      }
   }

   private File getArquivo(String numeroCaso) {
      String nomeArquivoAtendimento = numeroCaso + "_" + Mapa.getNomesTabelasA()[0] + ".txt";
      File arquivoAtendimento = new File(this.context.getPastaArqTxt(), nomeArquivoAtendimento);
      if (arquivoAtendimento.exists()) {
         if (!arquivoAtendimento.delete()) {
            throw new NegocioException("Erro ao apagar aquivo de atendimento da pasta de envio.");
         }
      } else {
         arquivoAtendimento.getParentFile().mkdirs();
      }

      return arquivoAtendimento;
   }

   private void gravarCamposComuns(BufferedWriter out, Atendimento atendimento, TipoAtendimento tipoAtendimento) throws IOException {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
      Versao versao = computador.getVersaoReceptor();
      boolean suportaCorretora = versao.suportaCorretora(computador);
      boolean suportaTransmissaoDocumentos = versao.suportaTransmissaoDocumentos(computador);
      if (suportaTransmissaoDocumentos) {
         this.write(out, CamposAtendimentoComum.TIPO_ATENDIMENTO.name(), tipoAtendimento.getCodigo());
      } else if (suportaCorretora) {
         String codigoTipoInstituicao;
         switch (tipoAtendimento) {
            case BANCARIO:
               codigoTipoInstituicao = TipoAtendimento.BANCARIO.getCodigo();
               break;
            case CORRETORA:
               codigoTipoInstituicao = TipoAtendimento.CORRETORA.getCodigo();
               break;
            default:
               StringBuilder msg = new StringBuilder();
               msg.append("O TIPO_ATENDIMENTO ");
               msg.append(tipoAtendimento);
               msg.append(" não é suportado pelo computador ");
               msg.append(computador.getNome_computador());
               throw new InfraestruturaException(msg.toString());
         }

         this.write(out, CamposAtendimentoComum.TIPO_INSTITUICAO.name(), codigoTipoInstituicao);
      }

      this.write(out, CamposAtendimentoComum.NUMERO_CASO.name(), atendimento.getNumeroCaso());
      if (atendimento.getCnpj() != null && (suportaTransmissaoDocumentos || suportaCorretora)) {
         this.write(out, CamposAtendimentoComum.CNPJ.name(), atendimento.getCnpj());
      }

      this.write(out, CamposAtendimentoComum.NUMERO_BANCO.name(), (long)atendimento.getNumeroBanco());
      this.write(out, CamposAtendimentoComum.NOME_BANCO.name(), atendimento.getNomeBanco());
      this.write(out, CamposAtendimentoComum.NOME_BANCO_ATENDIMENTO.name(), atendimento.getNomeBancoAtendimento());
      this.write(out, CamposAtendimentoComum.DATA_GERACAO_ARQUIVO.name(), atendimento.getDataGeracaoArquivo().format(formatter));
      this.write(out, CamposAtendimentoComum.NOME_FUNC_RESPONSAVEL.name(), atendimento.getNomeFuncionarioResponsavel());
      this.write(out, CamposAtendimentoComum.TELEFONE_FUNC_RESPONSAVEL.name(), atendimento.getTelefoneFuncionarioResponsavel());
      this.write(out, CamposAtendimentoComum.EMAIL_FUNC_RESPONSAVEL.name(), atendimento.getEmailFuncionarioResponsavel());
      this.write(out, CamposAtendimentoComum.DATA_INICIO_AFASTAMENTO.name(), atendimento.getDataInicioAfastamento().format(formatter));
      this.write(out, CamposAtendimentoComum.DATA_FIM_AFASTAMENTO.name(), atendimento.getDataFimAfastamento().format(formatter));
      this.write(out, CamposAtendimentoComum.NUMERO_CORREIO_BACEN.name(), atendimento.getNumeroCorreioBacen());
      this.write(out, CamposAtendimentoComum.NUMERO_OFICIO.name(), atendimento.getNumeroOficio());
      if (atendimento.getDataOficio() != null) {
         this.write(out, CamposAtendimentoComum.DATA_OFICIO.name(), atendimento.getDataOficio().format(formatter));
      }

      this.write(out, CamposAtendimentoComum.NUMERO_PROCESSO.name(), atendimento.getNumeroProcesso());
      this.write(out, CamposAtendimentoComum.NOME_TRIBUNAL.name(), atendimento.getNomeTribunal());
      this.write(out, CamposAtendimentoComum.NUMERO_VARA.name(), atendimento.getNumeroVara());
      this.write(out, CamposAtendimentoComum.NOME_MAGISTRADO.name(), atendimento.getNomeMagistrado());
      this.write(out, CamposAtendimentoComum.CARGO_MAGISTRADO.name(), atendimento.getCargoMagistrado());
      this.write(out, CamposAtendimentoComum.DESCRICAO_CONTEUDO.name(), atendimento.getDescricaoConteudo());
      this.write(out, CamposAtendimentoComum.VERSAO_VALIDADOR.name(), atendimento.getVersaoValidador());
   }

   private void gravarCamposBanco(BufferedWriter out, AtendimentoBancario atendimento) throws IOException {
      this.write(out, CamposAtendimentoBancario.HASH_ARQUIVO_1.name(), atendimento.getHashArquivo1());
      this.write(out, CamposAtendimentoBancario.HASH_ARQUIVO_2.name(), atendimento.getHashArquivo2());
      this.write(out, CamposAtendimentoBancario.HASH_ARQUIVO_3.name(), atendimento.getHashArquivo3());
      this.write(out, CamposAtendimentoBancario.HASH_ARQUIVO_4.name(), atendimento.getHashArquivo4());
      this.write(out, CamposAtendimentoBancario.HASH_ARQUIVO_5.name(), atendimento.getHashArquivo5());
      this.write(out, CamposAtendimentoBancario.LINHAS_ARQUIVO_1.name(), atendimento.getLinhasArquivo1());
      this.write(out, CamposAtendimentoBancario.LINHAS_ARQUIVO_2.name(), atendimento.getLinhasArquivo2());
      this.write(out, CamposAtendimentoBancario.LINHAS_ARQUIVO_3.name(), atendimento.getLinhasArquivo3());
      this.write(out, CamposAtendimentoBancario.LINHAS_ARQUIVO_4.name(), atendimento.getLinhasArquivo4());
      this.write(out, CamposAtendimentoBancario.LINHAS_ARQUIVO_5.name(), atendimento.getLinhasArquivo5());
   }

   private void gravarCamposCorretora(BufferedWriter out, AtendimentoGab atendimento) throws IOException {
      this.write(out, CamposAtendimentoGab.VERSAO_LAYOUT_GAB.name(), (long)this.configurador.getVersaoLayoutGab());
      this.write(out, CamposAtendimentoGab.HASH_ARQUIVO_GAB109.name(), atendimento.getHashArquivoGab109());
      this.write(out, CamposAtendimentoGab.HASH_ARQUIVO_GAB112.name(), atendimento.getHashArquivoGab112());
      this.write(out, CamposAtendimentoGab.HASH_ARQUIVO_GAB800.name(), atendimento.getHashArquivoGab800());
      this.write(out, CamposAtendimentoGab.LINHAS_ARQUIVO_GAB109.name(), atendimento.getLinhasArquivoGab109());
      this.write(out, CamposAtendimentoGab.LINHAS_ARQUIVO_GAB112.name(), atendimento.getLinhasArquivoGab112());
      this.write(out, CamposAtendimentoGab.LINHAS_ARQUIVO_GAB800.name(), atendimento.getLinhasArquivoGab800());
   }

   private void write(BufferedWriter out, String chave, long valor) throws IOException {
      out.write(chave + "\t" + valor + "\r\n");
   }

   private void write(BufferedWriter out, String chave, String valor) throws IOException {
      out.write(chave + "\t" + valor + "\r\n");
   }
}
