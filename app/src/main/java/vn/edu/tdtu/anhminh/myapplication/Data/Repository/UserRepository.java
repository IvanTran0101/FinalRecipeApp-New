package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.UserDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.UserEntity;

public class UserRepository {
    private final UserDAO userDAO;

    public UserRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        this.userDAO = db.userDao();

    }

    public UserEntity login(String username, String passwordHash) {
        return userDAO.authenticateUser(username, passwordHash);
    }

    public boolean usernameExists(String username) {
        return userDAO.getUserByUsername(username) != null;
    }

    public long register(UserEntity user){
        return userDAO.insert(user);
    }

    public UserEntity getUserById(int userId){
        return userDAO.getUserById(userId);
    }
}
