package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class GenerateNutritionOverviewUseCase {
    public GenerateNutritionOverviewUseCase() {}

    public Recipe execute(List<MealPlanItem> weeklyItems) {
        Recipe totalStats = new Recipe();

        double sumCal = 0;
        double sumPro = 0;
        double sumCarb = 0;
        double sumFat = 0;

        if (weeklyItems != null && !weeklyItems.isEmpty()) {
            for (MealPlanItem item : weeklyItems) {
                Recipe r = item.getRecipe();
                if (r == null) continue;

                if (r.getCalories() != null) sumCal += r.getCalories();
                if (r.getProtein() != null) sumPro += r.getProtein();
                if (r.getCarb() != null) sumCarb += r.getCarb();
                if (r.getFat() != null) sumFat += r.getFat();
            }
        }

        totalStats.setCalories(sumCal);
        totalStats.setProtein(sumPro);
        totalStats.setCarb(sumCarb);
        totalStats.setFat(sumFat);

        totalStats.setTitle("Weekly Totals");

        return totalStats;
    }
}