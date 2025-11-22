package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class AuthenticateUserUseCase {
    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User executeLogin(String username, String passwordHash) {
        if (username == null || username.trim().isEmpty() || passwordHash == null || passwordHash.trim().isEmpty()) {
            return null;
        }

        return userRepository.login(username, passwordHash);
    }
}
