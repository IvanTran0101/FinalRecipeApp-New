package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;

public class LoginViewModel extends ViewModel {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    // LiveData to hold the result
    // If successful, we hold the User object
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();

    // If failed, we hold the error message string
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    // Loading state (optional, but good for UX to show a spinner)
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // Constructor (Injected via Factory)
    public LoginViewModel(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    // Getters for UI to observe
    public LiveData<User> getLoginResult() { return loginResult; }
    public LiveData<String> getLoginError() { return loginError; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    // Logic: Called when user clicks "Login" button
    public void login(String username, String password) {
        // 1. Show Loading Spinner
        isLoading.setValue(true);

        // 2. Execute Use Case
        // Note: Repository usually handles background threads, but if not,
        // you might need to wrap this in a new Thread(() -> { ... }).start();
        authenticateUserUseCase.executeLogin(username, password, new AuthenticateUserUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(User user) {
                // Post value to main thread
                isLoading.postValue(false);
                loginResult.postValue(user);
            }

            @Override
            public void onError(String message) {
                // Post value to main thread
                isLoading.postValue(false);
                loginError.postValue(message);
            }
        });
    }
}