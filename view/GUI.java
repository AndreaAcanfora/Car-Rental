/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.AutoDAO;
import Controller.NoleggioDAO;
import Controller.SocioDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Auto;
import model.Noleggi;
import model.Soci;

/**
 *
 * @author Acanfora
 */
public class GUI extends javax.swing.JFrame {

    SimpleDateFormat dateformat;
    NoleggioDAO noleggio_dao;
    GUI_Auto frame_auto = null;
    GUI_Soci frame_soci = null;

    public GUI() {
        initComponents();
                noleggio_dao = new NoleggioDAO();
        this.setLocationRelativeTo(null);
        dateformat = new SimpleDateFormat("yyyy-M-dd");
        aggiornaTabella(noleggio_dao.mostra());
    }

    public void aggiornaTabella(List<Noleggi> lst) {

        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        try {

            AutoDAO auto = new AutoDAO();
            SocioDAO soci = new SocioDAO();
            for (Noleggi a : lst) {model.addRow(new Object[]{a.getCodiceNoleggio(),
                    auto.getAutoByTarga(a.getAuto()).getMarca() + " - " + auto.getAutoByTarga(a.getAuto()).getModello(),
                    soci.getSocioByCF(a.getSocio()).getCognome() + " " + soci.getSocioByCF(a.getSocio()).getNome(),
                    a.getInizio(),
                    a.getFine(),
                    a.isAutoRestituita()});
            }
            auto.close();
            soci.close();
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            JOptionPane.showMessageDialog(null, "Errore scrittura dati in tabella");
        }

        tbl.getColumnModel().getColumn(0).setPreferredWidth(20);
        aggiornaComboBox();

    }

    public void removeAutodalEsterno(String targa) {
        noleggio_dao.deleteRecordsbyTarga(targa);
    }

    public void removeSocidalEsterno(String cf) {
        noleggio_dao.deleteRecordsbyCF(cf);
    }

    public void aggiornaComboBox() {
        AutoDAO auto = new AutoDAO();
        SocioDAO soci = new SocioDAO();
        cbSocio.removeAllItems();
        cbAuto.removeAllItems();
        for (Soci s : soci.mostra()) {
            cbSocio.addItem(s.getCognome() + " " + s.getNome() + " -" + s.getCf() + ".");
        }
        for (Auto a : auto.getAutoDisp()) {

            cbAuto.addItem(a.getMarca() + " " + a.getModello() + " -" + a.getTarga() + ".");

        }
        auto.close();
        soci.close();
    }

    public void enableAuto() {
        btnAuto.setEnabled(true);
    }

    public void enableSoci() {
        btnSoci.setEnabled(true);
    }

    public void elimina(){
    
        if (tbl.getSelectedRowCount() == 1 && tbl.getSelectedRow() != -1) {

            int reply = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected record ?", "Delete", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                noleggio_dao.elimina((int) tbl.getValueAt(tbl.getSelectedRow(), 0));
                tbl.setRowSelectionInterval(0, 0);
                cbAuto.removeAllItems();
                cbSocio.removeAllItems();
                aggiornaTabella(noleggio_dao.mostra());
                JOptionPane.showMessageDialog(null, "Record eliminato");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Devi cliccare solo su una riga");
        }
    }
    public void modifica(){
      try {
            // TODO add your handling code here:

            if (tbl.getSelectedRowCount() == 1 && tbl.getSelectedRow() != -1) {

                Date d = dateformat.parse(cbAInizio.getSelectedItem().toString() + "-" + cbMInizio.getSelectedItem().toString() + "-" + cbGInizio.getSelectedItem().toString());
                Date d2 = dateformat.parse(cbAFine.getSelectedItem().toString() + "-" + cbMFine.getSelectedItem().toString() + "-" + cbGFine.getSelectedItem().toString());
                if (d2.compareTo(d) <= 0) {
                    JOptionPane.showMessageDialog(null, "La data non è valida");

                    return;
                }
                if (cbAuto.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Auto non valido");
                    return;
                }
                if (cbSocio.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Socio non valido");

                    return;
                }
                int reply = JOptionPane.showConfirmDialog(null, "Vuoi modicare questo noleggio ?", "Modifica", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    if (tbl.getValueAt(tbl.getSelectedRow(), 5).toString().equals("true")) {
                        Noleggi noleggio = (new Noleggi(cbAuto.getSelectedItem().toString().substring(
                                cbAuto.getSelectedItem().toString().length() - 8, cbAuto.getSelectedItem().toString().length() - 1),
                                cbSocio.getSelectedItem().toString().substring(cbSocio.getSelectedItem().toString().length() - 17, cbSocio.getSelectedItem().toString().length() - 1), d, d2, true));

                        noleggio_dao.aggiorna(noleggio, (int) tbl.getValueAt(tbl.getSelectedRow(), 0));
                        tbl.setRowSelectionInterval(0, 0);
                        cbAuto.removeAllItems();
                        cbSocio.removeAllItems();
                        noleggio_dao.close();
                        noleggio_dao = new NoleggioDAO();
                        aggiornaTabella(noleggio_dao.mostra());
                        JOptionPane.showMessageDialog(null, "Noleggio modificato, ricordati che non puoi modifcare lo stato del record (auto_restituita)");

                    } else {
                        Noleggi noleggio = noleggio_dao.getNoleggioByID((int) tbl.getValueAt(tbl.getSelectedRow(), 0));
                        noleggio.setAutoRestituita(checkAR.isSelected());
                        noleggio_dao.aggiorna(noleggio, (int) tbl.getValueAt(tbl.getSelectedRow(), 0));
                        cbAuto.removeAllItems();
                        cbSocio.removeAllItems();
                        aggiornaTabella(noleggio_dao.mostra());
                        JOptionPane.showMessageDialog(null, "Lo stato del noleggio è stato modificato");

                    }

                }
            } else {
                JOptionPane.showMessageDialog(null, "Devi cliccare solo su una riga");

            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Errore!");

        }
    }
    public void aggiungi(){
        
    
     try {
            // TODO add your handling code here:

            Date d = dateformat.parse(cbAInizio.getSelectedItem().toString() + "-" + cbMInizio.getSelectedItem().toString() + "-" + cbGInizio.getSelectedItem().toString());
            Date d2 = dateformat.parse(cbAFine.getSelectedItem().toString() + "-" + cbMFine.getSelectedItem().toString() + "-" + cbGFine.getSelectedItem().toString());
            if (d2.compareTo(d) <= 0) {
                JOptionPane.showMessageDialog(null, "Data non valida");

                return;
            }
            if (cbAuto.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(null, "Auto non valido");

                return;
            }
            if (cbSocio.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Socio non valido");

                return;
            }
            noleggio_dao.aggiungi(new Noleggi(cbAuto.getSelectedItem().toString().substring(
                    cbAuto.getSelectedItem().toString().length() - 8, cbAuto.getSelectedItem().toString().length() - 1),
                    cbSocio.getSelectedItem().toString().substring(cbSocio.getSelectedItem().toString().length() - 17, cbSocio.getSelectedItem().toString().length() - 1), d, d2, checkAR.isSelected()));
            cbAuto.removeAllItems();
            cbSocio.removeAllItems();
            noleggio_dao.close();
            noleggio_dao = new NoleggioDAO();
            aggiornaTabella(noleggio_dao.mostra());
            JOptionPane.showMessageDialog(null, "Noleggio aggiunto");

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Errore!");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        cbAuto = new javax.swing.JComboBox();
        cbAInizio = new javax.swing.JComboBox();
        cbAFine = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbGFine = new javax.swing.JComboBox();
        cbSocio = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbMInizio = new javax.swing.JComboBox();
        cbGInizio = new javax.swing.JComboBox();
        cbMFine = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        checkAR = new javax.swing.JCheckBox();
        panelBtn = new javax.swing.JPanel();
        btnUpdate = new javax.swing.JButton();
        btnFilter = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSoci = new javax.swing.JButton();
        btnAuto = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codice", "Auto", "Socio", "Inizio", "Fine", "Auto restituita"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);

        cbAFine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAFineActionPerformed(evt);
            }
        });

        jLabel4.setText("Data fine:");

        jLabel2.setText("Socio:");

        jLabel1.setText("Auto:");

        jLabel3.setText("Data inizio:");

        checkAR.setText("Auto restituita");
        checkAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkARActionPerformed(evt);
            }
        });

        btnUpdate.setText("Modifica");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnFilter.setText("Noleggi eff. da un socio in un determinato periodo");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        btnAdd.setText("Aggiungi");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Elimina");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSoci.setText("Tabella Soci");
        btnSoci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSociActionPerformed(evt);
            }
        });

        btnAuto.setText("Tabella Auto");
        btnAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutoActionPerformed(evt);
            }
        });

        jButton1.setText("Tabella Noleggi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBtnLayout = new javax.swing.GroupLayout(panelBtn);
        panelBtn.setLayout(panelBtnLayout);
        panelBtnLayout.setHorizontalGroup(
            panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBtnLayout.createSequentialGroup()
                        .addGroup(panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelBtnLayout.createSequentialGroup()
                                .addComponent(btnSoci)
                                .addGap(18, 18, 18)
                                .addComponent(btnAuto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(panelBtnLayout.createSequentialGroup()
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBtnLayout.setVerticalGroup(
            panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAuto)
                    .addComponent(btnSoci)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFilter)
                .addGap(203, 203, 203))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(7, 7, 7)
                                .addComponent(cbSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbGInizio, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbMInizio, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbAInizio, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(checkAR))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cbGFine, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbMFine, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cbAFine, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbAuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbGInizio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbMInizio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbAInizio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkAR))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbGFine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbMFine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbAFine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        for (int i = 2015; 2000 <=i; i--) {
            cbAInizio.addItem(i);

        }
        for (int i = 2015; 2000 <=i; i--) {
            cbAFine.addItem(i);

        }
        for (int i = 31; 1 <=i; i--) {
            cbGFine.addItem(i);

        }
        for (int i = 12; 1 <=i; i--) {
            cbMInizio.addItem(i);

        }
        for (int i = 31; 1 <=i; i--) {
            cbGInizio.addItem(i);

        }
        for (int i = 12; 1 <=i; i--) {
            cbMFine.addItem(i);

        }

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        elimina();

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

      modifica();

    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

       aggiungi();

    }//GEN-LAST:event_btnAddActionPerformed

    private void checkARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkARActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkARActionPerformed

    private void cbAFineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAFineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAFineActionPerformed

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
        // TODO add your handling code here:
        aggiornaCbInput();
    }//GEN-LAST:event_tblMouseClicked

    private void btnSociActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSociActionPerformed
  // TODO add your handling code here:
        btnSoci.setEnabled(false);
        frame_soci = new GUI_Soci(this);
        frame_soci.setVisible(true);        frame_soci.setVisible(true);    }//GEN-LAST:event_btnSociActionPerformed

    private void btnAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutoActionPerformed
  // TODO add your handling code here:
        btnAuto.setEnabled(false);
        frame_auto = new GUI_Auto(this);
        frame_auto.setVisible(true);        frame_auto.setVisible(true);    }//GEN-LAST:event_btnAutoActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        try {

            String d = (cbAInizio.getSelectedItem().toString() + "-" + cbMInizio.getSelectedItem().toString() + "-" + cbGInizio.getSelectedItem().toString());
            String d2 = (cbAFine.getSelectedItem().toString() + "-" + cbMFine.getSelectedItem().toString() + "-" + cbGFine.getSelectedItem().toString());
            Date data1 = dateformat.parse(cbAInizio.getSelectedItem().toString() + "-" + cbMInizio.getSelectedItem().toString() + "-" + cbGInizio.getSelectedItem().toString());
            Date data2 = dateformat.parse(cbAFine.getSelectedItem().toString() + "-" + cbMFine.getSelectedItem().toString() + "-" + cbGFine.getSelectedItem().toString());

            if (data2.compareTo(data1) <= 0) {
                            JOptionPane.showMessageDialog(null, "Data fine non valida");

                return;
            }
            if (cbSocio.getSelectedItem() == null) {
                            JOptionPane.showMessageDialog(null, "Devi scelgere un socio");

                return;
            }
            aggiornaTabella(noleggio_dao.mostraNoleggiPeriodo(cbSocio.getSelectedItem().toString().substring(cbSocio.getSelectedItem().toString().length() - 17, cbSocio.getSelectedItem().toString().length() - 1), d, d2));

        } catch (ParseException ex) {

        }
    }//GEN-LAST:event_btnFilterActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                aggiornaTabella(noleggio_dao.mostra());

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAuto;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnSoci;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox cbAFine;
    private javax.swing.JComboBox cbAInizio;
    private javax.swing.JComboBox cbAuto;
    private javax.swing.JComboBox cbGFine;
    private javax.swing.JComboBox cbGInizio;
    private javax.swing.JComboBox cbMFine;
    private javax.swing.JComboBox cbMInizio;
    private javax.swing.JComboBox cbSocio;
    private javax.swing.JCheckBox checkAR;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelBtn;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables

    public void aggiornaTabellaDalEsterno() {

        aggiornaComboBox();
        aggiornaTabella(noleggio_dao.mostra());
        JOptionPane.showMessageDialog(null, "Le combobox sono state aggiornate da un'altra finestra");

    }

    public void aggiornaCbInput() {
        if (tbl.getSelectedRowCount() == 1 && tbl.getSelectedRow() != -1) {
            int codicenoleggio = (int) (tbl.getValueAt(tbl.getSelectedRow(), 0));

            Noleggi noleggio = noleggio_dao.getNoleggioByID(codicenoleggio);
            if (tbl.getValueAt(tbl.getSelectedRow(), 5).toString().equals("true")) {
                checkAR.setSelected(true);
                Auto a = new AutoDAO().getAutoByTarga(noleggio.getAuto());
                String targaTable = a.getMarca() + " " + a.getModello() + " -" + a.getTarga() + ".";

                cbAuto.setSelectedItem(targaTable);
            } else {

                checkAR.setSelected(false);
                JOptionPane.showMessageDialog(null, "Non puoi modifcare il socio,auto e date perchè l'auto non è stata restituita");

            }

            Soci s = new SocioDAO().getSocioByCF(noleggio.getSocio());
            String nomeCognome = s.getCognome() + " " + s.getNome() + " -" + s.getCf() + ".";
            cbSocio.setSelectedItem(nomeCognome);
            String dataInizio = (tbl.getValueAt(tbl.getSelectedRow(), 3)).toString();
            String dataFine = (tbl.getValueAt(tbl.getSelectedRow(), 4)).toString();
            cbAInizio.setSelectedItem(Integer.parseInt(dataInizio.substring(0, 4)));
            cbMInizio.setSelectedItem(Integer.parseInt(dataInizio.substring(5, 7)));
            cbGInizio.setSelectedItem(Integer.parseInt(dataInizio.substring(8, 10)));
            cbAFine.setSelectedItem(Integer.parseInt(dataFine.substring(0, 4)));
            cbMFine.setSelectedItem(Integer.parseInt(dataFine.substring(5, 7)));
            cbGFine.setSelectedItem(Integer.parseInt(dataFine.substring(8, 10)));
        } else {

            JOptionPane.showMessageDialog(null, "Devi cliccare solo su una riga");

        }
    }

}
