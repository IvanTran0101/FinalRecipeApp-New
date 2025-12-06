package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation; // For back button

import java.util.Calendar;
import java.util.Map;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.CustomView.PieChartView;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.MealPlanViewModel;

public class AnalyticsFragment extends Fragment {

    private MealPlanViewModel viewModel;
    private TextView tvTotalCal, tvSummary, tvP, tvC, tvF;
    private PieChartView pieChart;

    public AnalyticsFragment() {
        super(R.layout.fragment_analytics);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        // Bind Views
        pieChart = view.findViewById(R.id.pie_chart);
        tvTotalCal = view.findViewById(R.id.tv_total_cal);
        tvSummary = view.findViewById(R.id.tv_recipes_summary);
        tvP = view.findViewById(R.id.tv_total_protein);
        tvC = view.findViewById(R.id.tv_total_carb);
        tvF = view.findViewById(R.id.tv_total_fat);

        View btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        loadData();

        viewModel.getAnalyticsData().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void loadData() {
        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();
        int weekId = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        viewModel.loadAnalytics(userId, weekId);
    }

    private void updateUI(Pair<Recipe, Map<String, Integer>> result) {
        if (result == null) return;

        Recipe totals = result.first;
        Map<String, Integer> frequencies = result.second;

        // 1. Update Nutrition
        if (totals != null) {
            double cal = totals.getCalories() != null ? totals.getCalories() : 0;
            double pro = totals.getProtein() != null ? totals.getProtein() : 0;
            double carb = totals.getCarb() != null ? totals.getCarb() : 0;
            double fat = totals.getFat() != null ? totals.getFat() : 0;

            // Set Text
            tvTotalCal.setText("Total Calories: " + (int)cal);
            tvP.setText("Total Protein: " + (int)pro + "g");
            tvC.setText("Total Carb: " + (int)carb + "g");
            tvF.setText("Total Fat: " + (int)fat + "g");

            // UPDATE CHART
            pieChart.setData(pro, carb, fat);
        }

        // 2. Update List (Name: Count Times)
        StringBuilder sb = new StringBuilder();
        if (frequencies == null || frequencies.isEmpty()) {
            sb.append("No recipes planned.");
        } else {
            for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
                sb.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append(" Times\n");
            }
        }
        tvSummary.setText(sb.toString());
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