package model;

public class QuestionModel {
    private int id;
    private String questionText;
    private String imageLink;
    private String answer;

    public QuestionModel(int id, String questionText, String imageLink, String answer) {
        this.id = id;
        this.questionText = questionText;
        this.imageLink = imageLink;
        this.answer = answer;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
