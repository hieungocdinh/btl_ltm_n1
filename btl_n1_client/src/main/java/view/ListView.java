/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import run.ClientRun;

/**
 *
 * @author Manh
 */
public class ListView extends javax.swing.JFrame {
    
    private JLabel welcomeLabel;
    private JButton refreshButton;
    private JTable dataTable;
    private JScrollPane scrollPane;
    private JButton inviteButton; // Thêm nút mời người chơi


    /**
     * Creates new form ListView
     */
    public ListView() {
        initComponents();
        initCustomComponents();
        
//        loadUserList();
    }
    
    private void initCustomComponents() {
        // Tạo các thành phần giao diện
        welcomeLabel = new JLabel("Hello, <Undefined>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        
        // Tạo bảng dữ liệu mẫu
        String[] columnNames = {"ID", "Username", "Full Name", "Score"}; // Cập nhật tiêu đề cột
        Object[][] data = {
//            {1, "johndoe", "John Doe", 85.0},
//            {2, "janesmith", "Jane Smith", 90.5},
//            {3, "alicejohnson", "Alice Johnson", 78.5},
        };
        
        dataTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(dataTable);

        // Tạo nút Refresh
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(evt -> {
            // Xử lý sự kiện khi nhấn nút Refresh
            loadUserList();
        });
        
        // Tạo nút Invite Player
        inviteButton = new JButton("Invite Player");
        inviteButton.addActionListener(evt -> invitePlayer());

        // Sắp xếp layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(welcomeLabel)
                .addComponent(scrollPane)
                .addComponent(refreshButton)
                .addComponent(inviteButton)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(welcomeLabel)
                .addComponent(scrollPane)
                .addComponent(refreshButton)
                .addComponent(inviteButton)
        );

        pack();
        
        
    }
    
    private void invitePlayer() {
        int selectedRow = dataTable.getSelectedRow(); // Lấy dòng được chọn trong bảng
        if (selectedRow != -1) {
            String selectedUserId = dataTable.getValueAt(selectedRow, 0).toString(); // Lấy ID của người dùng được chọn
            String selectedUsername = dataTable.getValueAt(selectedRow, 1).toString(); // Lấy tên người dùng được chọn

            // Gửi yêu cầu mời người chơi lên server
            ClientRun.socketHandler.invitePlayer(selectedUserId);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người chơi để mời!");
        }
    }

    
    public void updateUserList(Object[][] userData) {
        // Cập nhật dữ liệu trong JTable
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            userData,
            new String[] {"ID", "Username", "Full Name", "Score"} // Cập nhật tiêu đề cột ở đây
        ));
    }

    public void loadUserList() {
        // Gọi yêu cầu lấy danh sách người dùng từ socketHandler
        ClientRun.socketHandler.requestUserList();
    }
    
    public void loadUserName(){
        // Lấy username để hiển thị
        String username = ClientRun.socketHandler.getLoginUsername();
        welcomeLabel.setText("Hello, "+username+" !");
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListView().setVisible(true);
                
//                ClientRun.socketHandler.requestUserList();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
