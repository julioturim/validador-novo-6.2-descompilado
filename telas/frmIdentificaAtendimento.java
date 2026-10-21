package telas;

import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.Controlador;
import br.mp.mpf.pgr.client.cmd.GetListaCasoCmd;
import br.mp.mpf.pgr.client.to.Caso;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.to.Versao;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.client.ui.IdentificaCaso;
import br.mp.mpf.pgr.client.ui.frmAjuda;
import br.mp.mpf.pgr.client.ui.components.DebounceTimeTextField;
import br.mp.mpf.pgr.client.ui.listener.DebounceTimeListener;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.rules.ValidarCasoPossuiInvestigadoComRelacionamentoRule;
import br.mp.mpf.spea.simba.validador.ui.TransmissaoDocumentosFrame;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class frmIdentificaAtendimento extends IconFrame implements IdentificaCaso {
   private static final Logger log = LoggerFactory.getLogger(frmIdentificaAtendimento.class);
   private static final long serialVersionUID = -116834211056159333L;
   private static final Color FONT_COLOR = new Color(102, 102, 0);
   private static final Font FONT_11_BOLD = new Font("Tahoma", 1, 11);
   protected Computador computadorSelecionado;
   protected Caso casoSelecionado;
   private frmAjuda frmAjuda;
   @Inject
   protected ApplicationContext app;
   @Inject
   public Context context;
   @Inject
   private GetListaCasoCmd getListaCasoCmd;
   @Inject
   private ValidarCasoPossuiInvestigadoComRelacionamentoRule validarCasoPossuiInvestigadoComRelacionamentoRule;
   protected Controlador controlador;
   private Configurador configurador;
   private List<Caso> casos = new ArrayList();
   private List<Caso> casosBackup;
   private JPanel mainPanel;
   protected JLabel tituloLabel;
   protected JLabel descricaoLabel;
   private JComboBox<Computador> computadorComboBox;
   protected JLabel nomeOrgaoLabel;
   protected JLabel numeroCasoLabel;
   private JFormattedTextField numeroCasoText;
   private JLabel digitoVerificadorLabel;
   private JTextField digitoVerificadorText;
   private JButton gerarDvButton;
   private DefaultListModel<Caso> listaCasosModel;
   private JList<Caso> atendimentosList;
   private JLabel versaoLabel;
   private JLabel ambienteLabel;
   private JButton verDetalhesButton;
   private JButton excluirAtendimentoButton;
   protected JButton validarBancoButton;
   protected JButton validarCorretoraButton;
   protected JButton validarCorretoraNovoLayoutButton;
   protected JButton transmissaoDocumentosButton;
   private DebounceTimeTextField filtroAtendimentosText;

   public frmIdentificaAtendimento(Controlador controlador, Configurador configurador) throws HeadlessException {
      this.controlador = controlador;
      this.configurador = configurador;
      this.initComponents();
      if (configurador != null) {
         this.gerarDvButton.setVisible(configurador.isAmbienteDesenv());
      }

   }

   @PostConstruct
   protected void setup() {
      this.setAmbienteLabelText();
      this.versaoLabel.setText(this.configurador.getVersao());
      this.controlador.exibeMensagens();
      this.controlador.verificaImagens();
      SwingUtilities.invokeLater(new Runnable() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void run() {
            frmIdentificaAtendimento.this.refreshListaCasos();
            frmIdentificaAtendimento.this.carregarComputadoresCombo();
         }
      });
   }

   private void initComponents() {
      this.setDefaultCloseOperation(3);
      this.setTitle("Validador Bancário - Passo 1");
      this.setName("passo1");
      this.setBackground(Color.WHITE);
      this.mainPanel = (JPanel)this.getContentPane();
      SpringLayout mainPanelLayout = new SpringLayout();
      this.mainPanel.setLayout(mainPanelLayout);
      this.mainPanel.setBackground(Color.WHITE);
      this.mainPanel.setFocusable(false);
      this.tituloLabel = new JLabel("Validador de Dados Bancários");
      mainPanelLayout.putConstraint("East", this.tituloLabel, -10, "East", this.mainPanel);
      mainPanelLayout.putConstraint("North", this.tituloLabel, 10, "North", this.mainPanel);
      mainPanelLayout.putConstraint("West", this.tituloLabel, 10, "West", this.mainPanel);
      this.tituloLabel.setFont(new Font("Tahoma", 1, 18));
      this.tituloLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(this.tituloLabel);
      this.descricaoLabel = new JLabel();
      mainPanelLayout.putConstraint("North", this.descricaoLabel, 10, "South", this.tituloLabel);
      mainPanelLayout.putConstraint("West", this.descricaoLabel, 10, "West", this.mainPanel);
      mainPanelLayout.putConstraint("East", this.descricaoLabel, -10, "East", this.mainPanel);
      this.descricaoLabel.setForeground(FONT_COLOR);
      this.descricaoLabel.setHorizontalAlignment(2);
      this.descricaoLabel.setText("<html>Este programa visa auxiliar as instituições financeiras no preenchimento dos arquivos para atendimentos de afastamento de sigilo bancário.</html");
      this.descricaoLabel.setVerticalAlignment(1);
      this.descricaoLabel.setVerticalTextPosition(1);
      this.mainPanel.add(this.descricaoLabel);
      JLabel passo1Label = new JLabel("Passo 1 - Identificação do Atendimento");
      mainPanelLayout.putConstraint("North", passo1Label, 10, "South", this.descricaoLabel);
      mainPanelLayout.putConstraint("West", passo1Label, 10, "West", this.mainPanel);
      mainPanelLayout.putConstraint("East", passo1Label, -10, "East", this.mainPanel);
      passo1Label.setFont(new Font("Tahoma", 1, 18));
      passo1Label.setForeground(FONT_COLOR);
      this.mainPanel.add(passo1Label);
      SpringLayout sl_dadosCasoPanel = new SpringLayout();
      JPanel dadosCasoPanel = new JPanel(sl_dadosCasoPanel);
      mainPanelLayout.putConstraint("North", dadosCasoPanel, 10, "South", passo1Label);
      mainPanelLayout.putConstraint("West", dadosCasoPanel, 10, "West", this.mainPanel);
      mainPanelLayout.putConstraint("South", dadosCasoPanel, 115, "South", passo1Label);
      mainPanelLayout.putConstraint("East", dadosCasoPanel, -10, "East", this.mainPanel);
      String tituloDadosCasoPanel = "Adicione a identificação de um novo atendimento";
      TitledBorder dadosCasoBorder = BorderFactory.createTitledBorder((Border)null, tituloDadosCasoPanel, 0, 0, (Font)null, FONT_COLOR);
      dadosCasoPanel.setBorder(dadosCasoBorder);
      this.mainPanel.add(dadosCasoPanel);
      JLabel computadorDestinoLabel = new JLabel();
      sl_dadosCasoPanel.putConstraint("North", computadorDestinoLabel, 5, "North", dadosCasoPanel);
      sl_dadosCasoPanel.putConstraint("West", computadorDestinoLabel, 5, "West", dadosCasoPanel);
      computadorDestinoLabel.setFont(FONT_11_BOLD);
      computadorDestinoLabel.setForeground(FONT_COLOR);
      computadorDestinoLabel.setText("Computador Destino:");
      dadosCasoPanel.add(computadorDestinoLabel);
      DefaultListCellRenderer computadorCellRenderer = new DefaultListCellRenderer() {
         private static final long serialVersionUID = 3300554848916807747L;

         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Computador computador = (Computador)value;
            String valor = computador != null ? computador.getNome_computador() : "";
            return super.getListCellRendererComponent(list, valor, index, isSelected, cellHasFocus);
         }
      };
      this.computadorComboBox = new JComboBox();
      this.computadorComboBox.setRenderer(computadorCellRenderer);
      sl_dadosCasoPanel.putConstraint("North", this.computadorComboBox, -3, "North", computadorDestinoLabel);
      sl_dadosCasoPanel.putConstraint("West", this.computadorComboBox, 10, "East", computadorDestinoLabel);
      sl_dadosCasoPanel.putConstraint("East", this.computadorComboBox, -10, "East", dadosCasoPanel);
      this.computadorComboBox.addItemListener(new ItemListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void itemStateChanged(ItemEvent evt) {
            frmIdentificaAtendimento.this.computadorComboBoxItemStateChanged(evt);
         }
      });
      dadosCasoPanel.add(this.computadorComboBox);
      JLabel orgaoSolicitanteLabel = new JLabel();
      sl_dadosCasoPanel.putConstraint("North", orgaoSolicitanteLabel, 12, "South", computadorDestinoLabel);
      sl_dadosCasoPanel.putConstraint("West", orgaoSolicitanteLabel, 0, "West", computadorDestinoLabel);
      orgaoSolicitanteLabel.setFont(FONT_11_BOLD);
      orgaoSolicitanteLabel.setForeground(FONT_COLOR);
      orgaoSolicitanteLabel.setText("Órgão Solicitante:");
      dadosCasoPanel.add(orgaoSolicitanteLabel);
      this.nomeOrgaoLabel = new JLabel();
      sl_dadosCasoPanel.putConstraint("North", this.nomeOrgaoLabel, 0, "North", orgaoSolicitanteLabel);
      sl_dadosCasoPanel.putConstraint("West", this.nomeOrgaoLabel, 5, "East", orgaoSolicitanteLabel);
      sl_dadosCasoPanel.putConstraint("East", this.nomeOrgaoLabel, -10, "East", dadosCasoPanel);
      this.nomeOrgaoLabel.setFont(FONT_11_BOLD);
      this.nomeOrgaoLabel.setForeground(new Color(0, 51, 255));
      this.nomeOrgaoLabel.setText("Selecione um Computador Destino.");
      dadosCasoPanel.add(this.nomeOrgaoLabel);
      this.numeroCasoLabel = new JLabel();
      sl_dadosCasoPanel.putConstraint("North", this.numeroCasoLabel, 12, "South", orgaoSolicitanteLabel);
      sl_dadosCasoPanel.putConstraint("West", this.numeroCasoLabel, 0, "West", orgaoSolicitanteLabel);
      this.numeroCasoLabel.setFont(FONT_11_BOLD);
      this.numeroCasoLabel.setForeground(FONT_COLOR);
      this.numeroCasoLabel.setText("Número do Caso:");
      dadosCasoPanel.add(this.numeroCasoLabel);

      try {
         this.numeroCasoText = new JFormattedTextField(new MaskFormatter("######"));
         sl_dadosCasoPanel.putConstraint("North", this.numeroCasoText, -3, "North", this.numeroCasoLabel);
         sl_dadosCasoPanel.putConstraint("West", this.numeroCasoText, 5, "East", this.numeroCasoLabel);
         sl_dadosCasoPanel.putConstraint("East", this.numeroCasoText, 65, "East", this.numeroCasoLabel);
      } catch (ParseException var17) {
         ViewUtil.showInfoDialog("Formato Inválido. Use o formato: 000000");
      }

      this.numeroCasoText.addFocusListener(new FocusAdapter() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void focusLost(FocusEvent e) {
            frmIdentificaAtendimento.this.numeroCasoTextLostFocus();
         }

         public void focusGained(FocusEvent e) {
            frmIdentificaAtendimento.this.numeroCasoTextFocusGained();
         }
      });
      dadosCasoPanel.add(this.numeroCasoText);
      this.digitoVerificadorLabel = new JLabel();
      sl_dadosCasoPanel.putConstraint("North", this.digitoVerificadorLabel, 0, "North", this.numeroCasoLabel);
      sl_dadosCasoPanel.putConstraint("West", this.digitoVerificadorLabel, 10, "East", this.numeroCasoText);
      this.digitoVerificadorLabel.setFont(FONT_11_BOLD);
      this.digitoVerificadorLabel.setForeground(FONT_COLOR);
      this.digitoVerificadorLabel.setText("DV:");
      dadosCasoPanel.add(this.digitoVerificadorLabel);

      try {
         this.digitoVerificadorText = new JFormattedTextField(new MaskFormatter("##"));
         sl_dadosCasoPanel.putConstraint("North", this.digitoVerificadorText, -3, "North", this.digitoVerificadorLabel);
         sl_dadosCasoPanel.putConstraint("West", this.digitoVerificadorText, 5, "East", this.digitoVerificadorLabel);
         sl_dadosCasoPanel.putConstraint("East", this.digitoVerificadorText, 30, "East", this.digitoVerificadorLabel);
      } catch (ParseException var16) {
         ViewUtil.showInfoDialog("Formato Inválido. Use o dois números.");
      }

      dadosCasoPanel.add(this.digitoVerificadorText);
      this.gerarDvButton = new JButton();
      sl_dadosCasoPanel.putConstraint("North", this.gerarDvButton, 0, "North", this.digitoVerificadorText);
      sl_dadosCasoPanel.putConstraint("West", this.gerarDvButton, 15, "East", this.digitoVerificadorText);
      this.gerarDvButton.setText("DV ?");
      this.gerarDvButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.gerarDvButtonClick();
         }
      });
      dadosCasoPanel.add(this.gerarDvButton);
      JButton cadastrarButton = new JButton();
      sl_dadosCasoPanel.putConstraint("South", cadastrarButton, -8, "South", dadosCasoPanel);
      sl_dadosCasoPanel.putConstraint("East", cadastrarButton, -10, "East", dadosCasoPanel);
      cadastrarButton.setText("Cadastrar");
      cadastrarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.cadastrarButtonActionPerformed();
         }
      });
      dadosCasoPanel.add(cadastrarButton);
      JLabel selecioneUmAtendimentoLabel = new JLabel("Selecione um atendimento para Validar");
      mainPanelLayout.putConstraint("North", selecioneUmAtendimentoLabel, 123, "South", passo1Label);
      mainPanelLayout.putConstraint("West", selecioneUmAtendimentoLabel, 10, "West", this.mainPanel);
      selecioneUmAtendimentoLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(selecioneUmAtendimentoLabel);
      this.filtroAtendimentosText = new DebounceTimeTextField();
      this.filtroAtendimentosText.addDebounceTimeListener(new DebounceTimeListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void debounceTime(String text) {
            frmIdentificaAtendimento.this.filtrarAtendimentosList(text);
         }
      });
      mainPanelLayout.putConstraint("North", this.filtroAtendimentosText, 5, "South", selecioneUmAtendimentoLabel);
      mainPanelLayout.putConstraint("West", this.filtroAtendimentosText, 0, "West", selecioneUmAtendimentoLabel);
      this.mainPanel.add(this.filtroAtendimentosText);
      DefaultListCellRenderer casoCellRenderer = new DefaultListCellRenderer() {
         private static final long serialVersionUID = -4505106695856564929L;

         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Caso caso = (Caso)value;
            return super.getListCellRendererComponent(list, caso.getNumeroCaso(), index, isSelected, cellHasFocus);
         }
      };
      this.listaCasosModel = new DefaultListModel();
      this.atendimentosList = new JList(this.listaCasosModel);
      this.atendimentosList.setCellRenderer(casoCellRenderer);
      this.atendimentosList.setSelectionMode(0);
      this.atendimentosList.addMouseListener(new MouseAdapter() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void mouseReleased(MouseEvent evt) {
            frmIdentificaAtendimento.this.atendimentosListMouseReleased(evt);
         }
      });
      this.atendimentosList.addListSelectionListener(new ListSelectionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void valueChanged(ListSelectionEvent e) {
            frmIdentificaAtendimento.this.atendimentosListValueChanged();
         }
      });
      JScrollPane listaAtendimentosScrollPane = new JScrollPane();
      mainPanelLayout.putConstraint("North", listaAtendimentosScrollPane, 2, "South", this.filtroAtendimentosText);
      mainPanelLayout.putConstraint("East", listaAtendimentosScrollPane, 0, "East", this.filtroAtendimentosText);
      listaAtendimentosScrollPane.setViewportView(this.atendimentosList);
      mainPanelLayout.putConstraint("West", listaAtendimentosScrollPane, 10, "West", this.mainPanel);
      this.mainPanel.add(listaAtendimentosScrollPane);
      this.verDetalhesButton = new JButton("Ver Detalhes");
      mainPanelLayout.putConstraint("North", this.verDetalhesButton, 0, "North", this.filtroAtendimentosText);
      mainPanelLayout.putConstraint("East", this.filtroAtendimentosText, -10, "West", this.verDetalhesButton);
      mainPanelLayout.putConstraint("West", this.verDetalhesButton, -230, "East", this.getContentPane());
      mainPanelLayout.putConstraint("East", this.verDetalhesButton, -10, "East", this.mainPanel);
      this.verDetalhesButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.verDetalhesButtonClick();
         }
      });
      this.mainPanel.add(this.verDetalhesButton);
      this.excluirAtendimentoButton = new JButton("Excluir");
      mainPanelLayout.putConstraint("North", this.excluirAtendimentoButton, 5, "South", this.verDetalhesButton);
      mainPanelLayout.putConstraint("West", this.excluirAtendimentoButton, 0, "West", this.verDetalhesButton);
      mainPanelLayout.putConstraint("East", this.excluirAtendimentoButton, -10, "East", this.mainPanel);
      this.excluirAtendimentoButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.excluirAtendimentoButtonClick();
         }
      });
      this.mainPanel.add(this.excluirAtendimentoButton);
      this.validarBancoButton = new JButton("Validar Arquivos CC 3454");
      mainPanelLayout.putConstraint("West", this.validarBancoButton, 0, "West", this.verDetalhesButton);
      mainPanelLayout.putConstraint("North", this.validarBancoButton, 20, "South", this.excluirAtendimentoButton);
      mainPanelLayout.putConstraint("East", this.validarBancoButton, -10, "East", this.mainPanel);
      this.validarBancoButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.validarBancoButtonClick();
         }
      });
      this.mainPanel.add(this.validarBancoButton);
      this.validarCorretoraButton = new JButton("Validar Arquivos GAB");
      mainPanelLayout.putConstraint("North", this.validarCorretoraButton, 5, "South", this.validarBancoButton);
      mainPanelLayout.putConstraint("West", this.validarCorretoraButton, 0, "West", this.verDetalhesButton);
      mainPanelLayout.putConstraint("East", this.validarCorretoraButton, -10, "East", this.mainPanel);
      this.validarCorretoraButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.validarCorretoraButtonClick(1);
         }
      });
      this.mainPanel.add(this.validarCorretoraButton);
      this.validarCorretoraNovoLayoutButton = new JButton("Validar Arquivos GAB (Versão 2)");
      mainPanelLayout.putConstraint("North", this.validarCorretoraNovoLayoutButton, 5, "South", this.validarCorretoraButton);
      mainPanelLayout.putConstraint("West", this.validarCorretoraNovoLayoutButton, 0, "West", this.verDetalhesButton);
      mainPanelLayout.putConstraint("East", this.validarCorretoraNovoLayoutButton, -10, "East", this.mainPanel);
      this.validarCorretoraNovoLayoutButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.validarCorretoraButtonClick(2);
         }
      });
      this.mainPanel.add(this.validarCorretoraNovoLayoutButton);
      this.transmissaoDocumentosButton = new JButton("Transmissão de Documentos");
      mainPanelLayout.putConstraint("West", this.transmissaoDocumentosButton, 0, "West", this.validarBancoButton);
      mainPanelLayout.putConstraint("North", this.transmissaoDocumentosButton, 20, "South", this.validarCorretoraNovoLayoutButton);
      mainPanelLayout.putConstraint("East", this.transmissaoDocumentosButton, -10, "East", this.mainPanel);
      this.transmissaoDocumentosButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.transmissaoDocumentosButtonClick();
         }
      });
      this.mainPanel.add(this.transmissaoDocumentosButton);
      JButton ajudaButton = new JButton("Ajuda");
      mainPanelLayout.putConstraint("West", ajudaButton, 0, "West", this.validarBancoButton);
      mainPanelLayout.putConstraint("South", ajudaButton, 0, "South", listaAtendimentosScrollPane);
      mainPanelLayout.putConstraint("East", ajudaButton, -10, "East", this.mainPanel);
      ajudaButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmIdentificaAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmIdentificaAtendimento.this.ajudarButtonClick();
         }
      });
      this.mainPanel.add(ajudaButton);
      JLabel versaoTituloLabel = new JLabel("Versão:");
      mainPanelLayout.putConstraint("South", listaAtendimentosScrollPane, -10, "North", versaoTituloLabel);
      mainPanelLayout.putConstraint("West", versaoTituloLabel, 10, "West", this.mainPanel);
      mainPanelLayout.putConstraint("South", versaoTituloLabel, -10, "South", this.getContentPane());
      versaoTituloLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(versaoTituloLabel);
      this.versaoLabel = new JLabel("3.0.0");
      mainPanelLayout.putConstraint("West", this.versaoLabel, 10, "East", versaoTituloLabel);
      mainPanelLayout.putConstraint("North", this.versaoLabel, 0, "North", versaoTituloLabel);
      this.versaoLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(this.versaoLabel);
      this.ambienteLabel = new JLabel("Acessando ambiente de testes: orgaos vamp");
      mainPanelLayout.putConstraint("West", this.ambienteLabel, 50, "East", this.versaoLabel);
      mainPanelLayout.putConstraint("East", this.ambienteLabel, -10, "East", this.mainPanel);
      mainPanelLayout.putConstraint("North", this.ambienteLabel, 0, "North", versaoTituloLabel);
      this.ambienteLabel.setFont(new Font("Tahoma", 1, 12));
      this.ambienteLabel.setForeground(new Color(255, 51, 0));
      this.mainPanel.add(this.ambienteLabel);
      this.setMinimumSize(new Dimension(550, 600));
      this.setSize(new Dimension(550, 600));
      this.pack();
      this.setLocationRelativeTo((java.awt.Component)null);
   }

   public void refreshListaCasos() {
      synchronized(this.casos) {
         this.casos = this.getListaCasoCmd.get();
         this.refreshListaCasos(this.casos);
      }
   }

   private void refreshListaCasos(List<Caso> casos) {
      this.listaCasosModel.clear();
      this.casosBackup = new ArrayList(casos);

      for(Caso caso : casos) {
         this.listaCasosModel.addElement(caso);
      }

      this.atendimentosListValueChanged();
   }

   public void mostrar() {
      this.context.setNumeroCaso((String)null);
      this.setVisible(true);
      this.refreshListaCasos();
   }

   private void setAmbienteLabelText() {
      String textoAmbiente = this.configurador.getTextoAmbiente();
      if (textoAmbiente == null) {
         this.ambienteLabel.setVisible(false);
      } else {
         this.ambienteLabel.setText(textoAmbiente);
      }

   }

   private void carregarComputadoresCombo() {
      this.computadorComboBox.addItem((Object)null);

      for(Computador computador : this.controlador.getComputadores().getComputadores()) {
         this.computadorComboBox.addItem(computador);
      }

   }

   private void ajudarButtonClick() {
      if (this.frmAjuda == null) {
         this.frmAjuda = (frmAjuda)this.app.getBean(frmAjuda.class);
      }

      this.frmAjuda.setVisible(true);
   }

   protected void excluirAtendimentoButtonClick() {
      if (this.atendimentosList.isSelectionEmpty()) {
         ViewUtil.showInfoDialog("Selecione um caso.");
      } else {
         if (ViewUtil.showSimNaoDialog("Tem certeza que deseja remover todos os dados deste caso?", "Exclusão de Caso")) {
            this.context.setNumeroCaso((String)null);
            Caso caso = (Caso)this.atendimentosList.getSelectedValue();
            File pastaCaso = this.context.getPastaCaso(caso.getNumeroCaso());

            try {
               FileUtils.deleteDirectory(pastaCaso);
               synchronized(this.casos) {
                  this.casos.remove(caso);
                  this.listaCasosModel.removeElement(caso);
               }
            } catch (Exception var6) {
               ViewUtil.showErrorDialog("Erro ao tentar apagar o caso.<br>Verifique se não há outra instância deste programa usando este caso.");
            }
         }

      }
   }

   protected void verDetalhesButtonClick() {
      if (this.atendimentosList.isSelectionEmpty()) {
         ViewUtil.showInfoDialog("Selecione um caso.");
      } else {
         try {
            Caso caso = (Caso)this.atendimentosList.getSelectedValue();
            this.initFrmDadosAtendimento(caso.getNumeroCaso(), false);
         } catch (Exception e) {
            log.error("Ocorreu um erro interno ao tentar visualizar o atendimento.", e);
            ViewUtil.showErrorDialog("Ocorreu um erro interno ao tentar visualizar o atendimento.<br>Apague o atendimento e cadastre o novamente.");
         }

      }
   }

   protected void transmissaoDocumentosButtonClick() {
      if (this.atendimentosList.isSelectionEmpty()) {
         ViewUtil.showInfoDialog("Selecione um caso.");
      } else {
         Caso caso = (Caso)this.atendimentosList.getSelectedValue();
         this.context.setNumeroCaso(caso.getNumeroCaso());
         TransmissaoDocumentosFrame transmissaoFrame = (TransmissaoDocumentosFrame)this.app.getBean(TransmissaoDocumentosFrame.class);
         transmissaoFrame.setFrmIdentificaAtendimento(this);
         transmissaoFrame.setVisible(true);
         this.setVisible(false);
      }
   }

   protected void initFrmDadosAtendimento(String codigoAtendimento, boolean novo) {
      String var10000 = codigoAtendimento.split("-")[0];
      String nomeComputador = var10000 + "-" + codigoAtendimento.split("-")[1];
      this.computadorSelecionado = this.controlador.getComputador(nomeComputador);
      this.context.setNumeroCaso(codigoAtendimento);
      frmDadosAtendimento frmAtendimento = (frmDadosAtendimento)this.app.getBean(frmDadosAtendimento.class);
      if (novo) {
         frmAtendimento.setNomeIdentificador(this.numeroCasoLabel.getText().trim());
         frmAtendimento.setNomeOrgao(this.nomeOrgaoLabel.getText().trim());
      } else {
         frmAtendimento.setNomeIdentificador(this.computadorSelecionado.getLabelIdentificador());
         frmAtendimento.setNomeOrgao(this.computadorSelecionado.getNome_orgao());
         frmAtendimento.carregarDadosAtendimento();
      }

      this.setVisible(false);
      frmAtendimento.setVisible(true);
   }

   private boolean isEleitoral() {
      return this.computadorSelecionado == null ? false : this.context.isEleitoral(this.computadorSelecionado.getNome_computador());
   }

   protected void validarBancoButtonClick() {
      if (this.atendimentosList.isSelectionEmpty()) {
         ViewUtil.showInfoDialog("Selecione um caso.");
      } else {
         Caso caso = (Caso)this.atendimentosList.getSelectedValue();
         this.context.setNumeroCaso(caso.getNumeroCaso());
         if (!this.configurador.getCodigoPrograma().equals("VCCS") && !this.validarCasoPossuiInvestigadoComRelacionamentoRule.validar()) {
            this.context.setNumeroCaso((String)null);
            ViewUtil.showInfoDialog("Nenhum investigado cadastrado possui relacionamento.\nCadastre um investigado com relacionamento para prosseguir!");
         } else {
            this.setVisible(false);
            JFrame frameValidacao = this.getValidacaoFrame();
            frameValidacao.setVisible(true);
         }
      }
   }

   protected JFrame getValidacaoFrame() {
      frmSelecionaDiretorio selecionaDiretorio = (frmSelecionaDiretorio)this.app.getBean(frmSelecionaDiretorio.class);
      selecionaDiretorio.setFrmIdentificaAtendimento(this);
      return selecionaDiretorio;
   }

   private void validarCorretoraButtonClick(int versaoLayoutGab) {
      if (this.atendimentosList.isSelectionEmpty()) {
         ViewUtil.showInfoDialog("Selecione um caso.");
      } else {
         Caso caso = (Caso)this.atendimentosList.getSelectedValue();
         this.setVisible(false);
         this.context.setNumeroCaso(caso.getNumeroCaso());
         this.configurador.setVersaoLayoutGab(versaoLayoutGab);
         frmSelecionaDiretorioGab selecionaDiretorio = (frmSelecionaDiretorioGab)this.app.getBean(frmSelecionaDiretorioGab.class);
         selecionaDiretorio.setFrmIdentificaAtendimento(this);
         selecionaDiretorio.setVisible(true);
      }
   }

   private void filtrarAtendimentosList(String texto) {
      SwingUtilities.invokeLater(() -> {
         if (StringUtil.isEmpty(texto)) {
            this.refreshListaCasos(this.casosBackup);
         } else {
            synchronized(this.casos) {
               this.listaCasosModel.clear();

               for(Caso caso : this.casosBackup) {
                  String numeroCaso = caso.getNumeroCaso().toUpperCase();
                  if (numeroCaso.contains(texto.toUpperCase())) {
                     this.listaCasosModel.addElement(caso);
                  }
               }
            }
         }

      });
   }

   private void atendimentosListMouseReleased(MouseEvent evt) {
      if (evt.getClickCount() == 2) {
         this.validarBancoButtonClick();
      }

   }

   private void atendimentosListValueChanged() {
      if (this.atendimentosList.getSelectedIndex() == -1) {
         this.verDetalhesButton.setEnabled(false);
         this.excluirAtendimentoButton.setEnabled(false);
         this.validarBancoButton.setEnabled(false);
         this.validarCorretoraButton.setEnabled(false);
         this.validarCorretoraNovoLayoutButton.setEnabled(false);
         this.transmissaoDocumentosButton.setEnabled(false);
      } else {
         this.casoSelecionado = (Caso)this.atendimentosList.getSelectedValue();
         this.verDetalhesButton.setEnabled(true);
         this.excluirAtendimentoButton.setEnabled(true);
         this.validarBancoButton.setEnabled(true);
         Computador computador = this.casoSelecionado.getComputador();
         Versao versao = computador.getVersaoReceptor();
         boolean suportaCorretora = versao.suportaCorretora(computador);
         boolean suportaTransmissao = versao.suportaTransmissaoDocumentos(computador);
         boolean suportaCorretoraNovoLayout = versao.suportaCorretoraV2(computador);
         this.validarCorretoraButton.setEnabled(suportaCorretora);
         this.validarCorretoraNovoLayoutButton.setEnabled(suportaCorretoraNovoLayout);
         this.transmissaoDocumentosButton.setEnabled(suportaTransmissao);
      }
   }

   protected void cadastrarButtonActionPerformed() {
      int len = this.isEleitoral() ? 4 : 6;
      if (this.computadorSelecionado != null && this.numeroCasoText.getText().trim().length() == len && this.digitoVerificadorText.getText().trim().length() == 2) {
         StringBuilder numeroCaso = new StringBuilder();
         numeroCaso.append(this.computadorSelecionado.getNome_computador());
         if (this.isEleitoral()) {
            numeroCaso.append("00");
         }

         numeroCaso.append(this.numeroCasoText.getText().trim());
         numeroCaso.append(this.digitoVerificadorText.getText().trim());
         if (!Util.valida_DV_Atendimento(numeroCaso.toString())) {
            ViewUtil.showWarnDialog("O dígito verificador está inválido.");
         } else {
            StringBuilder codigoAtendimentoSb = new StringBuilder();
            codigoAtendimentoSb.append(this.computadorSelecionado.getNome_computador());
            codigoAtendimentoSb.append("-");
            if (this.isEleitoral()) {
               codigoAtendimentoSb.append("00");
            }

            codigoAtendimentoSb.append(this.numeroCasoText.getText().trim());
            codigoAtendimentoSb.append("-");
            codigoAtendimentoSb.append(this.digitoVerificadorText.getText().trim());
            String codigoAtendimento = codigoAtendimentoSb.toString();

            for(int i = 0; i < this.atendimentosList.getModel().getSize(); ++i) {
               String numeroCasoAtendimentosList = ((Caso)this.atendimentosList.getModel().getElementAt(i)).toString();
               if (codigoAtendimento.equalsIgnoreCase(numeroCasoAtendimentosList)) {
                  ViewUtil.showWarnDialog("Já existe um caso cadastrado com este número.");
                  return;
               }
            }

            this.initFrmDadosAtendimento(codigoAtendimento, true);
         }
      } else {
         ViewUtil.showWarnDialog("Informe o Computador de destino, o Numero do Caso e o DV.");
      }
   }

   private void computadorComboBoxItemStateChanged(ItemEvent evt) {
      if (evt.getStateChange() != 2) {
         if (this.computadorComboBox.getSelectedIndex() != -1) {
            Computador computadorAnterior = this.computadorSelecionado;
            this.computadorSelecionado = (Computador)this.computadorComboBox.getItemAt(this.computadorComboBox.getSelectedIndex());
            this.configurarNumeroCasoDigitoVerificador(this.computadorSelecionado, computadorAnterior);
            this.nomeOrgaoLabel.setText(this.computadorSelecionado.getNome_orgao());
         }
      }
   }

   private void configurarNumeroCasoDigitoVerificador(Computador computadorSelecionado, Computador computadorAnterior) {
      if (computadorSelecionado != computadorAnterior) {
         if (computadorSelecionado.getCodigo_computador() == 999) {
            this.numeroCasoText.setText("000001");
            this.digitoVerificadorText.setText("88");
            this.numeroCasoText.setEditable(false);
            this.digitoVerificadorText.setEditable(false);
         } else {
            if (computadorAnterior != null && computadorAnterior.getCodigo_computador() == 999) {
               this.numeroCasoText.setEditable(true);
               this.digitoVerificadorText.setEditable(true);
               this.numeroCasoText.setText("");
               this.digitoVerificadorText.setText("");
               this.numeroCasoText.repaint();
               this.digitoVerificadorText.repaint();
            }

            boolean isEleitoralComputadorSelecionado = this.context.isEleitoral(computadorSelecionado.getNome_computador());
            if (computadorAnterior != null) {
               boolean isEleitoralComputadorAnterior = this.context.isEleitoral(computadorAnterior.getNome_computador());
               if (isEleitoralComputadorSelecionado == isEleitoralComputadorAnterior) {
                  return;
               }
            }

            if (isEleitoralComputadorSelecionado) {
               this.numeroCasoLabel.setText("Ano:");

               try {
                  this.numeroCasoText.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####")));
               } catch (ParseException e) {
                  String message = "Formato Inválido. Use o formato: 0000";
                  log.error("Erro ao ler o campo \"Ano\".", e);
                  log.info("Mensagem para o usuário: {}", message);
                  ViewUtil.showInfoDialog(message);
               }

               this.digitoVerificadorLabel.setVisible(false);
               this.digitoVerificadorText.setVisible(false);
               this.numeroCasoText.setText(String.valueOf(LocalDate.now().getYear()));
               String var10000 = computadorSelecionado.getNome_computador();
               String numeroCaso = var10000 + "00" + this.numeroCasoText.getText().trim();
               String digitoVerificadorCalculado = Util.getDvAtendimento(numeroCaso);
               this.digitoVerificadorText.setText(digitoVerificadorCalculado);
            } else {
               this.numeroCasoLabel.setText("Número do Caso:");

               try {
                  this.numeroCasoText.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("######")));
               } catch (ParseException var6) {
                  ViewUtil.showInfoDialog("Formato Inválido. Use o formato: 000000");
               }

               this.digitoVerificadorLabel.setVisible(true);
               this.digitoVerificadorText.setVisible(true);
               this.numeroCasoText.setText("");
               this.digitoVerificadorText.setText("");
            }

            this.numeroCasoText.repaint();
            this.digitoVerificadorText.repaint();
         }
      }
   }

   private void numeroCasoTextLostFocus() {
      if (this.isEleitoral()) {
         JTextField var10000 = this.digitoVerificadorText;
         String var10001 = this.computadorSelecionado.getNome_computador();
         var10000.setText(Util.getDvAtendimento(var10001 + "00" + this.numeroCasoText.getText().trim()));
      }

   }

   private void numeroCasoTextFocusGained() {
      if (this.numeroCasoText.getText().trim().length() == 0) {
         this.numeroCasoText.setCaretPosition(0);
      }

   }

   private void gerarDvButtonClick() {
      if (this.computadorSelecionado != null) {
         String numero = this.numeroCasoText.getText().trim();
         if (numero.length() == 6) {
            String var10000 = this.computadorSelecionado.getNome_computador();
            String numeroCaso = var10000 + numero;
            this.digitoVerificadorText.setText(Util.getDvAtendimento(numeroCaso));
         }
      }
   }
}
