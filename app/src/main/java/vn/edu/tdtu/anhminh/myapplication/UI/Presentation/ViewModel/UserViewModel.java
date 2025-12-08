package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.UpdateAccountUseCase;

public class UserViewModel extends ViewModel {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();
    private final MutableLiveData<User> updateResult = new MutableLiveData<>();
    private final MutableLiveData<String> updateMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public UserViewModel(AuthenticateUserUseCase authenticateUserUseCase,
                         UpdateAccountUseCase updateAccountUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
    }

    public LiveData<User> getLoginResult() { return loginResult; }
    public LiveData<String> getLoginError() { return loginError; }
    public LiveData<User> getUpdateResult() { return updateResult; }
    public LiveData<String> getUpdateMessage() { return updateMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(String username, String password) {
        isLoading.setValue(true);

        authenticateUserUseCase.executeLogin(username, password, new AuthenticateUserUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.postValue(false);
                loginResult.postValue(user);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                loginError.postValue(message);
            }
        });
    }

    public void updateUser(User currentUser, String newUsername, String newAvatarUri) {
        isLoading.setValue(true);

        updateAccountUseCase.execute(currentUser, newUsername, newAvatarUri, new UpdateAccountUseCase.UpdateCallback() {
            @Override
            public void onSuccess(User updatedUser) {
                isLoading.postValue(false);
                updateResult.postValue(updatedUser);
                updateMessage.postValue("Profile updated successfully!");
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                updateMessage.postValue(message);
            }
        });
    }

    public void loadUserProfile(int userId) {
        isLoading.setValue(true);

        authenticateUserUseCase.executeGetUser(userId, new AuthenticateUserUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(User user) {
                isLoading.postValue(false);
                loginResult.postValue(user);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
            }
        });
    }
}