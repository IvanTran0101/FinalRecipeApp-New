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

    private static class UpdateUserTask extends AsyncTask<UserEntity, Void, Boolean> {
        private final UserDAO userDAO;
        private final RepositoryCallback callback;

        UpdateUserTask(UserDAO userDAO, RepositoryCallback callback) {
            this.userDAO = userDAO;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(UserEntity... entities) {
            if (entities != null && entities.length > 0) {
                // update returns the number of rows affected
                int rows = userDAO.update(entities[0]);
                return rows > 0;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (callback != null) {
                if (success) callback.onSuccess();
                else callback.onError("Failed to update user in database.");
            }
        }
    }

    // 1. Định nghĩa Callback
    public interface LoginCallback {
        void onResult(User user);
    }

    public interface RepositoryCallback {
        void onSuccess();
        void onError(String message);
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

    public void updateUserProfile(int userId, String newUsername, String newAvatarUri, RepositoryCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // 1. Check if Username is taken by ANOTHER user
                UserEntity existingUser = userDAO.getUserByUsername(newUsername);

                // Check collision:
                // A user exists with this name AND it is not the current user
                if (existingUser != null && existingUser.getUserId() != userId) {
                    return "Username already exists!";
                }

                // 2. If valid, proceed to update
                // Fetch the existing entity to ensure we have the correct record
                UserEntity currentEntity = userDAO.getUserById(userId);

                if (currentEntity != null) {
                    currentEntity.setUsername(newUsername);
                    // Only update avatar if a new one is provided
                    if(newAvatarUri != null) {
                        currentEntity.setAvatarImage(newAvatarUri);
                    }

                    int rows = userDAO.update(currentEntity);
                    if (rows > 0) {
                        return null; // Null indicates success
                    } else {
                        return "Database update failed.";
                    }
                }
                return "User not found in database";
            }

            @Override
            protected void onPostExecute(String errorMessage) {
                if (callback != null) {
                    if (errorMessage == null) {
                        callback.onSuccess();
                    } else {
                        callback.onError(errorMessage);
                    }
                }
            }
        }.execute();
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
