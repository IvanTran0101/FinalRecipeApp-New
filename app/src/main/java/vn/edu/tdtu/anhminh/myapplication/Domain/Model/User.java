package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

public class User {
    private String userId;
    private String username;
    private String passwordHash;
    private String avatarImage; //uri string

    public User(){

    }

    public User(String userId, String username, String passwordHash, String avatarImage) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatarImage = avatarImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }
}
