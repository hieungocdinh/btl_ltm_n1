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
                    case "EXIT":
                        running = false;
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

            // remove from clientManager
            ServerRun.clientManager.remove(this);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void handleGameRequest(String request) {
        // Phân tích yêu cầu từ client, ví dụ "ANSWER;answer_text"
        String[] parts = request.split(";");
        if (parts.length < 2) return;

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

        if (result.split(";")[0].equals("success")) {
            // set login user
            this.loginUser = username;
            this.loginUserId = username;
        }

        // send result
        sendData("LOGIN" + ";" + result);
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
}
