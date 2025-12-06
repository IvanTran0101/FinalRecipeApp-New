package vn.edu.tdtu.anhminh.myapplication.UI.Timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.CookingTimerViewModel;

public class CookingTimerFragment extends Fragment {

    private CookingTimerViewModel viewModel;

    // UI Components
    private TextView tvCountdown;
    private ProgressBar progressBar;
    private Button btnStart, btnCancel;

    // Pickers
    private NumberPicker npHours, npMinutes, npSeconds;

    // Containers for toggling visibility
    private FrameLayout layoutRunning;
    private LinearLayout layoutPicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cooking_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CookingTimerViewModel.class);

        // Bind Views
        tvCountdown = view.findViewById(R.id.tv_countdown);
        progressBar = view.findViewById(R.id.progress_bar_timer);
        btnStart = view.findViewById(R.id.btn_start_timer);
        btnCancel = view.findViewById(R.id.btn_cancel_timer);
        ImageView ivBack = view.findViewById(R.id.iv_back);

        layoutRunning = view.findViewById(R.id.layout_timer_running);
        layoutPicker = view.findViewById(R.id.layout_time_picker);

        npHours = view.findViewById(R.id.np_hours);
        npMinutes = view.findViewById(R.id.np_minutes);
        npSeconds = view.findViewById(R.id.np_seconds);

        // Setup Pickers
        setupPickers();

        // --- OBSERVE LIVE DATA ---
        viewModel.getTimeLeftText().observe(getViewLifecycleOwner(), text ->
                tvCountdown.setText(text)
        );

        viewModel.getProgress().observe(getViewLifecycleOwner(), progress ->
                progressBar.setProgress(progress)
        );

        viewModel.getIsTimerRunning().observe(getViewLifecycleOwner(), isRunning -> {
            updateUIState(isRunning);
        });

        // --- LISTENERS ---
        btnStart.setOnClickListener(v -> {
            int hours = npHours.getValue();
            int minutes = npMinutes.getValue();
            int seconds = npSeconds.getValue();

            long totalMillis = (hours * 3600L + minutes * 60L + seconds) * 1000L;

            if (totalMillis == 0) {
                Toast.makeText(getContext(), "Please set a time!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.startCookingTimer(totalMillis);
        });

        btnCancel.setOnClickListener(v -> {
            viewModel.stopCookingTimer();
        });

        ivBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void setupPickers() {
        // Hours: 0 to 99
        npHours.setMinValue(0);
        npHours.setMaxValue(99);
        npHours.setFormatter(i -> String.format("%02d", i));

        // Minutes: 0 to 59
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npMinutes.setFormatter(i -> String.format("%02d", i));

        // Seconds: 0 to 59
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
        npSeconds.setFormatter(i -> String.format("%02d", i));
    }

    private void updateUIState(boolean isRunning) {
        if (isRunning) {
            // Running: Show Progress Circle, Hide Pickers
            layoutRunning.setVisibility(View.VISIBLE);
            layoutPicker.setVisibility(View.GONE);

            btnStart.setEnabled(false);
            btnCancel.setEnabled(true);
        } else {
            // Stopped: Hide Progress Circle, Show Pickers
            layoutRunning.setVisibility(View.GONE);
            layoutPicker.setVisibility(View.VISIBLE);

            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            if (enter) {
                // When fragment is opening
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            } else {
                // When fragment is closing (popBackStack)
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
