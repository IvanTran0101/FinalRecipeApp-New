package vn.edu.tdtu.anhminh.myapplication.UI.Authenticate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.RegisterUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword, etRetypePassword;
    private Button btnRegister;
    private TextView tvLoginPrompt;
    private RegisterUserUseCase registerUserUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserRepository userRepository = new UserRepository(this);
        registerUserUseCase = new RegisterUserUseCase(userRepository);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRetypePassword = findViewById(R.id.et_retype_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginPrompt = findViewById(R.id.tv_login_prompt);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> registerUser());
        tvLoginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String retypePassword = etRetypePassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(retypePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUserUseCase.execute(username, password, new UserRepository.RepositoryCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
