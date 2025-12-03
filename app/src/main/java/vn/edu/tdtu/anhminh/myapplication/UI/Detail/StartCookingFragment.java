package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import vn.edu.tdtu.anhminh.myapplication.R;

public class StartCookingFragment extends Fragment {

    private ViewPager2 viewPager;
    private ImageButton btnPrev, btnNext, btnClose;
    private TextView tvStepCounter;

    public StartCookingFragment() {
        super(R.layout.fragment_start_cooking);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        viewPager = view.findViewById(R.id.view_pager_steps);
        btnPrev = view.findViewById(R.id.btn_prev_step);
        btnNext = view.findViewById(R.id.btn_next_step);
        btnClose = view.findViewById(R.id.btn_close_cooking);
        tvStepCounter = view.findViewById(R.id.tv_step_counter);

        // 2. Handle Close Button
        btnClose.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        // 3. TODO: Setup ViewPager Adapter
        // StepAdapter adapter = new StepAdapter(recipe.getInstructions());
        // viewPager.setAdapter(adapter);

        // 4. Handle Arrows (Logic placeholders)
        btnPrev.setOnClickListener(v -> {
            // Logic: viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });

        btnNext.setOnClickListener(v -> {
            // Logic: viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });

        // 5. Handle Page Change (to update text "Step 1 of 5")
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update tvStepCounter here
            }
        });
    }
}
