package telas;

import br.mp.mpf.asspa.client.Util;
import br.mp.mpf.pgr.client.ui.IconFrame;
import br.mp.mpf.pgr.client.ui.UpperCaseMaskFormatter;
import br.mp.mpf.pgr.util.ViewUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

public class frmInvestigado extends IconFrame {
   private static final long serialVersionUID = -137148621280698602L;
   private static final Color FONT_COLOR = new Color(102, 102, 0);
   private frmDadosAtendimento frmDadosAtendimento;
   private List<String> cpfCnpjCadastrados;
   private JPanel mainPanel;
   private JRadioButton pessoaFisicaRadioButton;
   private JRadioButton pessoaJuridicaRadioButton;
   private JFormattedTextField cpfTextField;
   private JFormattedTextField cnpjTextField;
   private JTextField nomeTextField;
   private JRadioButton naoRadioButton;
   private JRadioButton simRadioButton;
   private JPanel tipoRelacionamentoPanel;
   private JCheckBox contaCheckBox;
   private JCheckBox bdvCheckBox;
   private JScrollPane observacaoScroll;
   private JTextArea observacaoTextField;
   private JFormattedTextField dataInicioTextField;
   private JFormattedTextField dataFimTextField;
   private JButton salvarButton;
   private JButton cancelarButton;

   public frmInvestigado(frmDadosAtendimento frmDadosAtendimento) {
      this.frmDadosAtendimento = frmDadosAtendimento;
      this.setDefaultCloseOperation(2);
      this.setTitle("Investigado");
      this.setBackground(Color.WHITE);
      this.addWindowListener(new WindowAdapter() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void windowClosing(WindowEvent evt) {
            frmInvestigado.this.cancelarButtonClick();
         }
      });
      this.mainPanel = (JPanel)this.getContentPane();
      this.mainPanel.setBackground(Color.WHITE);
      SpringLayout mainLayout = new SpringLayout();
      this.mainPanel.setLayout(mainLayout);
      this.pessoaFisicaRadioButton = new JRadioButton("Pessoa Física");
      mainLayout.putConstraint("North", this.pessoaFisicaRadioButton, 10, "North", this.getContentPane());
      mainLayout.putConstraint("West", this.pessoaFisicaRadioButton, 10, "West", this.getContentPane());
      this.pessoaFisicaRadioButton.setSelected(true);
      this.pessoaFisicaRadioButton.setBackground(Color.WHITE);
      this.pessoaFisicaRadioButton.setForeground(FONT_COLOR);
      this.pessoaFisicaRadioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.pessoaFisicaRadioButtonClick();
         }
      });
      this.mainPanel.add(this.pessoaFisicaRadioButton);
      this.pessoaJuridicaRadioButton = new JRadioButton("Pessoa Jurídica");
      mainLayout.putConstraint("North", this.pessoaJuridicaRadioButton, 0, "North", this.pessoaFisicaRadioButton);
      mainLayout.putConstraint("West", this.pessoaJuridicaRadioButton, 15, "East", this.pessoaFisicaRadioButton);
      this.pessoaJuridicaRadioButton.setBackground(Color.WHITE);
      this.pessoaJuridicaRadioButton.setForeground(FONT_COLOR);
      this.pessoaJuridicaRadioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.pessoaJurificaRadioButtonClick();
         }
      });
      this.mainPanel.add(this.pessoaJuridicaRadioButton);
      ButtonGroup pessoaButtonGroup = new ButtonGroup();
      pessoaButtonGroup.add(this.pessoaFisicaRadioButton);
      pessoaButtonGroup.add(this.pessoaJuridicaRadioButton);
      JLabel cpfCnpjLabel = new JLabel("CPF/CNPJ:");
      mainLayout.putConstraint("North", cpfCnpjLabel, 10, "South", this.pessoaFisicaRadioButton);
      mainLayout.putConstraint("West", cpfCnpjLabel, 0, "West", this.pessoaFisicaRadioButton);
      cpfCnpjLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(cpfCnpjLabel);

      try {
         this.cpfTextField = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
         mainLayout.putConstraint("North", this.cpfTextField, -3, "North", cpfCnpjLabel);
         mainLayout.putConstraint("West", this.cpfTextField, 15, "East", cpfCnpjLabel);
         mainLayout.putConstraint("East", this.cpfTextField, 0, "East", this.pessoaJuridicaRadioButton);
      } catch (ParseException var21) {
         ViewUtil.showInfoDialog("CPF inválido. Use 11 números.");
      }

      this.cpfTextField.setToolTipText("Números que compõem o CPF do investigado.");
      this.mainPanel.add(this.cpfTextField);

      try {
         this.cnpjTextField = new JFormattedTextField(new MaskFormatter("**.***.***/####-##"));
         UpperCaseMaskFormatter.apply(this.cnpjTextField);
         mainLayout.putConstraint("North", this.cnpjTextField, -3, "North", cpfCnpjLabel);
         mainLayout.putConstraint("West", this.cnpjTextField, 15, "East", cpfCnpjLabel);
         mainLayout.putConstraint("East", this.cnpjTextField, 0, "East", this.pessoaJuridicaRadioButton);
      } catch (ParseException var20) {
         ViewUtil.showInfoDialog("CNPJ inválido. Use 14 caracteres.");
      }

      this.cnpjTextField.setVisible(false);
      this.cnpjTextField.setToolTipText("Números que compõem o CNPJ do investigado.");
      this.mainPanel.add(this.cnpjTextField);
      JLabel digiteApenasLabel = new JLabel("Digite apenas números");
      mainLayout.putConstraint("North", digiteApenasLabel, 0, "North", cpfCnpjLabel);
      mainLayout.putConstraint("West", digiteApenasLabel, 15, "East", this.cnpjTextField);
      digiteApenasLabel.setForeground(Color.red);
      this.mainPanel.add(digiteApenasLabel);
      JLabel nomeLabel = new JLabel("Nome:");
      mainLayout.putConstraint("North", nomeLabel, 15, "South", cpfCnpjLabel);
      mainLayout.putConstraint("West", nomeLabel, 0, "West", cpfCnpjLabel);
      nomeLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(nomeLabel);
      this.nomeTextField = new JTextField();
      mainLayout.putConstraint("North", this.nomeTextField, -3, "North", nomeLabel);
      mainLayout.putConstraint("West", this.nomeTextField, 0, "West", this.cpfTextField);
      this.nomeTextField.setToolTipText("Nome completo do investigado, sem abrebiações.(80 posições)");
      this.mainPanel.add(this.nomeTextField);
      String oInvestigadoTexto = "O investigado teve relacionamento com esta instituição no período do afastamento de sigilo?";
      JLabel oInvestigadoLabel = new JLabel(oInvestigadoTexto);
      mainLayout.putConstraint("East", this.nomeTextField, 0, "East", oInvestigadoLabel);
      mainLayout.putConstraint("North", oInvestigadoLabel, 15, "South", nomeLabel);
      mainLayout.putConstraint("West", oInvestigadoLabel, 0, "West", nomeLabel);
      oInvestigadoLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(oInvestigadoLabel);
      this.naoRadioButton = new JRadioButton("Não");
      mainLayout.putConstraint("North", this.naoRadioButton, 10, "South", oInvestigadoLabel);
      mainLayout.putConstraint("West", this.naoRadioButton, 0, "West", oInvestigadoLabel);
      this.naoRadioButton.setSelected(true);
      this.naoRadioButton.setBackground(Color.WHITE);
      this.naoRadioButton.setForeground(FONT_COLOR);
      this.naoRadioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.habilitaNAO();
         }
      });
      this.mainPanel.add(this.naoRadioButton);
      this.simRadioButton = new JRadioButton("Sim");
      mainLayout.putConstraint("North", this.simRadioButton, 0, "North", this.naoRadioButton);
      mainLayout.putConstraint("West", this.simRadioButton, 15, "East", this.naoRadioButton);
      this.simRadioButton.setBackground(Color.WHITE);
      this.simRadioButton.setForeground(FONT_COLOR);
      this.simRadioButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.habilitaSIM();
         }
      });
      this.mainPanel.add(this.simRadioButton);
      ButtonGroup btnGroupRelacionamento = new ButtonGroup();
      btnGroupRelacionamento.add(this.naoRadioButton);
      btnGroupRelacionamento.add(this.simRadioButton);
      SpringLayout tipoRelacionamentoLayout = new SpringLayout();
      this.tipoRelacionamentoPanel = new JPanel(tipoRelacionamentoLayout);
      mainLayout.putConstraint("North", this.tipoRelacionamentoPanel, -3, "North", this.simRadioButton);
      mainLayout.putConstraint("West", this.tipoRelacionamentoPanel, 15, "East", this.simRadioButton);
      mainLayout.putConstraint("South", this.tipoRelacionamentoPanel, 50, "North", this.simRadioButton);
      mainLayout.putConstraint("East", this.tipoRelacionamentoPanel, 0, "East", oInvestigadoLabel);
      this.tipoRelacionamentoPanel.setVisible(false);
      this.tipoRelacionamentoPanel.setBackground(Color.WHITE);
      String tituloTipoRelacionamento = "Tipo de Relacionamento";
      TitledBorder tipoRelacionametnoBorder = BorderFactory.createTitledBorder((Border)null, tituloTipoRelacionamento, 0, 0, (Font)null, FONT_COLOR);
      this.tipoRelacionamentoPanel.setBorder(tipoRelacionametnoBorder);
      this.mainPanel.add(this.tipoRelacionamentoPanel);
      this.contaCheckBox = new JCheckBox("Conta Depósito", false);
      tipoRelacionamentoLayout.putConstraint("North", this.contaCheckBox, 2, "North", this.tipoRelacionamentoPanel);
      tipoRelacionamentoLayout.putConstraint("West", this.contaCheckBox, 10, "West", this.tipoRelacionamentoPanel);
      this.contaCheckBox.setBackground(Color.WHITE);
      this.contaCheckBox.setForeground(FONT_COLOR);
      this.tipoRelacionamentoPanel.add(this.contaCheckBox);
      this.bdvCheckBox = new JCheckBox("B/D/V - Bens, Direitos ou Valores", false);
      tipoRelacionamentoLayout.putConstraint("North", this.bdvCheckBox, 0, "North", this.contaCheckBox);
      tipoRelacionamentoLayout.putConstraint("West", this.bdvCheckBox, 15, "East", this.contaCheckBox);
      this.bdvCheckBox.setBackground(Color.WHITE);
      this.bdvCheckBox.setForeground(FONT_COLOR);
      this.tipoRelacionamentoPanel.add(this.bdvCheckBox);
      JLabel outrasInformacoesLabel = new JLabel("Outras informações julgadas úteis sobre este investigado:");
      mainLayout.putConstraint("North", outrasInformacoesLabel, 10, "South", this.tipoRelacionamentoPanel);
      mainLayout.putConstraint("West", outrasInformacoesLabel, 0, "West", this.naoRadioButton);
      outrasInformacoesLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(outrasInformacoesLabel);
      this.observacaoTextField = new JTextArea();
      this.observacaoTextField.setColumns(20);
      this.observacaoTextField.setRows(10);
      this.observacaoTextField.setToolTipText("Campo livre para descrição de algum esclarecimento, caso seja necessário.");
      this.observacaoScroll = new JScrollPane();
      mainLayout.putConstraint("North", this.observacaoScroll, 5, "South", outrasInformacoesLabel);
      mainLayout.putConstraint("West", this.observacaoScroll, 0, "West", outrasInformacoesLabel);
      mainLayout.putConstraint("East", this.observacaoScroll, -10, "East", this.getContentPane());
      this.observacaoScroll.setViewportView(this.observacaoTextField);
      this.mainPanel.add(this.observacaoScroll);
      JLabel preenchaDatasLabel = new JLabel();
      mainLayout.putConstraint("South", this.observacaoScroll, -10, "North", preenchaDatasLabel);
      mainLayout.putConstraint("West", preenchaDatasLabel, 0, "West", this.observacaoScroll);
      mainLayout.putConstraint("East", preenchaDatasLabel, -10, "East", this.getContentPane());
      preenchaDatasLabel.setForeground(FONT_COLOR);
      preenchaDatasLabel.setText("<html>Preencha as datas abaixo, caso este investigado tenha um período de afastamento diferente do informado para o caso.</html>");
      this.mainPanel.add(preenchaDatasLabel);
      JLabel dataInicioLabel = new JLabel("Início:");
      mainLayout.putConstraint("South", preenchaDatasLabel, -10, "North", dataInicioLabel);
      mainLayout.putConstraint("West", dataInicioLabel, 0, "West", preenchaDatasLabel);
      dataInicioLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(dataInicioLabel);

      try {
         this.dataInicioTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
         mainLayout.putConstraint("North", this.dataInicioTextField, -3, "North", dataInicioLabel);
         mainLayout.putConstraint("West", this.dataInicioTextField, 10, "East", dataInicioLabel);
         mainLayout.putConstraint("East", this.dataInicioTextField, 90, "East", dataInicioLabel);
      } catch (ParseException var19) {
         ViewUtil.showInfoDialog("Formato Inválido. Use dd/mm/aaaa");
      }

      this.dataInicioTextField.setToolTipText("Data inicial de afastamento de sigilo conforme ofício da autoridiade judicial.");
      this.mainPanel.add(this.dataInicioTextField);
      JLabel dataFimLabel = new JLabel("Fim:");
      mainLayout.putConstraint("North", dataFimLabel, 0, "North", dataInicioLabel);
      mainLayout.putConstraint("West", dataFimLabel, 15, "East", this.dataInicioTextField);
      dataFimLabel.setForeground(FONT_COLOR);
      this.mainPanel.add(dataFimLabel);

      try {
         this.dataFimTextField = new JFormattedTextField(new MaskFormatter("##/##/####"));
         mainLayout.putConstraint("North", this.dataFimTextField, -3, "North", dataFimLabel);
         mainLayout.putConstraint("West", this.dataFimTextField, 10, "East", dataFimLabel);
         mainLayout.putConstraint("East", this.dataFimTextField, 90, "East", dataFimLabel);
      } catch (ParseException var18) {
         ViewUtil.showInfoDialog("Formato Inválido. Use dd/mm/aaaa");
      }

      this.dataFimTextField.setToolTipText("Data final de afastamento de sigilo conforme ofício da autoridiade judicial.");
      this.mainPanel.add(this.dataFimTextField);
      this.salvarButton = new JButton("Salvar");
      mainLayout.putConstraint("South", dataInicioLabel, -10, "North", this.salvarButton);
      this.salvarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.salvarButtonMouseReleased();
         }
      });
      this.mainPanel.add(this.salvarButton);
      this.cancelarButton = new JButton();
      mainLayout.putConstraint("North", this.salvarButton, 0, "North", this.cancelarButton);
      mainLayout.putConstraint("East", this.salvarButton, -10, "West", this.cancelarButton);
      mainLayout.putConstraint("South", this.cancelarButton, -10, "South", this.getContentPane());
      mainLayout.putConstraint("East", this.cancelarButton, -10, "East", this.getContentPane());
      this.cancelarButton.setText("Cancelar");
      this.cancelarButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(frmInvestigado.this);
         }

         public void actionPerformed(ActionEvent e) {
            frmInvestigado.this.cancelarButtonClick();
         }
      });
      this.mainPanel.add(this.cancelarButton);
      this.setMinimumSize(new Dimension(480, 400));
      this.setSize(new Dimension(480, 400));
      this.pack();
      this.setLocationRelativeTo((Component)null);
   }

   public void setCpfCnpjCadastrados(List<String> cpfCnpjCadastrados) {
      this.cpfCnpjCadastrados = cpfCnpjCadastrados;
   }

   private void pessoaFisicaRadioButtonClick() {
      this.cpfTextField.setVisible(true);
      this.cnpjTextField.setVisible(false);
   }

   private void pessoaJurificaRadioButtonClick() {
      this.cpfTextField.setVisible(false);
      this.cnpjTextField.setVisible(true);
   }

   private void habilitaSIM() {
      this.tipoRelacionamentoPanel.setVisible(true);
   }

   private void habilitaNAO() {
      this.tipoRelacionamentoPanel.setVisible(false);
   }

   private void salvarButtonMouseReleased() {
      String retorno = this.validaCamposObrigatorios();
      if (!retorno.equalsIgnoreCase("")) {
         ViewUtil.showErrorDialog(retorno);
      } else {
         String cpfCnpj;
         if (this.pessoaFisicaRadioButton.isSelected()) {
            cpfCnpj = this.cpfTextField.getText().trim();
         } else {
            cpfCnpj = this.cnpjTextField.getText().trim();
         }

         cpfCnpj = Util.tiraFormato(cpfCnpj).toUpperCase();
         if (this.temCpfCnpjRepetido(cpfCnpj)) {
            ViewUtil.showErrorDialog("Este CPF/CNPJ já foi cadastrado.");
         } else {
            String tipoPessoa = this.pessoaFisicaRadioButton.isSelected() ? "PF" : "PJ";
            String nome = this.nomeTextField.getText().trim().toUpperCase();
            String tipoRelacionamento = this.simRadioButton.isSelected() ? "Sim" : "Não";
            String temConta = this.contaCheckBox.isSelected() ? "Sim" : "Não";
            String temBdv = this.bdvCheckBox.isSelected() ? "Sim" : "Não";
            String observacao = this.observacaoTextField.getText().trim().toUpperCase();
            String dataInicio = this.dataInicioTextField.getText();
            String dataFim = this.dataFimTextField.getText();
            this.frmDadosAtendimento.adicionarInvestigado(tipoPessoa, cpfCnpj, nome, tipoRelacionamento, temConta, temBdv, observacao, dataInicio, dataFim);
            this.cancelarButtonClick();
         }
      }
   }

   private void cancelarButtonClick() {
      this.frmDadosAtendimento.setEnabled(true);
      this.setVisible(false);
      this.dispose();
   }

   private String validaCamposObrigatorios() {
      String retorno = "";
      boolean isPessoaFisica = this.pessoaFisicaRadioButton.isSelected();
      if (isPessoaFisica) {
         String cpf = this.cpfTextField.getText().trim();
         if (cpf.length() != 14) {
            retorno = retorno + "<br>O CPF deve ser preenchido com 11 números.";
         } else if (!Util.valida_CpfCnpj(Util.tiraFormato(cpf))) {
            retorno = retorno + "<br>O CPF não está correto.";
         }
      }

      boolean isPessoaJuridica = this.pessoaJuridicaRadioButton.isSelected();
      if (isPessoaJuridica) {
         String cnpj = this.cnpjTextField.getText().trim();
         if (cnpj.length() != 18) {
            retorno = retorno + "<br>O CNPJ deve ser preenchido com 14 caracteres.";
         } else if (!Util.valida_CpfCnpj(Util.tiraFormato(cnpj))) {
            retorno = retorno + "<br>O CNPJ não está correto.";
         }
      }

      if (!this.simRadioButton.isSelected() && !this.naoRadioButton.isSelected()) {
         retorno = retorno + "<br>O tipo de relationamento deve ser selecionado.";
      }

      String nome = this.nomeTextField.getText().trim();
      if (nome.isEmpty()) {
         retorno = retorno + "<br>O Nome do investigado é obrigatório.";
      }

      if (this.nomeTextField.getText().length() > 80) {
         retorno = retorno + "<br>O Nome do investigado não pode ter mais de 80 caracteres.";
      }

      if (this.observacaoTextField.getText().trim().length() > 500) {
         retorno = retorno + "<br>O Nome do investigado não pode ter mais de 500 caracteres.";
      }

      if (this.simRadioButton.isSelected() && !this.bdvCheckBox.isSelected() && !this.contaCheckBox.isSelected()) {
         retorno = "<br>Se o investigado possuía relacionamento, informe se era do tipo Conta, B/D/V ou ambos.";
      }

      retorno = retorno + Util.validaDataInicioEFim(this.dataInicioTextField.getText().trim(), this.dataFimTextField.getText().trim());
      if (retorno.indexOf("<br>") == 0) {
         retorno = retorno.substring(4);
      }

      return retorno;
   }

   private boolean temCpfCnpjRepetido(String cpfCnpjDigitado) {
      for(String cpfCnpjCadastrado : this.cpfCnpjCadastrados) {
         if (cpfCnpjDigitado.trim().equals(cpfCnpjCadastrado)) {
            return true;
         }
      }

      return false;
   }
}
