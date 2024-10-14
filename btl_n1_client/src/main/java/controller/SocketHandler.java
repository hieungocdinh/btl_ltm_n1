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

    public void sendAnswer(int questionId, String answer) {
        String data = "ANSWER;" + questionId + ";" + answer;
        sendData(data);
    }
    

    private void onReceiveAnswerResult(String received) {
        String[] parts = received.split(";");
        String result = parts[1];  // Kết quả trả lời: CORRECT/WRONG

        if (result.equals("CORRECT")) {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Correct Answer!");
        } else {
            JOptionPane.showMessageDialog(ClientRun.matchView, "Wrong Answer!");
        }

        // Kiểm tra xem trận đấu đã kết thúc hay chưa
        if (parts.length > 2 && parts[2].equals("END")) {
            String finalResult = parts[3]; // WIN/LOSE/DRAW
            JOptionPane.showMessageDialog(ClientRun.matchView, "Game Over: " + finalResult);
            ClientRun.closeScene(ClientRun.SceneName.MATCH); // Đóng scene match khi trận đấu kết thúc
        }
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
            // hiển thị lỗi

        } else if (status.equals("success")) {
            // lưu user login
            this.loginUser = splitted[2];
            this.score = Float.parseFloat(splitted[3]);
            System.out.println(this.loginUser);
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
