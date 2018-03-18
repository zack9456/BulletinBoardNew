package GUI.Swing;

import Data.DatabaseUtil;
import Domain.DomainFacade;
import Domain.Model.Bulletin;
import Domain.Model.BulletinHistory;
import Domain.States;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout.Alignment;

import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class StartingScreen extends javax.swing.JFrame {

    public final int height = 1080;
    public final int width = 1920;
    public CreateBulletin cb;
    private ListSelectionListener listener;

    public StartingScreen() {
        initGui();
    }

    public void setTableColumnsAndRows() {
        hideHistoryInfo();
        createBulletinTableModel();
        createHistoryTableModel();
        createEventListener();
        setColumnSize();
    }

    private void createEventListener() {
        listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                try {
                    getAndSetHistory();
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
        };
    }

    private void setColumnSize() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.bulletinTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.bulletinTable.getColumnModel().getColumn(0).setResizable(false);
        this.bulletinTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        this.bulletinTable.getColumnModel().getColumn(1).setResizable(false);
        this.bulletinTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.bulletinTable.getColumnModel().getColumn(2).setResizable(false);
        this.bulletinTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.bulletinTable.getColumnModel().getColumn(3).setResizable(false);
        this.bulletinTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.bulletinTable.getColumnModel().getColumn(4).setResizable(false);
        this.bulletinTable.getColumnModel().getColumn(4).setPreferredWidth(600);
        this.bulletinTable.getTableHeader().setReorderingAllowed(false);
        this.historyTable.getTableHeader().setReorderingAllowed(false);
    }

    private void createHistoryTableModel() {
        String[] historyColumns = {"Status", "Datum"};
        DefaultTableModel historyDTM = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.historyTable.setModel(historyDTM);
    }

    private void createBulletinTableModel() {
        String[] bulletinColumns = {"Id", "Prio", "Kategori", "Ämne", "Text"};
        DefaultTableModel bulletinDTM = new DefaultTableModel(bulletinColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Bulletin bulletin : DomainFacade.getInstance().getBulletinArray()) {
            bulletinDTM.addRow(new Object[]{
                bulletin.getId(),
                bulletin.getPriority(),
                bulletin.getCategoryName(),
                bulletin.getSubject(),
                bulletin.getBody()}
            );
        }
        
        this.bulletinTable.setModel(bulletinDTM);
    }
    
    private DefaultTableModel createHistoryTableModel(DefaultTableModel historyDTM, int bulletinId){
        if (historyDTM.getRowCount() > 0) {
            for (int i = historyDTM.getRowCount() - 1; i > -1; i--) {
                historyDTM.removeRow(i);
            }
        }        
        
        for (BulletinHistory bHistory : DomainFacade.getInstance().getBulletinHistoryArray(bulletinId)) {
            historyDTM.addRow(new Object[]{
                bHistory.getStatus(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bHistory.getDate())
            }
            );
        }
        
        return historyDTM;
    }

    private void getAndSetHistory() {
        int bulletinId = Integer.parseInt((String) bulletinTable.getValueAt(bulletinTable.getSelectedRow(), 0));
        this.jLabel2.setText(bulletinTable.getValueAt(bulletinTable.getSelectedRow(), 3).toString());
        this.jLabel4.setText(DomainFacade.getInstance().getBulletinStatus(bulletinId));

        
        this.historyTable.setModel(createHistoryTableModel((DefaultTableModel) this.historyTable.getModel(), bulletinId));

        this.jLabel1.setVisible(true);
        this.jLabel2.setVisible(true);
        this.jLabel3.setVisible(true);
        this.jLabel4.setVisible(true);
        this.statusComboBox.setEnabled(true);
        this.updateStatusButton.setEnabled(true);
    }

    private void hideHistoryInfo() {
        this.jLabel1.setVisible(false);
        this.jLabel2.setVisible(false);
        this.jLabel3.setVisible(false);
        this.jLabel4.setVisible(false);
        this.statusComboBox.setEnabled(false);
        this.updateStatusButton.setEnabled(false);
    }

    public void removeTableListener() {
        this.bulletinTable.getSelectionModel().removeListSelectionListener(listener);

        this.bulletinTable.getSelectionModel().clearSelection();
    }

    public void addTableListener() {
        this.bulletinTable.getSelectionModel().addListSelectionListener(listener);
    }

    private void initGui() {
        this.initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(this.width, this.height);
        this.setVisible(true);
        this.bulletinTable.setAutoCreateRowSorter(true);

        for (String states : States.getStates()) {
            this.statusComboBox.addItem(states);
        }
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bulletinTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        newDatabase = new javax.swing.JButton();
        newBulletin = new javax.swing.JButton();
        updateStatusButton = new javax.swing.JButton();
        statusComboBox = new JComboBox<String>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bulletinTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {},
                    {},
                    {},
                    {}
                },
                new String[]{}
        ));
        jScrollPane1.setViewportView(bulletinTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
                        .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                        .addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        newDatabase.setText("Skapa ny databas");
        newDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DatabaseUtil dbu = new DatabaseUtil();
                dbu.createDatabase();
            }
        });

        newBulletin.setText("Ny bulletin");
        newBulletin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBulletinActionPerformed(evt);
            }
        });

        updateStatusButton.setText("Uppdatera status");
        updateStatusButton.setEnabled(false);
        updateStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatusButtonActionPerformed(evt);
            }
        });

        statusComboBox.setEnabled(false);

        jLabel1.setText("Vald bulletin:");

        jLabel2.setText("jLabel2");

        jLabel3.setText("Status:");

        jLabel4.setText("jLabel4");

        jLabel5.setText("Historik: ");

        historyTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {},
                    {},
                    {},
                    {}
                },
                new String[]{}
        ));
        historyTable.setEnabled(false);
        jScrollPane2.setViewportView(historyTable);

        JButton btnNyKategori = new JButton("Ny kategori");
        btnNyKategori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new CreateCategory();
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jScrollPane2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
                                                .addComponent(statusComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel5)
                                                .addComponent(updateStatusButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(newBulletin, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                        .addPreferredGap(ComponentPlacement.UNRELATED)
                                        .addComponent(btnNyKategori, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
                                .addComponent(newDatabase, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE))
                        .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(newBulletin)
                                .addComponent(btnNyKategori))
                        .addGap(18)
                        .addComponent(jLabel1)
                        .addGap(1)
                        .addComponent(jLabel2)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(3)
                        .addComponent(jLabel4)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(statusComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18)
                        .addComponent(updateStatusButton)
                        .addGap(18)
                        .addComponent(jLabel5)
                        .addGap(18)
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 407, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(newDatabase)
                        .addContainerGap())
        );
        jPanel2.setLayout(jPanel2Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                        .addGap(18)
                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(24)
                                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }

    private void newBulletinActionPerformed(java.awt.event.ActionEvent evt) {
        cb = new CreateBulletin(this);
    }

    private void updateStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int bulletinId = Integer.parseInt((String) bulletinTable.getValueAt(bulletinTable.getSelectedRow(), 0));

        DomainFacade.getInstance().createBulletinHistory(bulletinId, statusComboBox.getSelectedItem().toString());

        getAndSetHistory();
    }

    private javax.swing.JTable bulletinTable;
    private javax.swing.JTable historyTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton newBulletin;
    private javax.swing.JButton newDatabase;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JButton updateStatusButton;
}
