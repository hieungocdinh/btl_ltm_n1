package service;

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
    
    public void createGameSession(String player1, String player2) {
        GameSession session = new GameSession(player1, player2);
        gameSessions.put(player1 + "-" + player2, session);
        session.sendNextQuestion(); // Gửi câu hỏi đầu tiên
    }

    public GameSession getGameSession(String player1, String player2) {
        return gameSessions.get(player1 + "-" + player2);
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
}
