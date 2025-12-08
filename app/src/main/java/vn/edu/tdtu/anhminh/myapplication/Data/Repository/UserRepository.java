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
                if (success) callback.onSuccess(0);
                else callback.onError("Failed to update user in database.");
            }
        }
    }

    public interface LoginCallback {
        void onResult(User user);
    }

    public interface RepositoryCallback {
        void onSuccess(int userId);
        void onError(String message);
    }

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

    public void register(User user, RepositoryCallback callback) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                if (userDAO.getUserByUsername(user.getUsername()) != null) {
                    return -1; // Error: Username already exists
                }
                UserEntity entity = UserMapper.toEntity(user, user.getPasswordHash());
                return (int) userDAO.insert(entity);
            }

            @Override
            protected void onPostExecute(Integer userId) {
                if (callback != null) {
                    if (userId != -1) {
                        callback.onSuccess(userId);
                    } else {
                        callback.onError("Username already exists!");
                    }
                }
            }
        }.execute();
    }

    public void updateUserProfile(int userId, String newUsername, String newAvatarUri, RepositoryCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                UserEntity existingUser = userDAO.getUserByUsername(newUsername);

                if (existingUser != null && existingUser.getUserId() != userId) {
                    return "Username already exists!";
                }

                UserEntity currentEntity = userDAO.getUserById(userId);

                if (currentEntity != null) {
                    currentEntity.setUsername(newUsername);
                    if(newAvatarUri != null) {
                        currentEntity.setAvatarImage(newAvatarUri);
                    }

                    int rows = userDAO.update(currentEntity);
                    if (rows > 0) {
                        return null;
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
                        callback.onSuccess(0);
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

    public User getUserProfile(int userId) {
        UserEntity entity = getUserByIdInternal(userId);
        return UserMapper.toModel(entity);
    }
}
