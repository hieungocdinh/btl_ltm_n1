package view;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import run.ClientRun;

/**
 *
 * @author vanh <your.name at your.org>
 */
public class MatchView extends javax.swing.JFrame {

    private JLabel imageLabel;
    
    private int currentQuestion = 1;
    private int correctAnswers = 0; // Đếm số câu trả lời đúng

    /**
     * Creates new form MatchView
     */
    public MatchView() {
        initComponents();
        
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendAnswer();
            }
        });
    }
    
    public void showEndGameMessage() {
        // Ẩn tất cả các thành phần
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        jLabel4.setVisible(false);

        // Tạo JLabel để hiển thị thông báo kết thúc game
        JLabel endGameMessage = new JLabel("Bạn đã hoàn thành 3 câu hỏi", JLabel.CENTER);
        endGameMessage.setFont(new java.awt.Font("Segoe UI", 1, 24)); // Thiết lập font lớn
        endGameMessage.setHorizontalAlignment(JLabel.CENTER); // Căn giữa
        endGameMessage.setVerticalAlignment(JLabel.CENTER);   // Căn giữa theo chiều dọc

        // Thêm thông báo vào giao diện chính
        this.setLayout(new java.awt.BorderLayout()); // Sử dụng BorderLayout để căn giữa
        this.add(endGameMessage, java.awt.BorderLayout.CENTER);

        // Cập nhật giao diện
        this.revalidate();
        this.repaint();
    }
    
    public void resetGameView() {
        // Hiển thị lại các thành phần giao diện
        jPanel1.setVisible(true);
        jPanel2.setVisible(true);
        jLabel1.setVisible(true);
        jLabel2.setVisible(true);
        jLabel4.setVisible(true);

        // Loại bỏ thông báo kết thúc game (nếu có)
        this.getContentPane().removeAll(); // Xóa toàn bộ nội dung
        this.setLayout(new javax.swing.GroupLayout(getContentPane())); // Khôi phục lại Layout ban đầu
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jLabel1);
        this.add(jLabel2);
        this.add(jLabel4);

        // Reset lại các giá trị giao diện
        jLabel1.setText("Câu hỏi thứ: " + currentQuestion);
        jLabel2.setText("Số câu trả lời đúng: " + correctAnswers);
        jLabel4.setText("Câu hỏi sẽ được hiển thị tại đây");

        // Cập nhật lại giao diện
        this.revalidate();
        this.repaint();
    }
    
    public void resetLocalResult() {
        correctAnswers = 0;
        currentQuestion = 1;
    }
    
    public void increaseCorrectAnswers() {
        correctAnswers++;
    }
    
    public void increaseCurrentQuestion() {
        currentQuestion++;
    }

    public void displayQuestion(int questionId, String questionText, String imageLink) {
        // Hiển thị câu hỏi
        jLabel4.setText(questionText);
        jLabel1.setText("Câu hỏi thứ: " + currentQuestion);
        jLabel2.setText("Số câu trả lời đúng: " + correctAnswers);

        // Hiển thị hình ảnh nếu có 
        loadImage(imageLink);
    }

    private void loadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);  // Dùng URL nếu bạn lưu link ảnh từ web
            ImageIcon imageIcon = new ImageIcon(url);
            Image image = imageIcon.getImage(); // Lấy Image từ ImageIcon
            Image scaledImage = image.getScaledInstance(jPanel1.getWidth(), jPanel1.getHeight(), Image.SCALE_SMOOTH); // Thay đổi kích thước ảnh theo kích thước jPanel1
            imageIcon = new ImageIcon(scaledImage);

            // Xóa tất cả thành phần cũ trong jPanel1 trước khi thêm ảnh mới
            jPanel1.removeAll();

            // Tạo JLabel chứa hình ảnh và thêm vào jPanel1
            imageLabel = new JLabel();
            imageLabel.setIcon(imageIcon);

            // Đặt Layout cho jPanel1 là BorderLayout (để ảnh chiếm toàn bộ diện tích)
            jPanel1.setLayout(new java.awt.BorderLayout());
            jPanel1.add(imageLabel, java.awt.BorderLayout.CENTER);

            // Cập nhật giao diện
            jPanel1.revalidate();
            jPanel1.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendAnswer() {
        String answer = jTextField3.getText().toUpperCase(); // Lấy đáp án từ người dùng nhập vào
        if (answer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đáp án trước khi gửi!");
            return;
        }

        // Gửi đáp án qua SocketHandler
        ClientRun.socketHandler.sendAnswer(answer);

        // Xóa trường nhập sau khi gửi
        jTextField3.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 341, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Câu hỏi thứ: ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Số câu trả lời đúng: ");

        jButton2.setText("Gửi");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nhập đáp án:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(85, 85, 85))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(120, 120, 120))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
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
            java.util.logging.Logger.getLogger(MatchView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MatchView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MatchView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Tạo một instance của MatchView và hiển thị giao diện
//        MatchView matchView = new MatchView();
//        matchView.setVisible(true);

        // Giả lập hiển thị một câu hỏi và link hình ảnh từ mạng
//        String questionText = "Đây là câu hỏi ví dụ";
//        String imageLink = "https://petdanphuong.com.vn/wp-content/uploads/2022/01/0e65088d0093cbf1e22295f07b112411.jpg"; // Link ảnh hợp lệ
//
//        // Gọi phương thức displayQuestion để hiển thị câu hỏi
//        matchView.displayQuestion(1, questionText, imageLink);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MatchView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
