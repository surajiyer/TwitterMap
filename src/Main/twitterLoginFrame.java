/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author S.S.Iyer
 */
public class twitterLoginFrame extends javax.swing.JFrame {

    private final GUIListener loginListener;
    
    /**
     * Creates new form twitterLoginFrame
     * @param gl
     */
    public twitterLoginFrame(GUIListener gl) {
        loginListener = gl;
        /* Set the Windows look and feel and initialize all the GUI components. */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
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
        //</editor-fold>
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        consumerKeyLabel = new javax.swing.JLabel();
        consumerKeyTextField = new javax.swing.JTextField();
        consumerSecretLabel = new javax.swing.JLabel();
        consumerSecretTextField = new javax.swing.JTextField();
        apiKeyTextField = new javax.swing.JTextField();
        apiKeyLabel = new javax.swing.JLabel();
        apiSecretLabel = new javax.swing.JLabel();
        apiSecretTextField = new javax.swing.JTextField();
        keysControlPanel = new javax.swing.JPanel();
        applyKeysButton = new javax.swing.JButton();
        clearAllKeysButton = new javax.swing.JButton();
        OkKeysButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TwitterMap");
        setResizable(false);

        consumerKeyLabel.setText("Enter your twitter Consumer key:");

        consumerKeyTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        consumerSecretLabel.setText("Enter your twitter Consumer secret:");

        apiKeyLabel.setText("Enter your twitter API key:");

        apiSecretLabel.setText("Enter your twitter API secret:");

        applyKeysButton.setText("Apply");
        applyKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyKeysButtonActionPerformed(evt);
            }
        });
        keysControlPanel.add(applyKeysButton);

        clearAllKeysButton.setText("Clear All");
        clearAllKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllKeysButtonActionPerformed(evt);
            }
        });
        keysControlPanel.add(clearAllKeysButton);

        OkKeysButton.setText("Ok");
        OkKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkBtnPerformed(evt);
            }
        });
        keysControlPanel.add(OkKeysButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(consumerSecretLabel)
                                    .addComponent(consumerKeyLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(consumerKeyTextField)
                                    .addComponent(consumerSecretTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(apiKeyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apiKeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(apiSecretLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apiSecretTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(keysControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consumerKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(consumerKeyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consumerSecretLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(consumerSecretTextField))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apiKeyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(apiKeyTextField))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apiSecretLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(apiSecretTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keysControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void applyKeysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyKeysButtonActionPerformed
        String cKey = consumerKeyTextField.getText();
        String cSecret = consumerSecretTextField.getText();
        String apiKey = apiKeyTextField.getText();
        String apiSecret = apiSecretTextField.getText();

        if(cKey.equals("")||cKey.equals("\t")
            || cSecret.equals("")||cSecret.equals("\t")
            || apiKey.equals("")||apiKey.equals("\t")
            || apiSecret.equals("")||apiSecret.equals("\t")) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.",
                "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(cKey.matches("^[a-zA-Z0-9]+$")
            && cSecret.matches("^[a-zA-Z0-9]+$")
            && apiKey.matches("^[a-zA-Z0-9]+\\-[a-zA-Z0-9]+$")
            && apiSecret.matches("^[a-zA-Z0-9]+$")) {
            loginListener.setTwitterCredentials(cKey, cSecret, apiKey, apiSecret);
        } else {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "1 or more keys/secrets uses invalid characters.",
                "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_applyKeysButtonActionPerformed

    private void clearAllKeysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllKeysButtonActionPerformed
        consumerKeyTextField.setText("");
        consumerSecretTextField.setText("");
        apiKeyTextField.setText("");
        apiSecretTextField.setText("");
    }//GEN-LAST:event_clearAllKeysButtonActionPerformed

    private void OkBtnPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkBtnPerformed
        if(loginListener.existsStream()) {
            dispose();
            loginListener.loadMainFrame();
        }
        else {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "You have to specify twitter credentials."
                    + " Don't forget to click Apply for the changes to take effect.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_OkBtnPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OkKeysButton;
    private javax.swing.JLabel apiKeyLabel;
    private javax.swing.JTextField apiKeyTextField;
    private javax.swing.JLabel apiSecretLabel;
    private javax.swing.JTextField apiSecretTextField;
    private javax.swing.JButton applyKeysButton;
    private javax.swing.JButton clearAllKeysButton;
    private javax.swing.JLabel consumerKeyLabel;
    private javax.swing.JTextField consumerKeyTextField;
    private javax.swing.JLabel consumerSecretLabel;
    private javax.swing.JTextField consumerSecretTextField;
    private javax.swing.JPanel keysControlPanel;
    // End of variables declaration//GEN-END:variables
}
