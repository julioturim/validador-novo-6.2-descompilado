package br.mp.mpf.spea.simba.validador;

import DAO.BancoHSQLDB;
import DAO.Mapa;
import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.ConnectionProvider;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.spea.simba.validador.domain.Arquivos;
import br.mp.mpf.spea.simba.validador.domain.ArquivosGab;
import br.mp.mpf.spea.simba.validador.enums.CamposInvestigado;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.validacao.ValidarAtendimento;
import br.mp.mpf.spea.simba.validador.validacao.ValidarAtendimentoGab;
import jakarta.inject.Inject;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import telas.frmSelecionaDiretorio;
import telas.frmSelecionaDiretorioGab;

@Component
public class ControladorValidador extends Controlador {
   private static final Logger log = LoggerFactory.getLogger(ControladorValidador.class);
   @Inject
   private ValidarAtendimento validarAtendimento;
   @Inject
   private ValidarAtendimentoGab validarAtendimentoGab;
   @Inject
   protected Context context;
   @Inject
   protected ConnectionProvider connectionProvider;
   @Inject
   protected BancoHSQLDB banco;
   @Inject
   protected Configurador configurador;

   public boolean criarHSQLDBAtendimento() {
      try {
         Connection cn = this.connectionProvider.get();
         cn.close();
         return true;
      } catch (SQLException e) {
         log.error("Erro ao criar banco HSQLDB.", e);
         return false;
      }
   }

   protected boolean findFile(Collection<File> files, String nomeArquivo) {
      for(File file : files) {
         if (file.getName().toUpperCase().equals(nomeArquivo.toUpperCase())) {
            return true;
         }
      }

      return false;
   }

   public boolean verificarExistenciaArquivosBancario(File pastaArquivosValidacao, String numCaso, StringBuilder messagem) {
      log.debug("Listando arquivos da pasta de validação...");
      File[] listFiles = pastaArquivosValidacao.listFiles();

      for(File file : listFiles) {
         if (!file.isDirectory()) {
            log.debug("Arquivo: {}", file.getAbsolutePath());
         }
      }

      Collection<File> files = FileUtils.listFiles(pastaArquivosValidacao, new SuffixFileFilter(".txt", IOCase.INSENSITIVE), FalseFileFilter.INSTANCE);
      String[] nomesTabelasA = Mapa.getNomesTabelasA();

      for(int i = 1; i < nomesTabelasA.length; ++i) {
         String tipoArquivo = nomesTabelasA[i];
         String nomeArquivo = numCaso + "_" + tipoArquivo + ".txt";
         File arquivo = new File(pastaArquivosValidacao, nomeArquivo);
         if (!arquivo.exists() && !this.findFile(files, nomeArquivo)) {
            String msg = "Arquivo esperado (" + nomeArquivo + ") não encontrado.";
            log.error(msg);
            messagem.append(msg);
            return false;
         }
      }

      return true;
   }

   public boolean verificarExistenciaArquivosGab(File pastaArquivosValidacao, String numeroCaso, StringBuilder messagem) {
      log.debug("Listando arquivos da pasta de validação...");
      File[] listFiles = pastaArquivosValidacao.listFiles();

      for(File file : listFiles) {
         if (!file.isDirectory()) {
            log.debug("Arquivo: {}", file.getAbsolutePath());
         }
      }

      Collection<File> files = FileUtils.listFiles(pastaArquivosValidacao, new SuffixFileFilter(".txt", IOCase.INSENSITIVE), FalseFileFilter.INSTANCE);

      for(NomeArquivoGab tipoArquivo : NomeArquivoGab.getNomesList()) {
         String nomeArquivo = numeroCaso + "_" + String.valueOf(tipoArquivo) + ".txt";
         File arquivo = new File(pastaArquivosValidacao, nomeArquivo);
         if (!arquivo.exists() && !this.findFile(files, nomeArquivo)) {
            String msg = "Arquivo esperado (" + nomeArquivo + ") não encontrado.";
            log.error(msg);
            messagem.append(msg + "\n");
            return false;
         }
      }

      return true;
   }

   public void verificarArquivosGabVazios(ArquivosGab a, ErrorReportCmd errorReportCmd) {
      if (a.getGab112().length() == 0L) {
         errorReportCmd.report(new ErroValidacao(0L, a.getGab112().getAbsolutePath(), "O arquivo GAB-112 não pode ser vazio."));
      }

      if (a.getGab800().length() == 0L && a.getGab109().length() != 0L) {
         errorReportCmd.report(new ErroValidacao(0L, a.getGab800().getAbsolutePath(), "O arquivo GAB-800 não pode ser vazio quando o arquivo GAB-109 possui dados."));
      }

      if (a.getGab109().length() == 0L && a.getGab800().length() != 0L) {
         errorReportCmd.report(new ErroValidacao(0L, a.getGab109().getAbsolutePath(), "O arquivo GAB-109 não pode ser vazio quando o arquivo GAB-800 possui dados."));
      }

   }

   public void verificarArquivosVazios(Arquivos arquivos, ErrorReportCmd errorReportCmd) {
      if (arquivos.getAgencias().length() == 0L) {
         errorReportCmd.report(new ErroValidacao(0L, arquivos.getAgencias().getAbsolutePath(), "O arquivo AGENCIAS não pode ser vazio."));
      }

      if (arquivos.getContas().length() == 0L) {
         errorReportCmd.report(new ErroValidacao(0L, arquivos.getContas().getAbsolutePath(), "O arquivo CONTAS não pode ser vazio."));
      }

      if (arquivos.getTitulares().length() == 0L) {
         errorReportCmd.report(new ErroValidacao(0L, arquivos.getTitulares().getAbsolutePath(), "O arquivo TITULARES não pode ser vazio."));
      }

   }

   public boolean checkVersaoSistemaBanco() {
      String versaoSistemaBanco = this.getVersaoSistemaBanco();
      return this.configurador.getVersao().equals(versaoSistemaBanco);
   }

   public String getVersaoSistemaBanco() {
      return this.banco.getVersaoSistema();
   }

   public boolean validarConteudo(int codigoArquivoA, frmSelecionaDiretorio.View view) {
      switch (codigoArquivoA) {
         case 1 -> {
            return this.validarAtendimento.validarArquivoAgencia(view);
         }
         case 2 -> {
            return this.validarAtendimento.validarArquivoConta(view);
         }
         case 3 -> {
            return this.validarAtendimento.validarArquivoTitularidade(view);
         }
         case 4 -> {
            return this.validarAtendimento.validarArquivoExtrato(view);
         }
         case 5 -> {
            return this.validarAtendimento.validarArquivoOrigemDestino(view);
         }
         default -> {
            return true;
         }
      }
   }

   public boolean validarConteudoGab(NomeArquivoGab nomeArquivo, frmSelecionaDiretorioGab.ViewGab viewGab) {
      switch (nomeArquivo) {
         case GAB112 -> {
            return this.validarAtendimentoGab.validarArquivoGab112(viewGab);
         }
         case GAB109 -> {
            return this.validarAtendimentoGab.validarArquivoGab109(viewGab);
         }
         case GAB800 -> {
            return this.validarAtendimentoGab.validarArquivoGab800(viewGab);
         }
         default -> {
            return false;
         }
      }
   }

   public void gravarHashArquivo(int codArquivo, String sHash) {
      this.banco.gravarHashArquivo(codArquivo, sHash);
   }

   public void gravarHashArquivoGab(NomeArquivoGab nome, String sHash) {
      this.banco.gravarHashArquivoGab(nome, sHash);
   }

   public boolean gerarHashArquivoZIP() {
      boolean bRet = false;
      Arquivo obj = new Arquivo(this.context.getPastaCaso());
      String nomeArqZip = Util.tiraBarra(this.context.getNumeroCaso()) + ".zip";
      bRet = obj.criarArquivoHash("envio", nomeArqZip);
      return bRet;
   }

   public String gerarHashArquivo(File arquivo, Computador computador) {
      return (new Arquivo(arquivo)).obterHashArquivo(computador);
   }

   public boolean preencheInvestigado(Map<CamposInvestigado, String> campos) {
      String SQL = "INSERT INTO INVESTIGADO (";
      SQL = SQL + br.mp.mpf.spea.simba.validador.util.Util.arrayParaCSV(campos.keySet());
      SQL = SQL + ") VALUES (";
      SQL = SQL + Util.arrayParaCSV(campos.values());
      SQL = SQL + ")";
      return this.banco.executaUpdate(SQL);
   }

   public List<Map<CamposInvestigado, String>> getInvestigado() {
      String[] sCampos = CamposInvestigado.getCamposArquivo();
      return this.getInvestigado(sCampos);
   }

   public List<Map<CamposInvestigado, String>> getInvestigado(String[] sCampos) {
      List<Map<CamposInvestigado, String>> investigadosMap = new ArrayList();
      String strSQL = "SELECT ";
      strSQL = strSQL + Util.arrayParaCSV(sCampos);
      strSQL = strSQL + " FROM INVESTIGADO";
      ResultSet result = null;

      try {
         result = this.banco.executaQuery(strSQL);

         while(result.next()) {
            ResultSetMetaData metaData = result.getMetaData();
            Map<CamposInvestigado, String> investigadoMap = new HashMap();

            for(int i = 1; i <= metaData.getColumnCount(); ++i) {
               CamposInvestigado chave = CamposInvestigado.valueOf(metaData.getColumnName(i));
               String valor = result.getString(i);
               valor = result.getString(i) == null ? "" : valor;
               investigadoMap.put(chave, valor);
            }

            investigadosMap.add(investigadoMap);
         }
      } catch (SQLException e) {
         log.error("Erro ao recuperar atendimento", e);
      }

      return investigadosMap;
   }
}
