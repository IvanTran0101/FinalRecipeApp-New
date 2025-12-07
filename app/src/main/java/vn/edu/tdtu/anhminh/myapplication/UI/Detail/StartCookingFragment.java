package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.CookingStepAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class StartCookingFragment extends Fragment {

    private ViewPager2 viewPager;
    private ImageButton btnPrev, btnNext, btnClose;
    private TextView tvStepCounter;
    private RecipeViewModel viewModel;

    public StartCookingFragment() {
        super(R.layout.fragment_start_cooking);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init Views
        viewPager = view.findViewById(R.id.view_pager_steps);
        btnPrev = view.findViewById(R.id.btn_prev_step);
        btnNext = view.findViewById(R.id.btn_next_step);
        btnClose = view.findViewById(R.id.btn_close_cooking);
        tvStepCounter = view.findViewById(R.id.tv_step_counter);

        // 2. Setup ViewModel
        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // 3. Close Button
        btnClose.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        // 4. Load Data
        if (getArguments() != null) {
            int recipeId = getArguments().getInt("recipe_id", -1);
            if (recipeId != -1) {

                // === CRITICAL FIX: Load Instructions List directly ===
                viewModel.getInstructions(recipeId).observe(getViewLifecycleOwner(), instructions -> {
                    if (instructions != null && !instructions.isEmpty()) {

                        // Setup Adapter
                        CookingStepAdapter adapter = new CookingStepAdapter(instructions);
                        viewPager.setAdapter(adapter);

                        // Initial Counter Update
                        updateCounter(0, instructions.size());

                        // Handle Arrow Visibility
                        btnPrev.setVisibility(View.INVISIBLE); // Hide Prev on first step
                        if (instructions.size() <= 1) btnNext.setVisibility(View.INVISIBLE); // Hide Next if only 1 step
                    } else {
                        Toast.makeText(getContext(), "No instructions found.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // 5. Handle Arrows
        btnPrev.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) {
                viewPager.setCurrentItem(current - 1);
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (viewPager.getAdapter() != null && current < viewPager.getAdapter().getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            }
        });

        // 6. Handle Page Change
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (viewPager.getAdapter() != null) {
                    int total = viewPager.getAdapter().getItemCount();
                    updateCounter(position, total);

                    // Toggle Arrow Visibility for better UX
                    btnPrev.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
                    btnNext.setVisibility(position == total - 1 ? View.INVISIBLE : View.VISIBLE);
                }
            }
        });
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

    private void updateCounter(int current, int total) {
        tvStepCounter.setText("Step " + (current + 1) + " of " + total);
    }
}