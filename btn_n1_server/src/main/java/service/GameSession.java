package service;

import controller.QuestionController;
import controller.ResultController;
import controller.UserController;
import java.util.List;
import model.QuestionModel;
import run.ServerRun;

public class GameSession {

    private Client player1;
    private Client player2;
    private int player1Score;
    private int player2Score;
    private int currentRound1;
    private int currentRound2;
    private List<QuestionModel> player1Questions;
    private List<QuestionModel> player2Questions;
    private int currentQuestionIndex1 = 0;
    private int currentQuestionIndex2 = 0;
    private int currentQuestionId1;
    private int currentQuestionId2;
    private boolean isFinished;

    public GameSession(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = 0;
        this.player2Score = 0;
        this.currentRound1 = 0;
        this.currentRound2 = 0;
        this.isFinished = false;
    }

    public Client getPlayer1() {
        return player1;
    }

    public Client getPlayer2() {
        return player2;
    }

    public void startGame() {
        QuestionController questionController = new QuestionController();

        // Lấy 3 câu hỏi cho mỗi người chơi
        player1Questions = questionController.getThreeRandomQuestions();
        player2Questions = questionController.getThreeRandomQuestions();

        // Gửi câu hỏi đầu tiên cho mỗi người chơi
        sendNextQuestionToPlayer1();
        sendNextQuestionToPlayer2();
    }

    public void sendNextQuestionToPlayer1() {
        if (currentQuestionIndex1 < 3) {
            QuestionModel question = player1Questions.get(currentQuestionIndex1);
            currentQuestionId1 = question.getId();
            currentRound1++;

            String questionMessage = "QUESTION;" + question.getId() + ";" + question.getQuestionText() + ";" + question.getImageLink();
            player1.sendData(questionMessage);

            currentQuestionIndex1++;
        } else {
            player1.sendData("ERROR; No more questions available.");
        }
    }

    public void sendNextQuestionToPlayer2() {
        if (currentQuestionIndex2 < 3) {
            QuestionModel question = player2Questions.get(currentQuestionIndex2);
            currentQuestionId2 = question.getId();
            currentRound2++;

            String questionMessage = "QUESTION;" + question.getId() + ";" + question.getQuestionText() + ";" + question.getImageLink();
            player2.sendData(questionMessage);

            currentQuestionIndex2++;
        } else {
            player2.sendData("ERROR; No more questions available.");
        }
    }

    public void receiveAnswer(String playerId, String answer) {
        QuestionController questionController = new QuestionController();
        boolean isCorrect = false;

        if (playerId.equals(player1.getLoginUserId())) {
            isCorrect = questionController.checkAnswer(currentQuestionId1, answer);
            if (isCorrect) {
                player1Score++;
            }
        } else if (playerId.equals(player2.getLoginUserId())) {
            isCorrect = questionController.checkAnswer(currentQuestionId2, answer);
            if (isCorrect) {
                player2Score++;
            }
        }

        Client respondingClient = playerId.equals(player1.getLoginUserId()) ? player1 : player2;
        respondingClient.sendData(isCorrect ? "CORRECT" : "WRONG");

        if (isGameOver()) {
            endGame();
        } else {
            // Chuyển câu hỏi tiếp theo sau khi nhận được đáp án
            if (playerId.equals(player1.getLoginUserId())) {
                sendNextQuestionToPlayer1();
            } else {
                sendNextQuestionToPlayer2();
            }
        }
    }

    public boolean isGameOver() {
        // Trò chơi kết thúc nếu player1 hoặc player2 thắng 2 trên 3 câu hỏi
        if (player1Score >= 2 || player2Score >= 2) {
            return true;
        }
        // Hoặc nếu cả hai người chơi đã hoàn thành 3 câu hỏi
        if (currentRound1 == 3 && currentRound2 == 3) {
            return true;
        }
        return false;
    }

    public void endGame() {
        String result;
        if (player1Score > player2Score) {
            result = "WIN";
            player1.sendData("ANSWER_RESULT;END;WIN");
            player2.sendData("ANSWER_RESULT;END;LOSE");
        } else if (player2Score > player1Score) {
            result = "WIN";
            player2.sendData("ANSWER_RESULT;END;WIN");
            player1.sendData("ANSWER_RESULT;END;LOSE");
        } else {
            result = "DRAW";
            player1.sendData("ANSWER_RESULT;END;DRAW");
            player2.sendData("ANSWER_RESULT;END;DRAW");
        }

        // Cập nhật vào bảng kết quả
        ResultController resultController = new ResultController();
        resultController.saveResult(player1.getLoginUserId(), player2.getLoginUserId(), result);

        // Đóng phiên chơi
        ServerRun.clientManager.removeGameSession(this);
    }
}
