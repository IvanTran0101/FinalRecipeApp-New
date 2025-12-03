package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "User")
public class UserEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private Integer userId;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "password_hash")
    private String passwordHash;

    @ColumnInfo(name = "avatar_image")
    private String avatarImage;

    public UserEntity(@NonNull Integer userId, @NonNull String username, @NonNull String passwordHash, String avatarImage) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatarImage = avatarImage;
    }

    @Ignore
    public UserEntity() {
    }

    @NonNull
    public Integer getUserId() { return userId; }
    public void setUserId(@NonNull Integer userId) { this.userId = userId; }

    @NonNull
    public String getUsername() { return username; }
    public void setUsername(@NonNull String username) { this.username = username; }

    @NonNull
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(@NonNull String passwordHash) { this.passwordHash = passwordHash; }

    public String getAvatarImage() { return avatarImage; }
    public void setAvatarImage(String avatarImage) { this.avatarImage = avatarImage; }
}