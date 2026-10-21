package telas;

import DAO.Mapa;
import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.client.ui.UpperCaseMaskFormatter;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.CpfCnpjUtil;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.cmd.GetAtendimentoCmd;
import br.mp.mpf.spea.simba.validador.cmd.InserirAtendimentoCmd;
import br.mp.mpf.spea.simba.validador.domain.Atendimento;
import br.mp.mpf.spea.simba.validador.enums.CamposInvestigado;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class frmDadosAtendimento extends IconFrame {
   private static final int HEIGHT = 550;
   private static final int WIDTH = 675;
   private static final Logger log = LoggerFactory.getLogger(frmDadosAtendimento.class);
   private static final long serialVersionUID = -3504103422460961992L;
   private String numeroCaso;
   private DefaultTableModel investigadoTableModel;
   @Inject
   private Context context;
   @Inject
   private ControladorValidador controlador;
   @Inject
   private frmIdentificaAtendimento frmIdentificaAtendimento;
   @Inject
   private Config config;
   private frmInvestigado formInvestigado;
   @Inject
   private GetAtendimentoCmd getAtendimentoCmd;
   @Inject
   private InserirAtendimentoCmd insertAtendimentoCmd;
   private Font font12Bold = new Font("Tahoma", 1, 12);
   private Font font14 = new Font("Tahoma", 0, 14);
   private Font font14Bold = new Font("Tahoma", 1, 14);
   private Color fontColor = new Color(102, 102, 0);
   private JPanel mainPanel;
   private JLabel identificadorNumeroLabel;
   private JLabel numeroCasoLabel;
   private JLabel nomeOrgaoLabel;
   private JButton gravarButton;
   private JButton cancelarButton;
   private JTabbedPane abasTabbedPane;
   private JFormattedTextField numeroBancoTextField;
   private JTextField nomeBancoTextField;
   private JFormattedTextField cnpjBancoTextField;
   private JTextField nomeFuncionarioTextField;
   private JTextField telefoneFuncionarioTextField;
   private JTextField emailFuncionarioTextField;
   private JTextField nomeBancoAtendimentoTextField;
   private JTextField numeroCorreioBacenTextField;
   private JTextField numeroOficioTextField;
   private JFormattedTextField dataOficioTextField;
   private JTextField numeroProcessoTextField;
   private JTextField numeroVaraTextField;
   private JTextField nomeTribunalTextField;
   private JTextField nomeMagistradoTextField;
   private JTextField cargoMagistradoTextField;
   private JFormattedTextField dataFimAfastamentoTextField;
   private JFormattedTextField dataInicioAfastamentoTextField;
   private JTextArea descricaoAtendimentoTextArea;
   private JTable investigadosTable;
   private JButton adicionarInvestigadosButton;
   private JButton removerInvestigadoButton;
   private JButton importarInvestigadosButton;
   private JButton removerTodosInvestigadosButton;

   public frmDadosAtendimento(ControladorValidador controlador) {
      this.controlador = controlador;
      this.initComponents();
   }

   @PostConstruct
   public void setup() {
      this.numeroCaso = this.context.getNumeroCaso();
      this.numeroCasoLabel.setText(this.numeroCaso);
      this.numeroCorreioBacenTextField.setEnabled(!this.context.isEleitoral());
      this.numeroOficioTextField.setEnabled(!this.context.isEleitoral());
      this.dataOficioTextField.setEnabled(!this.context.isEleitoral());
      this.numeroProcessoTextField.setEnabled(!this.context.isEleitoral());
      this.numeroVaraTextField.setEnabled(!this.context.isEleitoral());
      this.nomeTribunalTextField.setEnabled(!this.context.isEleitoral());
      this.nomeMagistradoTextField.setEnabled(!this.context.isEleitoral());
      this.cargoMagistradoTextField.setEnabled(!this.context.isEleitoral());
      if (this.context.isEleitoral()) {
         String ano = this.numeroCaso.split("-")[2].substring(2);
         this.dataInicioAfastamentoTextField.setText("01/01/" + ano);
         this.dataFimAfastamentoTextField.setText("31/12/" + ano);
      } else {
         this.dataInicioAfastamentoTextField.setText("");
         this.dataFimAfastamentoTextField.setText("");
      }

      this.pack();
      this.setLocationRelativeTo((java.awt.Component)null);
   }

   public void setNomeIdentificador(String nomeIdentificador) {
      this.identificadorNumeroLabel.setText(nomeIdentificador);
   }

   public void setNomeOrgao(String nomeOrgao) {
      this.nomeOrgaoLabel.setText(nomeOrgao);
   }

   public void adicionarInvestigado(String sTipoPessoa, String sCPF_CNPJ, String sNome, String sRelacionamento, String sConta, String sBDV, String sObservacao, String sDini, String sDfim) {
      this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();
      this.investigadoTableModel.addRow(new String[]{sTipoPessoa, sCPF_CNPJ, sNome, sRelacionamento, sConta, sBDV, sObservacao, sDini, sDfim});
   }

   public void carregarDadosAtendimento() {
      Atendimento atendimento = this.getAtendimentoCmd.get(this.numeroCaso);
      if (atendimento == null) {
         log.error("Erro ao procurar atendimento {}", this.numeroCaso);
         ViewUtil.showErrorDialog("Erro ao abrir caso " + this.numeroCaso + ".\nSe o erro persistir, exclua e cadastre o caso novamente.");
      } else {
         this.atendimentoToForm(atendimento);
         this.carregarInvestigados();
      }
   }

   private void initComponents() {
      this.setDefaultCloseOperation(2);
      this.setTitle("Validador Bancário - Dados do Caso");
      this.setBackground(Color.WHITE);
      this.setForeground(this.fontColor);
      this.addWindowListener(new WindowAdapter() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void windowClosing(WindowEvent evt) {
            frmDadosAtendimento.this.cancelarButtonClick();
         }
      });
      this.mainPanel = (JPanel)this.getContentPane();
      this.mainPanel.setBackground(Color.WHITE);
      SpringLayout mainLayout = new SpringLayout();
      this.mainPanel.setLayout(mainLayout);
      this.identificadorNumeroLabel = new JLabel("Código Identificador: ");
      mainLayout.putConstraint("North", this.identificadorNumeroLabel, 10, "North", this.mainPanel);
      mainLayout.putConstraint("West", this.identificadorNumeroLabel, 10, "West", this.mainPanel);
      this.identificadorNumeroLabel.setFont(this.font14Bold);
      this.identificadorNumeroLabel.setForeground(this.fontColor);
      this.getContentPane().add(this.identificadorNumeroLabel);
      this.numeroCasoLabel = new JLabel("0000-000000");
      mainLayout.putConstraint("North", this.numeroCasoLabel, 0, "North", this.identificadorNumeroLabel);
      mainLayout.putConstraint("West", this.numeroCasoLabel, 10, "East", this.identificadorNumeroLabel);
      this.numeroCasoLabel.setFont(this.font14Bold);
      this.numeroCasoLabel.setForeground(this.fontColor);
      this.getContentPane().add(this.numeroCasoLabel);
      this.nomeOrgaoLabel = new JLabel("Nome do Órgão");
      mainLayout.putConstraint("North", this.nomeOrgaoLabel, 5, "South", this.identificadorNumeroLabel);
      mainLayout.putConstraint("West", this.nomeOrgaoLabel, 0, "West", this.identificadorNumeroLabel);
      this.nomeOrgaoLabel.setFont(this.font12Bold);
      this.nomeOrgaoLabel.setForeground(this.fontColor);
      this.getContentPane().add(this.nomeOrgaoLabel);
      JLabel preenchaTodosLabel = new JLabel("Preencha TODOS os campos, exceto os que não se aplicarem.");
      mainLayout.putConstraint("North", preenchaTodosLabel, 10, "South", this.nomeOrgaoLabel);
      mainLayout.putConstraint("East", preenchaTodosLabel, -10, "East", this.mainPanel);
      preenchaTodosLabel.setForeground(Color.red);
      this.getContentPane().add(preenchaTodosLabel);
      this.abasTabbedPane = new JTabbedPane();
      mainLayout.putConstraint("North", this.abasTabbedPane, 5, "South", preenchaTodosLabel);
      mainLayout.putConstraint("West", this.abasTabbedPane, 10, "West", this.mainPanel);
      mainLayout.putConstraint("East", this.abasTabbedPane, -10, "East", this.mainPanel);
      this.abasTabbedPane.setForeground(this.fontColor);
      this.abasTabbedPane.setFont(this.font12Bold);
      this.abaInstituicaoFinanceiraPanel();
      this.abaProcessoInqueritoPanel();
      this.abaInvestigadosPanel();
      this.mainPanel.add(this.abasTabbedPane);
      this.gravarButton = new JButton("Gravar");
      this.gravarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmDadosAtendimento.this.gravarButtonClick();
         }
      });
      this.mainPanel.add(this.gravarButton);
      this.cancelarButton = new JButton("Cancelar");
      mainLayout.putConstraint("East", this.cancelarButton, -20, "East", this.getContentPane());
      mainLayout.putConstraint("South", this.abasTabbedPane, -10, "North", this.cancelarButton);
      mainLayout.putConstraint("North", this.gravarButton, 0, "North", this.cancelarButton);
      mainLayout.putConstraint("East", this.gravarButton, -10, "West", this.cancelarButton);
      mainLayout.putConstraint("South", this.cancelarButton, -10, "South", this.mainPanel);
      this.cancelarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent evt) {
            frmDadosAtendimento.this.cancelarButtonClick();
         }
      });
      this.mainPanel.add(this.cancelarButton);
      this.setMinimumSize(new Dimension(675, 550));
      this.setSize(new Dimension(675, 550));
      this.pack();
      this.setLocationRelativeTo((java.awt.Component)null);
   }

   private void abaInstituicaoFinanceiraPanel() {
      SpringLayout abaInstituicaoFinanceiraLayout = new SpringLayout();
      JPanel abaInstituicaoFinanceiraPanel = new JPanel(abaInstituicaoFinanceiraLayout);
      abaInstituicaoFinanceiraPanel.setBackground(Color.WHITE);
      abaInstituicaoFinanceiraPanel.setFont(this.font14);
      this.abasTabbedPane.addTab("Instituição Financeira", abaInstituicaoFinanceiraPanel);
      SpringLayout instituicaoFinanceiraLayout = new SpringLayout();
      JPanel instituicaoFinanceiraPanel = new JPanel(instituicaoFinanceiraLayout);
      abaInstituicaoFinanceiraLayout.putConstraint("North", instituicaoFinanceiraPanel, 15, "North", abaInstituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("West", instituicaoFinanceiraPanel, 10, "West", abaInstituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("South", instituicaoFinanceiraPanel, 130, "North", abaInstituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("East", instituicaoFinanceiraPanel, -10, "East", abaInstituicaoFinanceiraPanel);
      instituicaoFinanceiraPanel.setBackground(Color.WHITE);
      abaInstituicaoFinanceiraPanel.add(instituicaoFinanceiraPanel);
      String tituloInstituicaoFinanceira = "Instituição financeira";
      TitledBorder instituicaoFinanceiraInnerBorder = BorderFactory.createTitledBorder((Border)null, tituloInstituicaoFinanceira, 0, 0, (Font)null, this.fontColor);
      TitledBorder instituicaoFinanceiraOuterBorder = BorderFactory.createTitledBorder(instituicaoFinanceiraInnerBorder, "", 0, 0, (Font)null, this.fontColor);
      instituicaoFinanceiraPanel.setBorder(instituicaoFinanceiraOuterBorder);
      JLabel numeroBancoLabel = new JLabel("Número:");
      instituicaoFinanceiraLayout.putConstraint("North", numeroBancoLabel, 7, "North", instituicaoFinanceiraPanel);
      instituicaoFinanceiraLayout.putConstraint("West", numeroBancoLabel, 5, "West", instituicaoFinanceiraPanel);
      numeroBancoLabel.setForeground(this.fontColor);
      instituicaoFinanceiraPanel.add(numeroBancoLabel);

      try {
         this.numeroBancoTextField = new JFormattedTextField(new MaskFormatter("###"));
      } catch (ParseException var26) {
         ViewUtil.showInfoDialog("Número banco inválido. Use 3 números.");
      }

      instituicaoFinanceiraLayout.putConstraint("North", this.numeroBancoTextField, -1, "North", numeroBancoLabel);
      instituicaoFinanceiraLayout.putConstraint("West", this.numeroBancoTextField, 60, "West", instituicaoFinanceiraPanel);
      instituicaoFinanceiraLayout.putConstraint("East", this.numeroBancoTextField, 75, "East", numeroBancoLabel);
      instituicaoFinanceiraPanel.add(this.numeroBancoTextField);
      JLabel nomeBancoLabel = new JLabel("Nome:");
      instituicaoFinanceiraLayout.putConstraint("North", nomeBancoLabel, 15, "South", numeroBancoLabel);
      instituicaoFinanceiraLayout.putConstraint("West", nomeBancoLabel, 0, "West", numeroBancoLabel);
      nomeBancoLabel.setForeground(this.fontColor);
      instituicaoFinanceiraPanel.add(nomeBancoLabel);
      this.nomeBancoTextField = new JTextField();
      instituicaoFinanceiraLayout.putConstraint("North", this.nomeBancoTextField, -1, "North", nomeBancoLabel);
      instituicaoFinanceiraLayout.putConstraint("West", this.nomeBancoTextField, 0, "West", this.numeroBancoTextField);
      instituicaoFinanceiraLayout.putConstraint("East", this.nomeBancoTextField, -10, "East", instituicaoFinanceiraPanel);
      instituicaoFinanceiraPanel.add(this.nomeBancoTextField);
      JLabel cnpjLabel = new JLabel("CNPJ:");
      instituicaoFinanceiraLayout.putConstraint("North", cnpjLabel, 15, "South", nomeBancoLabel);
      instituicaoFinanceiraLayout.putConstraint("West", cnpjLabel, 0, "West", nomeBancoLabel);
      cnpjLabel.setForeground(this.fontColor);
      instituicaoFinanceiraPanel.add(cnpjLabel);

      try {
         this.cnpjBancoTextField = new JFormattedTextField(new MaskFormatter("**.***.***/####-##"));
         UpperCaseMaskFormatter.apply(this.cnpjBancoTextField);
      } catch (ParseException var25) {
         ViewUtil.showInfoDialog("CNPJ inválido. Use 14 caracteres.");
      }

      instituicaoFinanceiraLayout.putConstraint("North", this.cnpjBancoTextField, -1, "North", cnpjLabel);
      instituicaoFinanceiraLayout.putConstraint("West", this.cnpjBancoTextField, 0, "West", this.nomeBancoTextField);
      instituicaoFinanceiraLayout.putConstraint("East", this.cnpjBancoTextField, 150, "West", this.nomeBancoTextField);
      instituicaoFinanceiraPanel.add(this.cnpjBancoTextField);
      SpringLayout funcionarioPanelLayout = new SpringLayout();
      JPanel funcionarioPanel = new JPanel(funcionarioPanelLayout);
      abaInstituicaoFinanceiraLayout.putConstraint("North", funcionarioPanel, 10, "South", instituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("West", funcionarioPanel, 0, "West", instituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("South", funcionarioPanel, 125, "South", instituicaoFinanceiraPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("East", funcionarioPanel, 0, "East", instituicaoFinanceiraPanel);
      funcionarioPanel.setBackground(Color.WHITE);
      abaInstituicaoFinanceiraPanel.add(funcionarioPanel);
      String tituloFuncionario = "Funcionário responsável pelas informações";
      TitledBorder funcionarioInnerBorder = BorderFactory.createTitledBorder((Border)null, tituloFuncionario, 0, 0, (Font)null, this.fontColor);
      TitledBorder funcionarioOutterBorder = BorderFactory.createTitledBorder(funcionarioInnerBorder, "", 0, 0, (Font)null, this.fontColor);
      funcionarioPanel.setBorder(funcionarioOutterBorder);
      JLabel nomeFuncionarioLabel = new JLabel("Nome:");
      funcionarioPanelLayout.putConstraint("North", nomeFuncionarioLabel, 7, "North", funcionarioPanel);
      funcionarioPanelLayout.putConstraint("West", nomeFuncionarioLabel, 5, "West", funcionarioPanel);
      nomeFuncionarioLabel.setForeground(this.fontColor);
      funcionarioPanel.add(nomeFuncionarioLabel);
      this.nomeFuncionarioTextField = new JTextField();
      funcionarioPanelLayout.putConstraint("North", this.nomeFuncionarioTextField, -1, "North", nomeFuncionarioLabel);
      funcionarioPanelLayout.putConstraint("West", this.nomeFuncionarioTextField, 60, "West", funcionarioPanel);
      funcionarioPanelLayout.putConstraint("East", this.nomeFuncionarioTextField, -5, "East", funcionarioPanel);
      funcionarioPanel.add(this.nomeFuncionarioTextField);
      JLabel telefoneFuncionarioLabel = new JLabel("Telefone:");
      funcionarioPanelLayout.putConstraint("North", telefoneFuncionarioLabel, 15, "South", nomeFuncionarioLabel);
      funcionarioPanelLayout.putConstraint("West", telefoneFuncionarioLabel, 0, "West", nomeFuncionarioLabel);
      telefoneFuncionarioLabel.setForeground(this.fontColor);
      funcionarioPanel.add(telefoneFuncionarioLabel);
      this.telefoneFuncionarioTextField = new JTextField();
      funcionarioPanelLayout.putConstraint("North", this.telefoneFuncionarioTextField, -1, "North", telefoneFuncionarioLabel);
      funcionarioPanelLayout.putConstraint("West", this.telefoneFuncionarioTextField, 0, "West", this.nomeFuncionarioTextField);
      funcionarioPanelLayout.putConstraint("East", this.telefoneFuncionarioTextField, 150, "West", this.nomeFuncionarioTextField);
      funcionarioPanel.add(this.telefoneFuncionarioTextField);
      JLabel emailFuncionarioLabel = new JLabel("Email:");
      funcionarioPanelLayout.putConstraint("North", emailFuncionarioLabel, 15, "South", telefoneFuncionarioLabel);
      funcionarioPanelLayout.putConstraint("West", emailFuncionarioLabel, 0, "West", telefoneFuncionarioLabel);
      emailFuncionarioLabel.setForeground(this.fontColor);
      funcionarioPanel.add(emailFuncionarioLabel);
      this.emailFuncionarioTextField = new JTextField();
      funcionarioPanelLayout.putConstraint("North", this.emailFuncionarioTextField, -1, "North", emailFuncionarioLabel);
      funcionarioPanelLayout.putConstraint("West", this.emailFuncionarioTextField, 0, "West", this.telefoneFuncionarioTextField);
      funcionarioPanelLayout.putConstraint("East", this.emailFuncionarioTextField, 0, "East", this.nomeFuncionarioTextField);
      funcionarioPanel.add(this.emailFuncionarioTextField);
      SpringLayout instituicaoFinanceiraAtendimentoLayout = new SpringLayout();
      JPanel instituicaoFinanceiraAtendimentoPanel = new JPanel(instituicaoFinanceiraAtendimentoLayout);
      abaInstituicaoFinanceiraLayout.putConstraint("North", instituicaoFinanceiraAtendimentoPanel, 10, "South", funcionarioPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("West", instituicaoFinanceiraAtendimentoPanel, 0, "West", funcionarioPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("South", instituicaoFinanceiraAtendimentoPanel, 90, "South", funcionarioPanel);
      abaInstituicaoFinanceiraLayout.putConstraint("East", instituicaoFinanceiraAtendimentoPanel, 0, "East", funcionarioPanel);
      instituicaoFinanceiraAtendimentoPanel.setBackground(Color.WHITE);
      abaInstituicaoFinanceiraPanel.add(instituicaoFinanceiraAtendimentoPanel);
      String tituloPainelInstituicaoFinanceira = "Instituição Financeira que atende o pedido";
      TitledBorder borderPainelInstituicaoFinanceira = BorderFactory.createTitledBorder((Border)null, tituloPainelInstituicaoFinanceira, 0, 0, (Font)null, this.fontColor);
      instituicaoFinanceiraAtendimentoPanel.setBorder(borderPainelInstituicaoFinanceira);
      JLabel nomeBancoAtendimentoLabel = new JLabel();
      instituicaoFinanceiraAtendimentoLayout.putConstraint("North", nomeBancoAtendimentoLabel, 7, "North", instituicaoFinanceiraAtendimentoPanel);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("West", nomeBancoAtendimentoLabel, 5, "West", instituicaoFinanceiraAtendimentoPanel);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("East", nomeBancoAtendimentoLabel, 50, "West", instituicaoFinanceiraAtendimentoPanel);
      nomeBancoAtendimentoLabel.setText("Nome:");
      nomeBancoAtendimentoLabel.setForeground(this.fontColor);
      instituicaoFinanceiraAtendimentoPanel.add(nomeBancoAtendimentoLabel);
      this.nomeBancoAtendimentoTextField = new JTextField();
      instituicaoFinanceiraAtendimentoLayout.putConstraint("North", this.nomeBancoAtendimentoTextField, 5, "North", instituicaoFinanceiraAtendimentoPanel);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("West", this.nomeBancoAtendimentoTextField, 50, "West", instituicaoFinanceiraAtendimentoPanel);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("East", this.nomeBancoAtendimentoTextField, -10, "East", instituicaoFinanceiraAtendimentoPanel);
      this.nomeBancoAtendimentoTextField.setToolTipText("Nome da instituição que atende o pedido, se não for responsável pelas contas. (tamanho máximo: 200)");
      this.nomeBancoAtendimentoTextField.setName("nome_banco_atendimento");
      instituicaoFinanceiraAtendimentoPanel.add(this.nomeBancoAtendimentoTextField);
      JLabel alertaNomeBancoAtendimentoLabel = new JLabel();
      instituicaoFinanceiraAtendimentoLayout.putConstraint("North", alertaNomeBancoAtendimentoLabel, 3, "South", this.nomeBancoAtendimentoTextField);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("West", alertaNomeBancoAtendimentoLabel, 0, "West", this.nomeBancoAtendimentoTextField);
      instituicaoFinanceiraAtendimentoLayout.putConstraint("East", alertaNomeBancoAtendimentoLabel, 0, "East", this.nomeBancoAtendimentoTextField);
      alertaNomeBancoAtendimentoLabel.setText("<html><p>Preencha o campo acima somente quando a instituição que está atendendo a solicitação for diferente do Banco Responsável pelas contas.</p></html>");
      alertaNomeBancoAtendimentoLabel.setForeground(Color.RED);
      instituicaoFinanceiraAtendimentoPanel.add(alertaNomeBancoAtendimentoLabel);
   }

   private JPanel abaProcessoInqueritoPanel() {
      SpringLayout abaProcessoInqueritoLayout = new SpringLayout();
      JPanel abaProcessoInqueritoPanel = new JPanel(abaProcessoInqueritoLayout);
      abaProcessoInqueritoPanel.setBackground(Color.white);
      this.abasTabbedPane.addTab("Processo/Inquérito", abaProcessoInqueritoPanel);
      JLabel numeroCorreioBacenLabel = new JLabel("Nr Correio Bacen / Req. Sisbajud:");
      abaProcessoInqueritoLayout.putConstraint("North", numeroCorreioBacenLabel, 15, "North", abaProcessoInqueritoPanel);
      abaProcessoInqueritoLayout.putConstraint("West", numeroCorreioBacenLabel, 10, "West", abaProcessoInqueritoPanel);
      numeroCorreioBacenLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(numeroCorreioBacenLabel);
      this.numeroCorreioBacenTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.numeroCorreioBacenTextField, -3, "North", numeroCorreioBacenLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.numeroCorreioBacenTextField, 25, "East", numeroCorreioBacenLabel);
      abaProcessoInqueritoLayout.putConstraint("East", this.numeroCorreioBacenTextField, 110, "East", numeroCorreioBacenLabel);
      this.numeroCorreioBacenTextField.setToolTipText("Número do correio eletrônico enviado pelo BACEN ao banco responsável pela(s) conta(s).(tamanho máximo: 20)");
      abaProcessoInqueritoPanel.add(this.numeroCorreioBacenTextField);
      JLabel numeroOficioJudicialLabel = new JLabel("Nr. Ofício Judicial:");
      abaProcessoInqueritoLayout.putConstraint("North", numeroOficioJudicialLabel, 0, "North", numeroCorreioBacenLabel);
      abaProcessoInqueritoLayout.putConstraint("West", numeroOficioJudicialLabel, 15, "East", this.numeroCorreioBacenTextField);
      numeroOficioJudicialLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(numeroOficioJudicialLabel);
      this.numeroOficioTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.numeroOficioTextField, -3, "North", numeroOficioJudicialLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.numeroOficioTextField, 15, "East", numeroOficioJudicialLabel);
      abaProcessoInqueritoLayout.putConstraint("East", this.numeroOficioTextField, 95, "East", numeroOficioJudicialLabel);
      this.numeroOficioTextField.setToolTipText("Número do ofício cujo autor é a autoridade judicial (Magistrado) que autoriza o afastamento do sigilo bancário.(tamanho máximo: 20)");
      abaProcessoInqueritoPanel.add(this.numeroOficioTextField);
      JLabel dataOficioJudicialLabel = new JLabel("Data:");
      abaProcessoInqueritoLayout.putConstraint("North", dataOficioJudicialLabel, 0, "North", numeroOficioJudicialLabel);
      abaProcessoInqueritoLayout.putConstraint("West", dataOficioJudicialLabel, 15, "East", this.numeroOficioTextField);
      dataOficioJudicialLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(dataOficioJudicialLabel);

      try {
         this.dataOficioTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
         abaProcessoInqueritoLayout.putConstraint("North", this.dataOficioTextField, -3, "North", dataOficioJudicialLabel);
         abaProcessoInqueritoLayout.putConstraint("West", this.dataOficioTextField, 15, "East", dataOficioJudicialLabel);
         abaProcessoInqueritoLayout.putConstraint("East", this.dataOficioTextField, -10, "East", abaProcessoInqueritoPanel);
      } catch (ParseException var17) {
         ViewUtil.showInfoDialog("Formato Inválido. Use dd/mm/aaaa");
      }

      abaProcessoInqueritoPanel.add(this.dataOficioTextField);
      JLabel numeroProcessoLabel = new JLabel("Nr Processo / IPL:");
      abaProcessoInqueritoLayout.putConstraint("North", numeroProcessoLabel, 17, "South", numeroCorreioBacenLabel);
      abaProcessoInqueritoLayout.putConstraint("West", numeroProcessoLabel, 0, "West", numeroCorreioBacenLabel);
      numeroProcessoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(numeroProcessoLabel);
      this.numeroProcessoTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.numeroProcessoTextField, -3, "North", numeroProcessoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.numeroProcessoTextField, 0, "West", this.numeroCorreioBacenTextField);
      abaProcessoInqueritoLayout.putConstraint("East", this.numeroProcessoTextField, 0, "East", numeroOficioJudicialLabel);
      this.numeroProcessoTextField.setToolTipText("Número do processo informado no ofício da autoridiade judicial. Informar letras e números (tamanho máximo: 100)");
      abaProcessoInqueritoPanel.add(this.numeroProcessoTextField);
      JLabel numeroVaraLabel = new JLabel("Número da Vara:");
      abaProcessoInqueritoLayout.putConstraint("North", numeroVaraLabel, 0, "North", numeroProcessoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", numeroVaraLabel, 15, "East", this.numeroProcessoTextField);
      numeroVaraLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(numeroVaraLabel);
      this.numeroVaraTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.numeroVaraTextField, -3, "North", numeroVaraLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.numeroVaraTextField, 15, "East", numeroVaraLabel);
      abaProcessoInqueritoLayout.putConstraint("East", this.numeroVaraTextField, -10, "East", abaProcessoInqueritoPanel);
      this.numeroVaraTextField.setToolTipText("Número da Vara informado no ofício da autoridiade judicial. (tamanho máximo: 10)");
      abaProcessoInqueritoPanel.add(this.numeroVaraTextField);
      JLabel nomeTribunalLabel = new JLabel("Nome do Tribunal:");
      abaProcessoInqueritoLayout.putConstraint("North", nomeTribunalLabel, 17, "South", numeroProcessoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", nomeTribunalLabel, 0, "West", numeroProcessoLabel);
      nomeTribunalLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(nomeTribunalLabel);
      this.nomeTribunalTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.nomeTribunalTextField, -3, "North", nomeTribunalLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.nomeTribunalTextField, 0, "West", this.numeroProcessoTextField);
      abaProcessoInqueritoLayout.putConstraint("East", this.nomeTribunalTextField, -10, "East", abaProcessoInqueritoPanel);
      this.nomeTribunalTextField.setToolTipText("Nome do Tribunal informado no ofício da autoridiade judicial. (tamanho máximo: 80)");
      abaProcessoInqueritoPanel.add(this.nomeTribunalTextField);
      JLabel nomeMagistradoLabel = new JLabel("Nome do Magistrado:");
      abaProcessoInqueritoLayout.putConstraint("North", nomeMagistradoLabel, 17, "South", nomeTribunalLabel);
      abaProcessoInqueritoLayout.putConstraint("West", nomeMagistradoLabel, 0, "West", nomeTribunalLabel);
      nomeMagistradoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(nomeMagistradoLabel);
      this.nomeMagistradoTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.nomeMagistradoTextField, -3, "North", nomeMagistradoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.nomeMagistradoTextField, 0, "West", this.nomeTribunalTextField);
      abaProcessoInqueritoLayout.putConstraint("East", this.nomeMagistradoTextField, -10, "East", abaProcessoInqueritoPanel);
      this.nomeMagistradoTextField.setToolTipText("Nome Completo da autoridiade judicial. (tamanho máximo: 80)");
      abaProcessoInqueritoPanel.add(this.nomeMagistradoTextField);
      JLabel cargoMagistradoLabel = new JLabel("Cargo do Magistrado:");
      abaProcessoInqueritoLayout.putConstraint("North", cargoMagistradoLabel, 17, "South", nomeMagistradoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", cargoMagistradoLabel, 0, "West", nomeMagistradoLabel);
      cargoMagistradoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(cargoMagistradoLabel);
      this.cargoMagistradoTextField = new JTextField();
      abaProcessoInqueritoLayout.putConstraint("North", this.cargoMagistradoTextField, -3, "North", cargoMagistradoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", this.cargoMagistradoTextField, 0, "West", this.nomeMagistradoTextField);
      abaProcessoInqueritoLayout.putConstraint("East", this.cargoMagistradoTextField, -10, "East", abaProcessoInqueritoPanel);
      this.cargoMagistradoTextField.setToolTipText("Nome do Cargo da autoridiade judicial. (tamanho máximo: 80)");
      abaProcessoInqueritoPanel.add(this.cargoMagistradoTextField);
      JLabel dataInicioAfastamentoLabel = new JLabel("Período de afastamento de sigilo.  Início:");
      abaProcessoInqueritoLayout.putConstraint("North", dataInicioAfastamentoLabel, 17, "South", cargoMagistradoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", dataInicioAfastamentoLabel, 0, "West", cargoMagistradoLabel);
      dataInicioAfastamentoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(dataInicioAfastamentoLabel);

      try {
         this.dataInicioAfastamentoTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
         abaProcessoInqueritoLayout.putConstraint("North", this.dataInicioAfastamentoTextField, -3, "North", dataInicioAfastamentoLabel);
         abaProcessoInqueritoLayout.putConstraint("West", this.dataInicioAfastamentoTextField, 15, "East", dataInicioAfastamentoLabel);
         abaProcessoInqueritoLayout.putConstraint("East", this.dataInicioAfastamentoTextField, 105, "East", dataInicioAfastamentoLabel);
      } catch (ParseException var16) {
         ViewUtil.showInfoDialog("Formato Inválido. Use dd/mm/aaaa");
      }

      this.dataInicioAfastamentoTextField.setToolTipText("Data inicial de afastamento de sigilo conforme ofício da autoridiade judicial.");
      abaProcessoInqueritoPanel.add(this.dataInicioAfastamentoTextField);
      JLabel dataFimAfastamentoLabel = new JLabel("Fim:");
      abaProcessoInqueritoLayout.putConstraint("North", dataFimAfastamentoLabel, 0, "North", dataInicioAfastamentoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", dataFimAfastamentoLabel, 15, "East", this.dataInicioAfastamentoTextField);
      dataFimAfastamentoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(dataFimAfastamentoLabel);

      try {
         this.dataFimAfastamentoTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
         abaProcessoInqueritoLayout.putConstraint("North", this.dataFimAfastamentoTextField, -3, "North", dataFimAfastamentoLabel);
         abaProcessoInqueritoLayout.putConstraint("West", this.dataFimAfastamentoTextField, 15, "East", dataFimAfastamentoLabel);
         abaProcessoInqueritoLayout.putConstraint("East", this.dataFimAfastamentoTextField, 105, "East", dataFimAfastamentoLabel);
      } catch (ParseException var15) {
         ViewUtil.showInfoDialog("Formato Inválido. Use dd/mm/aaaa");
      }

      this.dataFimAfastamentoTextField.setToolTipText("Data final de afastamento de sigilo conforme ofício da autoridiade judicial.");
      abaProcessoInqueritoPanel.add(this.dataFimAfastamentoTextField);
      JLabel descricaoAtendimentoLabel = new JLabel("<html>Descrição do <br>atendimento:</html>");
      abaProcessoInqueritoLayout.putConstraint("North", descricaoAtendimentoLabel, 17, "South", dataInicioAfastamentoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", descricaoAtendimentoLabel, 0, "West", dataInicioAfastamentoLabel);
      descricaoAtendimentoLabel.setForeground(this.fontColor);
      abaProcessoInqueritoPanel.add(descricaoAtendimentoLabel);
      this.descricaoAtendimentoTextArea = new JTextArea();
      this.descricaoAtendimentoTextArea.setColumns(20);
      this.descricaoAtendimentoTextArea.setRows(10);
      this.descricaoAtendimentoTextArea.setToolTipText("Campo livre para descrição de algum esclarecimento, caso seja necessário.");
      this.descricaoAtendimentoTextArea.setMaximumSize(new Dimension(60, 60));
      JScrollPane descricaoAtendimentoScroll = new JScrollPane();
      abaProcessoInqueritoLayout.putConstraint("North", descricaoAtendimentoScroll, -3, "North", descricaoAtendimentoLabel);
      abaProcessoInqueritoLayout.putConstraint("West", descricaoAtendimentoScroll, 15, "East", descricaoAtendimentoLabel);
      abaProcessoInqueritoLayout.putConstraint("South", descricaoAtendimentoScroll, -10, "South", abaProcessoInqueritoPanel);
      abaProcessoInqueritoLayout.putConstraint("East", descricaoAtendimentoScroll, -10, "East", abaProcessoInqueritoPanel);
      descricaoAtendimentoScroll.setViewportView(this.descricaoAtendimentoTextArea);
      abaProcessoInqueritoPanel.add(descricaoAtendimentoScroll);
      return abaProcessoInqueritoPanel;
   }

   private JPanel abaInvestigadosPanel() {
      SpringLayout abaInvestigadosLayout = new SpringLayout();
      JPanel abaInvestigadosPanel = new JPanel(abaInvestigadosLayout);
      abaInvestigadosPanel.setBackground(Color.WHITE);
      this.abasTabbedPane.addTab("Investigados", abaInvestigadosPanel);
      this.investigadosTable = new JTable();
      JScrollPane investigadoTableScroll = new JScrollPane();
      abaInvestigadosLayout.putConstraint("North", investigadoTableScroll, 0, "North", abaInvestigadosPanel);
      abaInvestigadosLayout.putConstraint("West", investigadoTableScroll, 0, "West", abaInvestigadosPanel);
      abaInvestigadosLayout.putConstraint("East", investigadoTableScroll, 0, "East", abaInvestigadosPanel);
      investigadoTableScroll.setViewportView(this.investigadosTable);
      this.investigadosTable.setModel(new DefaultTableModel(new Object[0][], new String[]{"Tipo", "CPF_CNPJ", "Nome", "Relac", "Conta", "B/D/V", "Observacao", "Inicio", "Fim"}) {
         private static final long serialVersionUID = 1L;

         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public Class getColumnClass(int columnIndex) {
            return String.class;
         }

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
         }
      });
      this.investigadosTable.setSelectionMode(0);
      this.investigadosTable.getTableHeader().setReorderingAllowed(false);
      this.investigadosTable.getColumnModel().getColumn(0).setMinWidth(10);
      this.investigadosTable.getColumnModel().getColumn(0).setPreferredWidth(10);
      this.investigadosTable.getColumnModel().getColumn(2).setMinWidth(150);
      this.investigadosTable.getColumnModel().getColumn(2).setPreferredWidth(150);
      abaInvestigadosPanel.add(investigadoTableScroll);
      this.adicionarInvestigadosButton = new JButton("Adicionar");
      abaInvestigadosLayout.putConstraint("West", this.adicionarInvestigadosButton, 10, "West", abaInvestigadosPanel);
      abaInvestigadosLayout.putConstraint("South", this.adicionarInvestigadosButton, -15, "South", abaInvestigadosPanel);
      abaInvestigadosLayout.putConstraint("South", investigadoTableScroll, -10, "North", this.adicionarInvestigadosButton);
      this.adicionarInvestigadosButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmDadosAtendimento.this.adicionarInvestigadoButtonClick();
         }
      });
      abaInvestigadosPanel.add(this.adicionarInvestigadosButton);
      this.removerInvestigadoButton = new JButton("Remover");
      abaInvestigadosLayout.putConstraint("North", this.removerInvestigadoButton, 0, "North", this.adicionarInvestigadosButton);
      abaInvestigadosLayout.putConstraint("West", this.removerInvestigadoButton, 10, "East", this.adicionarInvestigadosButton);
      this.removerInvestigadoButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmDadosAtendimento.this.removerInvestigadoButtonClick();
         }
      });
      abaInvestigadosPanel.add(this.removerInvestigadoButton);
      this.importarInvestigadosButton = new JButton("Importar");
      this.importarInvestigadosButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmDadosAtendimento.this.importarInvestigadosButtonClick();
         }
      });
      abaInvestigadosPanel.add(this.importarInvestigadosButton);
      this.removerTodosInvestigadosButton = new JButton("Remover Todos");
      abaInvestigadosLayout.putConstraint("East", this.removerTodosInvestigadosButton, -10, "East", abaInvestigadosPanel);
      abaInvestigadosLayout.putConstraint("North", this.importarInvestigadosButton, 0, "North", this.removerTodosInvestigadosButton);
      abaInvestigadosLayout.putConstraint("East", this.importarInvestigadosButton, -10, "West", this.removerTodosInvestigadosButton);
      abaInvestigadosLayout.putConstraint("South", this.removerTodosInvestigadosButton, -15, "South", abaInvestigadosPanel);
      this.removerTodosInvestigadosButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmDadosAtendimento.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmDadosAtendimento.this.removerTodosInvestigadosButtonClick();
         }
      });
      abaInvestigadosPanel.add(this.removerTodosInvestigadosButton);
      return abaInvestigadosPanel;
   }

   private void gravarButtonClick() {
      if (this.gravarButton.isEnabled()) {
         String sRetorno = this.validaCamposObrigatorios();
         if (StringUtil.isNotEmpty(sRetorno)) {
            ViewUtil.showErrorDialog(sRetorno);
         } else {
            Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
            if (!emailPattern.matcher(this.emailFuncionarioTextField.getText().trim()).matches()) {
               ViewUtil.showErrorDialog("O Email do Funcionário Responsável é inválido. Por favor, insira um email válido.");
            } else if (ViewUtil.showSimNaoDialog("Confirma gravação?", "Atencão")) {
               if (this.context.getPastaCaso().exists()) {
                  String numeroCaso = this.context.getNumeroCaso();
                  File pastaCaso = this.context.getPastaCaso();
                  this.context.setNumeroCaso((String)null);
                  if (!this.controlador.apagarCaso(pastaCaso)) {
                     ViewUtil.showErrorDialog("Ocorreu um erro ao gravar o dados do caso. Exclua o atendimento e crie-o novamente.");
                     return;
                  }

                  this.context.setNumeroCaso(numeroCaso);
               }

               if (!this.controlador.criarHSQLDBAtendimento()) {
                  ViewUtil.showErrorDialog("Erro ao tentar cadastrar o caso. Favor verificar se usuário tem permissão de escrita no diretório ou se o caso já está cadastrado");
               } else {
                  Atendimento atendimento = new Atendimento();
                  atendimento.setNumeroCaso(this.numeroCaso);
                  this.formToAtendimento(atendimento);
                  this.insertAtendimentoCmd.inserir(atendimento);
                  this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();
                  Map<CamposInvestigado, String> camposInvestigados = new HashMap();
                  String valor = null;

                  for(int linha = 0; linha < this.investigadoTableModel.getRowCount(); ++linha) {
                     for(int coluna = 0; coluna < this.investigadoTableModel.getColumnCount(); ++coluna) {
                        valor = this.investigadoTableModel.getValueAt(linha, coluna).toString();
                        if (coluna == 0) {
                           valor = valor.equalsIgnoreCase("PF") ? "1" : "2";
                           CamposInvestigado var10001 = CamposInvestigado.TIPO_PESSOA;
                           String var10002 = this.limparTexto(valor);
                           camposInvestigados.put(var10001, "'" + var10002 + "'");
                        } else if (coluna == 1) {
                           valor = Util.tiraFormato(valor).toUpperCase();
                           CamposInvestigado var20 = CamposInvestigado.CPF_CNPJ_PESSOA;
                           String var28 = this.limparTexto(valor);
                           camposInvestigados.put(var20, "'" + var28 + "'");
                        } else if (coluna == 2) {
                           CamposInvestigado var21 = CamposInvestigado.NOME_PESSOA;
                           String var29 = this.limparTexto(valor);
                           camposInvestigados.put(var21, "'" + var29 + "'");
                        } else if (coluna == 3) {
                           valor = valor.equalsIgnoreCase("Não") ? "0" : "1";
                           CamposInvestigado var22 = CamposInvestigado.TEVE_RELACIONAMENTO;
                           String var30 = this.limparTexto(valor);
                           camposInvestigados.put(var22, "'" + var30 + "'");
                        } else if (coluna == 4) {
                           valor = valor.equalsIgnoreCase("Não") ? "0" : "1";
                           CamposInvestigado var23 = CamposInvestigado.TEVE_CONTA;
                           String var31 = this.limparTexto(valor);
                           camposInvestigados.put(var23, "'" + var31 + "'");
                        } else if (coluna == 5) {
                           valor = valor.equalsIgnoreCase("Não") ? "0" : "1";
                           CamposInvestigado var24 = CamposInvestigado.TEVE_BDV;
                           String var32 = this.limparTexto(valor);
                           camposInvestigados.put(var24, "'" + var32 + "'");
                        } else if (coluna == 6) {
                           CamposInvestigado var25 = CamposInvestigado.OBSERVACAO;
                           String var33 = this.limparTexto(valor);
                           camposInvestigados.put(var25, "'" + var33 + "'");
                        } else if (coluna == 7) {
                           if (valor.equalsIgnoreCase("  /  /    ")) {
                              valor = this.dataInicioAfastamentoTextField.getText();
                           }

                           String var10000 = valor.substring(6, 10);
                           valor = var10000 + "-" + valor.substring(3, 5) + "-" + valor.substring(0, 2);
                           CamposInvestigado var26 = CamposInvestigado.DATA_INICIO_AFASTAMENTO;
                           String var34 = this.limparTexto(valor);
                           camposInvestigados.put(var26, "'" + var34 + "'");
                        } else if (coluna == 8) {
                           if (valor.equalsIgnoreCase("  /  /    ")) {
                              valor = this.dataFimAfastamentoTextField.getText();
                           }

                           String var19 = valor.substring(6, 10);
                           valor = var19 + "-" + valor.substring(3, 5) + "-" + valor.substring(0, 2);
                           CamposInvestigado var27 = CamposInvestigado.DATA_FIM_AFASTAMENTO;
                           String var35 = this.limparTexto(valor);
                           camposInvestigados.put(var27, "'" + var35 + "'");
                        }
                     }

                     boolean cadastrouCaso = this.controlador.preencheInvestigado(camposInvestigados);
                     if (!cadastrouCaso) {
                        this.controlador.apagarCaso((File)null);
                        ViewUtil.showErrorDialog("Erro ao gravar dados do caso.");
                        return;
                     }
                  }

                  ViewUtil.showInfoDialog("Dados do caso gravados com sucesso.");
                  this.cancelarButtonClick();
               }
            }
         }
      }
   }

   private void cancelarButtonClick() {
      this.frmIdentificaAtendimento.mostrar();
      this.setVisible(false);
      this.dispose();
   }

   private void adicionarInvestigadoButtonClick() {
      if (this.adicionarInvestigadosButton.isEnabled()) {
         this.setEnabled(false);
         this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();
         int rowCount = this.investigadoTableModel.getRowCount();
         List<String> cpfCnpjCadastrados = new ArrayList(rowCount);

         for(int i = 0; i < rowCount; ++i) {
            cpfCnpjCadastrados.add(this.investigadoTableModel.getValueAt(i, 1).toString());
         }

         this.formInvestigado = new frmInvestigado(this);
         this.formInvestigado.setCpfCnpjCadastrados(cpfCnpjCadastrados);
         this.formInvestigado.setVisible(true);
      }
   }

   private void removerInvestigadoButtonClick() {
      if (this.removerInvestigadoButton.isEnabled()) {
         int linhaSelecionada = this.investigadosTable.getSelectedRow();
         if (linhaSelecionada >= 0) {
            this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();
            this.investigadoTableModel.removeRow(linhaSelecionada);
         }
      }
   }

   private void importarInvestigadosButtonClick() {
      JFileChooser fc = new JFileChooser("");
      String propertyPastaImportarInvestigados = "pasta.importarInvestigados";
      String pastaInicial = this.config.get(propertyPastaImportarInvestigados);
      if (!StringUtil.isEmpty(pastaInicial)) {
         File pastaImportarInvestigados = new File(pastaInicial);
         if (pastaImportarInvestigados.exists()) {
            fc.setCurrentDirectory(pastaImportarInvestigados);
         }
      }

      fc.setFileSelectionMode(0);
      fc.setDialogTitle("Selecione o arquivo de investigados");
      if (fc.showDialog((java.awt.Component)null, "Abrir") == 0) {
         String arquivoInvestigadoPath = fc.getSelectedFile().getAbsolutePath();
         String nomeEsperado = (this.numeroCaso + "_INVESTIGADO.txt").toUpperCase();
         if (!arquivoInvestigadoPath.toUpperCase().contains(nomeEsperado)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Arquivo esperado (");
            msg.append((this.numeroCaso + "_INVESTIGADO.txt").toUpperCase());
            msg.append(") não encontrado.");
            ViewUtil.showWarnDialog(msg.toString());
         } else {
            boolean funcionou = this.carregarArquivoInvestigados(arquivoInvestigadoPath);
            if (!funcionou) {
               this.removerTodosInvestigadosButtonClick();
            }

            this.config.set(propertyPastaImportarInvestigados, arquivoInvestigadoPath);
         }
      }
   }

   private void removerTodosInvestigadosButtonClick() {
      try {
         this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();

         while(this.investigadoTableModel.getRowCount() != 0) {
            this.investigadoTableModel.removeRow(0);
         }
      } catch (Exception e) {
         log.error("Erro ao remover todos os investigados.", e);
         ViewUtil.showErrorDialog("Erro ao remover todos os investigados.");
      }

   }

   private boolean carregarInvestigados() {
      List<Map<CamposInvestigado, String>> camposInvestigados = this.controlador.getInvestigado();
      if (camposInvestigados.isEmpty()) {
         log.error("Investigados não foram encontrados");
         return false;
      } else {
         this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();

         for(Map<CamposInvestigado, String> camposMap : camposInvestigados) {
            List<String> camposList = new ArrayList();
            if (((String)camposMap.get(CamposInvestigado.TIPO_PESSOA)).equalsIgnoreCase("1")) {
               camposList.add(0, "PF");
            } else {
               camposList.add(0, "PJ");
            }

            camposList.add(1, (String)camposMap.get(CamposInvestigado.CPF_CNPJ_PESSOA));
            camposList.add(2, (String)camposMap.get(CamposInvestigado.NOME_PESSOA));
            if (((String)camposMap.get(CamposInvestigado.TEVE_RELACIONAMENTO)).equalsIgnoreCase("0")) {
               camposList.add(3, "Não");
            } else {
               camposList.add(3, "Sim");
            }

            if (((String)camposMap.get(CamposInvestigado.TEVE_CONTA)).equalsIgnoreCase("0")) {
               camposList.add(4, "Não");
            } else {
               camposList.add(4, "Sim");
            }

            if (((String)camposMap.get(CamposInvestigado.TEVE_BDV)).equalsIgnoreCase("0")) {
               camposList.add(5, "Não");
            } else {
               camposList.add(5, "Sim");
            }

            camposList.add(6, (String)camposMap.get(CamposInvestigado.OBSERVACAO));
            String dataInicio = (String)camposMap.get(CamposInvestigado.DATA_INICIO_AFASTAMENTO);
            if (dataInicio != null) {
               String var10000 = dataInicio.substring(8, 10);
               dataInicio = var10000 + "/" + dataInicio.substring(5, 7) + "/" + dataInicio.substring(0, 4);
               if (dataInicio.equalsIgnoreCase(this.dataInicioAfastamentoTextField.getText())) {
                  dataInicio = "  /  /    ";
               }

               camposList.add(7, dataInicio);
            }

            String dataFim = (String)camposMap.get(CamposInvestigado.DATA_FIM_AFASTAMENTO);
            if (dataFim != null) {
               String var9 = dataFim.substring(8, 10);
               dataFim = var9 + "/" + dataFim.substring(5, 7) + "/" + dataFim.substring(0, 4);
               if (dataFim.equalsIgnoreCase(this.dataFimAfastamentoTextField.getText())) {
                  dataFim = "  /  /    ";
               }

               camposList.add(8, dataFim);
            }

            this.investigadoTableModel.addRow(camposList.toArray());
         }

         return true;
      }
   }

   private boolean carregarArquivoInvestigados(String strPath) {
      List<String[]> sRetInv = this.getInvestigadosArq(strPath);
      HashSet<String> cpfs = new HashSet();
      if (sRetInv.isEmpty()) {
         ViewUtil.showErrorDialog("Não foi possível obter dados de investigados.");
         return false;
      } else {
         this.investigadoTableModel = (DefaultTableModel)this.investigadosTable.getModel();

         for(int i = 0; i < sRetInv.size(); ++i) {
            String[] sCamposInv = (String[])sRetInv.get(i);
            String tipoPessoa = sCamposInv[0];
            String cpfCnpj = sCamposInv[1];
            String nomePessoa = sCamposInv[2];
            String teveRelacionamento = sCamposInv[3];
            String teveConta = sCamposInv[4];
            String teveBdv = sCamposInv[5];
            String dataInicio = sCamposInv[7];
            String dataFim = sCamposInv[8];
            int numeroLinha = i + 1;
            if (StringUtil.isEmpty(cpfCnpj)) {
               ViewUtil.showErrorDialog("CPF/CNPJ não informado. Linha " + numeroLinha + ".");
               return false;
            }

            cpfCnpj = cpfCnpj.trim();
            if (!Util.valida_CpfCnpj(cpfCnpj)) {
               ViewUtil.showErrorDialog("CPF/CNPJ inválido. Linha " + numeroLinha + ".");
               return false;
            }

            if (cpfs.contains(cpfCnpj)) {
               ViewUtil.showErrorDialog("Este CPF/CNPJ já foi cadastrado. Linha " + numeroLinha + ".");
               return false;
            }

            cpfs.add(cpfCnpj);
            if (StringUtil.isEmpty(tipoPessoa)) {
               ViewUtil.showErrorDialog("Tipo da Pessoa não informado. Linha " + numeroLinha + ".");
               return false;
            }

            if (StringUtil.isEmpty(nomePessoa)) {
               ViewUtil.showErrorDialog("Nome da Pessoa não informado. Linha " + numeroLinha + ".");
               return false;
            }

            if (StringUtil.isEmpty(teveRelacionamento)) {
               ViewUtil.showErrorDialog("Campo Teve Relacionamento não informado. Linha " + numeroLinha + ".");
               return false;
            }

            if (StringUtil.isEmpty(teveConta)) {
               ViewUtil.showErrorDialog("Campo Tipo Relacionamento Conta Depósito não informado. Linha " + numeroLinha + ".");
               return false;
            }

            if (StringUtil.isEmpty(teveBdv)) {
               ViewUtil.showErrorDialog("Campo Tipo Relacionamento \"B/D/V\" não informado. Linha " + numeroLinha + ".");
               return false;
            }

            if ("1".equals(teveRelacionamento) && "0".equals(teveConta) && "0".equals(teveBdv)) {
               ViewUtil.showErrorDialog("Para o campo Teve Relacionamento igual a 1, pelo menos um dos Tipo Relacionamento devem ser igual a 1 também. Linha " + numeroLinha + ".");
               return false;
            }

            if ("1".equals(tipoPessoa)) {
               sCamposInv[0] = "PF";
            } else {
               sCamposInv[0] = "PJ";
            }

            if ("0".equals(teveRelacionamento)) {
               sCamposInv[3] = "Não";
            } else {
               sCamposInv[3] = "Sim";
            }

            if ("0".equals(teveConta)) {
               sCamposInv[4] = "Não";
            } else {
               sCamposInv[4] = "Sim";
            }

            if ("0".equals(teveBdv)) {
               sCamposInv[5] = "Não";
            } else {
               sCamposInv[5] = "Sim";
            }

            String regex = "(\\d{2})(\\d{2})(\\d{4})";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(dataInicio);
            if (matcher.matches()) {
               String dia = matcher.group(1);
               String mes = matcher.group(2);
               String ano = matcher.group(3);
               sCamposInv[7] = dia + "/" + mes + "/" + ano;
            } else {
               sCamposInv[7] = "  /  /    ";
            }

            matcher = pattern.matcher(dataFim);
            if (matcher.matches()) {
               String dia = matcher.group(1);
               String mes = matcher.group(2);
               String ano = matcher.group(3);
               sCamposInv[8] = dia + "/" + mes + "/" + ano;
            } else {
               sCamposInv[8] = "  /  /    ";
            }

            String validaDataInicioEFim = Util.validaDataInicioEFim(sCamposInv[7], sCamposInv[8]);
            if (StringUtil.isNotEmpty(validaDataInicioEFim)) {
               ViewUtil.showErrorDialog(validaDataInicioEFim + " Linha " + numeroLinha + ".");
            }

            this.investigadoTableModel.addRow(sCamposInv);
         }

         return true;
      }
   }

   private List<String[]> getInvestigadosArq(String pathArquivo) {
      int codArquivoB = 7;
      List<String[]> linhas = new ArrayList();

      try {
         Arquivo arquivo = new Arquivo(pathArquivo);
         int numeroDaLinha = 0;
         String[] coluna = null;
         int qtdColunasMapa = Mapa.getCamposTabelasB()[codArquivoB].length - 1;

         while((coluna = arquivo.getLinha("\t")) != null) {
            ++numeroDaLinha;
            if (qtdColunasMapa != coluna.length) {
               StringBuilder msg = new StringBuilder();
               msg.append("Número incorreto de colunas na linha ");
               msg.append(numeroDaLinha);
               msg.append(".<br><br>Esperado: ");
               msg.append(qtdColunasMapa);
               msg.append("<br>Encontrado: ");
               msg.append(coluna.length);
               arquivo.fecha();
               ViewUtil.showErrorDialog(msg.toString());
               return Collections.emptyList();
            }

            linhas.add(coluna);
         }

         arquivo.fecha();
         String msg = "Quantidade de linhas encontradas: " + numeroDaLinha;
         ViewUtil.showInfoDialog(msg);
      } catch (Exception e) {
         log.error("Erro ao ler arquivo de investigados. Arquivo: " + pathArquivo, e);
         ViewUtil.showErrorDialog("Erro ao carregar arquivo de investigado.");
      }

      return linhas;
   }

   private String validaCamposObrigatorios() {
      String sRet = "";
      String numeroBanco = this.numeroBancoTextField.getText().trim();
      if (!numeroBanco.isEmpty()) {
         if (!numeroBanco.matches("\\d{3}")) {
            sRet = sRet + "<br>O Número do Banco deve ter 3 posições.";
         } else if (!numeroBanco.matches("\\d+")) {
            sRet = sRet + "<br>O número da instituição financeira deve conter apenas números.";
         }
      }

      String cnpjInstituicao = this.cnpjBancoTextField.getText().trim().replace(".", "").replace("/", "").replace("-", "");
      if (!cnpjInstituicao.matches("^[0-9A-Z]{14}")) {
         sRet = sRet + "<br>O CNPJ da instituição deve ter 14 posições.";
      } else if (!Util.valida_CpfCnpj(cnpjInstituicao)) {
         sRet = sRet + "<br>O CNPJ da institução está inválido.";
      }

      if (this.nomeBancoTextField.getText().trim().isEmpty()) {
         sRet = sRet + "<br>O Nome da instituição responsável pela conta é obrigatório.";
      }

      if (this.nomeFuncionarioTextField.getText().trim().isEmpty()) {
         sRet = sRet + "<br>O Nome do Funcionário Responsável é obrigatório.";
      }

      if (this.telefoneFuncionarioTextField.getText().trim().isEmpty()) {
         sRet = sRet + "<br>O Telefone do Funcionário Responsável é obrigatório.";
      }

      if (this.emailFuncionarioTextField.getText().trim().isEmpty()) {
         sRet = sRet + "<br>O Email do Funcionário Responsável é obrigatório.";
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      String textoDataOficio = this.dataOficioTextField.getText().trim();
      if (!this.context.isEleitoral()) {
         if ("/  /".equals(textoDataOficio)) {
            sRet = sRet + "<br>A Data do Ofício é obrigatória.";
         } else {
            try {
               LocalDate dataOficio = LocalDate.parse(textoDataOficio, formatter);
               if (!this.context.isEleitoral() && Util.dataAnterior1980(dataOficio)) {
                  sRet = sRet + "<br>A data do ofício deve ser maior que 01/01/1980.";
               }
            } catch (Exception var13) {
               sRet = sRet + "<br>A Data do Ofício é inválida.";
            }
         }
      }

      String textoDataInicio = this.dataInicioAfastamentoTextField.getText().trim();
      LocalDate dataInicio = null;
      if ("/  /".equals(textoDataInicio)) {
         sRet = sRet + "<br>A Data de Início do afastamento é obrigatória.";
      } else {
         try {
            dataInicio = LocalDate.parse(textoDataInicio, formatter);
            if (Util.dataAnterior1980(dataInicio)) {
               sRet = sRet + "<br>A data de inicio de afastamento deve ser maior que 01/01/1980.";
            }
         } catch (Exception var12) {
            sRet = sRet + "<br>A Data de Início do afastamento é inválida.";
         }
      }

      String textoDataFim = this.dataFimAfastamentoTextField.getText().trim();
      LocalDate dataFim = null;
      if ("/  /".equals(textoDataFim)) {
         sRet = sRet + "<br>A Data de Fim do afastamento é obrigatória.";
      } else {
         try {
            dataFim = LocalDate.parse(textoDataFim, formatter);
            if (Util.dataAnterior1980(dataFim)) {
               sRet = sRet + "<br>A data fim de afastamento deve ser maior que 01/01/1980.";
            }
         } catch (Exception var11) {
            sRet = sRet + "<br>A Data de Fim do afastamento é inválida.";
         }
      }

      if (Objects.nonNull(dataInicio) && Objects.nonNull(dataFim) && dataFim.isBefore(dataInicio)) {
         sRet = sRet + "<br>A Data de Início do afastamento deve ser menor que a Data de Fim.";
      }

      if (this.nomeBancoAtendimentoTextField.getText().length() > 200) {
         sRet = sRet + "<br>O Nome do Banco que atende o pedido não poder ter mais de 200 caracteres.";
      }

      if (this.nomeBancoTextField.getText().length() > 200) {
         sRet = sRet + "<br>O Nome da instituição Responsável não poder ter mais de 200 caracteres.";
      }

      if (this.nomeFuncionarioTextField.getText().length() > 80) {
         sRet = sRet + "<br>O Nome do Funcionário não poder ter mais de 80 caracteres.";
      }

      if (this.telefoneFuncionarioTextField.getText().length() > 20) {
         sRet = sRet + "<br>O Telefone do Funcionário não poder ter mais de 20 caracteres.";
      }

      if (this.emailFuncionarioTextField.getText().length() > 100) {
         sRet = sRet + "<br>O Email do Funcionário não poder ter mais de 100 caracteres.";
      }

      if (this.numeroCorreioBacenTextField.getText().length() > 20) {
         sRet = sRet + "<br>O Número do Correio BACEN não poder ter mais de 20 caracteres.";
      }

      if (this.numeroOficioTextField.getText().length() > 20) {
         sRet = sRet + "<br>O Número do Ofício não poder ter mais de 20 caracteres.";
      }

      if (this.numeroProcessoTextField.getText().length() > 100) {
         sRet = sRet + "<br>O Número do Processo não poder ter mais de 100 caracteres.";
      }

      if (this.nomeTribunalTextField.getText().length() > 80) {
         sRet = sRet + "<br>O Nome do Tribunal não poder ter mais de 80 caracteres.";
      }

      if (this.numeroVaraTextField.getText().length() > 10) {
         sRet = sRet + "<br>O Número da Vara não poder ter mais de 10 caracteres.";
      }

      if (this.nomeMagistradoTextField.getText().length() > 80) {
         sRet = sRet + "<br>O Nome do Magistrado não poder ter mais de 80 caracteres.";
      }

      if (this.cargoMagistradoTextField.getText().length() > 80) {
         sRet = sRet + "<br>O Cargo do Magistrado não poder ter mais de 80 caracteres.";
      }

      if (this.descricaoAtendimentoTextArea.getText().length() > 500) {
         sRet = sRet + "<br>A Descrição do conteúdo não poder ter mais de 500 caracteres.";
      }

      if (this.investigadosTable.getRowCount() == 0) {
         sRet = sRet + "<br>Não há investigados cadastrados.";
      }

      String numeroCasoBetween = this.context.getNumeroCaso().split("-")[2];
      if (numeroCasoBetween.length() != 6) {
         sRet = sRet + "<br>Número do caso incorreto: " + this.context.getNumeroCaso();
         sRet = sRet + ". Entre o nome do órgão e o dígito verificador deve conter 6 posições";
      }

      if (sRet.length() > 4) {
         sRet = sRet.substring(4);
      }

      return sRet;
   }

   private void formToAtendimento(Atendimento atendimento) {
      String numeroBanco = this.numeroBancoTextField.getText().trim();
      if (!numeroBanco.isEmpty()) {
         atendimento.setNumeroBanco(Integer.valueOf(numeroBanco));
      }

      atendimento.setCnpj(CpfCnpjUtil.removeFormatacao(this.cnpjBancoTextField.getText().trim()).toUpperCase());
      atendimento.setNomeBanco(this.limparTexto(this.nomeBancoTextField.getText().trim()));
      atendimento.setNomeFuncionarioResponsavel(this.limparTexto(this.nomeFuncionarioTextField.getText().trim()));
      atendimento.setTelefoneFuncionarioResponsavel(this.telefoneFuncionarioTextField.getText().trim());
      atendimento.setEmailFuncionarioResponsavel(this.limparTexto(this.emailFuncionarioTextField.getText().trim()));
      atendimento.setNomeBancoAtendimento(this.limparTexto(this.nomeBancoAtendimentoTextField.getText().trim()));
      atendimento.setNumeroCorreioBacen(this.numeroCorreioBacenTextField.getText().trim());
      atendimento.setNumeroOficio(this.numeroOficioTextField.getText().trim());
      atendimento.setNumeroProcesso(this.numeroProcessoTextField.getText().trim());
      atendimento.setNomeTribunal(this.limparTexto(this.nomeTribunalTextField.getText().trim()));
      atendimento.setNumeroVara(this.limparTexto(this.numeroVaraTextField.getText().trim()));
      atendimento.setNomeMagistrado(this.limparTexto(this.nomeMagistradoTextField.getText().trim()));
      atendimento.setCargoMagistrado(this.limparTexto(this.cargoMagistradoTextField.getText().trim()));
      atendimento.setDescricaoConteudo(this.limparTexto(this.descricaoAtendimentoTextArea.getText().trim()));
      atendimento.setDataOficio(this.dateFromForm(this.dataOficioTextField));
      atendimento.setDataInicioAfastamento(this.dateFromForm(this.dataInicioAfastamentoTextField));
      atendimento.setDataFimAfastamento(this.dateFromForm(this.dataFimAfastamentoTextField));
   }

   private void atendimentoToForm(Atendimento atendimento) {
      Integer numeroBanco = atendimento.getNumeroBanco();
      if (StringUtil.isNotEmpty(this.numeroCaso)) {
         this.numeroBancoTextField.setText(StringUtil.completarZerosEsquerda(String.valueOf(numeroBanco), 3));
      }

      this.cnpjBancoTextField.setText(atendimento.getCnpj());
      this.nomeBancoTextField.setText(atendimento.getNomeBanco());
      this.nomeFuncionarioTextField.setText(atendimento.getNomeFuncionarioResponsavel());
      this.telefoneFuncionarioTextField.setText(atendimento.getTelefoneFuncionarioResponsavel());
      this.emailFuncionarioTextField.setText(atendimento.getEmailFuncionarioResponsavel());
      this.nomeBancoAtendimentoTextField.setText(atendimento.getNomeBancoAtendimento());
      this.numeroCorreioBacenTextField.setText(atendimento.getNumeroCorreioBacen());
      this.numeroOficioTextField.setText(atendimento.getNumeroOficio());
      this.numeroProcessoTextField.setText(atendimento.getNumeroProcesso());
      this.nomeTribunalTextField.setText(atendimento.getNomeTribunal());
      this.numeroVaraTextField.setText(atendimento.getNumeroVara());
      this.nomeMagistradoTextField.setText(atendimento.getNomeMagistrado());
      this.cargoMagistradoTextField.setText(atendimento.getCargoMagistrado());
      this.descricaoAtendimentoTextArea.setText(atendimento.getDescricaoConteudo());
      this.dateToForm(this.dataOficioTextField, atendimento.getDataOficio());
      this.dateToForm(this.dataInicioAfastamentoTextField, atendimento.getDataInicioAfastamento());
      this.dateToForm(this.dataFimAfastamentoTextField, atendimento.getDataFimAfastamento());
   }

   private void dateToForm(JFormattedTextField componente, LocalDate valor) {
      if (valor != null) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         componente.setText(valor.format(formatter));
      }
   }

   private LocalDate dateFromForm(JFormattedTextField componente) {
      String texto = componente.getText().trim();
      if ("/  /".equals(texto)) {
         return null;
      } else {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         return LocalDate.parse(texto, formatter);
      }
   }

   private String limparTexto(String texto) {
      return texto.replace("'", " ").replace("\t", " ").replace("\n", " ");
   }
}
