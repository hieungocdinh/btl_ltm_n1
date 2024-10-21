/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author hieun
 */
public class UserModel {

    private int id;  // Đổi thành kiểu int
    private String username;
    private String password;
    private String fullName;
    private float totalScore;

    // Constructor không có id
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public UserModel(String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.totalScore = 0;
    }
    
    public UserModel(String username, String fullName, String password, float totalScore) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.totalScore = totalScore;
    }
   
    public UserModel(int id, String username, String fullName, float totalScore) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.totalScore = totalScore;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter methods cho các trường khác
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", fullName='" + fullName + '\''
                + ", totalScore=" + totalScore
                + '}';
    }
}
