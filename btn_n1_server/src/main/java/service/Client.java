/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import controller.UserController;
import run.ServerRun;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.util.List;
import model.UserModel;

/**
 *
 * @author hieun
 */
public class Client implements Runnable {

    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    String loginUser;
    String loginUserId;
    Client cCompetitor;

    public String getLoginUserId() {
        return loginUserId;
    }

    public Client(Socket s) throws IOException {
        this.s = s;

        // obtaining input and output streams 
        this.dis = new DataInputStream(s.getInputStream());
        this.dos = new DataOutputStream(s.getOutputStream());
    }

    @Override
    public void run() {

        String received;
        boolean running = true;

        while (!ServerRun.isShutDown) {
            try {
                // receive the request from client
                received = dis.readUTF();

                System.out.println(received);
                String type = received.split(";")[0];

                switch (type) {
                    case "LOGIN":
                        onReceiveLogin(received);
                        break;
                    case "ANSWER":
                        handleGameRequest(received);
                        break;
                    case "REGISTER":
                        onReceiveRegister(received);
                    case "GET_USERS": // Thêm trường hợp này
                        onRequestUserList(); // Gọi phương thức xử lý yêu cầu
                        break;
                    case "CREATE_GAME":  // Xử lý yêu cầu tạo game session
                        onRequestCreateGame(received);
                        break;
                    case "EXIT":
                        running = false;
                    case "INVITE":
                        handleInvitation(received);
                        break;
                    case "ACCEPT_INVITE":
                        handleAcceptInvitation(received);
                        break;
                    case "DECLINE_INVITE":
                        handleDeclineInvitation(received);
                        break;
                    case "GET_RANKING":
                        handleRankingRequest();
                        break;
                }

            } catch (IOException ex) {
                break;
            }
        }

        try {
            // closing resources 
            this.s.close();
            this.dis.close();
            this.dos.close();
            System.out.println("- Client disconnected: " + s);
            
            // Check if the client was in a game session
            GameSession session = ServerRun.clientManager.getGameSessionForPlayer(loginUserId);
            if (session != null) {
                session.endGame();  // End the game session if the client was involved
            }

            // remove from clientManager
            ServerRun.clientManager.remove(this);

            ServerRun.clientManager.setUserOffline(this.loginUserId);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isConnected() {
        // Kiểm tra xem Socket có còn kết nối hay không
        return s != null && !s.isClosed() && s.isConnected();
    }

    private void onRequestCreateGame(String received) {
        // CREATE_GAME:userId1;userId2
        // Phân tích cú pháp yêu cầu
        String[] parts = received.split(";");
        if (parts.length < 3) {
            return;
        }

        String player1Id = parts[1]; // userId1
        String player2Id = parts[2]; // userId2

        // Tìm client theo id
        Client player1 = ServerRun.clientManager.getClientById(player1Id);
        Client player2 = ServerRun.clientManager.getClientById(player2Id);

        // Kiểm tra nếu cả 2 người chơi đều có mặt
        if (player1 != null && player2 != null) {
            // Tạo game session
            ServerRun.clientManager.createGameSession(player1, player2);
        } else {
            sendData("ERROR; One or both players not found.");
        }
    }

    private void onRequestUserList() {
        UserController userController = new UserController();
        List<UserModel> users = userController.getAllUsers();

        // Tạo chuỗi kết quả để gửi lại cho client
        StringBuilder userList = new StringBuilder("USER_LIST;");
        for (UserModel user : users) {
            if (!String.valueOf(user.getId()).equals(this.loginUserId)) { // Exclude the logged-in user
                userList.append(user.getId()).append(";")
                        .append(user.getUsername()).append(";")
                        .append(user.getFullName()).append(";")
                        .append(user.getTotalScore()).append(";")
                        .append(user.getStatus()).append(";");
            }
        }

        // Send the user list back to the client
        sendData(userList.toString());
    }

    public void handleGameRequest(String request) {
        // Phân tích yêu cầu từ client, ví dụ "ANSWER;answer_text"
        String[] parts = request.split(";");
        if (parts.length < 2) {
            return;
        }

        String type = parts[0];
        String answer = parts[1];

        if ("ANSWER".equals(type)) {
            // Gửi câu trả lời đến GameSession để xử lý
            GameSession session = ServerRun.clientManager.getGameSessionForPlayer(loginUserId);
            if (session != null) {
                session.receiveAnswer(loginUserId, answer);
            } else {
                sendData("ERROR; No active game session found.");
            }
        }
    }

    // send data fucntions
    public String sendData(String data) {
        try {
            this.dos.writeUTF(data);
            return "success";
        } catch (IOException e) {
            System.err.println("Send data failed!");
            return "failed;" + e.getMessage();
        }
    }

    private void onReceiveLogin(String received) {
        // get email / password from data
        String[] splitted = received.split(";");
        String username = splitted[1];
        String password = splitted[2];

        // check login
        String result = new UserController().login(username, password);
        String[] parts = result.split(";"); // Phân tách chuỗi theo dấu ";"

        if (result.split(";")[0].equals("success")) {
            // set login user
            this.loginUser = username;
            this.loginUserId = parts[1];
            ServerRun.clientManager.setUserOnline(this.loginUserId);
            System.out.println("id da login: " + loginUserId);
        }

        // send result
        sendData("LOGIN" + ";" + result);

        // Request user list after successful login
        if (result.split(";")[0].equals("success")) {
            onRequestUserList();
        }
    }

    private void onReceiveRegister(String received) {
        // get email / password from data
        String[] splitted = received.split(";");
        String username = splitted[1];
        String password = splitted[2];
        String fullName = splitted[3];

        // reigster
        String result = new UserController().register(username, password, fullName);

        // send result
        sendData("REGISTER" + ";" + result);
    }

    private void handleInvitation(String received) {
        String[] parts = received.split(";");
        String invitedUserId = parts[1];
        Client invitedClient = ServerRun.clientManager.getClientById(invitedUserId);
        if (invitedClient != null) {
            invitedClient.sendData("INVITE;" + this.loginUserId + ";" + this.loginUser);
        }
    }

    private void handleAcceptInvitation(String received) {
        String[] parts = received.split(";");
        String invitingUserId = parts[1];
        Client invitingClient = ServerRun.clientManager.getClientById(invitingUserId);
        if (invitingClient != null) {
            // Create a new game session
            ServerRun.clientManager.createGameSession(invitingClient, this);
            invitingClient.sendData("GAME_STARTED;" + this.loginUserId);
            this.sendData("GAME_STARTED;" + invitingUserId);
        }
    }

    private void handleDeclineInvitation(String received) {
        String[] parts = received.split(";");
        String invitingUserId = parts[1];
        Client invitingClient = ServerRun.clientManager.getClientById(invitingUserId);
        if (invitingClient != null) {
            invitingClient.sendData("INVITE_DECLINED;" + this.loginUserId);
        }
    }

    private void handleRankingRequest() {
        UserController userController = new UserController();
        List<UserModel> rankedUsers = userController.getRankedUsers();

        StringBuilder rankingData = new StringBuilder("RANKING;");
        int rank = 1;
        for (UserModel user : rankedUsers) {
            rankingData.append(rank).append(";")
                       .append(user.getUsername()).append(";")
                       .append(user.getTotalScore()).append(";");
            rank++;
        }

        sendData(rankingData.toString());
    }
}
