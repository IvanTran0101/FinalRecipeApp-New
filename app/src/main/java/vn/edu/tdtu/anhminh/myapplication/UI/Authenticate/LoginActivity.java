package vn.edu.tdtu.anhminh.myapplication.UI.Authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Main.MainActivity;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.UserViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel viewModel;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegisterPrompt;
    private UserPrefs userPrefs; // 1. Add Prefs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. CHECK AUTO LOGIN BEFORE SETTING VIEW
        userPrefs = UserPrefs.getInstance(this);
        if (userPrefs.isLoggedIn()) {
            navigateToMain();
            return; // Stop execution of onCreate
        }

        setContentView(R.layout.activity_login);

        // 3. Initialize Views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegisterPrompt = findViewById(R.id.tv_register_prompt);

        // 4. Setup ViewModel
        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        // 5. Observe Success
        viewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                // SAVE TO SHARED PREF
                userPrefs.saveUserId(user.getUserId());
                userPrefs.setIsLoggedIn(true);

                Toast.makeText(this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
        });

        // 6. Observe Errors
        viewModel.getLoginError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        // 7. Observe Loading
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                btnLogin.setEnabled(!isLoading);
                btnLogin.setText(isLoading ? "Logging in..." : "Login");
            }
        });

        // 8. Handle Click
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.login(username, password);
        });

        tvRegisterPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Helper method to navigate to Main Activity
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // Clear the back stack so user cannot press "Back" to return to Login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
