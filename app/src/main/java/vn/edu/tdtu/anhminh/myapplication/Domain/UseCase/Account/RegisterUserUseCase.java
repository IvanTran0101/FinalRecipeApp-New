package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String username, String password, UserRepository.RepositoryCallback callback) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(password);

        userRepository.register(newUser, callback);
    }
}
