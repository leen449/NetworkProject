
package netlab3;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class mainGui extends javax.swing.JFrame {
 private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    
    public mainGui() {
        initComponents();
        jTabbedPane1.setUI(null); // لإخفاء التبويبات
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        nameField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        playerListArea = new javax.swing.JTextArea();
        joinGameButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        waitingListArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(nameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 14, 215, -1));

        connectButton.setText("connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        jPanel3.add(connectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(311, 11, -1, -1));

        playerListArea.setColumns(20);
        playerListArea.setRows(5);
        jScrollPane1.setViewportView(playerListArea);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 66, 271, 170));

        joinGameButton.setText("join game");
        joinGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinGameButtonActionPerformed(evt);
            }
        });
        jPanel3.add(joinGameButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 395, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/netlab3/image.jpg"))); // NOI18N
        jLabel2.setText("jLabel2");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -50, 470, 420));

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("waiting list ");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 12, -1, 21));

        waitingListArea.setColumns(20);
        waitingListArea.setRows(5);
        jScrollPane2.setViewportView(waitingListArea);

        jPanel4.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 45, 239, 166));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/netlab3/image.jpg"))); // NOI18N
        jLabel3.setText("jLabel3");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -20, 470, 390));

        jTabbedPane1.addTab("tab2", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
connectToServer();        // TODO add your handling code here:
    }//GEN-LAST:event_connectButtonActionPerformed

    private void joinGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinGameButtonActionPerformed
joinGame();        // TODO add your handling code here:
    }//GEN-LAST:event_joinGameButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    private void connectToServer() {
        try {
            socket = new Socket("192.168.15.1", 9090); // غيّر IP إذا لزم الأمر
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            playerName = nameField.getText();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name.");
                return;
            }

            out.println(playerName);
            connectButton.setEnabled(false);
            joinGameButton.setEnabled(true);

            new Thread(this::listenToServer).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server.");
        }
    }
    
     private void disconnectFromServer() {
        if (out != null) {
            out.println("DISCONNECT");
        }
        System.exit(0);
    }

    private void listenToServer() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("PLAYER_LIST:")) {
                    updatePlayerList(message.substring(12));
                } else if (message.startsWith("WAITING_LIST:")) {
                    updateWaitingList(message.substring(13));
                } else if (message.equals("GAME_START")) {
                    JOptionPane.showMessageDialog(this, "Game is starting!");
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection lost.");
        }
    }

    private void updatePlayerList(String players) {
        SwingUtilities.invokeLater(() -> {
            playerListArea.setText("Connected Players:\n" + players.replace(",", "\n"));
        });
    }

    private void updateWaitingList(String players) {
        SwingUtilities.invokeLater(() -> {
            waitingListArea.setText("Waiting Room Players:\n" + players.replace(",", "\n"));
        });
    }

    private void joinGame() {
        out.println("JOIN_GAME");
        jTabbedPane1.setSelectedIndex(1);
    }
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
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton joinGameButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextArea playerListArea;
    private javax.swing.JTextArea waitingListArea;
    // End of variables declaration//GEN-END:variables
}
