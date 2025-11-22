package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;
import android.os.AsyncTask;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.UserDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.UserEntity;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.UserMapper;

public class UserRepository {
    private final UserDAO userDAO;

    public UserRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        this.userDAO = db.userDao();
    }

    private static class InsertUserTask extends android.os.AsyncTask<UserEntity, Void, Void> {
        private final UserDAO userDAO;

        InsertUserTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(UserEntity... entities) {
            if (entities != null && entities.length > 0) {
                userDAO.insert(entities[0]);
            }
            return null;
        }
    }

    // 1. Định nghĩa Callback
    public interface LoginCallback {
        void onResult(User user);
    }

    // 2. Sửa hàm login
    public void login(String username, String passwordHash, LoginCallback callback) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                UserEntity entity = userDAO.authenticateUser(username, passwordHash);
                return UserMapper.toModel(entity);
            }
            @Override
            protected void onPostExecute(User user) {
                callback.onResult(user);
            }
        }.execute();
    }
    public boolean usernameExists(String username) {
        return userDAO.getUserByUsername(username) != null;
    }

    public void register(User user){
        if (user == null) return;
        UserEntity entity = UserMapper.toEntity(user, user.getPasswordHash());
        new InsertUserTask(userDAO).execute(entity);
    }

    private UserEntity getUserByIdInternal(int userId){
        return userDAO.getUserById(userId);
    }

    // Get user profile as domain model for UI
    public User getUserProfile(int userId) {
        UserEntity entity = getUserByIdInternal(userId);
        return UserMapper.toModel(entity);
    }
}
