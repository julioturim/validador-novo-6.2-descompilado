package br.mp.mpf.spea.simba.validador.ui;

import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.cmd.GetComputadoresAutorizacaoPendenteCmd;
import br.mp.mpf.pgr.client.cmd.GetComputadoresAutorizadosCmd;
import br.mp.mpf.pgr.client.cmd.GetDadosUsuarioChavesAsbCmd;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.ui.ChavePessoalPanel;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.client.ui.listener.ArquivoChaveChangedListener;
import br.mp.mpf.pgr.io.ByteFormatter;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.cmd.GetAtendimentoCmd;
import br.mp.mpf.spea.simba.validador.cmd.GetInvestigadosCmd;
import br.mp.mpf.spea.simba.validador.cmd.TransmitirDocumentosCmd;
import br.mp.mpf.spea.simba.validador.cmd.VerificarArquivosPermitidosTransmissaoCmd;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import br.mp.mpf.spea.simba.validador.domain.Investigado;
import br.mp.mpf.spea.simba.validador.to.Documento;
import br.mp.mpf.spea.simba.validador.to.TransmissaoDocumentoRequestTO;
import br.mp.mpf.spea.simba.validador.ui.listener.ListaDocumentoChangedListener;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import telas.frmIdentificaAtendimento;

@Component
@Scope(
   scopeName = "prototype"
)
public class TransmissaoDocumentosFrame extends IconFrame implements ArquivoChaveChangedListener, ListaDocumentoChangedListener {
   private static final long serialVersionUID = -5299573809387284271L;
   private static final Logger log = LoggerFactory.getLogger(TransmissaoDocumentosFrame.class);
   private Context context;
   private Controlador controlador;
   private Configurador configurador;
   private Config config;
   private GetDadosUsuarioChavesAsbCmd getDadosUsuarioChavesAsbCmd;
   private frmIdentificaAtendimento frmIdentificaAtendimento;
   @Inject
   private TransmitirDocumentosCmd transmitirDocumentosCmd;
   @Inject
   private GetAtendimentoCmd getAtendimentoCmd;
   @Inject
   private GetInvestigadosCmd getInvestigadosCmd;
   @Inject
   private VerificarArquivosPermitidosTransmissaoCmd verificarArquivosPermitidosTransmissaoCmd;
   private List<Documento> documentos;
   private GetComputadoresAutorizacaoPendenteCmd getComputadoresAutorizacaoPendenteCmd;
   private GetComputadoresAutorizadosCmd getComputadoresAutorizadosCmd;
   private Timer timer;
   private final Object timerLock = new Object();
   private int timerCount = 1;
   private JPanel mainPanel;
   private JLabel etapa1Label;
   private ChavePessoalPanel chavePanel;
   private JLabel numeroCasoLabel;
   private JLabel arquivosDescricaoLabel;
   private JLabel arquivosLabel;
   private JLabel arquivosMaximoLabel;
   private JLabel bytesMaximoLabel;
   private JLabel bytesLabel;
   private JLabel bytesDescricaoLabel;
   private JPanel acoesPanel;
   private JButton cancelarButton;
   private JButton transmitirButton;
   private JProgressBar progressBar;
   private TabelaArquivosPanel tabelaArquivosPanel;
   private JTextPane observacaoTextPane;
   private JLabel etapaTransmissaoLabel;
   private JLabel totalCaracteresLabel;

   public TransmissaoDocumentosFrame(Context context, Controlador controlador, Configurador configurador, Config config, GetDadosUsuarioChavesAsbCmd getDadosUsuarioChavesAsbCmd, VerificarArquivosPermitidosTransmissaoCmd verificarArquivosPermitidosTransmissaoCmd, GetComputadoresAutorizacaoPendenteCmd getComputadoresAutorizacaoPendenteCmd, GetComputadoresAutorizadosCmd getComputadoresAutorizadosCmd) throws HeadlessException {
      this.context = context;
      this.controlador = controlador;
      this.configurador = configurador;
      this.config = config;
      this.getDadosUsuarioChavesAsbCmd = getDadosUsuarioChavesAsbCmd;
      this.verificarArquivosPermitidosTransmissaoCmd = verificarArquivosPermitidosTransmissaoCmd;
      this.getComputadoresAutorizacaoPendenteCmd = getComputadoresAutorizacaoPendenteCmd;
      this.getComputadoresAutorizadosCmd = getComputadoresAutorizadosCmd;
      this.initComponents();
      this.setTitle("Simba - Transmissão de Documentos");
      this.setName("transmissao-documento");
      this.pack();
      this.setLocationRelativeTo((java.awt.Component)null);
   }

   @PostConstruct
   public void setup() {
      this.chavePanel.setListener(this);
      this.tabelaArquivosPanel.setListener(this);
      Computador computador = this.controlador.getComputador(this.context.getNumeroComputador());
      JLabel var10000 = this.numeroCasoLabel;
      String var10001 = this.context.getNumeroCaso();
      var10000.setText(var10001 + " - " + computador.getNome_orgao());
   }

   protected void initComponents() {
      this.setBackground(new Color(255, 255, 255));
      this.setMinimumSize(new Dimension(800, 600));
      this.setPreferredSize(new Dimension(800, 600));
      this.setDefaultCloseOperation(2);
      this.addWindowListener(new WindowAdapter() {
         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void windowClosed(WindowEvent e) {
            TransmissaoDocumentosFrame.this.cancelarAction();
         }
      });
      this.mainPanel = (JPanel)this.getContentPane();
      this.mainPanel.setBackground(new Color(255, 255, 255));
      SpringLayout mainLayout = new SpringLayout();
      this.mainPanel.setLayout(mainLayout);
      this.numeroCasoLabel = new JLabel("001-MPF-000001-18");
      this.numeroCasoLabel.setFont(new Font("Tahoma", 1, 18));
      this.numeroCasoLabel.setForeground(new Color(102, 102, 0));
      mainLayout.putConstraint("North", this.numeroCasoLabel, 10, "North", this.getContentPane());
      mainLayout.putConstraint("West", this.numeroCasoLabel, 10, "West", this.getContentPane());
      this.getContentPane().add(this.numeroCasoLabel);
      this.etapa1Label = new JLabel("1. Selecione a chave criptográfica do responsável pela transmissão:");
      mainLayout.putConstraint("North", this.etapa1Label, 10, "South", this.numeroCasoLabel);
      mainLayout.putConstraint("West", this.etapa1Label, 10, "West", this.getContentPane());
      this.etapa1Label.setFont(new Font("Tahoma", 0, 12));
      this.getContentPane().add(this.etapa1Label);
      this.chavePanel = new ChavePessoalPanel(this.config, this.context, this.controlador, this.configurador, this.getDadosUsuarioChavesAsbCmd, this.getComputadoresAutorizacaoPendenteCmd, this.getComputadoresAutorizadosCmd);
      this.chavePanel.setBackground(new Color(255, 243, 205));
      mainLayout.putConstraint("South", this.chavePanel, 92, "South", this.etapa1Label);
      mainLayout.putConstraint("North", this.chavePanel, 10, "South", this.etapa1Label);
      mainLayout.putConstraint("West", this.chavePanel, 10, "West", this.getContentPane());
      mainLayout.putConstraint("East", this.chavePanel, -10, "East", this.getContentPane());
      this.getContentPane().add(this.chavePanel);
      this.tabelaArquivosPanel = new TabelaArquivosPanel(this.config, this.context, this.verificarArquivosPermitidosTransmissaoCmd);
      this.tabelaArquivosPanel.setBackground(Color.WHITE);
      this.tabelaArquivosPanel.setMinimumSize(new Dimension(200, 200));
      SpringLayout observacaoPanelLayout = new SpringLayout();
      JPanel observacaoPanel = new JPanel(observacaoPanelLayout);
      observacaoPanel.setBackground(Color.WHITE);
      observacaoPanel.setMinimumSize(new Dimension(200, 100));
      JLabel observacaoLabel = new JLabel("Observação:");
      observacaoPanelLayout.putConstraint("North", observacaoLabel, 2, "North", observacaoPanel);
      observacaoPanelLayout.putConstraint("West", observacaoLabel, 0, "West", observacaoPanel);
      observacaoPanel.add(observacaoLabel);
      JScrollPane observacaoScrollPane = new JScrollPane();
      observacaoPanelLayout.putConstraint("North", observacaoScrollPane, 5, "South", observacaoLabel);
      observacaoPanelLayout.putConstraint("West", observacaoScrollPane, 0, "West", observacaoPanel);
      observacaoPanelLayout.putConstraint("South", observacaoScrollPane, 0, "South", observacaoPanel);
      observacaoPanelLayout.putConstraint("East", observacaoScrollPane, 0, "East", observacaoPanel);
      observacaoPanel.add(observacaoScrollPane);
      this.observacaoTextPane = new JTextPane(new DefaultStyledDocument() {
         private static final long serialVersionUID = 1564699994077927103L;

         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (this.getLength() + str.length() <= 2000) {
               super.insertString(offs, str, a);
            } else {
               TransmissaoDocumentosFrame.this.blinkTotalCaracteresObservacao();
            }

         }
      });
      this.observacaoTextPane.getDocument().addDocumentListener(new DocumentListener() {
         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void removeUpdate(DocumentEvent e) {
            TransmissaoDocumentosFrame.this.atualizarTotalCaracteresObservacao();
         }

         public void insertUpdate(DocumentEvent e) {
            TransmissaoDocumentosFrame.this.atualizarTotalCaracteresObservacao();
         }

         public void changedUpdate(DocumentEvent e) {
            TransmissaoDocumentosFrame.this.atualizarTotalCaracteresObservacao();
         }
      });
      observacaoScrollPane.setViewportView(this.observacaoTextPane);
      JSplitPane arquivosObservacaoSplitPane = new JSplitPane(0);
      SplitPaneUI splitPaneUI = arquivosObservacaoSplitPane.getUI();
      if (splitPaneUI instanceof BasicSplitPaneUI basic) {
         BasicSplitPaneDivider divider = basic.getDivider();
         divider.setBorder(BorderFactory.createEmptyBorder());
         divider.setBackground(Color.WHITE);
      }

      mainLayout.putConstraint("North", arquivosObservacaoSplitPane, 10, "South", this.chavePanel);
      arquivosObservacaoSplitPane.setBorder(BorderFactory.createEmptyBorder());
      arquivosObservacaoSplitPane.setDividerSize(10);
      arquivosObservacaoSplitPane.setContinuousLayout(true);
      arquivosObservacaoSplitPane.setTopComponent(this.tabelaArquivosPanel);
      arquivosObservacaoSplitPane.setBottomComponent(observacaoPanel);
      this.totalCaracteresLabel = new JLabel("0/2000");
      observacaoPanelLayout.putConstraint("North", this.totalCaracteresLabel, 0, "North", observacaoLabel);
      observacaoPanelLayout.putConstraint("East", this.totalCaracteresLabel, 0, "East", observacaoPanel);
      observacaoPanel.add(this.totalCaracteresLabel);
      arquivosObservacaoSplitPane.setResizeWeight((double)0.75F);
      mainLayout.putConstraint("West", arquivosObservacaoSplitPane, 10, "West", this.getContentPane());
      mainLayout.putConstraint("East", arquivosObservacaoSplitPane, -10, "East", this.getContentPane());
      this.getContentPane().add(arquivosObservacaoSplitPane);
      SpringLayout rodapePanelLayout = new SpringLayout();
      JPanel rodapePanel = new JPanel(rodapePanelLayout);
      mainLayout.putConstraint("North", rodapePanel, -85, "South", this.getContentPane());
      mainLayout.putConstraint("South", rodapePanel, -5, "South", this.getContentPane());
      mainLayout.putConstraint("South", arquivosObservacaoSplitPane, 0, "North", rodapePanel);
      rodapePanel.setBackground(Color.white);
      mainLayout.putConstraint("West", rodapePanel, 10, "West", this.getContentPane());
      mainLayout.putConstraint("East", rodapePanel, -10, "East", this.getContentPane());
      this.getContentPane().add(rodapePanel);
      this.arquivosDescricaoLabel = new JLabel("Arquivos:");
      rodapePanelLayout.putConstraint("West", this.arquivosDescricaoLabel, 0, "West", rodapePanel);
      rodapePanel.add(this.arquivosDescricaoLabel);
      this.arquivosLabel = new JLabel("0");
      rodapePanelLayout.putConstraint("East", this.arquivosLabel, 35, "East", this.arquivosDescricaoLabel);
      this.arquivosLabel.setHorizontalAlignment(4);
      rodapePanelLayout.putConstraint("North", this.arquivosLabel, 0, "North", this.arquivosDescricaoLabel);
      rodapePanelLayout.putConstraint("West", this.arquivosLabel, 10, "East", this.arquivosDescricaoLabel);
      rodapePanel.add(this.arquivosLabel);
      this.arquivosMaximoLabel = new JLabel("(máximo: 100)");
      rodapePanelLayout.putConstraint("North", this.arquivosMaximoLabel, 0, "North", this.arquivosDescricaoLabel);
      rodapePanelLayout.putConstraint("West", this.arquivosMaximoLabel, 45, "East", this.arquivosDescricaoLabel);
      rodapePanel.add(this.arquivosMaximoLabel);
      this.bytesMaximoLabel = new JLabel("(máximo: 100 MB)");
      rodapePanelLayout.putConstraint("East", this.bytesMaximoLabel, 0, "East", rodapePanel);
      rodapePanel.add(this.bytesMaximoLabel);
      this.bytesLabel = new JLabel("0 B");
      rodapePanelLayout.putConstraint("North", this.bytesLabel, 0, "North", this.bytesMaximoLabel);
      rodapePanelLayout.putConstraint("East", this.bytesLabel, -10, "West", this.bytesMaximoLabel);
      rodapePanel.add(this.bytesLabel);
      this.bytesDescricaoLabel = new JLabel("Tamanho:");
      rodapePanelLayout.putConstraint("North", this.bytesDescricaoLabel, 0, "North", this.bytesMaximoLabel);
      rodapePanelLayout.putConstraint("East", this.bytesDescricaoLabel, -50, "West", this.bytesMaximoLabel);
      rodapePanel.add(this.bytesDescricaoLabel);
      this.acoesPanel = new JPanel();
      rodapePanelLayout.putConstraint("West", this.acoesPanel, 0, "West", rodapePanel);
      rodapePanelLayout.putConstraint("South", this.acoesPanel, -13, "South", rodapePanel);
      rodapePanelLayout.putConstraint("East", this.acoesPanel, 0, "East", rodapePanel);
      this.acoesPanel.setBackground(Color.WHITE);
      rodapePanelLayout.putConstraint("South", this.bytesMaximoLabel, -10, "North", this.acoesPanel);
      rodapePanelLayout.putConstraint("South", this.arquivosDescricaoLabel, -10, "North", this.acoesPanel);
      rodapePanel.add(this.acoesPanel);
      this.cancelarButton = new JButton("Cancelar");
      this.cancelarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void actionPerformed(ActionEvent e) {
            TransmissaoDocumentosFrame.this.cancelarAction();
         }
      });
      this.acoesPanel.add(this.cancelarButton);
      this.transmitirButton = new JButton("Transmitir");
      this.transmitirButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void actionPerformed(ActionEvent e) {
            TransmissaoDocumentosFrame.this.transmitirAction();
         }
      });
      this.transmitirButton.setEnabled(false);
      this.acoesPanel.add(this.transmitirButton);
      this.progressBar = new JProgressBar();
      this.progressBar.setUI(new TinyProgressBarUI());
      mainLayout.putConstraint("North", this.progressBar, -5, "South", this.getContentPane());
      mainLayout.putConstraint("West", this.progressBar, 0, "West", this.getContentPane());
      mainLayout.putConstraint("South", this.progressBar, 0, "South", this.getContentPane());
      mainLayout.putConstraint("East", this.progressBar, 0, "East", this.getContentPane());
      mainLayout.putConstraint("South", this.acoesPanel, 0, "North", this.progressBar);
      this.etapaTransmissaoLabel = new JLabel("");
      this.etapaTransmissaoLabel.setHorizontalAlignment(0);
      rodapePanelLayout.putConstraint("West", this.etapaTransmissaoLabel, 0, "West", rodapePanel);
      rodapePanelLayout.putConstraint("South", this.etapaTransmissaoLabel, 0, "South", rodapePanel);
      rodapePanelLayout.putConstraint("East", this.etapaTransmissaoLabel, 0, "East", rodapePanel);
      rodapePanel.add(this.etapaTransmissaoLabel);
      this.progressBar.setVisible(false);
      this.progressBar.setBorderPainted(false);
      this.progressBar.setBackground(Color.WHITE);
      this.getContentPane().add(this.progressBar);
   }

   protected void transmitirAction() {
      if (this.documentos != null && this.documentos.size() != 0) {
         log.debug("Usuário selecionou botão Transmitir");
         long bytesArquivos = 0L;
         int numArquivos = 0;

         for(Documento documento : this.documentos) {
            File arquivo = documento.getArquivo();
            bytesArquivos += arquivo.length();
            ++numArquivos;
         }

         Atendimento atendimento = this.getAtendimentoCmd.get(this.context.getNumeroCaso());
         atendimento.setDataGeracaoArquivo(LocalDate.now());
         List<Investigado> investigados = this.getInvestigadosCmd.get();
         final TransmissaoDocumentoRequestTO request = new TransmissaoDocumentoRequestTO();
         request.setAtendimento(atendimento);
         request.setInvestigados(investigados);
         request.setTotalArquivos(numArquivos);
         request.setTotalBytes(bytesArquivos);
         request.setDocumentos(this.documentos);
         request.setObservacao(this.observacaoTextPane.getText().trim());
         SwingWorker<String, Long> worker = new SwingWorker<String, Long>() {
            {
               Objects.requireNonNull(TransmissaoDocumentosFrame.this);
            }

            protected String doInBackground() throws Exception {
               try {
                  int[] ultimaEtapa = new int[]{0};
                  TransmissaoDocumentosFrame.this.transmitirDocumentosCmd.transmitir(request, (etapa, progress) -> {
                     if (etapa != ultimaEtapa[0]) {
                        ultimaEtapa[0] = etapa;
                        if (etapa == 1) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Autenticando...");
                        } else if (etapa == 2) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Empacotando arquivos para transmissão...");
                        } else if (etapa == 3) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Transmitindo...");
                        } else if (etapa == 4) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Aguardando resposta do servidor...");
                        } else if (etapa == 4) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Aguardando resposta do servidor...");
                        } else if (etapa == 5) {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("Concluído");
                        } else {
                           TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("");
                        }
                     }

                     this.setProgress(progress);
                  });
                  TransmissaoDocumentosFrame.this.onTransmissao();
               } catch (NegocioException e) {
                  SwingUtilities.invokeLater(() -> {
                     TransmissaoDocumentosFrame.log.error("Erro de negócio na transmissão de documentos.", e);
                     ViewUtil.showErrorDialog(e.getMessage());
                  });
               } catch (Exception e) {
                  SwingUtilities.invokeLater(() -> {
                     TransmissaoDocumentosFrame.log.error("Erro interno na transmissão de documentos.", e);
                     ViewUtil.showErrorDialog("Erro interno na transmissão de documentos.");
                  });
               } finally {
                  TransmissaoDocumentosFrame.this.setEnabledAll(true);
               }

               return null;
            }

            protected void done() {
               TransmissaoDocumentosFrame.this.etapaTransmissaoLabel.setText("");
               TransmissaoDocumentosFrame.this.progressBar.setVisible(false);
            }
         };
         worker.addPropertyChangeListener(new PropertyChangeListener() {
            {
               Objects.requireNonNull(TransmissaoDocumentosFrame.this);
            }

            public void propertyChange(PropertyChangeEvent evt) {
               if ("progress".equals(evt.getPropertyName())) {
                  TransmissaoDocumentosFrame.this.progressBar.setValue((Integer)evt.getNewValue());
               }

            }
         });
         this.progressBar.setVisible(true);
         this.setEnabledAll(false);
         worker.execute();
      } else {
         throw new NegocioException("Nenhum arquivo selecionado.");
      }
   }

   public void setEnabledAll(boolean enabled) {
      this.chavePanel.setEnabledAll(enabled);
      this.tabelaArquivosPanel.setEnabledAll(enabled);
      this.cancelarButton.setEnabled(enabled);
      this.transmitirButton.setEnabled(enabled);
      this.observacaoTextPane.setEnabled(enabled);
   }

   protected void onTransmissao() {
      this.tabelaArquivosPanel.clear();
      this.observacaoTextPane.setText("");
      log.debug("Transmissão concluída");
   }

   protected void cancelarAction() {
      if (this.frmIdentificaAtendimento != null) {
         this.frmIdentificaAtendimento.mostrar();
      }

      this.setVisible(false);
      this.dispose();
   }

   public void onArquivoChaveChanged(boolean autorizado) {
      this.tabelaArquivosPanel.onArquivoChaveChanged(autorizado);
      this.transmitirButton.setEnabled(false);
   }

   public void onListaDocumentoChanged(List<Documento> documentos) {
      long bytesArquivos = 0L;
      int numArquivos = 0;
      boolean faltaTipoDocumento = false;

      for(Documento documento : documentos) {
         if (documento.getTipo() == null) {
            faltaTipoDocumento = true;
         }

         File arquivo = documento.getArquivo();
         bytesArquivos += arquivo.length();
         ++numArquivos;
      }

      boolean temArquivoDuplicado = false;

      for(Documento documento : documentos) {
         if (documento.isHashDuplicado()) {
            temArquivoDuplicado = true;
            break;
         }
      }

      boolean temObservacaoMuitoGrande = false;

      for(Documento documento : documentos) {
         if (StringUtil.isNotEmpty(documento.getObservacao()) && documento.getObservacao().length() > 2000) {
            temObservacaoMuitoGrande = true;
            break;
         }
      }

      int cemMega = 104857600;
      boolean excedeuTamanho = bytesArquivos > (long)cemMega;
      boolean excedeuQuantidade = numArquivos > 100;
      if (numArquivos != 0 && !faltaTipoDocumento && !temArquivoDuplicado && !temObservacaoMuitoGrande) {
         this.transmitirButton.setEnabled(!excedeuTamanho && !excedeuQuantidade);
      } else {
         this.transmitirButton.setEnabled(false);
      }

      this.documentos = documentos;
      String redSpan = "<html><span style=\"color: red;\">";
      String endSpan = "</span></html>";
      String humanReadableByteCountBin = ByteFormatter.humanReadableByteCountBin(bytesArquivos);
      if (excedeuTamanho) {
         this.bytesLabel.setText(redSpan + humanReadableByteCountBin + endSpan);
      } else {
         this.bytesLabel.setText(humanReadableByteCountBin);
      }

      String textoNumArquivo = String.valueOf(numArquivos);
      if (excedeuQuantidade) {
         this.arquivosLabel.setText(redSpan + textoNumArquivo + endSpan);
      } else {
         this.arquivosLabel.setText(textoNumArquivo);
      }

   }

   private void atualizarTotalCaracteresObservacao() {
      SwingUtilities.invokeLater(() -> this.totalCaracteresLabel.setText(this.observacaoTextPane.getText().length() + "/2000"));
   }

   private void blinkTotalCaracteresObservacao() {
      synchronized(this.timerLock) {
         if (this.timer != null) {
            return;
         }
      }

      this.timer = new Timer("totalObservacaoLabelBlink", true);
      this.timer.scheduleAtFixedRate(new TimerTask() {
         {
            Objects.requireNonNull(TransmissaoDocumentosFrame.this);
         }

         public void run() {
            if (TransmissaoDocumentosFrame.this.timerCount++ % 2 == 1) {
               TransmissaoDocumentosFrame.this.totalCaracteresLabel.setForeground(Color.RED);
            } else {
               TransmissaoDocumentosFrame.this.totalCaracteresLabel.setForeground(Color.BLACK);
            }

            if (TransmissaoDocumentosFrame.this.timerCount > 6) {
               TransmissaoDocumentosFrame.this.timerCount = 1;
               TransmissaoDocumentosFrame.this.timer.cancel();
               synchronized(TransmissaoDocumentosFrame.this.timerLock) {
                  TransmissaoDocumentosFrame.this.timer = null;
               }
            }

         }
      }, 200L, 200L);
   }

   public void setFrmIdentificaAtendimento(frmIdentificaAtendimento frmIdentificaAtendimento) {
      this.frmIdentificaAtendimento = frmIdentificaAtendimento;
   }
}
