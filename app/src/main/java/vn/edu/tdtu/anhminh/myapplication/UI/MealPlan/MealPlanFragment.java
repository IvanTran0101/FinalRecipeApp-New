package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.MealPlanAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.MealPlanViewModel;

public class MealPlanFragment extends Fragment implements AddMealDialogFragment.OnRecipeSelectedListener {

    private Calendar currentWeek;
    private TextView weekTitle;
    private MealPlanAdapter mealPlanAdapter;
    private MealPlanViewModel mealPlanViewModel;

    public MealPlanFragment() {
        super(R.layout.fragment_meal_plan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealPlanViewModel = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        currentWeek = Calendar.getInstance();
        weekTitle = view.findViewById(R.id.tv_week_title);
        ImageView prevWeekButton = view.findViewById(R.id.btn_prev_week);
        ImageView nextWeekButton = view.findViewById(R.id.btn_next_week);
        RecyclerView weekDaysRecycler = view.findViewById(R.id.recycler_week_days);
        ImageView btnCart = view.findViewById(R.id.btn_cart);
        Button btnStatistic = view.findViewById(R.id.btn_statistic);

        updateWeekTitle();

        prevWeekButton.setOnClickListener(v -> {
            currentWeek.add(Calendar.WEEK_OF_YEAR, -1);
            refreshData();
        });

        nextWeekButton.setOnClickListener(v -> {
            currentWeek.add(Calendar.WEEK_OF_YEAR, 1);
            refreshData();
        });

        btnCart.setOnClickListener(v -> {
            // Navigate to Shopping List
            Navigation.findNavController(view).navigate(R.id.action_mealPlan_to_shoppingList);
        });

        btnStatistic.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_mealPlan_to_analytics);
        });

        mealPlanAdapter = new MealPlanAdapter(getDaysOfWeek(), new MealPlanAdapter.MealPlanInteractionListener() {
            @Override
            public void onAddMealClick(Calendar day) {
                showAddMealDialog(day);
            }
            @Override
            public void onRemoveMealClick(Plan plan) {
                mealPlanViewModel.removeRecipeFromPlan(plan);
            }
        });

        weekDaysRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        weekDaysRecycler.setAdapter(mealPlanAdapter);

        loadMealPlanFromDb();
    }

    private void refreshData() {
        updateWeekTitle();
        mealPlanAdapter.updateDays(getDaysOfWeek());
        loadMealPlanFromDb();
    }

    private void loadMealPlanFromDb() {
        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int currentUserId = prefs.getUserId();
        int weekId = currentWeek.get(Calendar.WEEK_OF_YEAR);

        mealPlanViewModel.getWeeklyPlan(currentUserId, weekId).observe(getViewLifecycleOwner(), plans -> {
            Calendar weekStart = (Calendar) currentWeek.clone();
            weekStart.setFirstDayOfWeek(Calendar.MONDAY);
            weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            mealPlanAdapter.setMealData(plans, weekStart);
        });
    }

    private void updateWeekTitle() {
        Calendar weekStart = (Calendar) currentWeek.clone();
        weekStart.setFirstDayOfWeek(Calendar.MONDAY);
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Calendar weekEnd = (Calendar) weekStart.clone();
        weekEnd.add(Calendar.DAY_OF_YEAR, 6);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        String title = sdf.format(weekStart.getTime()) + " - " + sdf.format(weekEnd.getTime());
        weekTitle.setText(title);
    }

    private ArrayList<Calendar> getDaysOfWeek() {
        ArrayList<Calendar> days = new ArrayList<>();
        Calendar day = (Calendar) currentWeek.clone();
        day.setFirstDayOfWeek(Calendar.MONDAY);
        day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i < 7; i++) {
            days.add((Calendar) day.clone());
            day.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    private void showAddMealDialog(Calendar day) {
        AddMealDialogFragment dialog = AddMealDialogFragment.newInstance(day);
        dialog.setOnRecipeSelectedListener(this);
        dialog.show(getParentFragmentManager(), "AddMealDialogFragment");
    }

    @Override
    public void onRecipeSelected(Recipe recipe, Calendar day) {
        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int currentUserId = prefs.getUserId();
        int weekId = currentWeek.get(Calendar.WEEK_OF_YEAR);
        int weekNumber = currentWeek.get(Calendar.WEEK_OF_YEAR);

        int dayOfWeekIndex = 0;
        int calDay = day.get(Calendar.DAY_OF_WEEK);
        if(calDay == Calendar.MONDAY) dayOfWeekIndex = 1;
        else if(calDay == Calendar.TUESDAY) dayOfWeekIndex = 2;
        else if(calDay == Calendar.WEDNESDAY) dayOfWeekIndex = 3;
        else if(calDay == Calendar.THURSDAY) dayOfWeekIndex = 4;
        else if(calDay == Calendar.FRIDAY) dayOfWeekIndex = 5;
        else if(calDay == Calendar.SATURDAY) dayOfWeekIndex = 6;
        else if(calDay == Calendar.SUNDAY) dayOfWeekIndex = 7;

        mealPlanViewModel.addRecipeToPlan(currentUserId, weekId, weekNumber, dayOfWeekIndex, recipe.getRecipeId());
    }
}
