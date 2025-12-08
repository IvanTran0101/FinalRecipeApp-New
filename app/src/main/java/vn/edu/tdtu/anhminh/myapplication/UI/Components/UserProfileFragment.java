package vn.edu.tdtu.anhminh.myapplication.UI.Components;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.User;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Authenticate.LoginActivity;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.UserViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class UserProfileFragment extends Fragment {

    private UserViewModel viewModel;
    private EditText etUsername;
    private TextView tvUsernameLabel;
    private CardView cardAvatar;
    private ImageView imgAvatar;
    private Button btnSave;
    private Button btnLogout;

    // State
    private User currentUser;
    private String selectedAvatarUri = null;
    private ActivityResultLauncher<String> pickImageLauncher;

    public UserProfileFragment() { super(R.layout.fragment_profile); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String localPath = saveImageToInternalStorage(uri);
                if (localPath != null) {
                    selectedAvatarUri = localPath;
                    updateAvatarView(selectedAvatarUri);
                } else {
                    Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        tvUsernameLabel = view.findViewById(R.id.tv_username_label);
        etUsername = view.findViewById(R.id.et_username);
        cardAvatar = view.findViewById(R.id.card_avatar);
        imgAvatar = (ImageView) cardAvatar.getChildAt(0);
        btnSave = view.findViewById(R.id.btn_save_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();

        viewModel.loadUserProfile(userId);

        observeViewModel();

        cardAvatar.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> {
            if (currentUser != null) {
                String inputName = etUsername.getText().toString().trim();
                String finalName = inputName.isEmpty() ? currentUser.getUsername() : inputName;
                viewModel.updateUser(currentUser, finalName, selectedAvatarUri);
            }
        });

        btnLogout.setOnClickListener(v -> handleLogout(prefs));
    }

    private void observeViewModel() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.currentUser = user;
                populateUI(user);
            }
        });

        viewModel.getUpdateResult().observe(getViewLifecycleOwner(), updatedUser -> {
            if (updatedUser != null) {
                this.currentUser = updatedUser;
                populateUI(updatedUser);
                etUsername.setText("");
                Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(User user) {
        tvUsernameLabel.setText(user.getUsername());
        if (user.getAvatarImage() != null) {
            selectedAvatarUri = user.getAvatarImage();
            updateAvatarView(selectedAvatarUri);
        }
    }

    private void updateAvatarView(String uriString) {
        try {
            imgAvatar.setImageURI(Uri.parse(uriString));
            imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (Exception e) {
            imgAvatar.setImageResource(android.R.drawable.ic_menu_camera);
        }
    }

    private void handleLogout(UserPrefs prefs) {
        prefs.clearPrefs();
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            File file = new File(requireContext().getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return Uri.fromFile(file).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}