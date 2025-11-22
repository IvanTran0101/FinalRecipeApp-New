package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.PlanRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;

public class GenerateShoppingListUseCase {
    private final PlanRepository planRepository;
    private final IngredientRepository ingredientRepository;

    public GenerateShoppingListUseCase(PlanRepository planRepository, IngredientRepository ingredientRepository) {
        this.planRepository = planRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<ShoppingListItem> executeGeneration(int userId, int weekId) {
        if (userId <= 0 || weekId <= 0) {
            return new ArrayList<>();
        }

        List<Plan> weeklyPlans = planRepository.getPlanForWeekSync(userId, weekId);

        if (weeklyPlans == null || weeklyPlans.isEmpty()) {
            return new ArrayList<>(); //No plans for the week
        }

        //Get a distinct list of recipe IDs scheduled this week
        List<Integer> distinctRecipeIds = weeklyPlans.stream().map(Plan::getRecipeId).distinct().collect(Collectors.toList());

        Map<String, Double> aggregatedQuantities = new HashMap<>();

        for (int recipeId : distinctRecipeIds) {
            List<Ingredient> ingredients = ingredientRepository.getAllIngredientsByRecipeIdSync(recipeId);

            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName() == null || ingredient.getUnit() == null) {
                    continue;
                }

                String key = ingredient.getName().trim().toLowerCase() + "_" + ingredient.getUnit().trim().toLowerCase();

                double currentTotal = aggregatedQuantities.getOrDefault(key, 0.0);
                aggregatedQuantities.put(key, currentTotal + ingredient.getQuantity());
            }
        }

        List<ShoppingListItem> finalShoppingList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : aggregatedQuantities.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("_", 2);

            String name = parts[0];
            String unit = (parts.length > 1) ? parts[1] : "";

            double finalQuantity = entry.getValue();

            String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);

            finalShoppingList.add(new ShoppingListItem(capitalizedName, finalQuantity, unit));
        }

        return finalShoppingList;
    }

    private static class ShoppingListItem {
        private final String name;
        private final double totalQuantity;
        private final String unit;

        public ShoppingListItem(String name, double totalQuantity, String unit) {
            this.name = name;
            this.totalQuantity = totalQuantity;
            this.unit = unit;
        }
    }
}
