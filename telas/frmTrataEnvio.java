package telas;

import br.mp.mpf.asspa.client.Arquivo;
import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.Configurador;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.enums.TipoAtendimento;
import br.mp.mpf.spea.simba.validador.ControladorValidador;
import br.mp.mpf.spea.simba.validador.cmd.GerarArquivoEnvioCmd;
import br.mp.mpf.spea.simba.validador.ui.listener.VoltarFrameInicialListener;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class frmTrataEnvio extends IconFrame {
   private static final long serialVersionUID = 1902376867844955154L;
   private static final Logger log = LoggerFactory.getLogger(frmTrataEnvio.class);
   private VoltarFrameInicialListener frmSelecionaDiretorio;
   @Inject
   private Context context;
   @Inject
   private ControladorValidador controlador;
   @Inject
   private Configurador configurador;
   @Inject
   private GerarArquivoEnvioCmd gerarArquivoEnvioCmd;
   private JButton fecharButton;
   private JButton gerarButton;
   private JButton voltarButton;
   private JLabel localizacaoPastaLabel;
   private JLabel codigoHashTituloLabel;
   private JLabel passo3Label;
   private JLabel nestePassoLabel;
   private JLabel gerarArquivoEnvioTituloLabel;
   private JLabel atencaoLabel;
   private JLabel gerarHashTituloLabel;
   private JLabel gerarArquivoEnvioLabel;
   private JLabel gerarHashLabel;
   private JLabel casoLabel;
   private JLabel codigoHashLabel;
   private JTextArea detalhesTextArea;
   private JLabel localArquivoEnvioLabel;
   private JLabel mensagemLabel;
   private JLabel okLabel;
   private JLabel erroLabel;
   private JLabel waitLabel;
   private JScrollPane mensagemScrollPane;

   public frmTrataEnvio() {
      this.initComponents();
   }

   @PostConstruct
   public void setup() {
      this.casoLabel.setText(this.context.getNumeroCaso());
      this.codigoHashLabel.setText("");
      this.detalhesTextArea.setText("");
      this.localArquivoEnvioLabel.setText("");
      this.okLabel.setVisible(false);
      this.erroLabel.setVisible(false);
      this.waitLabel.setVisible(false);
      this.zeraIcones();
   }

   public void setFrmSelecionaDiretorio(VoltarFrameInicialListener frmSelecionaDiretorio) {
      this.frmSelecionaDiretorio = frmSelecionaDiretorio;
   }

   private void zeraIcones() {
      this.gerarArquivoEnvioLabel.setIcon(this.waitLabel.getIcon());
      this.gerarHashLabel.setIcon(this.waitLabel.getIcon());
   }

   private void initComponents() {
      this.passo3Label = new JLabel();
      this.nestePassoLabel = new JLabel();
      this.gerarArquivoEnvioTituloLabel = new JLabel();
      this.gerarButton = new JButton();
      this.gerarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmTrataEnvio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmTrataEnvio.this.gerarButtonActionPerformed();
         }
      });
      this.gerarHashTituloLabel = new JLabel();
      this.gerarArquivoEnvioLabel = new JLabel();
      this.gerarHashLabel = new JLabel();
      this.voltarButton = new JButton();
      this.voltarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmTrataEnvio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmTrataEnvio.this.voltarButtonActionPerformed();
         }
      });
      this.mensagemLabel = new JLabel();
      this.okLabel = new JLabel();
      this.waitLabel = new JLabel();
      this.erroLabel = new JLabel();
      this.codigoHashTituloLabel = new JLabel();
      this.setDefaultCloseOperation(3);
      this.addWindowListener(new WindowAdapter() {
         {
            Objects.requireNonNull(frmTrataEnvio.this);
         }

         public void windowClosing(WindowEvent evt) {
            frmTrataEnvio.this.fecharButtonActionPerformed();
         }
      });
      this.getContentPane().setBackground(new Color(255, 255, 255));
      this.passo3Label.setFont(new Font("Tahoma", 1, 18));
      this.passo3Label.setForeground(new Color(102, 102, 0));
      this.passo3Label.setText("Passo 3 - Gerar arquivo para envio");
      this.nestePassoLabel.setForeground(new Color(102, 102, 0));
      this.nestePassoLabel.setHorizontalAlignment(2);
      this.nestePassoLabel.setText("<html>Neste passo o programa irá gerar arquivo(s) para envio pelo programa Transmissor</html>");
      this.nestePassoLabel.setVerticalAlignment(1);
      this.nestePassoLabel.setVerticalTextPosition(1);
      this.gerarArquivoEnvioTituloLabel.setForeground(new Color(102, 102, 0));
      this.gerarArquivoEnvioTituloLabel.setHorizontalAlignment(2);
      this.gerarArquivoEnvioTituloLabel.setText("Gerar arquivo de envio");
      this.gerarArquivoEnvioTituloLabel.setVerticalAlignment(1);
      this.gerarArquivoEnvioTituloLabel.setVerticalTextPosition(1);
      this.gerarButton.setText("Gerar");
      this.gerarHashTituloLabel.setForeground(new Color(102, 102, 0));
      this.gerarHashTituloLabel.setHorizontalAlignment(2);
      this.gerarHashTituloLabel.setText("Gerar hash");
      this.gerarHashTituloLabel.setVerticalAlignment(1);
      this.gerarHashTituloLabel.setVerticalTextPosition(1);
      this.gerarArquivoEnvioLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.gerarHashLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.voltarButton.setText("<< Voltar");
      this.mensagemLabel.setForeground(new Color(102, 102, 0));
      this.mensagemLabel.setHorizontalAlignment(2);
      this.mensagemLabel.setText("Mensagens do Sistema");
      this.mensagemLabel.setVerticalAlignment(1);
      this.mensagemLabel.setVerticalTextPosition(1);
      this.okLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_OK.jpg")));
      this.waitLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_wait.jpg")));
      this.erroLabel.setIcon(new ImageIcon(this.getClass().getResource("/telas/recursos/check_erro.jpg")));
      this.codigoHashTituloLabel.setForeground(new Color(102, 102, 0));
      this.codigoHashTituloLabel.setHorizontalAlignment(2);
      this.codigoHashTituloLabel.setText("Código HASH");
      this.codigoHashTituloLabel.setVerticalAlignment(1);
      this.codigoHashTituloLabel.setVerticalTextPosition(1);
      SpringLayout sl_jPanel1 = new SpringLayout();
      sl_jPanel1.putConstraint("North", this.passo3Label, 10, "North", this.getContentPane());
      sl_jPanel1.putConstraint("North", this.okLabel, 0, "North", this.erroLabel);
      sl_jPanel1.putConstraint("East", this.okLabel, -10, "West", this.erroLabel);
      sl_jPanel1.putConstraint("North", this.erroLabel, 0, "North", this.waitLabel);
      sl_jPanel1.putConstraint("East", this.erroLabel, -10, "West", this.waitLabel);
      sl_jPanel1.putConstraint("North", this.waitLabel, 10, "North", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.waitLabel, -10, "East", this.getContentPane());
      sl_jPanel1.putConstraint("West", this.voltarButton, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("West", this.mensagemLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("West", this.codigoHashTituloLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("North", this.gerarHashTituloLabel, 17, "South", this.gerarArquivoEnvioLabel);
      sl_jPanel1.putConstraint("North", this.gerarHashLabel, -7, "North", this.gerarHashTituloLabel);
      sl_jPanel1.putConstraint("West", this.gerarHashLabel, 0, "West", this.gerarArquivoEnvioLabel);
      sl_jPanel1.putConstraint("West", this.gerarHashTituloLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("North", this.gerarArquivoEnvioLabel, -7, "North", this.gerarArquivoEnvioTituloLabel);
      sl_jPanel1.putConstraint("West", this.gerarArquivoEnvioLabel, 50, "East", this.gerarArquivoEnvioTituloLabel);
      sl_jPanel1.putConstraint("North", this.gerarArquivoEnvioTituloLabel, 10, "South", this.gerarButton);
      sl_jPanel1.putConstraint("West", this.gerarArquivoEnvioTituloLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("West", this.gerarButton, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.gerarButton, 0, "East", this.voltarButton);
      sl_jPanel1.putConstraint("West", this.nestePassoLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.nestePassoLabel, -10, "East", this.getContentPane());
      sl_jPanel1.putConstraint("West", this.passo3Label, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.passo3Label, -10, "East", this.getContentPane());
      this.getContentPane().setLayout(sl_jPanel1);
      this.getContentPane().add(this.passo3Label);
      this.casoLabel = new JLabel();
      sl_jPanel1.putConstraint("North", this.nestePassoLabel, 10, "South", this.casoLabel);
      sl_jPanel1.putConstraint("North", this.casoLabel, 10, "South", this.passo3Label);
      sl_jPanel1.putConstraint("West", this.casoLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.casoLabel, -10, "East", this.getContentPane());
      this.casoLabel.setFont(new Font("Tahoma", 1, 14));
      this.casoLabel.setForeground(new Color(102, 102, 0));
      this.casoLabel.setText("COMPUTADOR-001082");
      this.getContentPane().add(this.casoLabel);
      this.getContentPane().add(this.nestePassoLabel);
      this.atencaoLabel = new JLabel();
      sl_jPanel1.putConstraint("North", this.gerarButton, 10, "South", this.atencaoLabel);
      sl_jPanel1.putConstraint("North", this.atencaoLabel, 10, "South", this.nestePassoLabel);
      sl_jPanel1.putConstraint("West", this.atencaoLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.atencaoLabel, -10, "East", this.getContentPane());
      this.atencaoLabel.setForeground(Color.red);
      this.atencaoLabel.setHorizontalAlignment(2);
      this.atencaoLabel.setText("<html>Atenção: Não abra ou altere os arquivos gerados, pois isso caraterizará uma alteração indevida e, assim, os dados não serão aceitos.</html>");
      this.atencaoLabel.setVerticalAlignment(1);
      this.atencaoLabel.setVerticalTextPosition(1);
      this.getContentPane().add(this.atencaoLabel);
      this.getContentPane().add(this.gerarButton);
      this.getContentPane().add(this.gerarArquivoEnvioTituloLabel);
      this.getContentPane().add(this.gerarArquivoEnvioLabel);
      this.getContentPane().add(this.gerarHashTituloLabel);
      this.getContentPane().add(this.gerarHashLabel);
      this.localizacaoPastaLabel = new JLabel();
      sl_jPanel1.putConstraint("North", this.localizacaoPastaLabel, 10, "South", this.gerarHashLabel);
      sl_jPanel1.putConstraint("West", this.localizacaoPastaLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.localizacaoPastaLabel, -10, "East", this.getContentPane());
      this.localizacaoPastaLabel.setForeground(new Color(102, 102, 0));
      this.localizacaoPastaLabel.setHorizontalAlignment(2);
      this.localizacaoPastaLabel.setText("Localização da pasta dadosValidador a ser informada ao Transmissor:");
      this.localizacaoPastaLabel.setVerticalAlignment(1);
      this.localizacaoPastaLabel.setVerticalTextPosition(1);
      this.getContentPane().add(this.localizacaoPastaLabel);
      this.localArquivoEnvioLabel = new JLabel();
      sl_jPanel1.putConstraint("North", this.codigoHashTituloLabel, 10, "South", this.localArquivoEnvioLabel);
      sl_jPanel1.putConstraint("North", this.localArquivoEnvioLabel, 10, "South", this.localizacaoPastaLabel);
      sl_jPanel1.putConstraint("West", this.localArquivoEnvioLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("South", this.localArquivoEnvioLabel, 60, "South", this.localizacaoPastaLabel);
      sl_jPanel1.putConstraint("East", this.localArquivoEnvioLabel, -10, "East", this.getContentPane());
      this.localArquivoEnvioLabel.setFont(new Font("Tahoma", 1, 14));
      this.localArquivoEnvioLabel.setForeground(new Color(102, 102, 0));
      this.localArquivoEnvioLabel.setText("<html>C:\\user\\usuario\\Documentos\\Programas Simba\\dadosValidador\\001-MPF-000001-18\\Envio</html>");
      this.localArquivoEnvioLabel.setVerticalAlignment(1);
      this.getContentPane().add(this.localArquivoEnvioLabel);
      this.getContentPane().add(this.codigoHashTituloLabel);
      this.codigoHashLabel = new JLabel();
      sl_jPanel1.putConstraint("North", this.mensagemLabel, 10, "South", this.codigoHashLabel);
      sl_jPanel1.putConstraint("North", this.codigoHashLabel, 10, "South", this.codigoHashTituloLabel);
      sl_jPanel1.putConstraint("West", this.codigoHashLabel, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.codigoHashLabel, -10, "East", this.getContentPane());
      this.codigoHashLabel.setFont(new Font("Tahoma", 1, 14));
      this.codigoHashLabel.setForeground(new Color(102, 102, 0));
      this.codigoHashLabel.setText("123XPTO987AAB4CC7739GGH0789KCX");
      this.getContentPane().add(this.codigoHashLabel);
      this.getContentPane().add(this.mensagemLabel);
      this.mensagemScrollPane = new JScrollPane();
      sl_jPanel1.putConstraint("South", this.mensagemScrollPane, 110, "South", this.mensagemLabel);
      sl_jPanel1.putConstraint("North", this.voltarButton, 10, "South", this.mensagemScrollPane);
      sl_jPanel1.putConstraint("North", this.mensagemScrollPane, 10, "South", this.mensagemLabel);
      sl_jPanel1.putConstraint("West", this.mensagemScrollPane, 10, "West", this.getContentPane());
      sl_jPanel1.putConstraint("East", this.mensagemScrollPane, -10, "East", this.getContentPane());
      this.detalhesTextArea = new JTextArea();
      this.detalhesTextArea.setColumns(50);
      this.detalhesTextArea.setEditable(false);
      this.detalhesTextArea.setRows(5);
      this.mensagemScrollPane.setViewportView(this.detalhesTextArea);
      this.getContentPane().add(this.mensagemScrollPane);
      this.getContentPane().add(this.voltarButton);
      this.fecharButton = new JButton();
      sl_jPanel1.putConstraint("North", this.fecharButton, 10, "South", this.mensagemScrollPane);
      sl_jPanel1.putConstraint("East", this.fecharButton, 0, "East", this.mensagemScrollPane);
      this.fecharButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmTrataEnvio.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmTrataEnvio.this.fecharButtonActionPerformed();
         }
      });
      this.fecharButton.setText("Fechar");
      this.getContentPane().add(this.fecharButton);
      this.getContentPane().add(this.okLabel);
      this.getContentPane().add(this.erroLabel);
      this.getContentPane().add(this.waitLabel);
      this.setPreferredSize(new Dimension(550, 580));
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   private void voltarButtonActionPerformed() {
      this.setVisible(false);
      this.dispose();
      this.frmSelecionaDiretorio.setVisible(true);
   }

   private void fecharButtonActionPerformed() {
      this.setVisible(false);
      this.frmSelecionaDiretorio.onVoltarFrameInicial();
   }

   private void gerarButtonActionPerformed() {
      this.gerarButton.setEnabled(false);
      this.voltarButton.setEnabled(false);
      this.detalhesTextArea.setText("");
      this.localArquivoEnvioLabel.setText("");
      this.codigoHashLabel.setText("");
      this.zeraIcones();
      (new TarefaBackground()).execute();
   }

   private class TarefaBackground extends SwingWorker<Void, Object> {
      private String hashArquivo;

      private TarefaBackground() {
         Objects.requireNonNull(frmTrataEnvio.this);
         super();
         this.hashArquivo = "-";
      }

      protected Void doInBackground() {
         Arquivo.apaga(frmTrataEnvio.this.context.getPastaEnvio().getAbsolutePath());
         boolean isCorretora = frmTrataEnvio.this.frmSelecionaDiretorio instanceof frmSelecionaDiretorioGab;

         try {
            frmTrataEnvio.this.gerarArquivoEnvioCmd.gerar(isCorretora ? TipoAtendimento.CORRETORA : TipoAtendimento.BANCARIO);
         } catch (Exception var5) {
            frmTrataEnvio.this.zeraIcones();
            frmTrataEnvio.this.gerarArquivoEnvioLabel.setIcon(frmTrataEnvio.this.erroLabel.getIcon());
            Arquivo.apaga(frmTrataEnvio.this.context.getPastaEnvio().getAbsolutePath());
            return null;
         }

         frmTrataEnvio.this.localArquivoEnvioLabel.setText("<html>" + frmTrataEnvio.this.configurador.getPastaCasos().getAbsolutePath() + "dadosValidador</html>");
         frmTrataEnvio.log.info("Aquivo de envio gerado com êxito.");
         frmTrataEnvio.this.gerarArquivoEnvioLabel.setIcon(frmTrataEnvio.this.okLabel.getIcon());
         if (!frmTrataEnvio.this.controlador.gerarHashArquivoZIP()) {
            frmTrataEnvio.this.gerarHashLabel.setIcon(frmTrataEnvio.this.erroLabel.getIcon());
         }

         frmTrataEnvio.log.info("Aquivo de HASH gerado com êxito.");
         frmTrataEnvio.this.gerarHashLabel.setIcon(frmTrataEnvio.this.okLabel.getIcon());
         File arquivoHash = new File(frmTrataEnvio.this.context.getPastaEnvio(), Util.tiraBarra(frmTrataEnvio.this.context.getNumeroCaso()) + ".zip.hash");

         try {
            this.hashArquivo = (new Arquivo(arquivoHash)).getLinha();
         } catch (IOException e) {
            frmTrataEnvio.log.error("Erro ao ler arquivo hash do atendimento: " + arquivoHash.getAbsolutePath(), e);
         }

         frmTrataEnvio.this.configurador.setConfig(frmTrataEnvio.this.context.getNumeroCaso(), frmTrataEnvio.this.configurador.getVersao());
         frmTrataEnvio.this.gerarButton.setEnabled(false);
         return null;
      }

      protected void process(List<Object> mensagens) {
      }

      public void done() {
         frmTrataEnvio.this.voltarButton.setEnabled(true);
         frmTrataEnvio.this.codigoHashLabel.setText(this.hashArquivo);
      }
   }
}
