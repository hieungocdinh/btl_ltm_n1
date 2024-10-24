package service;

import controller.UserController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hieun
 */
public class ClientManager {

    ArrayList<Client> clients;
    private Map<String, GameSession> gameSessions = new HashMap<>();

    public ClientManager() {
        clients = new ArrayList<>();
    }
    
    public Client getClientById(String userId) {
        for (Client c : clients) {
            if (c.getLoginUserId().equals(userId)) {
                return c;
            }
        }
        return null;
    }

    public boolean add(Client c) {
        if (!clients.contains(c)) {
            clients.add(c);
            return true;
        }
        return true;
    }

    public boolean remove(Client c) {
        if (clients.contains(c)) {
            clients.remove(c);
            return true;
        }
        return false;
    }
    
    public void createGameSession(Client player1, Client player2) {
        GameSession session = new GameSession(player1, player2);
        gameSessions.put(player1.getLoginUserId() + "-" + player2.getLoginUserId(), session);
        updateUserStatus(player1.getLoginUserId(), "Ingame");
        updateUserStatus(player2.getLoginUserId(), "Ingame");
        session.sendNextQuestionToPlayer1(); // Gửi câu hỏi đầu tiên cho user 1
        session.sendNextQuestionToPlayer2(); // Gửi câu hỏi đầu tiên cho user 2
    }
    
    public GameSession getGameSessionForPlayer(String loginUserId) {
        // Duyệt qua các phiên chơi trong gameSessions
        for (GameSession session : gameSessions.values()) {
            // Kiểm tra nếu người chơi là player1 hoặc player2 trong phiên chơi
            if (session.getPlayer1().getLoginUserId().equals(loginUserId) ||
                session.getPlayer2().getLoginUserId().equals(loginUserId)) {
                return session;
            }
        }
        // Trả về null nếu không tìm thấy phiên chơi
        return null;
    }


    public GameSession getGameSession(Client player1, Client player2) {
        return gameSessions.get(player1.getLoginUserId() + "-" + player2.getLoginUserId());
    }
    
    public void removeGameSession(GameSession session) {
        // Duyệt qua các mục trong gameSessions để tìm phiên chơi tương ứng
        String sessionKey = null;
        for (Map.Entry<String, GameSession> entry : gameSessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                sessionKey = entry.getKey();
                break;
            }
        }

        // Nếu tìm thấy khóa của phiên chơi, xóa nó khỏi danh sách
        if (sessionKey != null) {
            gameSessions.remove(sessionKey);
            System.out.println("Removed game session: " + sessionKey);
        }
    }

//    public Client find(String username) {
//        for (Client c : clients) {
//            if (c.getLoginUser() != null && c.getLoginUser().equals(username)) {
//                return c;
//            }
//        }
//        return null;
//    }
//
//    public void broadcast(String msg) {
//        clients.forEach((c) -> {
//            c.sendData(msg);
//        });
//    }
//
//    public void sendToAClient(String username, String msg) {
//        clients.forEach((c) -> {
//            if (c.getLoginUser().equals(username)) {
//                c.sendData(msg);
//            }
//        });
//    }

    public int getSize() {
        return clients.size();
    }

//    public String getListUseOnline() {
//        String result = "success;" + String.valueOf(clients.size()) + ";";
//        for (int i = 0; i < clients.size(); i++) {
//            result += clients.get(i).getLoginUser() + ";";
//        }
//        return result;
//    }

    public void updateUserStatus(String userId, String status) {
        UserController userController = new UserController();
        userController.updateUserStatus(userId, status);
    }

    public void setUserOnline(String userId) {
        updateUserStatus(userId, "Online");
    }

    public void setUserOffline(String userId) {
        updateUserStatus(userId, "Offline");
    }
}
