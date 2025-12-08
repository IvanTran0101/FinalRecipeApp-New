package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

public class MealPlanItem {
    private final Plan plan;
    private final Recipe recipe;

    public MealPlanItem(Plan plan, Recipe recipe) {
        this.plan = plan;
        this.recipe = recipe;
    }

    public Plan getPlan() { return plan; }
    public Recipe getRecipe() { return recipe; }
}