package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vn.edu.tdtu.anhminh.myapplication.R;

public class MealPlanFragment extends Fragment {
    public MealPlanFragment() {
        super(R.layout.fragment_meal_plan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Initialize RecyclerView with an Adapter for the days of the week
        // Use R.layout.item_meal_plan_day for the rows
    }
}