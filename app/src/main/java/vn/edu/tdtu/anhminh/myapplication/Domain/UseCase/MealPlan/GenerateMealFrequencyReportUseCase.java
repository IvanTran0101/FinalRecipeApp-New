package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.PlanRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class GenerateMealFrequencyReportUseCase {
    private static class RecipeFrequencyItem {
        private final int recipeId;
        private final String name;
        private final int frequency;

        public RecipeFrequencyItem(int recipeId, String name, int frequency) {
            this.recipeId = recipeId;
            this.name = name;
            this.frequency = frequency;
        }

        public int getRecipeId() { return recipeId; }
        public String getName() { return name; }
        public int getFrequency() { return frequency; }
    }

    private final PlanRepository planRepository;
    private final RecipeRepository recipeRepository;

    public GenerateMealFrequencyReportUseCase(PlanRepository planRepository, RecipeRepository recipeRepository) {
        this.planRepository = planRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeFrequencyItem> executeReport(int userId, int weekId) {
        if (userId <= 0 || weekId <= 0) return new ArrayList<>();

        List<Plan> weeklyPlans = planRepository.getPlanForWeekSync(userId, weekId);
        if (weeklyPlans == null || weeklyPlans.isEmpty()) return new ArrayList<>();

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (Plan plan : weeklyPlans) {
            int recipeId = plan.getRecipeId();
            frequencyMap.put(recipeId, frequencyMap.getOrDefault(recipeId, 0) + 1);
        }

        List<RecipeFrequencyItem> finalReport = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int recipeId = entry.getKey();
            int count = entry.getValue();

            Recipe recipe = recipeRepository.getRecipeDetail(recipeId);

            String recipeName = (recipe != null && recipe.getTitle() != null) ? recipe.getTitle() : "Unknown Recipe (ID: " + recipeId + ")";

            finalReport.add(new RecipeFrequencyItem(recipeId, recipeName, count));
        }

        return finalReport;
    }
}
