package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class GenerateMealFrequencyReportUseCase {

    public GenerateMealFrequencyReportUseCase() {
    }
    public Map<String, Integer> executeReport(List<MealPlanItem> weeklyItems) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        if (weeklyItems == null || weeklyItems.isEmpty()) return frequencyMap;

        for (MealPlanItem item : weeklyItems) {
            Recipe recipe = item.getRecipe();
            if (recipe != null) {
                String name = recipe.getTitle();
                // Count: Get current count (default 0) and add 1
                frequencyMap.put(name, frequencyMap.getOrDefault(name, 0) + 1);
            }
        }
        return frequencyMap;
    }
}