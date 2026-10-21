package telas;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.error.ErrorReportCmd;
import br.mp.mpf.pgr.client.to.Computador;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.pgr.util.ViewUtil;
import br.mp.mpf.spea.simba.validador.ConfiguradorValidador;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.carga.CarregarGab;
import br.mp.mpf.spea.simba.validador.carga.CarregarGabVer2;
import br.mp.mpf.spea.simba.validador.carga.GetAtendimentoGabCmd;
import br.mp.mpf.spea.simba.validador.cmd.CopiarArquivoParaPastaTxtCmd;
import br.mp.mpf.spea.simba.validador.cmd.GerarHashArquivosGabCmd;
import br.mp.mpf.spea.simba.validador.desempenho.Desempenho;
import br.mp.mpf.spea.simba.validador.domain.ArquivosGab;
import br.mp.mpf.spea.simba.validador.domain.AtendimentoGab;
import br.mp.mpf.spea.simba.validador.enums.NomeArquivoGab;
import br.mp.mpf.spea.simba.validador.ui.listener.VoltarFrameInicialListener;
import com.google.common.base.Stopwatch;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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
import org.springframework.util.StreamUtils;

public class frmSelecionaDiretorioGab extends IconFrame implements VoltarFrameInicialListener {
   private static final long serialVersionUID = 2992294815219416635L;
   private static final Logger log = LoggerFactory.getLogger(frmSelecionaDiretorioGab.class);
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
   private GerarHashArquivosGabCmd gerarHashArquivosCmd;
   private JButton selecionarPastaButton = new JButton();
   private JButton continuarButton;
   private JButton verRelatorioButton;
   private JButton voltarButton;
   private JLabel gab112Label;
   private JLabel gab800Label;
   private JLabel formatoLabel;
   private JLabel jLabel24;
   private JLabel conteudoLabel;
   private JLabel passo2Label = new JLabel();
   private JLabel descricaoLabel = new JLabel();
   private JLabel gab109Label;
   private JLabel gab109ConteudoLabel;
   private JLabel gab112ConteudoLabel;
   private JLabel gab800ConteudoLabel;
   private JLabel gab109FormatoLabel;
   private JLabel gab112FormatoLabel;
   private JLabel gab800FormatoLabel;
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

   public frmSelecionaDiretorioGab() {
      this.selecionarPastaButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorioGab.this.selecionarPastaActionPerformed();
         }
      });
      this.selecionarPastaButton.setText("Selecionar Pasta");
      this.jLabel24 = new JLabel();
      this.voltarButton = new JButton();
      this.voltarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorioGab.this.voltarButtonActionPerformed();
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
            Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         }

         public void windowClosing(WindowEvent evt) {
            frmSelecionaDiretorioGab.this.onWindowClosing(evt);
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
            Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorioGab.this.continuarButtonMouseReleased();
         }
      });
      this.verRelatorioButton.setText("Ver relatório");
      this.verRelatorioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmSelecionaDiretorioGab.this.verRelatorioButtonMouseReleased();
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
      this.gab109Label = new JLabel();
      this.gab109Label.setHorizontalAlignment(2);
      GridBagConstraints gbc_gab109Label = new GridBagConstraints();
      gbc_gab109Label.anchor = 17;
      gbc_gab109Label.insets = new Insets(0, 0, 5, 5);
      gbc_gab109Label.gridx = 0;
      gbc_gab109Label.gridy = 1;
      this.arquivosPanel.add(this.gab109Label, gbc_gab109Label);
      this.gab109Label.setForeground(new Color(102, 102, 0));
      this.gab109Label.setText("Gab109");
      this.gab109Label.setVerticalAlignment(1);
      this.gab109Label.setVerticalTextPosition(1);
      this.gab109FormatoLabel = new JLabel();
      GridBagConstraints gbc_gab109FormatoLabel = new GridBagConstraints();
      gbc_gab109FormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab109FormatoLabel.gridx = 1;
      gbc_gab109FormatoLabel.gridy = 1;
      this.arquivosPanel.add(this.gab109FormatoLabel, gbc_gab109FormatoLabel);
      this.gab109FormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gab109ConteudoLabel = new JLabel();
      GridBagConstraints gbc_gab109ConteudoLabel = new GridBagConstraints();
      gbc_gab109ConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab109ConteudoLabel.gridx = 2;
      gbc_gab109ConteudoLabel.gridy = 1;
      this.arquivosPanel.add(this.gab109ConteudoLabel, gbc_gab109ConteudoLabel);
      this.gab109ConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gab112Label = new JLabel();
      GridBagConstraints gbc_contaLabel = new GridBagConstraints();
      gbc_contaLabel.anchor = 17;
      gbc_contaLabel.insets = new Insets(0, 0, 5, 5);
      gbc_contaLabel.gridx = 0;
      gbc_contaLabel.gridy = 2;
      this.arquivosPanel.add(this.gab112Label, gbc_contaLabel);
      this.gab112Label.setForeground(new Color(102, 102, 0));
      this.gab112Label.setHorizontalAlignment(2);
      this.gab112Label.setText("Gab112");
      this.gab112Label.setVerticalAlignment(1);
      this.gab112Label.setVerticalTextPosition(1);
      this.gab112FormatoLabel = new JLabel();
      GridBagConstraints gbc_gab112FormatoLabel = new GridBagConstraints();
      gbc_gab112FormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab112FormatoLabel.gridx = 1;
      gbc_gab112FormatoLabel.gridy = 2;
      this.arquivosPanel.add(this.gab112FormatoLabel, gbc_gab112FormatoLabel);
      this.gab112FormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gab112ConteudoLabel = new JLabel();
      GridBagConstraints gbc_gab112ConteudoLabel = new GridBagConstraints();
      gbc_gab112ConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab112ConteudoLabel.gridx = 2;
      gbc_gab112ConteudoLabel.gridy = 2;
      this.arquivosPanel.add(this.gab112ConteudoLabel, gbc_gab112ConteudoLabel);
      this.gab112ConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gab800Label = new JLabel();
      GridBagConstraints gbc_gab800Label = new GridBagConstraints();
      gbc_gab800Label.anchor = 17;
      gbc_gab800Label.insets = new Insets(0, 0, 5, 5);
      gbc_gab800Label.gridx = 0;
      gbc_gab800Label.gridy = 3;
      this.arquivosPanel.add(this.gab800Label, gbc_gab800Label);
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
      this.gab800Label.setForeground(new Color(102, 102, 0));
      this.gab800Label.setHorizontalAlignment(2);
      this.gab800Label.setText("Gab800");
      this.gab800Label.setVerticalAlignment(1);
      this.gab800Label.setVerticalTextPosition(1);
      this.gab800FormatoLabel = new JLabel();
      GridBagConstraints gbc_gab800FormatoLabel = new GridBagConstraints();
      gbc_gab800FormatoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab800FormatoLabel.gridx = 1;
      gbc_gab800FormatoLabel.gridy = 3;
      this.arquivosPanel.add(this.gab800FormatoLabel, gbc_gab800FormatoLabel);
      this.gab800FormatoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gab800ConteudoLabel = new JLabel();
      GridBagConstraints gbc_gab800ConteudoLabel = new GridBagConstraints();
      gbc_gab800ConteudoLabel.insets = new Insets(0, 0, 5, 5);
      gbc_gab800ConteudoLabel.gridx = 2;
      gbc_gab800ConteudoLabel.gridy = 3;
      this.arquivosPanel.add(this.gab800ConteudoLabel, gbc_gab800ConteudoLabel);
      this.gab800ConteudoLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
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
      this.gab109FormatoLabel.setIcon(this.waitLabel.getIcon());
      this.gab112FormatoLabel.setIcon(this.waitLabel.getIcon());
      this.gab800FormatoLabel.setIcon(this.waitLabel.getIcon());
      this.gab109ConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.gab112ConteudoLabel.setIcon(this.waitLabel.getIcon());
      this.gab800ConteudoLabel.setIcon(this.waitLabel.getIcon());
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

   private void voltarButtonActionPerformed() {
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
      TarefaBackgroundGab tarefaBackground = new TarefaBackgroundGab();
      this.desempenho.tarefaBackgroundGab = tarefaBackground;
      tarefaBackground.setPastaArquivosValidacao(pastaArquivosValidacao);
      this.desempenho.startTime = System.currentTimeMillis();
      tarefaBackground.execute();
   }

   public File getPastaCaso() {
      File pastaDadosValidador = this.configurador.getPastaDadosValidador();
      return new File(pastaDadosValidador, Util.tiraBarra(this.context.getNumeroCaso()));
   }

   public class DadoFormatoConteudo {
      public NomeArquivoGab nomeArquivo;
      public String formato;
      public String conteudo;

      public DadoFormatoConteudo() {
         Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         super();
      }

      public NomeArquivoGab getNomeArquivo() {
         return this.nomeArquivo;
      }

      public void setNomeArquivo(NomeArquivoGab nomeArquivo) {
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

   public class TarefaBackgroundGab extends SwingWorker<Void, Desempenho> implements ViewGab, ErrorReportCmd.MessageConsumer {
      private File pastaArquivosValidacao;
      private Map<NomeArquivoGab, JLabel> formatoLabels;
      private Map<NomeArquivoGab, JLabel> conteudoLabels;
      private ErrorReportCmd errorReportCmd;
      private AtendimentoGab atendimentoGab;
      private GetAtendimentoGabCmd getAtendimentoGabCmd;
      private NumberFormat numberFormat;
      private CarregarGab carregar;
      private CarregarGabVer2 carregarVer2;
      private ConfiguradorValidador configuradorValidador;

      public TarefaBackgroundGab() {
         Objects.requireNonNull(frmSelecionaDiretorioGab.this);
         super();
         this.formatoLabels = new HashMap();
         this.conteudoLabels = new HashMap();
         this.numberFormat = NumberFormat.getNumberInstance();
         this.configuradorValidador = (ConfiguradorValidador)frmSelecionaDiretorioGab.this.app.getBean(ConfiguradorValidador.class);

         try {
            this.errorReportCmd = (ErrorReportCmd)frmSelecionaDiretorioGab.this.app.getBean(ErrorReportCmd.class);
            this.errorReportCmd.setMessageConsumer(this);
            this.getAtendimentoGabCmd = (GetAtendimentoGabCmd)frmSelecionaDiretorioGab.this.app.getBean(GetAtendimentoGabCmd.class);
            this.atendimentoGab = this.getAtendimentoGabCmd.get(frmSelecionaDiretorioGab.this.context.getNumeroCaso());
            switch (this.configuradorValidador.getVersaoLayoutGab()) {
               case 1:
                  this.carregar = (CarregarGab)frmSelecionaDiretorioGab.this.app.getBean(CarregarGab.class);
                  this.carregar.setDesempenho(frmSelecionaDiretorioGab.this.desempenho);
                  break;
               case 2:
                  this.carregarVer2 = (CarregarGabVer2)frmSelecionaDiretorioGab.this.app.getBean(CarregarGabVer2.class);
                  this.carregarVer2.setDesempenho(frmSelecionaDiretorioGab.this.desempenho);
            }

            this.formatoLabels.put(NomeArquivoGab.GAB109, frmSelecionaDiretorioGab.this.gab109FormatoLabel);
            this.formatoLabels.put(NomeArquivoGab.GAB112, frmSelecionaDiretorioGab.this.gab112FormatoLabel);
            this.formatoLabels.put(NomeArquivoGab.GAB800, frmSelecionaDiretorioGab.this.gab800FormatoLabel);
            this.conteudoLabels.put(NomeArquivoGab.GAB109, frmSelecionaDiretorioGab.this.gab109ConteudoLabel);
            this.conteudoLabels.put(NomeArquivoGab.GAB112, frmSelecionaDiretorioGab.this.gab112ConteudoLabel);
            this.conteudoLabels.put(NomeArquivoGab.GAB800, frmSelecionaDiretorioGab.this.gab800ConteudoLabel);
         } catch (Exception e) {
            frmSelecionaDiretorioGab.log.error("Erro bean", e);
         }

      }

      public void doPublish(Desempenho desempenho) {
         this.publish(new Desempenho[]{desempenho});
      }

      public void progress() {
         this.publish(new Desempenho[]{frmSelecionaDiretorioGab.this.desempenho});
      }

      public void setFormatoValido(NomeArquivoGab nomeArquivo, boolean valido) {
         if (this.formatoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.formatoLabels.get(nomeArquivo);
            label.setIcon(frmSelecionaDiretorioGab.this.okLabel.getIcon());
            label.setIcon(valido ? frmSelecionaDiretorioGab.this.okLabel.getIcon() : frmSelecionaDiretorioGab.this.erroLabel.getIcon());
         }

      }

      public void setConteudoValido(NomeArquivoGab nomeArquivo, boolean valido) {
         if (this.conteudoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.conteudoLabels.get(nomeArquivo);
            label.setIcon(valido ? frmSelecionaDiretorioGab.this.okLabel.getIcon() : frmSelecionaDiretorioGab.this.erroLabel.getIcon());
         }

      }

      public void startLoad(NomeArquivoGab nomeArquivo) {
         if (this.formatoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.formatoLabels.get(nomeArquivo);
            label.setIcon(frmSelecionaDiretorioGab.this.processingLabel.getIcon());
         }

      }

      public void startValidation(NomeArquivoGab nomeArquivo) {
         if (this.conteudoLabels.containsKey(nomeArquivo)) {
            JLabel label = (JLabel)this.conteudoLabels.get(nomeArquivo);
            label.setIcon(frmSelecionaDiretorioGab.this.processingLabel.getIcon());
         }

      }

      public void addMessage(String msg) {
         frmSelecionaDiretorioGab.this.detalhesTextArea.append(msg + "\n");
      }

      public void message(String msg) {
         this.addMessage(msg);
      }

      protected Void doInBackground() {
         Stopwatch stopwatch = Stopwatch.createStarted();
         boolean arquivosExistem = false;

         label258: {
            Object var4;
            try {
               frmSelecionaDiretorioGab.this.detalhesTextArea.setText("");
               frmSelecionaDiretorioGab.this.zeraIcones();
               frmSelecionaDiretorioGab.this.excluiRelatorio();
               StringBuilder message = new StringBuilder();
               arquivosExistem = frmSelecionaDiretorioGab.this.controlador.verificarExistenciaArquivosGab(this.pastaArquivosValidacao, frmSelecionaDiretorioGab.this.context.getNumeroCaso(), message);
               if (arquivosExistem) {
                  String horaInicioMsg = "Hora início validação: " + Util.getDataAtual("dd/MM/yyyy HH:mm:ss");
                  frmSelecionaDiretorioGab.log.info(horaInicioMsg);
                  this.addMessage(message.toString());
                  if (!frmSelecionaDiretorioGab.this.controlador.checkVersaoSistemaBanco()) {
                     StringBuilder msg = new StringBuilder();
                     msg.append("A versão do sistema (");
                     msg.append(frmSelecionaDiretorioGab.this.configurador.getVersao());
                     msg.append(") é diferente da versão do cadastro de atendimento (");
                     msg.append(frmSelecionaDiretorioGab.this.controlador.getVersaoSistemaBanco());
                     msg.append("). Para solucionar este problema, clique no botão Voltar, selecione o caso, clique em Ver Detalhes e grave o caso novamente.");
                     String msgString = msg.toString();
                     frmSelecionaDiretorioGab.log.info(msgString);
                     ViewUtil.showWarnDialog(msgString);
                     Object var20 = null;
                     return (Void)var20;
                  }

                  ArquivosGab a = this.getArquivoBean();
                  Computador computador = frmSelecionaDiretorioGab.this.controlador.getComputador(frmSelecionaDiretorioGab.this.context.getNumeroComputador());
                  frmSelecionaDiretorioGab.this.gerarHashArquivosCmd.gerar(frmSelecionaDiretorioGab.this.controlador, computador, a);
                  CopiarArquivoParaPastaTxtCmd copiarArquivoParaPastaTxtCmd = (CopiarArquivoParaPastaTxtCmd)frmSelecionaDiretorioGab.this.app.getBean(CopiarArquivoParaPastaTxtCmd.class);
                  copiarArquivoParaPastaTxtCmd.copiarGab(frmSelecionaDiretorioGab.this.context.getNumeroCaso(), a);
                  frmSelecionaDiretorioGab.this.controlador.verificarArquivosGabVazios(a, this.errorReportCmd);
                  if (this.errorReportCmd.hasErros()) {
                     this.finishWithError();
                     Object var22 = null;
                     return (Void)var22;
                  }

                  switch (this.configuradorValidador.getVersaoLayoutGab()) {
                     case 1 -> this.carregar.carregarGab(this.atendimentoGab, a);
                     case 2 -> this.carregarVer2.carregarGab(this.atendimentoGab, a);
                  }

                  if (this.errorReportCmd.hasErros()) {
                     this.finishWithError();
                     Object var21 = null;
                     return (Void)var21;
                  }

                  NomeArquivoGab[] nomeArquivos = NomeArquivoGab.values();

                  for(int i = 0; i < nomeArquivos.length; ++i) {
                     NomeArquivoGab nomeArquivo = nomeArquivos[i];
                     boolean valido = frmSelecionaDiretorioGab.this.controlador.validarConteudoGab(nomeArquivo, frmSelecionaDiretorioGab.this.desempenho.tarefaBackgroundGab);
                     this.setConteudoValido(nomeArquivo, valido);
                     if (!valido) {
                        this.addMessage("Arquivo " + String.valueOf(nomeArquivo) + ": conteúdo não passou na validação.");
                        break;
                     }

                     this.addMessage("Arquivo " + String.valueOf(nomeArquivo) + ": conteúdo validado com êxito.");
                  }

                  if (this.errorReportCmd.hasErros()) {
                     this.finishWithError();
                     Object var23 = null;
                     return (Void)var23;
                  }

                  if (!"999-OUTROS-000001-88".equals(frmSelecionaDiretorioGab.this.context.getNumeroCaso())) {
                     frmSelecionaDiretorioGab.this.continuarButton.setEnabled(true);
                  }
                  break label258;
               }

               this.addMessage(message.toString());
               var4 = null;
            } catch (Exception e) {
               arquivosExistem = false;
               frmSelecionaDiretorioGab.log.error("Erro durante a validação.", e);
               this.addMessage("Ocorreu um erro do sistema durante a validação. Verifique o log do sistema ou contate o órgão responsável pelo caso.");
               break label258;
            } finally {
               this.addMessage("Fim da verificação.");
               if (!this.errorReportCmd.hasErros() && arquivosExistem) {
                  frmSelecionaDiretorioGab.this.continuarButton.setEnabled(true);
               }

               this.errorReportCmd.reset();
               frmSelecionaDiretorioGab.this.voltarButton.setEnabled(true);
               frmSelecionaDiretorioGab.this.selecionarPastaButton.setEnabled(true);
            }

            return (Void)var4;
         }

         stopwatch.stop();
         frmSelecionaDiretorioGab.log.info("Tempo validação: {} milisegundos", stopwatch.elapsed(TimeUnit.MILLISECONDS));
         return null;
      }

      protected void finishWithError() {
         try {
            File pastaEnvio = new File(frmSelecionaDiretorioGab.this.getPastaCaso(), "envio");
            Arquivo.apaga(pastaEnvio);
            this.gerarRelatorio(this.errorReportCmd);
            this.addMessage("Relatório de inconsistências gerado.");
            frmSelecionaDiretorioGab.this.verRelatorioButton.setEnabled(true);
         } catch (Exception e) {
            frmSelecionaDiretorioGab.log.error("Erro ao gerar relatório.", e);
            File var10001 = new File(frmSelecionaDiretorioGab.this.context.getPastaCaso(), "relatorio.html");
            this.addMessage("Erro ao gerar relatório. Verifique se o arquivo não está aberto. Caminho: " + var10001.getAbsolutePath());
            frmSelecionaDiretorioGab.this.verRelatorioButton.setEnabled(false);
         }

      }

      protected ArquivosGab getArquivoBean() {
         ArquivosGab a = new ArquivosGab();

         for(File arquivo : FileUtils.listFiles(this.pastaArquivosValidacao, new SuffixFileFilter(".txt", IOCase.INSENSITIVE), FalseFileFilter.INSTANCE)) {
            if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorioGab.this.context.getNumeroCaso() + "_GAB109.TXT")) {
               a.setGab109(arquivo);
               frmSelecionaDiretorioGab.log.debug("Arquivo GAB109 utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorioGab.this.context.getNumeroCaso() + "_GAB112.TXT")) {
               a.setGab112(arquivo);
               frmSelecionaDiretorioGab.log.debug("Arquivo GAB112 utilizado: {}", arquivo.getAbsolutePath());
            } else if (arquivo.getName().toUpperCase().equals(frmSelecionaDiretorioGab.this.context.getNumeroCaso() + "_GAB800.TXT")) {
               a.setGab800(arquivo);
               frmSelecionaDiretorioGab.log.debug("Arquivo GAB800 utilizado: {}", arquivo.getAbsolutePath());
            }
         }

         return a;
      }

      protected void process(List<Desempenho> desempenhos) {
         frmSelecionaDiretorioGab.this.totalDeLinhasLbl.setText(String.valueOf(frmSelecionaDiretorioGab.this.desempenho.totalLinha));
         frmSelecionaDiretorioGab.this.linhaAtualLbl.setText(String.valueOf(frmSelecionaDiretorioGab.this.desempenho.linhaAtual));
         frmSelecionaDiretorioGab.this.linhasPorSegundoLbl.setText(this.numberFormat.format(frmSelecionaDiretorioGab.this.desempenho.linhasPorSegundo) + " l/s");
      }

      public void done() {
         frmSelecionaDiretorioGab.this.voltarButton.setEnabled(true);
         frmSelecionaDiretorioGab.this.selecionarPastaButton.setEnabled(true);
      }

      private File getReportFile() {
         File pastaDadosValidador = frmSelecionaDiretorioGab.this.configurador.getPastaDadosValidador();
         File pastaCaso = new File(pastaDadosValidador, Util.tiraBarra(frmSelecionaDiretorioGab.this.context.getNumeroCaso()));
         File arquivo = new File(pastaCaso, "relatorio.html");
         if (arquivo.exists()) {
            arquivo.delete();
         }

         return arquivo;
      }

      private void gerarRelatorio(ErrorReportCmd errorReportCmd) {
         GetAtendimentoGabCmd getAtendimentoCmd = (GetAtendimentoGabCmd)frmSelecionaDiretorioGab.this.app.getBean(GetAtendimentoGabCmd.class);
         AtendimentoGab atendimento = getAtendimentoCmd.get(frmSelecionaDiretorioGab.this.context.getNumeroCaso());
         Map<String, Object> map = new HashMap();
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         map.put("numeroCaso", atendimento.getNumeroCaso());
         map.put("dataEmissao", sdf.format(new Date()));
         map.put("numeroBanco", atendimento.getNumeroBanco());
         map.put("nomeBanco", atendimento.getNomeBanco());
         map.put("nomeFuncionario", atendimento.getNomeFuncionarioResponsavel());
         map.put("telefoneFuncionario", atendimento.getTelefoneFuncionarioResponsavel());
         map.put("emailFuncionario", atendimento.getEmailFuncionarioResponsavel());
         this.putImages(map);
         File pastaConfig = new File(frmSelecionaDiretorioGab.this.configurador.getPastaDadosValidador(), "conf");
         File pastaImagens = new File(pastaConfig, "imagens");
         File brasaoFile = Util.getBrasao(pastaImagens.getAbsolutePath(), frmSelecionaDiretorioGab.this.context.getNumeroComputador());
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
            frmSelecionaDiretorioGab.log.error("Erro ao gerar relatório de inconsistência", e);
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
            frmSelecionaDiretorioGab.log.error("Erro ao localizar image: " + path, e);
         }

      }

      private void putImage(Map<String, Object> map, String name, File arquivo) {
         try {
            byte[] byteArray = FileUtils.readFileToByteArray(arquivo);
            String image = Base64.encodeBase64String(byteArray);
            map.put(name, image);
         } catch (IOException e) {
            frmSelecionaDiretorioGab.log.error("Erro ao localizar image: " + arquivo.getAbsolutePath(), e);
         }

      }

      private void putDadoFormatoConteudo(Map<String, Object> map) {
         List<DadoFormatoConteudo> dados = new ArrayList(5);
         NomeArquivoGab[] nomeArquivos = NomeArquivoGab.values();

         for(NomeArquivoGab nomeArquivo : nomeArquivos) {
            if (this.formatoLabels.containsKey(nomeArquivo)) {
               DadoFormatoConteudo dadoFormatoConteudo = frmSelecionaDiretorioGab.this.new DadoFormatoConteudo();
               dadoFormatoConteudo.nomeArquivo = nomeArquivo;
               if (((JLabel)this.formatoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorioGab.this.okLabel.getIcon())) {
                  dadoFormatoConteudo.formato = "ok";
               } else if (((JLabel)this.formatoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorioGab.this.erroLabel.getIcon())) {
                  dadoFormatoConteudo.formato = "erro";
               } else {
                  dadoFormatoConteudo.formato = "wait";
               }

               if (((JLabel)this.conteudoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorioGab.this.okLabel.getIcon())) {
                  dadoFormatoConteudo.conteudo = "ok";
               } else if (((JLabel)this.conteudoLabels.get(nomeArquivo)).getIcon().equals(frmSelecionaDiretorioGab.this.erroLabel.getIcon())) {
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
         return frmSelecionaDiretorioGab.this.desempenho;
      }

      public void setPastaArquivosValidacao(File pastaArquivosValidacao) {
         this.pastaArquivosValidacao = pastaArquivosValidacao;
      }

      public void increment(NomeArquivoGab nomeArquivo) {
      }

      public void total(NomeArquivoGab gab112, int i) {
      }
   }

   public interface ViewGab {
      void progress();

      void startValidation(NomeArquivoGab nomeArquivo);

      void setFormatoValido(NomeArquivoGab nomeArquivo, boolean valido);

      void setConteudoValido(NomeArquivoGab nomeArquivo, boolean valido);

      void increment(NomeArquivoGab nomeArquivo);

      void total(NomeArquivoGab gab112, int i);

      void startLoad(NomeArquivoGab nomeArquivo);

      void addMessage(String msg);
   }
}
