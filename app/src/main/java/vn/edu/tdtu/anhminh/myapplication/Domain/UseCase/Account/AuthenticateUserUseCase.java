package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class AuthenticateUserUseCase {
    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Tạo Interface để gửi kết quả về UI
    public interface UseCaseCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    // 2. Sửa hàm executeLogin: Không return User nữa, mà dùng Callback
    public void executeLogin(String username, String passwordHash, UseCaseCallback callback) {
        if (username == null || username.trim().isEmpty() || passwordHash == null || passwordHash.trim().isEmpty()) {
            if (callback != null) callback.onError("Tên đăng nhập hoặc mật khẩu không hợp lệ");
            return;
        }

        // 3. Gọi Repository (Lúc này Repository cũng đã được sửa để nhận Callback)
        // Giả sử bên UserRepository bạn đã định nghĩa interface LoginCallback
        userRepository.login(username, passwordHash, new UserRepository.LoginCallback() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    // Tìm thấy user -> Báo thành công
                    callback.onSuccess(user);
                } else {
                    // User null -> Báo thất bại (sai pass hoặc không tồn tại)
                    callback.onError("Sai thông tin đăng nhập");
                }
            }
        });
    }
}