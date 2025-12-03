package vn.edu.tdtu.anhminh.myapplication.UI.Components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Login.LoginActivity;

public class UserProfileFragment extends Fragment {
    public UserProfileFragment() { super(R.layout.fragment_profile); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Logic to load user data and handle clicks will go here

        Button btnLogout = view.findViewById(R.id.btn_logout);

        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();

        btnLogout.setOnClickListener(v -> {
            prefs.clearPrefs();

            // B. Navigate back to Login Activity
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            // Clear backstack so user can't press back to return here
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
