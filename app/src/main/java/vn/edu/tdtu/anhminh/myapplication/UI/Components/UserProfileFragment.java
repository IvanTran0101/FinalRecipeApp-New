package vn.edu.tdtu.anhminh.myapplication.UI.Components;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.edu.tdtu.anhminh.myapplication.R;

public class UserProfileFragment extends Fragment {
    public UserProfileFragment() { super(R.layout.fragment_profile); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Logic to load user data and handle clicks will go here
    }
}
