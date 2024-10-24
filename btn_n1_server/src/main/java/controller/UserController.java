package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;
import model.UserModel;

public class UserController {

    //  SQL
    private final String INSERT_USER = "INSERT INTO users (username, password, full_name, total_score) VALUES (?, ?, ?, 0)";

    private final String CHECK_USER = "SELECT username from users WHERE username = ? limit 1";

    private final String LOGIN_USER = "SELECT id, username, password, total_score FROM users WHERE username=? AND password=?";

    private final String GET_INFO_USER = "SELECT username, password, score, win, draw, lose, avgCompetitor, avgTime FROM users WHERE username=?";

    private final String UPDATE_USER = "UPDATE users SET score = ?, win = ?, draw = ?, lose = ?, avgCompetitor = ?, avgTime = ? WHERE username=?";
    //  Instance
    private final Connection con;

    public UserController() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public String register(String username, String password, String fullName) {
        //  Check user exit
        try {
            PreparedStatement p = con.prepareStatement(CHECK_USER);
            p.setString(1, username);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                return "failed;" + "User Already Exit";
            } else {
                //Packing
                UserModel user = new UserModel(username, fullName, password);
                
                //Register User
                r.close();
                p.close();
                p = con.prepareStatement(INSERT_USER);
                p.setString(1, user.getUsername());
                p.setString(2, user.getPassword());
                p.setString(3, user.getFullName());
                p.executeUpdate();
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "success;";
    }

    public String login(String username, String password) {
        try {
            PreparedStatement p = con.prepareStatement(LOGIN_USER);
            p.setString(1, username);
            p.setString(2, password);
            ResultSet r = p.executeQuery();

            if (r.next()) {
                int id = r.getInt("id");
                float score = r.getFloat("total_score");
                updateUserStatus(String.valueOf(id), "Online");
                return "success;" + id + ";" + username + ";" + score;
            } else {
                return "failed;Please enter the correct account password!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "failed;" + e.getMessage();
        }
    }
    
    public void updateScore(String userId, float score) {
        String sql = "UPDATE users SET total_score = total_score + ? WHERE id = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            // Thiết lập các tham số cho câu truy vấn
            stmt.setFloat(1, score);
            stmt.setString(2, userId);

            // Thực thi truy vấn
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private final String GET_ALL_USERS = "SELECT id, username, full_name, total_score, status FROM users";

    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_USERS)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String fullName = rs.getString("full_name");
                float totalScore = rs.getFloat("total_score");
                String status = rs.getString("status");

                UserModel user = new UserModel(id, username, fullName, totalScore, status);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Add method to update user status
    public void updateUserStatus(String userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserModel> getRankedUsers() {
        List<UserModel> rankedUsers = new ArrayList<>();
        String GET_RANKED_USERS = "SELECT username, total_score FROM users ORDER BY total_score DESC LIMIT 10";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_RANKED_USERS)) {

            while (rs.next()) {
                String username = rs.getString("username");
                float totalScore = rs.getFloat("total_score");

                UserModel user = new UserModel(username, totalScore);
                rankedUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rankedUsers;
    }
}
