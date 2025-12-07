package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class AuthenticateUserUseCase {
    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public interface UseCaseCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public void executeLogin(String username, String passwordHash, UseCaseCallback callback) {
        if (username == null || username.trim().isEmpty() || passwordHash == null || passwordHash.trim().isEmpty()) {
            if (callback != null) callback.onError("Tên đăng nhập hoặc mật khẩu không hợp lệ");
            return;
        }

        userRepository.login(username, passwordHash, new UserRepository.LoginCallback() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Sai thông tin đăng nhập");
                }
            }
        });
    }

    public void executeGetUser(int userId, UseCaseCallback callback) {
        new Thread(() -> {
            User user = userRepository.getUserProfile(userId);
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onError("User not found");
            }
        }).start();
    }
}