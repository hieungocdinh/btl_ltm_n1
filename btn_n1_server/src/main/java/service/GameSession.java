package service;

public class GameSession {
    private Client player1;
    private Client player2;
    private int player1Score;
    private int player2Score;
    private int currentRound;
    private boolean isFinished;

    public GameSession(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = 0;
        this.player2Score = 0;
        this.currentRound = 0;
        this.isFinished = false;
    }

    public void sendNextQuestion() {
        // Logic để lấy câu hỏi từ cơ sở dữ liệu và gửi đến hai người chơi
    }

    public void receiveAnswer(String player, String answer) {
        // Logic để kiểm tra câu trả lời, cập nhật điểm số và phản hồi cho người chơi
    }

    public boolean isGameOver() {
        // Logic kiểm tra nếu trận đấu kết thúc, ví dụ nếu một người chơi đạt đúng 2/3 câu
        return true;
    }

    public void endGame() {
        // Cập nhật kết quả, lưu vào cơ sở dữ liệu, và thông báo cho người chơi
    }
}
