package service;

import controller.QuestionController;
import controller.UserController;
import model.QuestionModel;
import run.ServerRun;

public class GameSession {
    private Client player1;
    private Client player2;
    private int player1Score;
    private int player2Score;
    private int currentRound1;
    private int currentRound2;
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

    public void sendNextQuestionToPlayer1() {
        // Tạo đối tượng QuestionController để lấy câu hỏi
        QuestionController questionController = new QuestionController();
        // Lấy một câu hỏi ngẫu nhiên từ cơ sở dữ liệu
        QuestionModel question = questionController.getRandomQuestion();

        // Kiểm tra nếu lấy được câu hỏi từ cơ sở dữ liệu
        if (question != null) {
            // Lưu lại ID của câu hỏi hiện tại để sử dụng khi kiểm tra đáp án
            currentQuestionId1 = question.getId();
            currentRound1++;

            // Tạo thông điệp chứa nội dung câu hỏi và đường dẫn hình ảnh
            String questionMessage = "QUESTION;" + question.getQuestionText() + ";" + question.getImageLink();

            // Gửi câu hỏi đến người chơi cụ thể
            player1.sendData(questionMessage);
        } else {
            // Trường hợp không lấy được câu hỏi, gửi thông báo lỗi
            player1.sendData("ERROR; Unable to retrieve question.");
        }
    }
    
    public void sendNextQuestionToPlayer2() {
        // Tạo đối tượng QuestionController để lấy câu hỏi
        QuestionController questionController = new QuestionController();
        // Lấy một câu hỏi ngẫu nhiên từ cơ sở dữ liệu
        QuestionModel question = questionController.getRandomQuestion();

        // Kiểm tra nếu lấy được câu hỏi từ cơ sở dữ liệu
        if (question != null) {
            // Lưu lại ID của câu hỏi hiện tại để sử dụng khi kiểm tra đáp án
            currentQuestionId2 = question.getId();
            currentRound2++;

            // Tạo thông điệp chứa nội dung câu hỏi và đường dẫn hình ảnh
            String questionMessage = "QUESTION;" + question.getQuestionText() + ";" + question.getImageLink();

            // Gửi câu hỏi đến người chơi cụ thể
            player2.sendData(questionMessage);
        } else {
            // Trường hợp không lấy được câu hỏi, gửi thông báo lỗi
            player2.sendData("ERROR; Unable to retrieve question.");
        }
    }

    public void receiveAnswer(String playerId, String answer) {
        QuestionController questionController = new QuestionController();
        boolean isCorrect = false;

        // Kiểm tra nếu người chơi là player1
        if (playerId.equals(player1.getLoginUserId())) {
            isCorrect = questionController.checkAnswer(currentQuestionId1, answer);
            if (isCorrect) {
                player1Score++;
            }
        }
        // Kiểm tra nếu người chơi là player2
        else if (playerId.equals(player2.getLoginUserId())) {
            isCorrect = questionController.checkAnswer(currentQuestionId2, answer);
            if (isCorrect) {
                player2Score++;
            }
        }

        // Gửi phản hồi đến client tương ứng
        Client respondingClient = playerId.equals(player1.getLoginUserId()) ? player1 : player2;
        respondingClient.sendData(isCorrect ? "CORRECT" : "WRONG");

        // Kiểm tra nếu trò chơi đã kết thúc
        if (isGameOver()) {
            endGame();
        } else {
            // Tiếp tục gửi câu hỏi tiếp theo cho người chơi
            if (playerId.equals(player1.getLoginUserId())) {
                sendNextQuestionToPlayer1();
            } else if (playerId.equals(player2.getLoginUserId())) {
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
        UserController userController = new UserController();

        if (player1Score > player2Score) {
            result = "WIN:" + player1.getLoginUserId();
            player1.sendData("WIN;You won the game!");
            player2.sendData("LOSE;You lost the game.");

            // Cập nhật điểm số cho người thắng và người thua
            userController.updateScore(player1.getLoginUserId(), 1);

        } else if (player2Score > player1Score) {
            result = "WIN:" + player2.getLoginUserId();
            player2.sendData("WIN;You won the game!");
            player1.sendData("LOSE;You lost the game.");

            // Cập nhật điểm số cho người thắng và người thua
            userController.updateScore(player2.getLoginUserId(), 1);

        } else {
            result = "DRAW";
            player1.sendData("DRAW;The game is a draw.");
            player2.sendData("DRAW;The game is a draw.");

            // Cập nhật điểm số cho cả hai người chơi trong trường hợp hòa
            userController.updateScore(player1.getLoginUserId(), 0.5f);
            userController.updateScore(player2.getLoginUserId(), 0.5f);
        }

        // Đánh dấu trò chơi đã kết thúc
        isFinished = true;

        // Loại bỏ phiên chơi khỏi ClientManager
        ServerRun.clientManager.removeGameSession(this);
    }
}
