package controller;

import connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultController {

    private final Connection con;

    public ResultController() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public void saveResult(String userId1, String userId2, String result) {
        String sql = "INSERT INTO results (userId1, userId2, UserWin) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, userId1);
            stmt.setString(2, userId2);
            stmt.setString(3, result);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
