package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;

public class UpdateAccountUseCase {
    private final UserRepository userRepository;

    public UpdateAccountUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public interface UpdateCallback {
        void onSuccess(User updatedUser);
        void onError(String message);
    }

    public void execute(User currentUser, String newUsername, String newAvatarUri, UpdateCallback callback) {
        if (currentUser == null) {
            callback.onError("User not found");
            return;
        }

        if (newUsername == null || newUsername.trim().isEmpty()) {
            callback.onError("Username cannot be empty");
            return;
        }


        // Pass the request to Repository
        userRepository.updateUserProfile(currentUser.getUserId(), newUsername, newAvatarUri, new UserRepository.RepositoryCallback() {
            @Override
            public void onSuccess(int userId) {
                // Now that DB is updated, we update the Model to return to UI
                currentUser.setUsername(newUsername);
                if (newAvatarUri != null) {
                    currentUser.setAvatarImage(newAvatarUri);
                }
                callback.onSuccess(currentUser);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}
