package br.mp.mpf.spea.simba.validador.carga;

import DAO.Mapa;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.error.ErroValidacao;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.jdbc.SharedConnection;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import br.mp.mpf.spea.simba.validador.domain.Gab109;
import br.mp.mpf.spea.simba.validador.domain.Gab109Ver2;
import br.mp.mpf.spea.simba.validador.domain.Gab112;
import br.mp.mpf.spea.simba.validador.domain.Gab112Ver2;
import br.mp.mpf.spea.simba.validador.domain.Gab800;
import br.mp.mpf.spea.simba.validador.domain.Gab800Ver2;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.enums.NomesTabelaAGab;
import com.google.common.base.Stopwatch;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telas.frmSelecionaDiretorio;
import telas.frmSelecionaDiretorioGab;

public abstract class AbstractCarregarArquivoCmd<D> implements HasCargaInfo {
   private static final Logger log = LoggerFactory.getLogger(AbstractCarregarArquivoCmd.class);
   protected final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
   protected final Connection con;
   protected final PreparedStatement ps;
   protected final ErrorReportCmd errorReportCmd;
   protected D domain;
   protected final int quantidadeColunas;
   protected final CampoArquivo[] camposArquivo;
   protected final int batchSize;
   protected CargaInfo cargaInfo;
   protected File file;
   protected frmSelecionaDiretorio.View view;
   protected frmSelecionaDiretorioGab.ViewGab viewGab;
   @Inject
   protected Context context;
   @Inject
   protected GetCamposGabColunaFixaCmd camposGab;
   @Inject
   protected Configurador configurador;
   protected boolean useBatch = false;

   protected abstract String getNomeTabela();

   protected abstract String getNomeArquivo();

   protected abstract Object[] getFieldEnums();

   protected abstract int getQuantidadeColunas();

   protected abstract CampoArquivo[] getCamposArquivo();

   protected abstract D createDomain();

   protected abstract D convert(String[] dados, int numeroLinha) throws SQLException;

   protected abstract void setParametros(D domain) throws SQLException, ParseException;

   protected abstract void validar(D domain);

   protected AbstractCarregarArquivoCmd(SharedConnection sharedConnection, ErrorReportCmd errorReportCmd) throws SQLException {
      this.errorReportCmd = errorReportCmd;
      this.con = sharedConnection.get();
      this.con.setAutoCommit(false);
      this.ps = this.con.prepareStatement(this.getSql());
      this.domain = (D)this.createDomain();
      this.quantidadeColunas = this.getQuantidadeColunas();
      this.batchSize = this.getBatchSize();
      this.camposArquivo = this.getCamposArquivo();
   }

   public void carregar(File file, Desempenho desempenho, boolean isCorretora, int versaoLayoutGab) throws SQLException, ParseException {
      this.file = file;
      if (isCorretora) {
         this.viewGab = desempenho.tarefaBackgroundGab;
      } else {
         this.view = desempenho.tarefaBackground;
      }

      String fileName = file.getName();
      log.debug("{} - Iniciando carga. Tamanho: {} Arquivo: {}", new Object[]{fileName, file.length(), file});
      desempenho.totalLinha = Util.countLines(file);
      Stopwatch cargaStopwatch = Stopwatch.createStarted();

      try {
         Reader r = Channels.newReader(Channels.newChannel(new FileInputStream(file)), StandardCharsets.ISO_8859_1);

         try {
            BufferedReader reader = new BufferedReader(r, 10485760);

            try {
               int numeroLinha = 0;
               int i = 0;
               Object[] a = new Object[4];
               Object[] b = new Object[3];
               a[0] = fileName;
               b[0] = fileName;
               Stopwatch stopwatch = Stopwatch.createStarted();

               label130:
               while(true) {
                  while(true) {
                     String linha;
                     if ((linha = reader.readLine()) == null) {
                        if (i % this.batchSize > 0) {
                           b[1] = i;
                           b[2] = numeroLinha;
                           log.trace("{} - Adicionando {} linhas. Total: {}", b);
                           if (this.useBatch) {
                              this.ps.executeBatch();
                           }
                        }

                        log.trace("{} - Commit no banco de dados", fileName);
                        Stopwatch commitStopWatch = Stopwatch.createStarted();
                        this.con.commit();
                        commitStopWatch.stop();
                        log.trace("Tempo total do commit: {} milisegundos", commitStopWatch.elapsed(TimeUnit.MILLISECONDS));
                        cargaStopwatch.stop();
                        a[1] = cargaStopwatch.elapsed(TimeUnit.MILLISECONDS);
                        a[2] = numeroLinha;
                        a[3] = (double)numeroLinha / (double)(Long)a[1] * (double)1000.0F;
                        log.debug("{} - Fim da carga. Tempo: {} ms Linhas: {}, Linhas/seg: {}", a);
                        this.atualizaQuantidadeLinhaTabela(numeroLinha, isCorretora);
                        break label130;
                     }

                     try {
                        ++numeroLinha;
                        String[] dados;
                        if (isCorretora) {
                           NomeArquivoGab arquivoGab = NomeArquivoGab.valueOf(this.domain.getClass().getSimpleName().toUpperCase().replace("VER2", ""));
                           if (linha.length() != NomeArquivoGab.getTamanhoLinha(arquivoGab, versaoLayoutGab)) {
                              this.errorReportCmd.report(new ErroValidacao((long)numeroLinha, fileName, "Certifique-se de completar com espaços os campos que não atingirem o tamanho predeterminado e Não use tab para separar os campos."));
                              this.cargaInfo.setTemErro(true);
                              ++i;
                              continue;
                           }

                           dados = this.camposGab.getCampos(this.domain.getClass().getSimpleName().toUpperCase().replace("VER2", ""), linha);
                        } else {
                           dados = br.mp.mpf.spea.simba.validador.util.Util.tabSeparatedStringToArray(linha);
                           if (dados.length != this.quantidadeColunas) {
                              this.errorReportCmd.report(new ErroValidacao((long)numeroLinha, fileName, "Quantidade de colunas inválida. Esperado: " + this.quantidadeColunas + " Atual: " + dados.length));
                              this.cargaInfo.setTemErro(true);
                              ++i;
                              continue;
                           }
                        }

                        try {
                           this.validarCampos(dados, numeroLinha);
                           if (this.errorReportCmd.hasErros()) {
                              this.cargaInfo.setTemErro(this.errorReportCmd.hasErros());
                              ++i;
                              continue;
                           }

                           this.domain = (D)this.convert(dados, numeroLinha);
                           this.validar(this.domain);
                           if (this.errorReportCmd.hasErros()) {
                              this.cargaInfo.setTemErro(this.errorReportCmd.hasErros());
                              ++i;
                              continue;
                           }

                           this.setParametros(this.domain);
                           if (this.useBatch) {
                              this.ps.addBatch();
                           } else {
                              this.ps.executeUpdate();
                           }
                        } catch (NegocioException e) {
                           this.errorReportCmd.report(new ErroValidacao((long)numeroLinha, fileName, e.getMessage()));
                           this.cargaInfo.setTemErro(true);
                           ++i;
                           continue;
                        } catch (Exception e) {
                           StringBuilder msg = new StringBuilder();
                           msg.append("Erro na leitura da linha ");
                           msg.append(numeroLinha);
                           msg.append(" do arquivo ");
                           msg.append(fileName);
                           log.error(msg.toString(), e);
                           this.errorReportCmd.report(new ErroValidacao((long)numeroLinha, fileName, "Erro desconhecido. Consulte o log do sistema."));
                           this.cargaInfo.setTemErro(true);
                           ++i;
                           continue;
                        }

                        ++i;
                        break;
                     } catch (Exception e) {
                        log.error("Erro arquivo " + fileName + " linha " + numeroLinha, e);
                        throw new InfraestruturaException(e);
                     }
                  }

                  if (i % this.batchSize == 0) {
                     b[1] = i;
                     b[2] = numeroLinha;
                     log.trace("{} - Adicionando {} linhas. Total: {}", b);
                     if (this.useBatch) {
                        this.ps.executeBatch();
                     }

                     stopwatch.stop();
                     a[1] = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                     a[2] = i;
                     a[3] = (double)i / (double)(Long)a[1] * (double)1000.0F;
                     log.trace("{} - Tempo: {} ms Linhas: {} Linhas/seg: {}", a);
                     i = 0;
                     desempenho.linhasPorSegundo = (Double)a[3];
                     stopwatch.reset();
                     stopwatch.start();
                  }

                  if (numeroLinha % 250000 == 0) {
                     this.con.commit();
                  }

                  desempenho.linhaAtual = (long)numeroLinha;
                  if (isCorretora) {
                     desempenho.tarefaBackgroundGab.progress();
                  } else {
                     desempenho.tarefaBackground.progress();
                  }
               }
            } catch (Throwable var23) {
               try {
                  reader.close();
               } catch (Throwable var19) {
                  var23.addSuppressed(var19);
               }

               throw var23;
            }

            reader.close();
         } catch (Throwable var24) {
            if (r != null) {
               try {
                  r.close();
               } catch (Throwable var18) {
                  var24.addSuppressed(var18);
               }
            }

            throw var24;
         }

         if (r != null) {
            r.close();
         }

      } catch (FileNotFoundException e) {
         throw new InfraestruturaException(e);
      } catch (IOException e) {
         throw new InfraestruturaException(e);
      }
   }

   protected void atualizaQuantidadeLinhaTabela(int quantidadeLinha, boolean isCorretora) throws SQLException {
      if (isCorretora) {
         this.atualizaQuantidadeLinhaTabelaGab(this.con, this.getNomeTabela(), quantidadeLinha);
      } else {
         this.atualizaQuantidadeLinhaTabela(this.con, this.getNomeTabela(), quantidadeLinha);
      }

   }

   protected void atualizaQuantidadeLinhaTabela(Connection con, String nometabela, int quantidadeLinha) throws SQLException {
      String[] tabelasA = Mapa.getNomesTabelasA();

      for(int i = 0; i < tabelasA.length; ++i) {
         if (tabelasA[i].equals(nometabela)) {
            String sql = "UPDATE ATENDIMENTO SET LINHAS_ARQUIVO_" + i + " = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, quantidadeLinha);
            ps.executeUpdate();
            con.commit();
         }
      }

   }

   protected void validarCampos(String[] dados, int numeroLinha) {
      for(int i = 0; i < dados.length; ++i) {
         CampoArquivo campo = this.camposArquivo[i];
         int lenArquivo = dados[i].length();
         int maxExpected = campo.getMaxLength();
         if (lenArquivo > maxExpected) {
            String mensagem = String.format("Tamanho do campo %s excedido. Tamanho máximo permitido: %d", campo.getNome(), campo.getMaxLength());
            this.errorReportCmd.report(new ErroValidacao((long)numeroLinha, this.getNomeArquivo(), mensagem));
            this.cargaInfo.setTemErro(true);
         }
      }

   }

   protected void atualizaQuantidadeLinhaTabelaGab(Connection con, String nometabela, int quantidadeLinha) throws SQLException {
      String sufixoNomeTabela = "";
      switch (this.configurador.getVersaoLayoutGab()) {
         case 2:
            sufixoNomeTabela = "VER2";
         default:
            String[] tabelasA = NomesTabelaAGab.getStringArray();

            for(int i = 0; i < tabelasA.length; ++i) {
               String nomeTabelaComSufixo = tabelasA[i] + sufixoNomeTabela;
               if (nomeTabelaComSufixo.equalsIgnoreCase(nometabela)) {
                  String sql = "UPDATE ATENDIMENTO SET LINHAS_ARQUIVO_" + tabelasA[i] + " = ?";
                  PreparedStatement ps = con.prepareStatement(sql);
                  ps.setInt(1, quantidadeLinha);
                  ps.executeUpdate();
                  con.commit();
               }
            }

      }
   }

   protected int getBatchSize() {
      return 500;
   }

   public CargaInfo getCargaInfo() {
      return this.cargaInfo;
   }

   public void setCargaInfo(CargaInfo cargaInfo) {
      this.cargaInfo = cargaInfo;
   }

   protected final String getSql() {
      String nomeTabela = this.getNomeTabela();
      String[] fields = null;
      if (!nomeTabela.equals(Gab109.class.getSimpleName()) && !nomeTabela.equals(Gab112.class.getSimpleName()) && !nomeTabela.equals(Gab800.class.getSimpleName()) && !nomeTabela.equals(Gab109Ver2.class.getSimpleName()) && !nomeTabela.equals(Gab112Ver2.class.getSimpleName()) && !nomeTabela.equals(Gab800Ver2.class.getSimpleName())) {
         fields = this.getSqlFields(this.getFieldEnums());
      } else {
         fields = this.getSqlFieldsGab(this.getFieldEnums());
      }

      StringBuilder sql = new StringBuilder();
      sql.append("INSERT INTO ");
      sql.append(nomeTabela);
      sql.append(" (");
      sql.append(br.mp.mpf.spea.simba.validador.util.Util.toCsv(fields));
      sql.append(") VALUES (");
      sql.append(this.getInterrogacoesCsv(fields.length));
      sql.append(")");
      log.trace("Sql tabela {}: {}", nomeTabela, sql);
      return sql.toString();
   }

   protected String[] getSqlFields(Object[] camposEnum) {
      String[] campos = new String[camposEnum.length - 1];

      for(int i = 1; i < camposEnum.length; ++i) {
         campos[i - 1] = camposEnum[i].toString();
      }

      return campos;
   }

   protected String[] getSqlFieldsGab(Object[] camposEnum) {
      int size = camposEnum.length;
      String[] campos = new String[size];

      for(int i = 0; i < camposEnum.length; ++i) {
         campos[i] = camposEnum[i].toString();
      }

      return campos;
   }

   protected String getInterrogacoesCsv(int length) {
      String[] array = new String[length];

      for(int i = 0; i < length; ++i) {
         array[i] = "?";
      }

      return br.mp.mpf.spea.simba.validador.util.Util.toCsv(array);
   }

   protected int getInt(String dado, int linha, String campo) {
      if (StringUtil.isNumber(dado)) {
         return Integer.valueOf(dado);
      } else {
         StringBuilder msg = new StringBuilder();
         msg.append("Campo ");
         msg.append(campo);
         msg.append(" não é um número.");
         this.errorReportCmd.report(new ErroValidacao((long)linha, this.file.getAbsolutePath(), msg.toString()));
         return 0;
      }
   }

   protected long getLong(String dado, int linha, String campo) {
      if (StringUtil.isNumber(dado)) {
         return Long.valueOf(dado);
      } else {
         StringBuilder msg = new StringBuilder();
         msg.append("Campo ");
         msg.append(campo);
         msg.append(" não é um número.");
         this.errorReportCmd.report(new ErroValidacao((long)linha, this.file.getAbsolutePath(), msg.toString()));
         return 0L;
      }
   }

   protected void setString(int index, String valor) throws SQLException {
      if (!StringUtil.isEmpty(valor)) {
         this.ps.setString(index, valor);
      } else {
         this.ps.setNull(index, 12);
      }

   }

   protected void setInt(int index, String valor) throws SQLException {
      if (!StringUtil.isEmpty(valor)) {
         this.ps.setInt(index, Integer.valueOf(valor));
      } else {
         this.ps.setNull(index, 4);
      }

   }

   protected void setInt(int index, Integer valor) throws SQLException {
      if (valor != null) {
         this.ps.setInt(index, valor);
      } else {
         this.ps.setNull(index, 4);
      }

   }

   protected void setLong(int index, Long valor) throws SQLException {
      if (valor != null) {
         this.ps.setLong(index, valor);
      } else {
         this.ps.setNull(index, -5);
      }

   }

   protected void setDouble(int index, Double valor) throws SQLException {
      if (valor != null) {
         this.ps.setDouble(index, valor);
      } else {
         this.ps.setNull(index, 8);
      }

   }

   protected void setDate(int index, Date valor) throws SQLException {
      this.ps.setDate(index, valor);
   }

   public ErrorReportCmd getErrorReportCmd() {
      return this.errorReportCmd;
   }
}
