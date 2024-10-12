/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

    String loginUser = null; // lưu tài khoản đăng nhập hiện tại
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
                    case "QUESTION":
                        onReceiveQuestion(received);  // Xử lý câu hỏi
                        break;
                    case "ERROR":
                        // Handle error
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

        // Gọi phương thức để hiển thị câu hỏi trong MatchView
        ClientRun.matchView.displayQuestion(questionId, questionText, imageLink);
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
            // Tạo một mảng để chứa danh sách người dùng
            Object[][] userData = new Object[(splitted.length - 1) / 4][4]; // Mỗi người dùng có 4 phần (ID, Username, Full Name, Score)

            for (int i = 1; i < splitted.length; i += 4) {
                userData[(i - 1) / 4][0] = splitted[i];     // ID
                userData[(i - 1) / 4][1] = splitted[i + 1]; // Username
                userData[(i - 1) / 4][2] = splitted[i + 2]; // Full Name
                userData[(i - 1) / 4][3] = splitted[i + 3]; // Score
            }

            // Cập nhật dữ liệu trong ListView
            ClientRun.listView.updateUserList(userData);
        }
    }

    
    // Thêm vào SocketHandler
    public void requestUserList() {
        // Gửi yêu cầu đến máy chủ
        String data = "GET_USERS"; // Một loại yêu cầu để nhận danh sách người dùng
        sendData(data);
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
            this.loginUser = splitted[2];
            this.score = Float.parseFloat(splitted[4]);
            System.out.println("Id user vua dang nhap: " + this.loginUser);

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
}
