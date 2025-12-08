package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.PlanRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.ShoppingListItem;

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
            return new ArrayList<>();
        }

        List<Integer> distinctRecipeIds = weeklyPlans.stream()
                .map(Plan::getRecipeId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Double> aggregatedQuantities = new HashMap<>();
        Map<String, String> unitMap = new HashMap<>();

        for (int recipeId : distinctRecipeIds) {
            List<Ingredient> ingredients = ingredientRepository.getIngredientsForRecipeSync(recipeId);

            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName() == null || ingredient.getUnit() == null) continue;

                String key = ingredient.getName().trim().toLowerCase() + "_" + ingredient.getUnit().trim().toLowerCase();

                if (!unitMap.containsKey(key)) {
                    unitMap.put(key, ingredient.getUnit());
                }

                double currentTotal = aggregatedQuantities.getOrDefault(key, 0.0);
                aggregatedQuantities.put(key, currentTotal + ingredient.getQuantity());
            }
        }

        List<ShoppingListItem> finalShoppingList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : aggregatedQuantities.entrySet()) {
            String key = entry.getKey();
            String rawName = key.split("_")[0];

            String name = rawName.substring(0, 1).toUpperCase() + rawName.substring(1);

            String unit = unitMap.getOrDefault(key, "");
            double qty = entry.getValue();

            finalShoppingList.add(new ShoppingListItem(name, qty, unit));
        }
        return finalShoppingList;
    }
}