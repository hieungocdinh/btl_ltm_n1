/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import run.ClientRun;
import view.ListView;

/**
 *
 * @author hieun
 */
public class SocketHandler {

    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    String loginUserId = null; // lưu tài khoản đăng nhập hiện tại
    String loginUsername = null; // lưu tên tài khoản đăng nhập hiện tại
    float score = 0;

    Thread listener = null;

    public String connect(String addr, int port) {
        try {
            // getting ip 
            InetAddress ip = InetAddress.getByName(addr);

            // establish the connection with server port 
            s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 4000);
            System.out.println("Connected to " + ip + ":" + port + ", localport:" + s.getLocalPort());

            // obtaining input and output streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            // close old listener
            if (listener != null && listener.isAlive()) {
                listener.interrupt();
            }

            // listen to server
            listener = new Thread(this::listen);
            listener.start();

            // connect success
            return "success";

        } catch (IOException e) {
            // connect failed
            return "failed;" + e.getMessage();
        }
    }

    private void listen() {
        boolean running = true;

        while (running) {
            try {
                // receive the data from server
                String received = dis.readUTF();

                System.out.println("RECEIVED: " + received);

                String type = received.split(";")[0];

                switch (type) {
                    case "LOGIN":
                        onReceiveLogin(received);
                        break;
                    case "REGISTER":
                        onReceiveRegister(received);
                        break;
                    case "USER_LIST":
                        onReceiveUserList(received); // Xử lý danh sách người dùng
                        break;
                    case "QUESTION":
                        onReceiveQuestion(received);  // Xử lý câu hỏi
                        break;
                    case "CORRECT":
                    case "WRONG":
                        onReceiveAnswerResult(received);
                        break;
                    case "WIN":
                    case "LOSE":
                    case "DRAW":
//                    case "WIN1":
                        onReceiveGameResult(received);  // Xử lý kết quả thắng/thua
                        break;
                    case "WAIT_END_GAME":
                        ClientRun.matchView.showEndGameMessage();
                        break;
                    case "ERROR":
                        // Handle error
                        break;
                    case "INVITE":
                        onReceiveInvitation(received);
                        break;
                    case "GAME_STARTED":
                        onGameStarted(received);
                        break;
                    case "INVITE_DECLINED":
                        onInviteDeclined(received);
                        break;
                    case "RANKING":
                        onReceiveRanking(received);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        try {
            // closing resources
            s.close();
            dis.close();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * Handle from client
     */
    private void onReceiveQuestion(String received) {
        String[] parts = received.split(";");
        int questionId = Integer.parseInt(parts[1]);  // Lấy id câu hỏi
        String questionText = parts[2];  // Nội dung câu hỏi
        String imageLink = parts[3];  // Đường dẫn hình ảnh

        if (ClientRun.matchView.isVisible() == false) {
            ClientRun.openScene(ClientRun.SceneName.MATCH);
            ClientRun.closeScene(ClientRun.SceneName.LIST);
        }

        // Gọi phương thức để hiển thị câu hỏi trong MatchView
        ClientRun.matchView.displayQuestion(questionId, questionText, imageLink);
        ClientRun.matchView.increaseCurrentQuestion();
    }

    public void sendAnswer(String answer) {
        String data = "ANSWER;" + answer;
        sendData(data);
    }

    private void onReceiveAnswerResult(String received) {
        if (received.equals("CORRECT")) {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Bạn trả lời đúng, bạn có thêm 1 điểm!");
            ClientRun.matchView.increaseCorrectAnswers();
        } else {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Bạn trả lời sai!");
        }
    }

    private void onReceiveGameResult(String received) {
        String type = received.split(";")[0];
        String message = received.split(";")[1];

        if (type.equals("WIN")) {
            if (message.equals("Your opponent has exited the game!")) {
                JOptionPane.showMessageDialog(ClientRun.matchView, "Chúc mừng! Bạn đã thắng do đối thủ của bạn bỏ cuộc", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(ClientRun.matchView, "Chúc mừng! Bạn đã thắng!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (type.equals("LOSE")) {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Rất tiếc! Bạn đã thua!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        } else if (type.equals("DRAW")) {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Trận đấu kết thúc với tỉ số hòa!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        } else if (type.equals("WIN1")) {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Bạn đã thắng! Đối thủ đã thoát trận!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }

        // Quay về màn hình danh sách người chơi
        ClientRun.matchView.resetLocalResult();
        ClientRun.matchView.resetGameView();
        ClientRun.openScene(ClientRun.SceneName.LIST);
        ClientRun.closeScene(ClientRun.SceneName.MATCH);
    }

    public void invitePlayer(String player2Id) {
        // Gửi yêu cầu mời người chơi đến server
        String data = "CREATE_GAME;" + loginUserId + ";" + player2Id;
        sendData(data);
    }

    public void login(String email, String password) {
        // prepare data
        String data = "LOGIN" + ";" + email + ";" + password;
        // send data
        sendData(data);
    }

    public void register(String username, String password, String fullName) {
        // prepare data
        String data = "REGISTER" + ";" + username + ";" + password + ";" + fullName;
        // send data
        sendData(data);
    }

    // Thêm phương thức xử lý danh sách người dùng
    private void onReceiveUserList(String received) {
        String[] splitted = received.split(";");
        if (splitted.length > 1) {
            List<Object[]> userList = new ArrayList<>();

            for (int i = 1; i < splitted.length; i += 5) {
                Object[] user = new Object[5];
                user[0] = splitted[i];     // ID
                user[1] = splitted[i + 1]; // Username
                user[2] = splitted[i + 2]; // Full Name
                user[3] = splitted[i + 3]; // Score
                user[4] = splitted[i + 4]; // Status
                userList.add(user);
            }

            Object[][] userData = userList.toArray(new Object[0][]);
            ClientRun.listView.updateUserList(userData);
        }
    }

    // Thêm vào SocketHandler
    public void requestUserList() {
        // Gửi yêu cầu đến máy chủ
        String data = "GET_USERS"; // Một loại yêu cầu để nhận danh sách người dùng
        sendData(data);
    }

    // Lấy username cho trang List View
    public String getLoginUsername() {
        return this.loginUsername;
    }

    /**
     * *
     * Handle send data to server
     */
    public void sendData(String data) {
        try {
            dos.writeUTF(data);
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * Handle receive data from server
     */
    private void onReceiveLogin(String received) {
        // get status from data
        String[] splitted = received.split(";");
        String status = splitted[1];

        if (status.equals("failed")) {
            // Hiển thị lỗi
            JOptionPane.showMessageDialog(null, "Login failed: " + splitted[2], "Error", JOptionPane.ERROR_MESSAGE);
        } else if (status.equals("success")) {
            // Lưu user login
            this.loginUserId = splitted[2];
            this.loginUsername = splitted[3];
            this.score = Float.parseFloat(splitted[4]);
            System.out.println("Id user vua dang nhap: " + this.loginUserId);

            // Mở trang ListView và đóng trang đăng nhập
            ClientRun.openScene(ClientRun.SceneName.LIST);
            // Đóng cửa sổ hiện tại (có thể là trang đăng nhập)
            ClientRun.closeScene(ClientRun.SceneName.LOGIN);// Giả sử ClientRun.loginView là JFrame của trang đăng nhập
        }
    }

    private void onReceiveRegister(String received) {
        // get status from data
        String[] splitted = received.split(";");
        String status = splitted[1];

        if (status.equals("failed")) {
            // hiển thị lỗi
            String failedMsg = splitted[2];
            JOptionPane.showMessageDialog(ClientRun.registerView, failedMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else if (status.equals("success")) {
            JOptionPane.showMessageDialog(ClientRun.registerView, "Register account successfully! Please login!");
            // chuyển scene
            ClientRun.closeScene(ClientRun.SceneName.REGISTER);
            ClientRun.openScene(ClientRun.SceneName.LOGIN);
        }
    }

    public void sendInvitation(String invitedUserId) {
        sendData("INVITE;" + invitedUserId);
    }

    private void onReceiveInvitation(String received) {
        String[] parts = received.split(";");
        String invitingUserId = parts[1];
        String invitingUsername = parts[2];

        int choice = JOptionPane.showConfirmDialog(
            null,
            invitingUsername + " invites you to play. Do you accept?",
            "Game Invitation",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            sendData("ACCEPT_INVITE;" + invitingUserId);
        } else {
            sendData("DECLINE_INVITE;" + invitingUserId);
        }
    }

    private void onGameStarted(String received) {
        String[] parts = received.split(";");
        String opponentId = parts[1];
        JOptionPane.showMessageDialog(null, "Game started with player " + opponentId);
        ClientRun.openScene(ClientRun.SceneName.MATCH);
    }

    private void onInviteDeclined(String received) {
        String[] parts = received.split(";");
        String declinedUserId = parts[1];
        JOptionPane.showMessageDialog(null, "Player " + declinedUserId + " declined your invitation.");
    }

    public void requestRanking() {
        sendData("GET_RANKING");
    }

    private void onReceiveRanking(String received) {
        String[] parts = received.split(";");
        if (parts.length > 1) {
            List<Object[]> rankingList = new ArrayList<>();

            for (int i = 1; i < parts.length; i += 3) {
                Object[] user = new Object[3];
                user[0] = Integer.parseInt(parts[i]);     // Rank
                user[1] = parts[i + 1];                   // Username
                user[2] = Float.parseFloat(parts[i + 2]); // Score
                rankingList.add(user);
            }

            Object[][] rankingData = rankingList.toArray(new Object[0][]);
            showRankingDialog(rankingData);
        }
    }

    private void showRankingDialog(Object[][] rankingData) {
        JTable rankingTable = new JTable(rankingData, new String[]{"Rank", "Username", "Score"});
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JOptionPane.showMessageDialog(null, scrollPane, "Player Rankings", JOptionPane.INFORMATION_MESSAGE);
    }
}
