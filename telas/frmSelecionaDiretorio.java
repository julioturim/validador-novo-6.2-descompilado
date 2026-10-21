package telas;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.asspa.client.exception.InfraestruturaException;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.StopWatchUtil;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.carga.ApagarTabelasCmd;
import br.mp.mpf.spea.simba.validador.carga.Carregar;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoBancarioCmd;
import br.mp.mpf.spea.simba.validador.cmd.CopiarArquivoParaPastaTxtCmd;
import br.mp.mpf.spea.simba.validador.cmd.GerarHashArquivosCmd;
import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import br.mp.mpf.spea.simba.validador.domain.Arquivos;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoBancario;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivo;
import br.mp.mpf.spea.simba.validador.ui.listener.VoltarFrameInicialListener;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

public class frmSelecionaDiretorio extends IconFrame implements VoltarFrameInicialListener {
   private static final long serialVersionUID = -734921883510133245L;
   private static final Logger log = LoggerFactory.getLogger(frmSelecionaDiretorio.class);
   private Desempenho desempenho = new Desempenho();
   @Inject
   private Config config;
   private frmIdentificaAtendimento frmIdentificaAtendimento;
   @Inject
   private ApplicationContext app;
   @Inject
   private Context context;
   @Inject
   private ControladorValidador controlador;
   @Inject
   private Configurador configurador;
   @Inject
   private ApagarTabelasCmd apagarTabelasCmd;
   private JButton selecionarPastaButton = new JButton();
   private JButton continuarButton;
   private JButton verRelatorioButton;
   private JButton voltarButton;
   private JLabel contaLabel;
   private JLabel titularesLabel;
   private JLabel extratoLabel;
   private JLabel origemDestinoLabel;
   private JLabel formatoLabel;
   private JLabel jLabel24;
   private JLabel conteudoLabel;
   private JLabel passo2Label = new JLabel();
   private JLabel descricaoLabel = new JLabel();
   private JLabel agenciaLabel;
   private JLabel agenciaConteudoLabel;
   private JLabel contaConteudoLabel;
   private JLabel titularesConteudoLabel;
   private JLabel extratoConteudoLabel;
   private JLabel origemDestinoConteudoLabel;
   private JLabel agenciaFormatoLabel;
   private JLabel contaFormatoLabel;
   private JLabel titularesFormatoLabel;
   private JLabel extratoFormatoLabel;
   private JLabel origemDestinoFormatoLabel;
   private JLabel casoLabel;
   private JLabel mensagensSistemaLabel;
   private JTextArea detalhesTextArea;
   private JLabel okLabel;
   private JLabel erroLabel;
   private JLabel waitLabel;
   private JLabel processingLabel;
   private JPanel corpoPanel = new JPanel();
   private JScrollPane jScrollPane1;
   private JTextField pathPastaTextField;
   private SpringLayout sl_corpoPanel;
   private JPanel desempenhoPanel;
   private JLabel linhaAtualLbl;
   private JLabel totalDeLinhasLbl;
   private JLabel desempenhoLbl;
   private JLabel linhasPorSegundoLbl;
   private JPanel arquivosPanel;
   private JLabel progressoLabel;
   private JLabel progressoTituloLabel;

   public frmSelecionaDiretorio() {
      this.selecionarPastaButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorio.this.selecionarPastaActionPerformed();
         }
      });
      this.selecionarPastaButton.setText("Selecionar Pasta");
      this.jLabel24 = new JLabel();
      this.voltarButton = new JButton();
      this.voltarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorio.this.voltarButtonActionPerformed();
         }
      });
      this.mensagensSistemaLabel = new JLabel();
      this.pathPastaTextField = new JTextField();
      this.continuarButton = new JButton();
      this.continuarButton.setEnabled(false);
      this.verRelatorioButton = new JButton();
      this.detalhesTextArea = new JTextArea();
      this.detalhesTextArea.setFont(new Font("Arial", 0, 12));
      this.detalhesTextArea.setEditable(false);
      DefaultCaret caret = (DefaultCaret)this.detalhesTextArea.getCaret();
      caret.setUpdatePolicy(2);
      this.jScrollPane1 = new JScrollPane();
      this.jScrollPane1.setViewportView(this.detalhesTextArea);
      this.setDefaultCloseOperation(3);
      this.addWindowListener(new WindowAdapter() {
         {
            Objects.requireNonNull(frmSelecionaDiretorio.this);
         }

         public void windowClosing(WindowEvent evt) {
            frmSelecionaDiretorio.this.onWindowClosing(evt);
         }
      });
      this.casoLabel = new JLabel();
      this.casoLabel.setText("001-MPF-000001-18");
      this.casoLabel.setFont(new Font("Tahoma", 1, 14));
      this.casoLabel.setForeground(new Color(102, 102, 0));
      this.corpoPanel.setBackground(new Color(255, 255, 255));
      this.corpoPanel.setForeground(new Color(102, 102, 0));
      this.passo2Label.setFont(new Font("Tahoma", 1, 18));
      this.passo2Label.setForeground(new Color(102, 102, 0));
      this.passo2Label.setText("Passo 2 - Seleção de diretório de arquivos ");
      this.descricaoLabel.setForeground(new Color(102, 102, 0));
      this.descricaoLabel.setHorizontalAlignment(2);
      this.descricaoLabel.setText("<html>Neste passo voc&ecirc; dever&aacute; informar a localiza&ccedil;&atilde;o dos arquivos a serem validados.<br>Os arquivos ser&atilde;o validados quanto ao formato e conte&uacute;do.</html>");
      this.descricaoLabel.setVerticalAlignment(1);
      this.descricaoLabel.setVerticalTextPosition(1);
      this.jLabel24.setForeground(new Color(102, 102, 0));
      this.jLabel24.setHorizontalAlignment(2);
      this.jLabel24.setText("Ofício");
      this.jLabel24.setVerticalAlignment(1);
      this.jLabel24.setVerticalTextPosition(1);
      this.voltarButton.setText("<< Voltar");
      this.mensagensSistemaLabel.setFont(new Font("Tahoma", 1, 13));
      this.mensagensSistemaLabel.setForeground(new Color(102, 102, 0));
      this.mensagensSistemaLabel.setHorizontalAlignment(2);
      this.mensagensSistemaLabel.setText("Mensagens do Sistema");
      this.mensagensSistemaLabel.setVerticalAlignment(1);
      this.mensagensSistemaLabel.setVerticalTextPosition(1);
      this.continuarButton.setText("Continuar >>");
      this.continuarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorio.this.continuarButtonMouseReleased();
         }
      });
      this.verRelatorioButton.setText("Ver relatório");
      this.verRelatorioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorio.this.verRelatorioButtonMouseReleased();
         }
      });
      this.sl_corpoPanel = new SpringLayout();
      this.sl_corpoPanel.putConstraint("North", this.passo2Label, 10, "North", this.corpoPanel);
      this.sl_corpoPanel.putConstraint("East", this.passo2Label, -10, "East", this.corpoPanel);
      this.sl_corpoPanel.putConstraint("East", this.selecionarPastaButton, 0, "East", this.passo2Label);
      this.sl_corpoPanel.putConstraint("West", this.passo2Label, 10, "West", this.corpoPanel);
      this.sl_corpoPanel.putConstraint("South", this.jScrollPane1, -20, "North", this.voltarButton);
      this.sl_corpoPanel.putConstraint("North", this.continuarButton, 0, "North", this.verRelatorioButton);
      this.sl_corpoPanel.putConstraint("North", this.verRelatorioButton, 0, "North", this.voltarButton);
      this.sl_corpoPanel.putConstraint("South", this.voltarButton, -10, "South", this.corpoPanel);
      this.sl_corpoPanel.putConstraint("East", this.verRelatorioButton, -10, "West", this.continuarButton);
      this.sl_corpoPanel.putConstraint("East", this.continuarButton, 0, "East", this.passo2Label);
      this.sl_corpoPanel.putConstraint("West", this.voltarButton, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("North", this.jScrollPane1, 5, "South", this.mensagensSistemaLabel);
      this.sl_corpoPanel.putConstraint("West", this.jScrollPane1, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("East", this.jScrollPane1, 0, "East", this.passo2Label);
      this.sl_corpoPanel.putConstraint("West", this.mensagensSistemaLabel, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("North", this.selecionarPastaButton, 8, "South", this.descricaoLabel);
      this.sl_corpoPanel.putConstraint("East", this.pathPastaTextField, -10, "West", this.selecionarPastaButton);
      this.sl_corpoPanel.putConstraint("North", this.descricaoLabel, 10, "South", this.casoLabel);
      this.sl_corpoPanel.putConstraint("West", this.descricaoLabel, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("North", this.pathPastaTextField, 10, "South", this.descricaoLabel);
      this.sl_corpoPanel.putConstraint("West", this.pathPastaTextField, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("North", this.casoLabel, 10, "South", this.passo2Label);
      this.sl_corpoPanel.putConstraint("West", this.casoLabel, 0, "West", this.passo2Label);
      this.corpoPanel.setLayout(this.sl_corpoPanel);
      this.corpoPanel.add(this.passo2Label);
      this.corpoPanel.add(this.casoLabel);
      this.corpoPanel.add(this.descricaoLabel);
      this.corpoPanel.add(this.pathPastaTextField);
      this.corpoPanel.add(this.selecionarPastaButton);
      this.arquivosPanel = new JPanel();
      this.sl_corpoPanel.putConstraint("West", this.arquivosPanel, 0, "West", this.passo2Label);
      this.sl_corpoPanel.putConstraint("North", this.arquivosPanel, 15, "South", this.pathPastaTextField);
      this.arquivosPanel.setBackground(Color.WHITE);
      this.corpoPanel.add(this.arquivosPanel);
      GridBagLayout gbl_arquivosPanel = new GridBagLayout();
      gbl_arquivosPanel.columnWidths = new int[]{0, 100, 100};
      gbl_arquivosPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
      gbl_arquivosPanel.columnWeights = new double[]{(double)0.0F, (double)0.0F, (double)0.0F};
      gbl_arquivosPanel.rowWeights = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};
      this.arquivosPanel.setLayout(gbl_arquivosPanel);
      this.formatoLabel = new JLabel();
      GridBagConstraints gbc_formatoLabel = new GridBagConstraints();
      gbc_formatoLabel.insets = new Insets(5, 0, 5, 5);
      gbc_formatoLabel.gridx = 1;
      gbc_formatoLabel.gridy = 0;
      this.arquivosPanel.add(this.formatoLabel, gbc_formatoLabel);
      this.formatoLabel.setForeground(new Color(102, 102, 0));
      this.formatoLabel.setHorizontalAlignment(2);
      this.formatoLabel.setText("Formato");
      this.formatoLabel.setVerticalAlignment(1);
      this.formatoLabel.setVerticalTextPosition(1);
      this.conteudoLabel = new JLabel();
      GridBagConstraints gbc_conteudoLabel = new GridBagConstraints();
      gbc_conteudoLabel.insets = new Insets(5, 0, 5, 5);
      gbc_conteudoLabel.gridx = 2;
      gbc_conteudoLabel.gridy = 0;
      this.arquivosPanel.add(this.conteudoLabel, gbc_conteudoLabel);
      this.conteudoLabel.setForeground(new Color(102, 102, 0));
      this.conteudoLabel.setHorizontalAlignment(2);
      this.conteudoLabel.setText("Conteúdo");
      this.conteudoLabel.setVerticalAlignment(1);
      this.conteudoLabel.setVerticalTextPosition(1);
      this.agenciaLabel = new JLabel();
      this.agenciaLabel.setHorizontalAlignment(2);
      GridBagConstraints gbc_agenciaLabel = new GridBagConstraints();
      gbc_agenciaLabel.anchor = 17;
      gbc_agenciaLabel.insets = new Insets(0, 0, 5, 5);
      gbc_agenciaLabel.gridx = 0;
      gbc_agenciaLabel.gridy = 1;
      this.arquivosPanel.add(this.agenciaLabel, gbc_agenciaLabel);
      this.agenciaLabel.setForeground(new Color(102, 102, 0));
      this.agenciaLabel.setText("Agência");
      this.agenciaLabel.setVerticalAlignment(1);
      this.agenciaLabel.setVerticalTextPosition(1);
      this.agenciaFormatoLabel = new JLabel();
      GridBagConstraints gbc_agenciaFormatoLabel = new GridBagConstraints();
      gbc_agenciaFormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_agenciaFormatoLabel.gridx = 1;
      gbc_agenciaFormatoLabel.gridy = 1;
      this.arquivosPanel.add(this.agenciaFormatoLabel, gbc_agenciaFormatoLabel);
      this.agenciaFormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.agenciaConteudoLabel = new JLabel();
      GridBagConstraints gbc_agenciaConteudoLabel = new GridBagConstraints();
      gbc_agenciaConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_agenciaConteudoLabel.gridx = 2;
      gbc_agenciaConteudoLabel.gridy = 1;
      this.arquivosPanel.add(this.agenciaConteudoLabel, gbc_agenciaConteudoLabel);
      this.agenciaConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.contaLabel = new JLabel();
      GridBagConstraints gbc_contaLabel = new GridBagConstraints();
      gbc_contaLabel.anchor = 17;
      gbc_contaLabel.insets = new Insets(0, 0, 5, 5);
      gbc_contaLabel.gridx = 0;
      gbc_contaLabel.gridy = 2;
      this.arquivosPanel.add(this.contaLabel, gbc_contaLabel);
      this.contaLabel.setForeground(new Color(102, 102, 0));
      this.contaLabel.setHorizontalAlignment(2);
      this.contaLabel.setText("Conta");
      this.contaLabel.setVerticalAlignment(1);
      this.contaLabel.setVerticalTextPosition(1);
      this.contaFormatoLabel = new JLabel();
      GridBagConstraints gbc_contaFormatoLabel = new GridBagConstraints();
      gbc_contaFormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_contaFormatoLabel.gridx = 1;
      gbc_contaFormatoLabel.gridy = 2;
      this.arquivosPanel.add(this.contaFormatoLabel, gbc_contaFormatoLabel);
      this.contaFormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.contaConteudoLabel = new JLabel();
      GridBagConstraints gbc_contaConteudoLabel = new GridBagConstraints();
      gbc_contaConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_contaConteudoLabel.gridx = 2;
      gbc_contaConteudoLabel.gridy = 2;
      this.arquivosPanel.add(this.contaConteudoLabel, gbc_contaConteudoLabel);
      this.contaConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.titularesLabel = new JLabel();
      GridBagConstraints gbc_titularesLabel = new GridBagConstraints();
      gbc_titularesLabel.anchor = 17;
      gbc_titularesLabel.insets = new Insets(0, 0, 5, 5);
      gbc_titularesLabel.gridx = 0;
      gbc_titularesLabel.gridy = 3;
      this.arquivosPanel.add(this.titularesLabel, gbc_titularesLabel);
      this.desempenhoLbl = new JLabel("Desempenho");
      this.desempenhoLbl.setForeground(new Color(102, 102, 0));
      this.sl_corpoPanel.putConstraint("North", this.desempenhoLbl, 0, "North", this.arquivosPanel);
      this.sl_corpoPanel.putConstraint("West", this.desempenhoLbl, 10, "East", this.arquivosPanel);
      this.sl_corpoPanel.putConstraint("East", this.desempenhoLbl, 0, "East", this.passo2Label);
      this.desempenhoLbl.setHorizontalAlignment(0);
      this.corpoPanel.add(this.desempenhoLbl);
      this.desempenhoPanel = new JPanel();
      this.sl_corpoPanel.putConstraint("North", this.desempenhoPanel, 5, "South", this.desempenhoLbl);
      this.sl_corpoPanel.putConstraint("South", this.desempenhoPanel, -56, "North", this.jScrollPane1);
      this.sl_corpoPanel.putConstraint("East", this.desempenhoPanel, 0, "East", this.passo2Label);
      this.desempenhoPanel.setMaximumSize(new Dimension(250, 400));
      this.sl_corpoPanel.putConstraint("West", this.desempenhoPanel, 10, "East", this.arquivosPanel);
      this.corpoPanel.add(this.desempenhoPanel);
      this.desempenhoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
      this.desempenhoPanel.setBackground(new Color(255, 255, 240));
      this.desempenhoPanel.setPreferredSize(new Dimension(150, 200));
      JLabel totalDeLinhasTituloLbl = new JLabel("Total de linhas:");
      totalDeLinhasTituloLbl.setBounds(0, 0, 0, 0);
      this.totalDeLinhasLbl = new JLabel("2350");
      this.totalDeLinhasLbl.setBounds(0, 0, 0, 0);
      JLabel linhaAtualTituloLbl = new JLabel("Linha atual:");
      linhaAtualTituloLbl.setBounds(0, 0, 0, 0);
      this.linhaAtualLbl = new JLabel("1000");
      this.linhaAtualLbl.setBounds(0, 0, 0, 0);
      JLabel linhasPorSegundoTituloLbl = new JLabel("Desempenho:");
      linhasPorSegundoTituloLbl.setBounds(0, 0, 0, 0);
      this.linhasPorSegundoLbl = new JLabel("2500 l/s");
      this.linhasPorSegundoLbl.setBounds(0, 0, 0, 0);
      SpringLayout sl_desempenhoPanel = new SpringLayout();
      sl_desempenhoPanel.putConstraint("North", this.linhasPorSegundoLbl, 0, "North", linhasPorSegundoTituloLbl);
      sl_desempenhoPanel.putConstraint("West", this.linhasPorSegundoLbl, 5, "East", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("East", this.linhasPorSegundoLbl, -5, "East", this.desempenhoPanel);
      sl_desempenhoPanel.putConstraint("North", linhasPorSegundoTituloLbl, 5, "South", linhaAtualTituloLbl);
      sl_desempenhoPanel.putConstraint("West", linhasPorSegundoTituloLbl, 0, "West", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("North", this.linhaAtualLbl, 0, "North", linhaAtualTituloLbl);
      sl_desempenhoPanel.putConstraint("East", this.totalDeLinhasLbl, -5, "East", this.desempenhoPanel);
      sl_desempenhoPanel.putConstraint("West", this.linhaAtualLbl, 5, "East", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("East", this.linhaAtualLbl, -5, "East", this.desempenhoPanel);
      sl_desempenhoPanel.putConstraint("North", linhaAtualTituloLbl, 5, "South", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("West", linhaAtualTituloLbl, 0, "West", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("West", totalDeLinhasTituloLbl, 5, "West", this.desempenhoPanel);
      sl_desempenhoPanel.putConstraint("North", this.totalDeLinhasLbl, 0, "North", totalDeLinhasTituloLbl);
      sl_desempenhoPanel.putConstraint("North", totalDeLinhasTituloLbl, 5, "North", this.desempenhoPanel);
      sl_desempenhoPanel.putConstraint("West", this.totalDeLinhasLbl, 5, "East", totalDeLinhasTituloLbl);
      this.desempenhoPanel.setLayout(sl_desempenhoPanel);
      this.desempenhoPanel.add(totalDeLinhasTituloLbl);
      this.desempenhoPanel.add(this.totalDeLinhasLbl);
      this.desempenhoPanel.add(linhaAtualTituloLbl);
      this.desempenhoPanel.add(this.linhaAtualLbl);
      this.desempenhoPanel.add(linhasPorSegundoTituloLbl);
      this.desempenhoPanel.add(this.linhasPorSegundoLbl);
      this.titularesLabel.setForeground(new Color(102, 102, 0));
      this.titularesLabel.setHorizontalAlignment(2);
      this.titularesLabel.setText("Cadastro de Titulares");
      this.titularesLabel.setVerticalAlignment(1);
      this.titularesLabel.setVerticalTextPosition(1);
      this.titularesFormatoLabel = new JLabel();
      GridBagConstraints gbc_titularesFormatoLabel = new GridBagConstraints();
      gbc_titularesFormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_titularesFormatoLabel.gridx = 1;
      gbc_titularesFormatoLabel.gridy = 3;
      this.arquivosPanel.add(this.titularesFormatoLabel, gbc_titularesFormatoLabel);
      this.titularesFormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.titularesConteudoLabel = new JLabel();
      GridBagConstraints gbc_titularesConteudoLabel = new GridBagConstraints();
      gbc_titularesConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_titularesConteudoLabel.gridx = 2;
      gbc_titularesConteudoLabel.gridy = 3;
      this.arquivosPanel.add(this.titularesConteudoLabel, gbc_titularesConteudoLabel);
      this.titularesConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.extratoLabel = new JLabel();
      GridBagConstraints gbc_extratoLabel = new GridBagConstraints();
      gbc_extratoLabel.anchor = 17;
      gbc_extratoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_extratoLabel.gridx = 0;
      gbc_extratoLabel.gridy = 4;
      this.arquivosPanel.add(this.extratoLabel, gbc_extratoLabel);
      this.extratoLabel.setForeground(new Color(102, 102, 0));
      this.extratoLabel.setHorizontalAlignment(2);
      this.extratoLabel.setText("Extrato");
      this.extratoLabel.setVerticalAlignment(1);
      this.extratoLabel.setVerticalTextPosition(1);
      this.extratoFormatoLabel = new JLabel();
      GridBagConstraints gbc_extratoFormatoLabel = new GridBagConstraints();
      gbc_extratoFormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_extratoFormatoLabel.gridx = 1;
      gbc_extratoFormatoLabel.gridy = 4;
      this.arquivosPanel.add(this.extratoFormatoLabel, gbc_extratoFormatoLabel);
      this.extratoFormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.extratoConteudoLabel = new JLabel();
      GridBagConstraints gbc_extratoConteudoLabel = new GridBagConstraints();
      gbc_extratoConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_extratoConteudoLabel.gridx = 2;
      gbc_extratoConteudoLabel.gridy = 4;
      this.arquivosPanel.add(this.extratoConteudoLabel, gbc_extratoConteudoLabel);
      this.extratoConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.origemDestinoLabel = new JLabel();
      GridBagConstraints gbc_origemDestinoLabel = new GridBagConstraints();
      gbc_origemDestinoLabel.anchor = 17;
      gbc_origemDestinoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_origemDestinoLabel.gridx = 0;
      gbc_origemDestinoLabel.gridy = 5;
      this.arquivosPanel.add(this.origemDestinoLabel, gbc_origemDestinoLabel);
      this.origemDestinoLabel.setForeground(new Color(102, 102, 0));
      this.origemDestinoLabel.setHorizontalAlignment(2);
      this.origemDestinoLabel.setText("Origem Destino");
      this.origemDestinoLabel.setVerticalAlignment(1);
      this.origemDestinoLabel.setVerticalTextPosition(1);
      this.origemDestinoFormatoLabel = new JLabel();
      GridBagConstraints gbc_origemDestinoFormatoLabel = new GridBagConstraints();
      gbc_origemDestinoFormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_origemDestinoFormatoLabel.gridx = 1;
      gbc_origemDestinoFormatoLabel.gridy = 5;
      this.arquivosPanel.add(this.origemDestinoFormatoLabel, gbc_origemDestinoFormatoLabel);
      this.origemDestinoFormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.origemDestinoConteudoLabel = new JLabel();
      GridBagConstraints gbc_origemDestinoConteudoLabel = new GridBagConstraints();
      gbc_origemDestinoConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_origemDestinoConteudoLabel.gridx = 2;
      gbc_origemDestinoConteudoLabel.gridy = 5;
      this.arquivosPanel.add(this.origemDestinoConteudoLabel, gbc_origemDestinoConteudoLabel);
      this.origemDestinoConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_erro.jpg")));
      GridBagConstraints gbc_oficioJudicialLabel = new GridBagConstraints();
      gbc_oficioJudicialLabel.anchor = 17;
      gbc_oficioJudicialLabel.insets = new Insets(0, 0, 0, 5);
      gbc_oficioJudicialLabel.gridx = 0;
      gbc_oficioJudicialLabel.gridy = 6;
      GridBagConstraints gbc_oficioJudicialFormatoLabel = new GridBagConstraints();
      gbc_oficioJudicialFormatoLabel.insets = new Insets(0, 0, 0, 5);
      gbc_oficioJudicialFormatoLabel.gridx = 1;
      gbc_oficioJudicialFormatoLabel.gridy = 6;
      this.corpoPanel.add(this.mensagensSistemaLabel);
      this.corpoPanel.add(this.jScrollPane1);
      this.corpoPanel.add(this.voltarButton);
      this.corpoPanel.add(this.verRelatorioButton);
      this.corpoPanel.add(this.continuarButton);
      this.okLabel = new JLabel();
      this.sl_corpoPanel.putConstraint("North", this.okLabel, 6, "South", this.passo2Label);
      this.sl_corpoPanel.putConstraint("East", this.okLabel, -100, "East", this.passo2Label);
      this.corpoPanel.add(this.okLabel);
      this.okLabel.setVisible(false);
      this.okLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.erroLabel = new JLabel();
      this.sl_corpoPanel.putConstraint("West", this.erroLabel, 6, "East", this.okLabel);
      this.sl_corpoPanel.putConstraint("South", this.erroLabel, 0, "South", this.okLabel);
      this.corpoPanel.add(this.erroLabel);
      this.erroLabel.setVisible(false);
      this.erroLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_erro.jpg")));
      this.waitLabel = new JLabel();
      this.sl_corpoPanel.putConstraint("West", this.waitLabel, 6, "East", this.erroLabel);
      this.sl_corpoPanel.putConstraint("South", this.waitLabel, 0, "South", this.okLabel);
      this.corpoPanel.add(this.waitLabel);
      this.waitLabel.setVisible(false);
      this.waitLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_wait.jpg")));
      this.processingLabel = new JLabel();
      this.sl_corpoPanel.putConstraint("West", this.processingLabel, 6, "East", this.waitLabel);
      this.sl_corpoPanel.putConstraint("South", this.processingLabel, 0, "South", this.okLabel);
      this.corpoPanel.add(this.processingLabel);
      this.processingLabel.setVisible(false);
      this.processingLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/processing.gif")));
      this.progressoTituloLabel = new JLabel("Progresso:");
      this.sl_corpoPanel.putConstraint("North", this.mensagensSistemaLabel, 10, "South", this.progressoTituloLabel);
      this.sl_corpoPanel.putConstraint("North", this.progressoTituloLabel, 10, "South", this.arquivosPanel);
      this.sl_corpoPanel.putConstraint("West", this.progressoTituloLabel, 0, "West", this.passo2Label);
      this.progressoTituloLabel.setForeground(new Color(102, 102, 0));
      this.corpoPanel.add(this.progressoTituloLabel);
      this.progressoLabel = new JLabel("-");
      this.sl_corpoPanel.putConstraint("North", this.progressoLabel, 0, "North", this.progressoTituloLabel);
      this.sl_corpoPanel.putConstraint("West", this.progressoLabel, 10, "East", this.progressoTituloLabel);
      this.progressoLabel.setForeground(new Color(102, 102, 0));
      this.corpoPanel.add(this.progressoLabel);
      this.zeraIcones();
      this.setName("passo2");
      this.getContentPane().add(this.corpoPanel, "Center");
      this.setPreferredSize(new Dimension(600, 700));
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   @PostConstruct
   public void setup() {
      this.initDesempenho();
      this.casoLabel.setText(this.context.getNumeroCaso());
   }

   public void setFrmIdentificaAtendimento(frmIdentificaAtendimento frmIdentificaAtendimento) {
      this.frmIdentificaAtendimento = frmIdentificaAtendimento;
   }

   private void initDesempenho() {
      if (!this.context.isEleitoral()) {
         this.desempenhoPanel.setVisible(false);
         this.desempenhoLbl.setVisible(false);
         this.progressoTituloLabel.setVisible(false);
         this.progressoLabel.setVisible(false);
      } else {
         this.totalDeLinhasLbl.setText("0");
         this.linhaAtualLbl.setText("0");
         this.linhasPorSegundoLbl.setText("0 l/s");
      }

   }

   public void excluiRelatorio() {
      Arquivo.apaga(new File(this.context.getPastaCaso(), "relatorio.html"));
   }

   public void zeraIcones() {
      this.continuarButton.setEnabled(false);
      this.verRelatorioButton.setEnabled(false);
      this.agenciaFormatoLabel.setIcon(this.waitLabel.getIcon());
      this.contaFormatoLabel.setIcon(this.waitLabel.getIcon());
      this.titularesFormatoLabel.setIcon(this.waitLabel.getIcon());
      this.extratoFormatoLabel.setIcon(this.waitLabel.getIcon());
      this.origemDestinoFormatoLabel.setIcon(this.waitLabel.getIcon());
      this.agenciaConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.contaConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.titularesConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.extratoConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.origemDestinoConteudoLabel.setIcon(this.waitLabel.getIcon());
   }

   private void onWindowClosing(WindowEvent evt) {
      this.setDefaultCloseOperation(0);
      this.voltarButtonActionPerformed();
   }

   private void verRelatorioButtonMouseReleased() {
      if (this.verRelatorioButton.isEnabled()) {
         try {
            File htmlFile = (new File(this.context.getPastaCaso(), "relatorio.html")).getAbsoluteFile();
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
               Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
               log.error("Desktop não suportado. Caminho do arquivo de relatório: {}", htmlFile);
               if (Util.isWindows()) {
                  File var10000 = new File(this.context.getPastaCaso(), "relatorio.html");
                  String command = "cmd.exe /C \"" + var10000.getAbsolutePath() + "\"";
                  log.debug("Executando o comando: {}", command);
                  (new ProcessBuilder(new String[]{command})).start();
               }
            }
         } catch (IOException e) {
            log.error("Erro ao abrir relatório.", e);
         }

      }
   }

   private void continuarButtonMouseReleased() {
      if (this.continuarButton.isEnabled()) {
         this.setVisible(false);
         frmTrataEnvio envio = (frmTrataEnvio)this.app.getBean(frmTrataEnvio.class);
         envio.setFrmSelecionaDiretorio(this);
         envio.setVisible(true);
      }
   }

   public void onVoltarFrameInicial() {
      this.setVisible(false);
      this.dispose();
      this.frmIdentificaAtendimento.mostrar();
   }

   void voltarButtonActionPerformed() {
      this.onVoltarFrameInicial();
   }

   private void selecionarPastaActionPerformed() {
      if (this.selecionarPastaButton.isEnabled()) {
         JFileChooser fc = new JFileChooser();
         fc.setFileSelectionMode(1);
         fc.setDialogTitle("Selecione diretório para validação");
         String propertyPastaValidacao = "pasta.validacao";
         String pathPastaValidacaoInicial = this.config.get(propertyPastaValidacao);
         if (!StringUtil.isEmpty(pathPastaValidacaoInicial)) {
            File pastaValidacaoInicial = new File(pathPastaValidacaoInicial);
            if (pastaValidacaoInicial.exists()) {
               if (pastaValidacaoInicial.getParentFile() == null) {
                  fc.setCurrentDirectory(pastaValidacaoInicial);
               } else {
                  fc.setCurrentDirectory(pastaValidacaoInicial.getParentFile());
               }
            }
         }

         if (fc.showDialog((Component)null, "Selecionar Pasta") == 0) {
            File pastaArquivosValidacao = fc.getSelectedFile().getAbsoluteFile();

            try {
               this.config.set(propertyPastaValidacao, pastaArquivosValidacao.getCanonicalPath());
            } catch (IOException var6) {
               log.warn("Erro ao obter canonical path de: {}", pastaArquivosValidacao.getAbsolutePath());
            }

            if (!this.verificarPathPastaDadosValidador(pastaArquivosValidacao, this.configurador.getPastaDadosValidador())) {
               StringBuilder msg = new StringBuilder();
               msg.append("Não é possível utilizar a pasta dadosValidador no processo de validação. ");
               msg.append("Essa pasta é de uso interno da aplicação.");
               log.error(msg.toString());
               ViewUtil.showErrorDialog(msg.toString());
            } else {
               this.pathPastaTextField.setText(pastaArquivosValidacao.getAbsolutePath());
               this.iniciarValidacao(pastaArquivosValidacao.getAbsoluteFile());
            }
         }
      }
   }

   private boolean verificarPathPastaDadosValidador(File pastaArquivosValidacao, File dadosValidadorFile) {
      while(pastaArquivosValidacao != null) {
         if (pastaArquivosValidacao.getAbsolutePath().equals(dadosValidadorFile.getAbsolutePath())) {
            return false;
         }

         pastaArquivosValidacao = pastaArquivosValidacao.getParentFile();
      }

      return true;
   }

   private void iniciarValidacao(File pastaArquivosValidacao) {
      log.debug("Iniciando validação da pasta: {}", pastaArquivosValidacao.getAbsolutePath());
      this.voltarButton.setEnabled(false);
      this.selecionarPastaButton.setEnabled(false);
      TarefaBackground tarefaBackground = new TarefaBackground();
      this.desempenho.tarefaBackground = tarefaBackground;
      tarefaBackground.setPastaArquivosValidacao(pastaArquivosValidacao);
      this.desempenho.startTime = System.currentTimeMillis();
      tarefaBackground.execute();
   }

   public File getPastaCaso() {
      File pastaDadosValidador = this.configurador.getPastaDadosValidador();
      return new File(pastaDadosValidador, Util.tiraBarra(this.context.getNumeroCaso()));
   }

   public class DadoFormatoConteudo {
      public NomeArquivo nomeArquivo;
      public String formato;
      public String conteudo;

      public DadoFormatoConteudo() {
         Objects.requireNonNull(frmSelecionaDiretorio.this);
         super();
      }

      public NomeArquivo getNomeArquivo() {
         return this.nomeArquivo;
      }

      public void setNomeArquivo(NomeArquivo nomeArquivo) {
         this.nomeArquivo = nomeArquivo;
      }

      public String getFormato() {
         return this.formato;
      }

      public void setFormato(String formato) {
         this.formato = formato;
      }

      public String getConteudo() {
         return this.conteudo;
      }

      public void setConteudo(String conteudo) {
         this.conteudo = conteudo;
      }
   }

   public class TarefaBackground extends SwingWorker<Void, Desempenho> implements View, ErrorReportCmd.MessageConsumer {
      private File pastaArquivosValidacao;
      private int atualValidacaoConteudo;
      private int totalValidacaoConteudo;
      private Map<NomeArquivo, JLabel> formatoLabels;
      private Map<NomeArquivo, JLabel> conteudoLabels;
      private ErrorReportCmd errorReportCmd;
      private GetAtendimentoBancarioCmd getAtendimentoBancarioCmd;
      private AtendimentoBancario atendimentoBancario;
      private Carregar carregar;
      private NumberFormat numberFormat;

      public TarefaBackground() {
         Objects.requireNonNull(frmSelecionaDiretorio.this);
         super();
         this.atualValidacaoConteudo = 0;
         this.totalValidacaoConteudo = 0;
         this.formatoLabels = new HashMap();
         this.conteudoLabels = new HashMap();
         this.numberFormat = NumberFormat.getNumberInstance();

         try {
            this.errorReportCmd = (ErrorReportCmd)frmSelecionaDiretorio.this.app.getBean(ErrorReportCmd.class);
            this.errorReportCmd.setMessageConsumer(this);
            this.getAtendimentoBancarioCmd = (GetAtendimentoBancarioCmd)frmSelecionaDiretorio.this.app.getBean(GetAtendimentoBancarioCmd.class);
            this.atendimentoBancario = this.getAtendimentoBancarioCmd.get(frmSelecionaDiretorio.this.context.getNumeroCaso());
            this.carregar = (Carregar)frmSelecionaDiretorio.this.app.getBean(Carregar.class);
            this.carregar.setDesempenho(frmSelecionaDiretorio.this.desempenho);
            this.formatoLabels.put(NomeArquivo.AGENCIAS, frmSelecionaDiretorio.this.agenciaFormatoLabel);
            this.formatoLabels.put(NomeArquivo.CONTAS, frmSelecionaDiretorio.this.contaFormatoLabel);
            this.formatoLabels.put(NomeArquivo.TITULARES, frmSelecionaDiretorio.this.titularesFormatoLabel);
            this.formatoLabels.put(NomeArquivo.EXTRATO, frmSelecionaDiretorio.this.extratoFormatoLabel);
            this.formatoLabels.put(NomeArquivo.ORIGEM_DESTINO, frmSelecionaDiretorio.this.origemDestinoFormatoLabel);
            this.conteudoLabels.put(NomeArquivo.AGENCIAS, frmSelecionaDiretorio.this.agenciaConteudoLabel);
            this.conteudoLabels.put(NomeArquivo.CONTAS, frmSelecionaDiretorio.this.contaConteudoLabel);
            this.conteudoLabels.put(NomeArquivo.TITULARES, frmSelecionaDiretorio.this.titularesConteudoLabel);
            this.conteudoLabels.put(NomeArquivo.EXTRATO, frmSelecionaDiretorio.this.extratoConteudoLabel);
            this.conteudoLabels.put(NomeArquivo.ORIGEM_DESTINO, frmSelecionaDiretorio.this.origemDestinoConteudoLabel);
         } catch (Exception e) {
            frmSelecionaDiretorio.log.error("Erro na TarefaBackground", e);

            try {
               frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
            } catch (Exception e1) {
               throw new InfraestruturaException("Erro ao limpar tabelas temporárias", e1);
            }
         }

      }

      public void doPublish(Desempenho desempenho) {
         this.publish(new Desempenho[]{desempenho});
      }

      public void progress() {
         this.publish(new Desempenho[]{frmSelecionaDiretorio.this.desempenho});
      }

      public void setFormatoValido(NomeArquivo nomeArquivo, boolean valido) {
         if (this.formatoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.formatoLabels.get(nomeArquivo);
            label.setIcon(valido ? frmSelecionaDiretorio.this.okLabel.getIcon() : frmSelecionaDiretorio.this.erroLabel.getIcon());
            this.addMessage("Formato do arquivo " + nomeArquivo.name() + " validado com sucesso.");
         }

      }

      public void setConteudoValido(NomeArquivo nomeArquivo, boolean valido) {
         if (this.conteudoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.conteudoLabels.get(nomeArquivo);
            label.setIcon(valido ? frmSelecionaDiretorio.this.okLabel.getIcon() : frmSelecionaDiretorio.this.erroLabel.getIcon());
         }

      }

      public void startLoad(NomeArquivo nomeArquivo) {
         if (this.formatoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.formatoLabels.get(nomeArquivo);
            label.setIcon(frmSelecionaDiretorio.this.processingLabel.getIcon());
         }

      }

      public void startValidation(NomeArquivo nomeArquivo) {
         if (this.conteudoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.conteudoLabels.get(nomeArquivo);
            label.setIcon(frmSelecionaDiretorio.this.processingLabel.getIcon());
         }

      }

      public void total(NomeArquivo agencias, int total) {
         this.totalValidacaoConteudo = total;
         this.atualValidacaoConteudo = 1;
         frmSelecionaDiretorio.this.progressoLabel.setText(this.atualValidacaoConteudo + " / " + this.totalValidacaoConteudo);
      }

      public void increment(NomeArquivo agencias) {
         ++this.atualValidacaoConteudo;
         frmSelecionaDiretorio.this.progressoLabel.setText(this.atualValidacaoConteudo + " / " + this.totalValidacaoConteudo);
      }

      public void addMessage(String msg) {
         frmSelecionaDiretorio.this.detalhesTextArea.append(String.format("%1$td/%1$tm/%1$ty %1$tT - %2$s\n", LocalDateTime.now(), msg));
      }

      public void message(String msg) {
         this.addMessage(msg);
      }

      protected Void doInBackground() {
         StopWatch stopwatch = StopWatchUtil.getStarted();
         boolean arquivosExistem = false;
         boolean var28 = false;

         Object var41;
         label436: {
            Object gerarHashArquivosCmd;
            label437: {
               Object var51;
               label438: {
                  label439: {
                     Object tempoValidacaox;
                     label452: {
                        label453: {
                           try {
                              var28 = true;
                              frmSelecionaDiretorio.this.detalhesTextArea.setText("");
                              frmSelecionaDiretorio.this.zeraIcones();
                              frmSelecionaDiretorio.this.excluiRelatorio();
                              StringBuilder message = new StringBuilder();
                              arquivosExistem = frmSelecionaDiretorio.this.controlador.verificarExistenciaArquivosBancario(this.pastaArquivosValidacao, frmSelecionaDiretorio.this.context.getNumeroCaso(), message);
                              if (!arquivosExistem) {
                                 this.addMessage(message.toString());
                                 var41 = null;
                                 var28 = false;
                                 break label436;
                              }

                              String horaInicioMsg = "Início da validação";
                              frmSelecionaDiretorio.log.info(horaInicioMsg);
                              this.addMessage(horaInicioMsg);
                              if (!frmSelecionaDiretorio.this.controlador.checkVersaoSistemaBanco()) {
                                 StringBuilder msg = new StringBuilder();
                                 msg.append("A versão do sistema (");
                                 msg.append(frmSelecionaDiretorio.this.configurador.getVersao());
                                 msg.append(") é diferente da versão do cadastro de atendimento (");
                                 msg.append(frmSelecionaDiretorio.this.controlador.getVersaoSistemaBanco());
                                 msg.append("). Para solucionar este problema, clique no botão Voltar, selecione o caso, clique em Ver Detalhes e grave o caso novamente.");
                                 String msgString = msg.toString();
                                 frmSelecionaDiretorio.log.info(msgString);
                                 ViewUtil.showWarnDialog(msgString);
                                 gerarHashArquivosCmd = null;
                                 var28 = false;
                                 break label437;
                              }

                              Arquivos arquivos = this.getArquivoBean();
                              Computador computador = frmSelecionaDiretorio.this.controlador.getComputador(frmSelecionaDiretorio.this.context.getNumeroComputador());
                              GerarHashArquivosCmd gerarHashArquivosCmd = (GerarHashArquivosCmd)frmSelecionaDiretorio.this.app.getBean(GerarHashArquivosCmd.class);
                              gerarHashArquivosCmd.gerar(frmSelecionaDiretorio.this.controlador, computador, arquivos);
                              CopiarArquivoParaPastaTxtCmd copiarArquivoParaPastaTxtCmd = (CopiarArquivoParaPastaTxtCmd)frmSelecionaDiretorio.this.app.getBean(CopiarArquivoParaPastaTxtCmd.class);
                              copiarArquivoParaPastaTxtCmd.copiar(frmSelecionaDiretorio.this.context.getNumeroCaso(), arquivos);
                              frmSelecionaDiretorio.this.controlador.verificarArquivosVazios(arquivos, this.errorReportCmd);
                              if (this.errorReportCmd.hasErros()) {
                                 this.finishWithError();
                                 var51 = null;
                                 var28 = false;
                                 break label438;
                              }

                              this.carregar.carregar(this.atendimentoBancario, arquivos);
                              if (this.errorReportCmd.hasErros()) {
                                 this.finishWithError();
                                 var51 = null;
                                 var28 = false;
                                 break label439;
                              }

                              NomeArquivo[] nomeArquivos = NomeArquivo.values();

                              for(int i = 0; i < nomeArquivos.length; ++i) {
                                 NomeArquivo nomeArquivo = nomeArquivos[i];
                                 boolean valido = frmSelecionaDiretorio.this.controlador.validarConteudo(i, frmSelecionaDiretorio.this.desempenho.tarefaBackground);
                                 this.setConteudoValido(nomeArquivo, valido);
                                 if (!valido) {
                                    this.addMessage("Arquivo " + String.valueOf(nomeArquivo) + ": conteúdo não passou na validação.");
                                    break;
                                 }

                                 this.addMessage("Conteúdo do arquivo " + String.valueOf(nomeArquivo) + " validado com sucesso.");
                              }

                              if (this.errorReportCmd.hasErros()) {
                                 this.finishWithError();
                                 tempoValidacaox = null;
                                 var28 = false;
                                 break label452;
                              }

                              var28 = false;
                           } catch (Exception e) {
                              arquivosExistem = false;
                              frmSelecionaDiretorio.log.error("Erro durante a validação.", e);
                              this.addMessage("Ocorreu um erro do sistema durante a validação. Verifique o log do sistema ou contate o órgão responsável pelo caso.");
                              var28 = false;
                              break label453;
                           } finally {
                              if (var28) {
                                 frmSelecionaDiretorio.log.info("Limpando dados temporários");
                                 this.addMessage("Finalizando...");

                                 try {
                                    frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
                                 } catch (Exception e) {
                                    frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
                                 }

                                 stopwatch.stop();
                                 frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
                                 if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                                    frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                                 }

                                 double tempoValidacao = stopwatch.getTotalTimeSeconds();
                                 String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
                                 frmSelecionaDiretorio.log.info(horaFimMsg);
                                 this.addMessage(horaFimMsg);
                                 if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                                    frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                                 }

                                 this.errorReportCmd.reset();
                                 frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
                                 frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
                              }
                           }

                           frmSelecionaDiretorio.log.info("Limpando dados temporários");
                           this.addMessage("Finalizando...");

                           try {
                              frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
                           } catch (Exception e) {
                              frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
                           }

                           stopwatch.stop();
                           frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
                           if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                              frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                           }

                           double tempoValidacao = stopwatch.getTotalTimeSeconds();
                           String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
                           frmSelecionaDiretorio.log.info(horaFimMsg);
                           this.addMessage(horaFimMsg);
                           if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                              frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                           }

                           this.errorReportCmd.reset();
                           frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
                           frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
                           return null;
                        }

                        frmSelecionaDiretorio.log.info("Limpando dados temporários");
                        this.addMessage("Finalizando...");

                        try {
                           frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
                        } catch (Exception e) {
                           frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
                        }

                        stopwatch.stop();
                        frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
                        if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                           frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                        }

                        double tempoValidacao = stopwatch.getTotalTimeSeconds();
                        String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
                        frmSelecionaDiretorio.log.info(horaFimMsg);
                        this.addMessage(horaFimMsg);
                        if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                           frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                        }

                        this.errorReportCmd.reset();
                        frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
                        frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
                        return null;
                     }

                     frmSelecionaDiretorio.log.info("Limpando dados temporários");
                     this.addMessage("Finalizando...");

                     try {
                        frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
                     } catch (Exception e) {
                        frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
                     }

                     stopwatch.stop();
                     frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
                     if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                        frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                     }

                     double tempoValidacao = stopwatch.getTotalTimeSeconds();
                     String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
                     frmSelecionaDiretorio.log.info(horaFimMsg);
                     this.addMessage(horaFimMsg);
                     if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                        frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                     }

                     this.errorReportCmd.reset();
                     frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
                     frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
                     return (Void)tempoValidacaox;
                  }

                  frmSelecionaDiretorio.log.info("Limpando dados temporários");
                  this.addMessage("Finalizando...");

                  try {
                     frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
                  } catch (Exception e) {
                     frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
                  }

                  stopwatch.stop();
                  frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
                  if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                     frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                  }

                  double tempoValidacao = stopwatch.getTotalTimeSeconds();
                  String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
                  frmSelecionaDiretorio.log.info(horaFimMsg);
                  this.addMessage(horaFimMsg);
                  if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                     frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
                  }

                  this.errorReportCmd.reset();
                  frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
                  frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
                  return (Void)var51;
               }

               frmSelecionaDiretorio.log.info("Limpando dados temporários");
               this.addMessage("Finalizando...");

               try {
                  frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
               } catch (Exception e) {
                  frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
               }

               stopwatch.stop();
               frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
               if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
                  frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
               }

               double tempoValidacao = stopwatch.getTotalTimeSeconds();
               String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
               frmSelecionaDiretorio.log.info(horaFimMsg);
               this.addMessage(horaFimMsg);
               if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                  frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
               }

               this.errorReportCmd.reset();
               frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
               frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
               return (Void)var51;
            }

            frmSelecionaDiretorio.log.info("Limpando dados temporários");
            this.addMessage("Finalizando...");

            try {
               frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
            } catch (Exception e) {
               frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
            }

            stopwatch.stop();
            frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
            if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
               frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
            }

            double tempoValidacao = stopwatch.getTotalTimeSeconds();
            String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
            frmSelecionaDiretorio.log.info(horaFimMsg);
            this.addMessage(horaFimMsg);
            if (!this.errorReportCmd.hasErros() && arquivosExistem) {
               frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
            }

            this.errorReportCmd.reset();
            frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
            frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
            return (Void)gerarHashArquivosCmd;
         }

         frmSelecionaDiretorio.log.info("Limpando dados temporários");
         this.addMessage("Finalizando...");

         try {
            frmSelecionaDiretorio.this.apagarTabelasCmd.apagar();
         } catch (Exception e) {
            frmSelecionaDiretorio.log.error("Erro ao limpar dados temporários.", e);
         }

         stopwatch.stop();
         frmSelecionaDiretorio.log.info("Tempo validação: {} ms", stopwatch.getTotalTimeMillis());
         if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorio.this.context.getNumeroCaso())) {
            frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
         }

         double tempoValidacao = stopwatch.getTotalTimeSeconds();
         String horaFimMsg = String.format("Fim da validação. Tempo total: %,.2f s", tempoValidacao);
         frmSelecionaDiretorio.log.info(horaFimMsg);
         this.addMessage(horaFimMsg);
         if (!this.errorReportCmd.hasErros() && arquivosExistem) {
            frmSelecionaDiretorio.this.continuarButton.setEnabled(true);
         }

         this.errorReportCmd.reset();
         frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
         frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
         return (Void)var41;
      }

      protected void finishWithError() {
         try {
            File pastaEnvio = new File(frmSelecionaDiretorio.this.getPastaCaso(), "envio");
            Arquivo.apaga(pastaEnvio);
            this.gerarRelatorio(this.errorReportCmd);
            this.addMessage("Relatório de inconsistências gerado.");
            frmSelecionaDiretorio.this.verRelatorioButton.setEnabled(true);
         } catch (Exception e) {
            frmSelecionaDiretorio.log.error("Erro ao gerar relatório.", e);
            File var10001 = new File(frmSelecionaDiretorio.this.context.getPastaCaso(), "relatorio.html");
            this.addMessage("Erro ao gerar relatório. Verifique se o arquivo não está aberto. Caminho: " + var10001.getAbsolutePath());
            frmSelecionaDiretorio.this.verRelatorioButton.setEnabled(false);
         }

      }

      protected Arquivos getArquivoBean() {
         Arquivos a = new Arquivos();

         for(File arquivo : FileUtils.listFiles(this.pastaArquivosValidacao, new SuffixFileFilter(".txt", IOCase.INSENSITIVE), FalseFileFilter.INSTANCE)) {
            if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorio.this.context.getNumeroCaso() + "_AGENCIAS.TXT")) {
               a.setAgencias(arquivo);
               frmSelecionaDiretorio.log.debug("Arquivo AGENCIAS utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorio.this.context.getNumeroCaso() + "_CONTAS.TXT")) {
               a.setContas(arquivo);
               frmSelecionaDiretorio.log.debug("Arquivo CONTAS utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorio.this.context.getNumeroCaso() + "_TITULARES.TXT")) {
               a.setTitulares(arquivo);
               frmSelecionaDiretorio.log.debug("Arquivo TITULARES utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorio.this.context.getNumeroCaso() + "_EXTRATO.TXT")) {
               a.setExtrato(arquivo);
               frmSelecionaDiretorio.log.debug("Arquivo EXTRATO utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorio.this.context.getNumeroCaso() + "_ORIGEM_DESTINO.TXT")) {
               a.setOrigemDestino(arquivo);
               frmSelecionaDiretorio.log.debug("Arquivo ORIGEM_DESTINO utilizado: {}", arquivo.getAbsolutePath());
            }
         }

         return a;
      }

      protected void process(List<Desempenho> desempenhos) {
         frmSelecionaDiretorio.this.totalDeLinhasLbl.setText(String.valueOf(frmSelecionaDiretorio.this.desempenho.totalLinha));
         frmSelecionaDiretorio.this.linhaAtualLbl.setText(String.valueOf(frmSelecionaDiretorio.this.desempenho.linhaAtual));
         frmSelecionaDiretorio.this.linhasPorSegundoLbl.setText(this.numberFormat.format(frmSelecionaDiretorio.this.desempenho.linhasPorSegundo) + " l/s");
      }

      public void done() {
         frmSelecionaDiretorio.this.voltarButton.setEnabled(true);
         frmSelecionaDiretorio.this.selecionarPastaButton.setEnabled(true);
      }

      private File getReportFile() {
         File pastaDadosValidador = frmSelecionaDiretorio.this.configurador.getPastaDadosValidador();
         File pastaCaso = new File(pastaDadosValidador, Util.tiraBarra(frmSelecionaDiretorio.this.context.getNumeroCaso()));
         File arquivo = new File(pastaCaso, "relatorio.html");
         if (arquivo.exists()) {
            arquivo.delete();
         }

         return arquivo;
      }

      private void gerarRelatorio(ErrorReportCmd errorReportCmd) {
         GetAtendimentoBancarioCmd getAtendimentoBancarioCmd = (GetAtendimentoBancarioCmd)frmSelecionaDiretorio.this.app.getBean(GetAtendimentoBancarioCmd.class);
         AtendimentoBancario atendimentoBancario = getAtendimentoBancarioCmd.get(frmSelecionaDiretorio.this.context.getNumeroCaso());
         Map<String, Object> map = new HashMap();
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         map.put("numeroCaso", atendimentoBancario.getNumeroCaso());
         map.put("dataEmissao", sdf.format(new Date()));
         map.put("numeroBanco", atendimentoBancario.getNumeroBanco());
         map.put("nomeBanco", atendimentoBancario.getNomeBanco());
         map.put("nomeFuncionario", atendimentoBancario.getNomeFuncionarioResponsavel());
         map.put("telefoneFuncionario", atendimentoBancario.getTelefoneFuncionarioResponsavel());
         map.put("emailFuncionario", atendimentoBancario.getEmailFuncionarioResponsavel());
         this.putImages(map);
         File pastaConfig = new File(frmSelecionaDiretorio.this.configurador.getPastaDadosValidador(), "conf");
         File pastaImagens = new File(pastaConfig, "imagens");
         File brasaoFile = Util.getBrasao(pastaImagens.getAbsolutePath(), frmSelecionaDiretorio.this.context.getNumeroComputador());
         this.putImage(map, "logoBase64", brasaoFile);
         map.put("extensaoLogo", FilenameUtils.getExtension(brasaoFile.getName()));
         this.putDadoFormatoConteudo(map);
         map.put("erros", this.errorReportCmd.iterator());
         String arquivoHtml = "templates/relatorio-inconsistencias.html";
         Properties properties = new Properties();
         properties.put("resource.loaders", "class");
         properties.put("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
         Velocity.init(properties);
         VelocityContext context = new VelocityContext(map);

         try {
            FileWriter writer = new FileWriter(this.getReportFile(), StandardCharsets.UTF_8);

            try {
               Template template = Velocity.getTemplate(arquivoHtml, StandardCharsets.UTF_8.displayName());
               StringWriter sw = new StringWriter();
               template.merge(context, sw);
               writer.append(sw.toString());
            } catch (Throwable var16) {
               try {
                  writer.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }

               throw var16;
            }

            writer.close();
         } catch (Exception e) {
            frmSelecionaDiretorio.log.error("Erro ao gerar relatório de inconsistência", e);
         }

      }

      private void putImages(Map<String, Object> map) {
         this.putImage(map, "imageOk", "telas/recursos/check_OK.jpg");
         this.putImage(map, "imageErro", "telas/recursos/check_erro.jpg");
         this.putImage(map, "imageWait", "telas/recursos/check_wait.jpg");
      }

      private void putImage(Map<String, Object> map, String name, String path) {
         try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
            byte[] byteArray = StreamUtils.copyToByteArray(in);
            String image = Base64.encodeBase64String(byteArray);
            map.put(name, image);
         } catch (IOException e) {
            frmSelecionaDiretorio.log.error("Erro ao localizar image: " + path, e);
         }

      }

      private void putImage(Map<String, Object> map, String name, File arquivo) {
         try {
            byte[] byteArray = FileUtils.readFileToByteArray(arquivo);
            String image = Base64.encodeBase64String(byteArray);
            map.put(name, image);
         } catch (IOException e) {
            frmSelecionaDiretorio.log.error("Erro ao localizar image: " + arquivo.getAbsolutePath(), e);
         }

      }

      private void putDadoFormatoConteudo(Map<String, Object> map) {
         List<DadoFormatoConteudo> dados = new ArrayList(5);
         NomeArquivo[] nomeArquivos = NomeArquivo.values();

         for(NomeArquivo nomeArquivo : nomeArquivos) {
            if (this.formatoLabels.containsKey(nomeArquivo)) {
               DadoFormatoConteudo dadoFormatoConteudo = frmSelecionaDiretorio.this.new DadoFormatoConteudo();
               dadoFormatoConteudo.nomeArquivo = nomeArquivo;
               if (((JLabel)this.formatoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorio.this.okLabel.getIcon())) {
                  dadoFormatoConteudo.formato = "ok";
               } else if (((JLabel)this.formatoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorio.this.erroLabel.getIcon())) {
                  dadoFormatoConteudo.formato = "erro";
               } else {
                  dadoFormatoConteudo.formato = "wait";
               }

               if (((JLabel)this.conteudoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorio.this.okLabel.getIcon())) {
                  dadoFormatoConteudo.conteudo = "ok";
               } else if (((JLabel)this.conteudoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorio.this.erroLabel.getIcon())) {
                  dadoFormatoConteudo.conteudo = "erro";
               } else {
                  dadoFormatoConteudo.conteudo = "wait";
               }

               dados.add(dadoFormatoConteudo);
            }
         }

         map.put("arquivos", dados);
      }

      public Desempenho getDesempenho() {
         return frmSelecionaDiretorio.this.desempenho;
      }

      public void setPastaArquivosValidacao(File pastaArquivosValidacao) {
         this.pastaArquivosValidacao = pastaArquivosValidacao;
      }
   }

   public interface View {
      void progress();

      void setFormatoValido(NomeArquivo nomeArquivo, boolean valido);

      void setConteudoValido(NomeArquivo nomeArquivo, boolean valido);

      void total(NomeArquivo agencias, int i);

      void increment(NomeArquivo nomeArquivo);

      void startLoad(NomeArquivo nomeArquivo);

      void startValidation(NomeArquivo nomeArquivo);

      void addMessage(String msg);
   }
}
