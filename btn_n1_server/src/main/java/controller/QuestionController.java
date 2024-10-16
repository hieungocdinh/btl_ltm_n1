package controller;

import connection.DatabaseConnection;
import model.QuestionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionController {

    private final Connection con;

    public QuestionController() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public QuestionModel getRandomQuestion() {
        String sql = "SELECT id, questionText, imageLink, answer FROM questions ORDER BY RAND() LIMIT 1";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String questionText = rs.getString("questionText");
                String imageLink = rs.getString("imageLink");
                String answer = rs.getString("answer");

                return new QuestionModel(id, questionText, imageLink, answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkAnswer(int questionId, String answer) {
        String sql = "SELECT answer FROM questions WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String correctAnswer = rs.getString("answer");
                // So sánh đáp án không phân biệt hoa thường và bỏ khoảng trắng
                return correctAnswer.equalsIgnoreCase(answer.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
