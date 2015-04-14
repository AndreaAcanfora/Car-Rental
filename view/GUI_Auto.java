/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Controller.AutoDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import model.Auto;

/**
 *
 * @author Acanfora
 */
public class GUI_Auto extends javax.swing.JFrame {

    GUI frame_noleggio;
    AutoDAO auto_dao;
    public GUI_Auto(GUI noleggio) {
        initComponents();
        this.frame_noleggio=noleggio;
        SpinnerNumberModel model = new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1);
        spnCosto.setModel(model);
        JSpinner.NumberEditor editor2 = new JSpinner.NumberEditor(spnCosto);
        spnCosto.setEditor(editor2);
        auto_dao= new AutoDAO();
        this.aggiornaTabella(auto_dao.mostra());
       
    }
     public void aggiornaTxtFields(){
        if (tbl.getSelectedRowCount() == 1 && tbl.getSelectedRow() != -1) {
           txtTarga.setText(tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
           txtMarca.setText(tbl.getValueAt(tbl.getSelectedRow(), 1).toString());
           txtModello.setText(tbl.getValueAt(tbl.getSelectedRow(),2).toString());
           spnCosto.setValue(Double.parseDouble(tbl.getValueAt(tbl.getSelectedRow(), 3).toString()));
        } else {
                                        JOptionPane.showMessageDialog(null,"Devi cliccare solo su una riga");
        }
    }
  
   public void elimina(){
     if (tbl.getSelectedRowCount() == 1 && tbl.getSelectedRow() != -1) {
                   int reply = JOptionPane.showConfirmDialog(null, "Vuoi eliminare questa auto?", "Elimina", JOptionPane.YES_NO_OPTION);
            if(reply== JOptionPane.YES_OPTION){
                          auto_dao.elimina(tbl.getValueAt(tbl.getSelectedRow(),0).toString());

          frame_noleggio.removeAutodalEsterno(tbl.getValueAt(tbl.getSelectedRow(),0).toString());
                                tbl.setRowSelectionInterval(0, 0);

                      aggiornaTabella(auto_dao.mostra());

            frame_noleggio.aggiornaTabellaDalEsterno();
            


            }
        } else {
                                        JOptionPane.showMessageDialog(null,"Devi cliccare su una riga");

        }
   }
   public void aggiungi(){
    Double prezzo;
        if(txtTarga.getText().trim().equals("") || txtTarga.getText().length()!=7){
                                        JOptionPane.showMessageDialog(null,"Targa non valida");

            return;
        }
        if(txtMarca.getText().trim().equals("")){
                                        JOptionPane.showMessageDialog(null,"Ins una targa");

        return;
        }
        
        if(txtModello.getText().trim().equals("")){
                                        JOptionPane.showMessageDialog(null,"Ins un modello");

        return;
        }
        
        try {
            prezzo = Double.parseDouble(spnCosto.getValue().toString());
            if (prezzo < 0.0 || spnCosto.getValue().toString().trim().equals("")) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
                                        JOptionPane.showMessageDialog(null,"Prezzo non valido");

            return ;
        }
        try {
            auto_dao.aggiungi(new Auto(txtTarga.getText(),txtMarca.getText(),txtModello.getText(), prezzo));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        frame_noleggio.aggiornaTabellaDalEsterno();
        aggiornaTabella(auto_dao.mostra());
   }
      public void aggiornaTabella(List<Auto> lst) {

        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
            for (Auto a : lst) {
                model.addRow(new Object[]{a.getTarga(),a.getMarca(),a.getModello(),a.getCostoGiornallero()});
            }
        tbl.getColumnModel().getColumn(3).setPreferredWidth(30);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        spnCosto = new javax.swing.JSpinner();
        btnAdd = new javax.swing.JButton();
        txtTarga = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        txtMarca = new javax.swing.JTextField();
        txtModello = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Auto");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Targa", "Marca", "Modello", "Costo G."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);

        jLabel1.setText("Targa:");

        jLabel2.setText("Marca:");

        jLabel3.setText("Modello:");

        jLabel4.setText("Costo Giornallero:");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTarga, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtModello, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spnCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtTarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModello, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(spnCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        frame_noleggio.enableAuto();
    }//GEN-LAST:event_formWindowClosing

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
       aggiungi();

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
      elimina();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
        // TODO add your handling code here:
        aggiornaTxtFields();
    }//GEN-LAST:event_tblMouseClicked

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnCosto;
    private javax.swing.JTable tbl;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModello;
    private javax.swing.JTextField txtTarga;
    // End of variables declaration//GEN-END:variables
}
