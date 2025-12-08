package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.PlanWithRecipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.Utils.WeekDay;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.ViewHolder> {

    private ArrayList<Calendar> days;
    private Map<String, List<MealPlanItem>> mealData = new HashMap<>();
    private final MealPlanInteractionListener listener;

    public interface MealPlanInteractionListener {
        void onAddMealClick(Calendar day);
        void onRemoveMealClick(Plan plan);
    }

    public MealPlanAdapter(ArrayList<Calendar> days, MealPlanInteractionListener listener) {
        this.days = days;
        this.listener = listener;
    }

    public void setMealData(List<MealPlanItem> allPlans, Calendar weekStart) {
        Map<String, List<MealPlanItem>> newMealData = new HashMap<>();
        if (allPlans != null) {
            for (MealPlanItem item : allPlans) {
                String key = convertPlanToDateKey(weekStart, item.getPlan().getWeekDay());
                if (!newMealData.containsKey(key)) {
                    newMealData.put(key, new ArrayList<>());
                }
                newMealData.get(key).add(item);
            }
        }

        List<Integer> indexesToUpdate = new ArrayList<>();

        for (int i = 0; i < days.size(); i++) {
            Calendar day = days.get(i);
            String key = getDateKey(day);

            List<MealPlanItem> oldList = this.mealData.get(key);
            List<MealPlanItem> newList = newMealData.get(key);

            if (isDayChanged(oldList, newList)) {
                indexesToUpdate.add(i);
            }
        }

        this.mealData.clear();
        this.mealData.putAll(newMealData);

        for (int index : indexesToUpdate) {
            notifyItemChanged(index);
        }
    }

    private boolean isDayChanged(List<MealPlanItem> oldList, List<MealPlanItem> newList) {
        if (oldList == null && newList == null) return false;
        if (oldList == null || newList == null) return true;
        if (oldList.size() != newList.size()) return true;

        for (int i = 0; i < oldList.size(); i++) {
            int oldId = oldList.get(i).getRecipe().getRecipeId();
            int newId = newList.get(i).getRecipe().getRecipeId();
            if (oldId != newId) return true;
        }

        return false;
    }

    private String convertPlanToDateKey(Calendar weekStart, int dayOfWeekIndex) {
        Calendar cal = (Calendar) weekStart.clone();
        cal.add(Calendar.DAY_OF_YEAR, dayOfWeekIndex - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private String getDateKey(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_plan_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar day = days.get(position);
        String key = getDateKey(day);
        List<MealPlanItem> items = mealData.get(key);
        holder.bind(day, items, listener);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void updateDays(ArrayList<Calendar> newDays) {
        this.days = newDays;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayOfWeek;
        private final ChipGroup mealChipGroup;
        private final View addMealButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.tv_day_of_week); // Ensure ID matches XML
            mealChipGroup = itemView.findViewById(R.id.chip_group_meals);
            addMealButton = itemView.findViewById(R.id.btn_add_meal);
        }

        public void bind(final Calendar day, List<MealPlanItem> items, final MealPlanInteractionListener listener) {
            WeekDay displayEnum = getWeekDayFromCalendar(day);
            if (displayEnum != null) dayOfWeek.setText(displayEnum.name());

            addMealButton.setOnClickListener(v -> listener.onAddMealClick(day));

            mealChipGroup.removeAllViews();
            if (items != null) {
                for (MealPlanItem item : items) {
                    Chip chip = new Chip(itemView.getContext());

                    if (item.getRecipe() != null) {
                        chip.setText(item.getRecipe().getTitle());
                    } else {
                        chip.setText("Unknown");
                    }

                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(v -> {
                        listener.onRemoveMealClick(item.getPlan());
                    });
                    mealChipGroup.addView(chip);
                }
            }
        }

        private WeekDay getWeekDayFromCalendar(Calendar calendar) {
            int dayInt = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayInt) {
                case Calendar.MONDAY:    return WeekDay.Mon;
                case Calendar.TUESDAY:   return WeekDay.Tue;
                case Calendar.WEDNESDAY: return WeekDay.Wed;
                case Calendar.THURSDAY:  return WeekDay.Thu;
                case Calendar.FRIDAY:    return WeekDay.Fri;
                case Calendar.SATURDAY:  return WeekDay.Sat;
                case Calendar.SUNDAY:    return WeekDay.Sun;
                default: return null;
            }
        }
    }
}