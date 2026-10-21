package br.mp.mpf.spea.simba.validador.ui;

import br.mp.mpf.asspa.client.Certificado;
import br.mp.mpf.asspa.client.exception.NegocioException;
import br.mp.mpf.pgr.client.Context;
import br.mp.mpf.pgr.client.comparator.LongComparator;
import br.mp.mpf.pgr.client.ui.listener.ArquivoChaveChangedListener;
import br.mp.mpf.pgr.enums.ArquivoTransmissaoPermitido;
import br.mp.mpf.pgr.filefilter.ArquivosTransmissaoDocumentosFileFilter;
import br.mp.mpf.pgr.io.ByteFormatter;
import br.mp.mpf.pgr.util.Config;
import br.mp.mpf.pgr.util.StringUtil;
import br.mp.mpf.spea.simba.validador.cmd.VerificarArquivosPermitidosTransmissaoCmd;
import br.mp.mpf.spea.simba.validador.to.Documento;
import br.mp.mpf.spea.simba.validador.to.TipoDocumento;
import br.mp.mpf.spea.simba.validador.ui.listener.ListaDocumentoChangedListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabelaArquivosPanel extends JPanel implements ArquivoChaveChangedListener {
   public static final Logger log = LoggerFactory.getLogger(TabelaArquivosPanel.class);
   private static final String chavePastaUltimoArquivoAdicionadoKey = "pasta.ultimo.arquivo.adicionado";
   public static final FileFilter arquivoTransmissaoDocumentosFileFilter = new ArquivosTransmissaoDocumentosFileFilter();
   private static final int DOCUMENTO_COLUMN = 0;
   private static final int TIPO_COLUMN = 1;
   private static final int TAMANHO_COLUMN = 2;
   private static final int HASH_COLUMN = 3;
   private static final int OBSERVACAO_COLUMN = 4;
   private Config config;
   private Context context;
   private VerificarArquivosPermitidosTransmissaoCmd verificarArquivosPermitidosTransmissaoCmd;
   private ListaDocumentoChangedListener listener;
   private List<Documento> documentos = new ArrayList(100);
   private List<String> nomeArquivos = new ArrayList();
   private static final long serialVersionUID = 66481564918122227L;
   private JTable arquivosTable;
   private JButton adicionarArquivosBtn;
   private JScrollPane arquivosTableScrollPane;
   private JLabel etapa2Label;
   private JButton removerButton;

   public TabelaArquivosPanel(Config config, Context context, VerificarArquivosPermitidosTransmissaoCmd verificarArquivosPermitidosTransmissaoCmd) {
      this.config = config;
      this.context = context;
      this.verificarArquivosPermitidosTransmissaoCmd = verificarArquivosPermitidosTransmissaoCmd;
      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);
      this.etapa2Label = new JLabel("2. Adicione os arquivos:");
      layout.putConstraint("North", this.etapa2Label, 10, "North", this);
      layout.putConstraint("West", this.etapa2Label, 0, "West", this);
      this.etapa2Label.setFont(new Font("Tahoma", 0, 12));
      this.add(this.etapa2Label);
      this.adicionarArquivosBtn = new JButton("Adicionar...");
      this.adicionarArquivosBtn.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public void actionPerformed(ActionEvent e) {
            TabelaArquivosPanel.this.adicionarAction();
         }
      });
      this.adicionarArquivosBtn.setEnabled(false);
      layout.putConstraint("North", this.adicionarArquivosBtn, -3, "North", this.etapa2Label);
      this.add(this.adicionarArquivosBtn);
      this.removerButton = new JButton("Remover");
      this.removerButton.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public void actionPerformed(ActionEvent e) {
            TabelaArquivosPanel.this.removerAction();
         }
      });
      layout.putConstraint("East", this.removerButton, -10, "East", this);
      this.removerButton.setEnabled(false);
      layout.putConstraint("East", this.adicionarArquivosBtn, -10, "West", this.removerButton);
      layout.putConstraint("North", this.removerButton, 0, "North", this.adicionarArquivosBtn);
      this.add(this.removerButton);
      this.arquivosTable = new JTable() {
         private static final long serialVersionUID = 2498899220752052738L;

         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public Component prepareEditor(TableCellEditor editor, int row, int column) {
            Component component = super.prepareEditor(editor, row, column);
            if (component instanceof JComponent) {
               ((JComponent)component).setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            }

            return component;
         }

         public TableCellEditor getCellEditor(int row, int column) {
            TableColumn tableColumn = this.getColumnModel().getColumn(column);
            TableCellEditor editor = tableColumn.getCellEditor();
            if (editor == null) {
               editor = this.getDefaultEditor(this.getColumnClass(column));
               if (column == 4) {
                  editor.addCellEditorListener(new CellEditorListener() {
                     // $FF: synthetic field
                     final <undefinedtype> this$1;

                     {
                        Objects.requireNonNull(this$1);
                        this.this$1 = this$1;
                        super();
                     }

                     public void editingStopped(ChangeEvent e) {
                        Object source = e.getSource();
                        if (source instanceof TableCellEditor editor) {
                           Object value = editor.getCellEditorValue();
                           int row = this.this$1.this$0.arquivosTable.getSelectedRow();
                           if (row >= 0) {
                              int modelRow = this.this$1.this$0.arquivosTable.convertRowIndexToModel(row);
                              Object valueAt = this.this$1.this$0.arquivosTable.getModel().getValueAt(modelRow, 0);
                              if (valueAt instanceof Documento) {
                                 Documento documento = (Documento)valueAt;
                                 documento.setObservacao(value.toString());
                                 this.this$1.this$0.listener.onListaDocumentoChanged(this.this$1.this$0.documentos);
                              }
                           }
                        }

                     }

                     public void editingCanceled(ChangeEvent e) {
                     }
                  });
               }
            }

            return editor;
         }
      };
      this.arquivosTable.addMouseListener(new MouseAdapter() {
         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public void mouseClicked(MouseEvent e) {
            TabelaArquivosPanel.this.tableClick(e);
         }
      });
      this.arquivosTable.setEnabled(false);
      this.arquivosTable.setRowSelectionAllowed(true);
      this.arquivosTable.setCellSelectionEnabled(true);
      this.arquivosTable.setFillsViewportHeight(true);
      this.arquivosTable.registerKeyboardAction(new CopyAction(), KeyStroke.getKeyStroke(67, 128), 0);
      this.arquivosTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
      this.arquivosTable.setModel(new DefaultTableModel(new Object[0][], new String[]{"Nome", "Tipo", "Tamanho", "Hash", "Observação"}) {
         private static final long serialVersionUID = 1L;
         boolean[] columnEditables;

         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
            this.columnEditables = new boolean[]{false, true, false, false, true};
         }

         public boolean isCellEditable(int row, int column) {
            return this.columnEditables[column];
         }
      });
      TableColumnModel columnModel = this.arquivosTable.getColumnModel();
      columnModel.getColumn(0).setPreferredWidth(150);
      columnModel.getColumn(1).setPreferredWidth(150);
      columnModel.getColumn(2).setMaxWidth(300);
      columnModel.getColumn(3).setPreferredWidth(300);
      columnModel.getColumn(4).setPreferredWidth(250);
      TipoDocumento[] tipos = TipoDocumento.tipos();
      final JComboBox<TipoDocumento> tipoComboBox = new JComboBox(new DefaultComboBoxModel(tipos));
      tipoComboBox.setSelectedItem((Object)null);
      tipoComboBox.addActionListener(new ActionListener() {
         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public void actionPerformed(ActionEvent e) {
            if (tipoComboBox.getSelectedItem() != null && TabelaArquivosPanel.this.arquivosTable.isEditing() && TabelaArquivosPanel.this.arquivosTable.getCellEditor() != null) {
               TabelaArquivosPanel.this.arquivosTable.getCellEditor().stopCellEditing();
            }

         }
      });
      TableColumn tipoColumn = columnModel.getColumn(1);
      ComboBoxEditor<TipoDocumento> cellEditor = new ComboBoxEditor<TipoDocumento>(tipoComboBox);
      cellEditor.addCellEditorListener(new CellEditorListener() {
         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public void editingStopped(ChangeEvent e) {
            Object source = e.getSource();
            if (source instanceof TableCellEditor editor) {
               Object value = editor.getCellEditorValue();
               int row = TabelaArquivosPanel.this.arquivosTable.getSelectedRow();
               if (row >= 0) {
                  int modelRow = TabelaArquivosPanel.this.arquivosTable.convertRowIndexToModel(row);
                  Object valueAt = TabelaArquivosPanel.this.arquivosTable.getModel().getValueAt(modelRow, 0);
                  if (valueAt instanceof Documento) {
                     Documento documento = (Documento)valueAt;
                     documento.setTipo((TipoDocumento)value);
                     TabelaArquivosPanel.this.listener.onListaDocumentoChanged(TabelaArquivosPanel.this.documentos);
                  }
               }
            }

         }

         public void editingCanceled(ChangeEvent e) {
         }
      });
      tipoColumn.setCellEditor(cellEditor);
      TableRowSorter<TableModel> sorter = new TableRowSorter(this.arquivosTable.getModel());
      sorter.setComparator(2, LongComparator.instance);
      this.arquivosTable.setRowSorter(sorter);
      List<RowSorter.SortKey> sortKeys = new ArrayList(25);
      sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
      sorter.setSortKeys(sortKeys);
      DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
         private static final long serialVersionUID = 1L;

         {
            Objects.requireNonNull(TabelaArquivosPanel.this);
         }

         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Documento documento) {
               ;
            }

            if (column == 0) {
               value = documento.getNome();
            } else if (column == 2) {
               if (value instanceof Long) {
                  value = ByteFormatter.humanReadableByteCountBin((Long)value);
               }
            } else if (column == 1) {
               if (value != null && value instanceof TipoDocumento) {
                  value = ((TipoDocumento)value).getDescricao();
               }
            } else if (column == 3) {
               value = documento.getHash();
            }

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 2) {
               this.setForeground(Color.blue);
               this.setHorizontalAlignment(4);
            } else {
               this.setForeground(Color.BLACK);
               this.setHorizontalAlignment(10);
            }

            if (column != 0 && column != 1) {
               if (column == 3) {
                  if (documento.isHashDuplicado()) {
                     this.setToolTipText("Arquivo duplicado. Existe outro arquivo de mesmo hash na lista.");
                  } else {
                     this.setToolTipText(documento.getHash());
                  }
               } else if (column == 4) {
                  String observacao = (String)value;
                  if (StringUtil.isNotEmpty(observacao) && observacao.length() > 2000) {
                     this.setToolTipText("Tamanho máximo excedido. Atual: " + observacao.length() + " Permitido: 2000");
                  } else {
                     this.setToolTipText(observacao);
                  }
               }
            } else {
               this.setToolTipText((String)value);
            }

            if (column == 1 && value == null) {
               Border outsideBorder = BorderFactory.createLineBorder(Color.RED, 1);
               Border insideBorder = BorderFactory.createEmptyBorder(1, 4, 1, 4);
               this.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
            } else {
               this.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            }

            if (column == 3) {
               if (documento.isHashDuplicado()) {
                  Border outsideBorder = BorderFactory.createLineBorder(Color.RED, 1);
                  Border insideBorder = BorderFactory.createEmptyBorder(1, 4, 1, 4);
                  this.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
               } else {
                  this.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
               }
            }

            if (column == 4) {
               String observacao = (String)value;
               if (StringUtil.isNotEmpty(observacao) && observacao.length() > 2000) {
                  Border outsideBorder = BorderFactory.createLineBorder(Color.RED, 1);
                  Border insideBorder = BorderFactory.createEmptyBorder(1, 4, 1, 4);
                  this.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
               } else {
                  this.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
               }
            }

            return this;
         }
      };
      columnModel.getColumn(0).setCellRenderer(cellRenderer);
      columnModel.getColumn(1).setCellRenderer(cellRenderer);
      columnModel.getColumn(2).setCellRenderer(cellRenderer);
      columnModel.getColumn(3).setCellRenderer(cellRenderer);
      columnModel.getColumn(4).setCellRenderer(cellRenderer);
      this.arquivosTable.setRowHeight(this.arquivosTable.getRowHeight() + 4);
      this.arquivosTableScrollPane = new JScrollPane();
      layout.putConstraint("South", this.arquivosTableScrollPane, -2, "South", this);
      this.arquivosTableScrollPane.setViewportView(this.arquivosTable);
      layout.putConstraint("North", this.arquivosTableScrollPane, 10, "South", this.adicionarArquivosBtn);
      layout.putConstraint("West", this.arquivosTableScrollPane, 0, "West", this);
      layout.putConstraint("East", this.arquivosTableScrollPane, 0, "East", this);
      this.add(this.arquivosTableScrollPane);
   }

   public void setEnabledAll(boolean enabled) {
      this.arquivosTable.setEnabled(enabled);
      this.adicionarArquivosBtn.setEnabled(enabled);
      if (!enabled) {
         this.removerButton.setEnabled(enabled);
      }

   }

   protected void adicionarAction() {
      JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(this.getPastaUltimoArquivoAdicionado());
      fc.setMultiSelectionEnabled(true);
      Action details = fc.getActionMap().get("viewTypeDetails");
      details.actionPerformed((ActionEvent)null);
      this.configChoosableFileFilterForSave(fc);
      if (fc.showOpenDialog(this) == 0) {
         File[] selectedFiles = fc.getSelectedFiles();
         if (selectedFiles != null && selectedFiles.length != 0) {
            for(File file : selectedFiles) {
               if (file == null || !file.exists()) {
                  throw new NegocioException("Arquivo " + file.getName() + " não localizado.");
               }
            }

            List<File> arquivosRecusados = this.verificarArquivosPermitidosTransmissaoCmd.verificar(selectedFiles);
            if (arquivosRecusados.size() > 0) {
               List<File> zipFiles = new ArrayList();
               List<File> nonZipFiles = new ArrayList();

               for(File file : arquivosRecusados) {
                  if (ArquivoTransmissaoPermitido.isZip(file)) {
                     zipFiles.add(file);
                  } else {
                     nonZipFiles.add(file);
                  }
               }

               StringBuilder msg = new StringBuilder();
               if (!zipFiles.isEmpty()) {
                  msg.append("Os seguintes arquivos ZIP contêm arquivos internos de tipos não permitidos:\n\n");

                  for(File file : zipFiles) {
                     msg.append(file.getName() + "\n");
                  }
               }

               if (!zipFiles.isEmpty() && !nonZipFiles.isEmpty()) {
                  msg.append("\n");
               }

               if (!nonZipFiles.isEmpty()) {
                  msg.append("Os seguintes arquivos não são permitidos:\n\n");

                  for(File file : nonZipFiles) {
                     msg.append(file.getName() + "\n");
                  }
               }

               ArquivoTransmissaoPermitido[] tiposPermitidos = ArquivoTransmissaoPermitido.values();
               msg.append("\n");
               msg.append("Os tipos de arquivos permitidos são: ");
               msg.append(StringUtil.virgulaE(Arrays.asList(tiposPermitidos)));
               if (!zipFiles.isEmpty()) {
                  msg.append("\n\nNão são permitidos arquivos ZIP dentro de outro arquivo ZIP.");
               }

               throw new NegocioException(msg.toString());
            } else {
               List<String> selectedFilesNames = new ArrayList();

               for(File file : selectedFiles) {
                  if (this.nomeArquivos.contains(file.getName()) || selectedFilesNames.contains(file.getName())) {
                     throw new NegocioException("Não é possível inserir dois arquivos diferentes com o mesmo nome.");
                  }

                  selectedFilesNames.add(file.getName());
               }

               this.nomeArquivos.addAll(selectedFilesNames);
               this.setPastaUltimoArquivoAdicionado(selectedFiles[0].getParentFile());
               List<Documento> documentos = new ArrayList(selectedFiles.length);

               for(File selectedFile : selectedFiles) {
                  Documento documento = new Documento(selectedFile);

                  try {
                     FileInputStream in = new FileInputStream(selectedFile);

                     try {
                        String sha512Hex = DigestUtils.sha512Hex(in);
                        documento.setHash(sha512Hex);
                     } catch (Throwable var16) {
                        try {
                           in.close();
                        } catch (Throwable var15) {
                           var16.addSuppressed(var15);
                        }

                        throw var16;
                     }

                     in.close();
                  } catch (Exception var17) {
                     throw new NegocioException("Erro interno no cálculo de hash.");
                  }

                  documento.setTamanho(selectedFile.length());
                  String mimeType = ArquivoTransmissaoPermitido.mimeType(selectedFile.getName());
                  if (StringUtil.isEmpty(mimeType)) {
                     throw new NegocioException("Tipo de arquivo não permitido. Arquivo: " + selectedFile.getName());
                  }

                  documento.setMimeType(mimeType);
                  documentos.add(documento);
               }

               this.addDocumentos(documentos);
            }
         } else {
            throw new NegocioException("Nenhum arquivo selecioando.");
         }
      }
   }

   protected void removerAction() {
      int selectedRowCount = this.arquivosTable.getSelectedRowCount();
      if (selectedRowCount != 0) {
         int[] selectedRows = this.arquivosTable.getSelectedRows();
         DefaultTableModel model = (DefaultTableModel)this.arquivosTable.getModel();
         int[] tableModelRowToDelete = new int[selectedRows.length];

         for(int i = 0; i < selectedRows.length; ++i) {
            int selectedRow = selectedRows[i];
            int tableModelRow = this.arquivosTable.convertRowIndexToModel(selectedRow);
            tableModelRowToDelete[i] = tableModelRow;
            Documento documento = (Documento)model.getValueAt(tableModelRow, 0);
            this.documentos.remove(documento);
         }

         this.identificarHashsDuplicados();
         Arrays.sort(tableModelRowToDelete);

         for(int i = tableModelRowToDelete.length - 1; i >= 0; --i) {
            model.removeRow(tableModelRowToDelete[i]);
         }

         this.arquivosTable.clearSelection();
         this.removerButton.setEnabled(false);
         this.fireListaDocumentosChanged();
      }
   }

   protected void tableClick(MouseEvent evt) {
      if (this.arquivosTable.getSelectedRow() >= 0) {
         this.removerButton.setEnabled(true);
      }

   }

   public void onArquivoChaveChanged(boolean autorizado) {
      File arquivoCaso = this.context.getArquivoCaso();
      Certificado certificado = this.context.getCertificado();
      if (arquivoCaso != null && certificado != null && autorizado) {
         this.adicionarArquivosBtn.setEnabled(true);
         this.arquivosTable.setEnabled(true);
      } else {
         this.adicionarArquivosBtn.setEnabled(false);
         this.arquivosTable.setEnabled(false);
      }

   }

   public void addDocumentos(List<Documento> docs) {
      List<Documento> documentosNovos = new ArrayList(docs.size());

      for(Documento doc : docs) {
         boolean found = false;

         for(Documento documento : this.documentos) {
            File arquivo1 = doc.getArquivo();
            File arquivo2 = documento.getArquivo();
            if (arquivo1.getAbsolutePath().equals(arquivo2.getAbsolutePath())) {
               found = true;
               break;
            }
         }

         if (!found) {
            documentosNovos.add(doc);
         }
      }

      if (documentosNovos.size() != 0) {
         this.documentos.addAll(documentosNovos);
         this.documentos.sort((o1, o2) -> o1.getNome().toLowerCase().compareTo(o2.getNome().toLowerCase()));
         this.identificarHashsDuplicados();
         this.addDocumentosToTable(documentosNovos);
         this.fireListaDocumentosChanged();
      }
   }

   protected void identificarHashsDuplicados() {
      Map<String, List<Documento>> hashes = new LinkedHashMap();

      for(Documento documento : this.documentos) {
         if (hashes.containsKey(documento.getHash())) {
            documento.setHashDuplicado(true);
            List<Documento> docs = (List)hashes.get(documento.getHash());
            docs.add(documento);

            for(Documento doc : docs) {
               doc.setHashDuplicado(true);
            }
         } else {
            documento.setHashDuplicado(false);
            List<Documento> docs = new ArrayList();
            docs.add(documento);
            hashes.put(documento.getHash(), docs);
         }
      }

   }

   public void clear() {
      DefaultTableModel model = (DefaultTableModel)this.arquivosTable.getModel();

      for(int i = model.getRowCount() - 1; i >= 0; --i) {
         model.removeRow(i);
      }

      this.adicionarArquivosBtn.setEnabled(true);
      this.removerButton.setEnabled(false);
      this.documentos.clear();
      this.nomeArquivos.clear();
      this.fireListaDocumentosChanged();
   }

   protected void addDocumentosToTable(List<Documento> documentos) {
      DefaultTableModel model = (DefaultTableModel)this.arquivosTable.getModel();

      for(Documento documento : documentos) {
         model.addRow(new Object[]{documento, null, documento.getTamanho(), documento, ""});
      }

   }

   protected void fireListaDocumentosChanged() {
      this.listener.onListaDocumentoChanged(this.documentos);
   }

   protected File getPastaUltimoArquivoAdicionado() {
      if (this.config.contains("pasta.ultimo.arquivo.adicionado")) {
         String path = this.config.get("pasta.ultimo.arquivo.adicionado");
         File arquivoChave = new File(path);
         if (arquivoChave.exists()) {
            return arquivoChave.getAbsoluteFile();
         }
      }

      return null;
   }

   protected void setPastaUltimoArquivoAdicionado(File arquivo) {
      if (arquivo != null && arquivo.exists()) {
         try {
            this.config.set("pasta.ultimo.arquivo.adicionado", arquivo.getCanonicalPath());
         } catch (IOException e) {
            log.warn("Erro ao gravar último arquivo de documento adicionado", e);
         }

      }
   }

   private void configChoosableFileFilterForSave(JFileChooser fc) {
      FileFilter[] choosableFileFilters = fc.getChoosableFileFilters();
      Arrays.stream(choosableFileFilters).forEach((filter) -> fc.removeChoosableFileFilter(filter));
      fc.addChoosableFileFilter(arquivoTransmissaoDocumentosFileFilter);
      Arrays.stream(choosableFileFilters).forEach((filter) -> fc.addChoosableFileFilter(filter));
   }

   public List<Documento> getDocumentos() {
      return this.documentos;
   }

   public void setListener(ListaDocumentoChangedListener listener) {
      this.listener = listener;
   }

   private class ComboBoxEditor<D> extends AbstractCellEditor implements TableCellEditor {
      private static final long serialVersionUID = -1036679697740636754L;
      private JComboBox<D> comboBox;

      public ComboBoxEditor(JComboBox<D> comboBox) {
         Objects.requireNonNull(TabelaArquivosPanel.this);
         super();
         this.comboBox = comboBox;
      }

      public Object getCellEditorValue() {
         return this.comboBox.getSelectedItem();
      }

      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         this.comboBox.setSelectedItem(value);
         return this.comboBox;
      }

      public boolean stopCellEditing() {
         this.fireEditingStopped();
         return true;
      }
   }

   private static class CopyAction extends AbstractAction {
      private static final long serialVersionUID = -1577266045318639637L;

      public void actionPerformed(ActionEvent e) {
         JTable tbl = (JTable)e.getSource();
         int[] selectedRows = tbl.getSelectedRows();
         int[] selectedColumns = tbl.getSelectedColumns();
         TableModel model = tbl.getModel();
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < selectedRows.length; ++i) {
            int selectedRow = selectedRows[i];

            for(int j = 0; j < selectedColumns.length; ++j) {
               int selectedColumn = selectedColumns[j];
               int modelRow = tbl.convertRowIndexToModel(selectedRow);
               int modelColumn = tbl.convertColumnIndexToModel(selectedColumn);
               Object valueAt = model.getValueAt(modelRow, modelColumn);
               if (valueAt != null) {
                  sb.append(valueAt.toString());
               }

               if (j == selectedColumns.length - 1) {
                  sb.append("\n");
               } else if (i < selectedRows.length - 1 || j < selectedColumns.length - 1) {
                  sb.append("\t");
               }
            }
         }

         StringSelection selection = new StringSelection(sb.toString());
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(selection, selection);
      }
   }
}
