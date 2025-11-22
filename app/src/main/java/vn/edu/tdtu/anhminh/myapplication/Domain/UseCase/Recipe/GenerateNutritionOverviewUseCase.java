package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class GenerateNutritionOverviewUseCase {
    public static class NutritionOverview{
        public double totalCalories;
        public double totalProteins;
        public double totalCarb;
        public double totalFat;
    }

    public NutritionOverview execute(Recipe recipe){
        if (recipe == null) return null;

        NutritionOverview result = new NutritionOverview();
        result.totalCalories = recipe.getCalories() != null ? recipe.getCalories() :0;
        result.totalProteins = recipe.getProtein() != null ? recipe.getProtein() :0;
        result.totalCarb = recipe.getCarb() != null ? recipe.getCarb() :0;
        result.totalFat = recipe.getFat() != null ? recipe.getFat() :0;
        return result;
    }

    public NutritionOverview execute(List<Recipe> recipes){
        if (recipes == null || recipes.isEmpty()) return null;

        NutritionOverview result = new NutritionOverview();

        for (Recipe r : recipes){
            if (r == null) continue;
            if (r.getCalories() != null) result.totalCalories += r.getCalories();
            if (r.getProtein() != null) result.totalProteins += r.getProtein();
            if (r.getCarb() != null) result.totalCarb += r.getCarb();
            if (r.getFat() != null) result.totalFat += r.getFat();

        }
        return result;
    }
}
