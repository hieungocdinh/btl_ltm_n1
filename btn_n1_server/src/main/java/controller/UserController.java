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
                r.close();
                p.close();
                p = con.prepareStatement(INSERT_USER);
                p.setString(1, username);
                p.setString(2, password);
                p.setString(3, fullName);
                p.executeUpdate();
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "success;";
    }

    public String login(String username, String password) {
        //  Check user exit
        try {
            PreparedStatement p = con.prepareStatement(LOGIN_USER);
            //  Login User 
            p.setString(1, username);
            p.setString(2, password);
            ResultSet r = p.executeQuery();

            if (r.next()) {
                int id = r.getInt("id");
                float score = r.getFloat("total_score");
                return "success;" + id + ";" + username + ";" + score;
            } else {
                return "failed;" + "Please enter the correct account password!";
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
    
    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        // Thêm ID vào truy vấn
        String GET_ALL_USERS = "SELECT id, username, full_name, total_score FROM users"; 

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_USERS)) {

            while (rs.next()) {
                // Lấy ID từ ResultSet
                int id = rs.getInt("id"); // Thêm dòng này để lấy ID
                String username = rs.getString("username");
                String fullName = rs.getString("full_name");
                float totalScore = rs.getFloat("total_score");

                // Giả sử UserModel có một constructor có 4 tham số để nhận ID
                UserModel user = new UserModel(id, username, fullName, totalScore);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

}
