package vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.UserEntity;
import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserEntity user);

    @Update
    int update(UserEntity user);

    @Delete
    int delete(UserEntity user);


    @Query("SELECT * FROM User WHERE user_id = :userId")
    UserEntity getUserById(int userId);

    @Query("SELECT * FROM User WHERE username = :username AND password_hash = :passwordHash")
    UserEntity authenticateUser(String username, String passwordHash);

    @Query("SELECT * FROM User WHERE username = :username")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM User ORDER BY username ASC")
    LiveData<List<UserEntity>> getAllUsers();
}
