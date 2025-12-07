package vn.edu.tdtu.anhminh.myapplication.Data.Local;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;

public class DummyData {

    public static void createDummyRecipes(RecipeViewModel viewModel, int userId) {
        viewModel.createRecipe(createPastaRecipe(userId), createPastaIngredients(), createPastaInstructions());
        viewModel.createRecipe(createDonutRecipe(userId), createDonutIngredients(), createDonutInstructions());
        viewModel.createRecipe(createPhoRecipe(userId), createPhoIngredients(), createPhoInstructions());
        viewModel.createRecipe(createSaladRecipe(userId), createSaladIngredients(), createSaladInstructions());
        viewModel.createRecipe(createPizzaRecipe(userId), createPizzaIngredients(), createPizzaInstructions());
    }

    private static Recipe createPastaRecipe(int userId) {
        Recipe recipe = new Recipe();
        recipe.setUserId(userId);
        recipe.setTitle("Spaghetti Carbonara");
        recipe.setRecipeImage("android.resource://vn.edu.tdtu.anhminh.myapplication/" + R.drawable.image_pasta);
        recipe.setCategory("Dinner");
        recipe.setDietMode("None");
        recipe.setCalories(600.0);
        recipe.setProtein(25.0);
        recipe.setFat(30.0);
        recipe.setCarb(55.0);
        return recipe;
    }

    private static List<Ingredient> createPastaIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Spaghetti");
        ingredient1.setQuantity(200);
        ingredient1.setUnit("g");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Pancetta");
        ingredient2.setQuantity(100);
        ingredient2.setUnit("g");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Eggs");
        ingredient3.setQuantity(2);
        ingredient3.setUnit("pcs");
        ingredients.add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("Parmesan Cheese");
        ingredient4.setQuantity(50);
        ingredient4.setUnit("g");
        ingredients.add(ingredient4);

        Ingredient ingredient5 = new Ingredient();
        ingredient5.setName("Black Pepper");
        ingredient5.setQuantity(1);
        ingredient5.setUnit("tsp");
        ingredients.add(ingredient5);

        return ingredients;
    }

    private static List<Instruction> createPastaInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        Instruction instruction1 = new Instruction();
        instruction1.setInstruction("Cook spaghetti according to package directions.");
        instruction1.setStepNumber(1);
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction();
        instruction2.setInstruction("Fry pancetta until crisp.");
        instruction2.setStepNumber(2);
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction();
        instruction3.setInstruction("Whisk eggs and cheese.");
        instruction3.setStepNumber(3);
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction();
        instruction4.setInstruction("Drain spaghetti and combine with pancetta, egg mixture, and pepper.");
        instruction4.setStepNumber(4);
        instructions.add(instruction4);
        return instructions;
    }

    private static Recipe createDonutRecipe(int userId) {
        Recipe recipe = new Recipe();
        recipe.setUserId(userId);
        recipe.setTitle("Glazed Donuts");
        recipe.setRecipeImage("android.resource://vn.edu.tdtu.anhminh.myapplication/" + R.drawable.image_donut);
        recipe.setCategory("Dessert");
        recipe.setDietMode("None");
        recipe.setCalories(300.0);
        recipe.setProtein(4.0);
        recipe.setFat(15.0);
        recipe.setCarb(35.0);
        return recipe;
    }

    private static List<Ingredient> createDonutIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Flour");
        ingredient1.setQuantity(2);
        ingredient1.setUnit("cups");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Sugar");
        ingredient2.setQuantity(0.5);
        ingredient2.setUnit("cups");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Milk");
        ingredient3.setQuantity(1);
        ingredient3.setUnit("cup");
        ingredients.add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("Butter");
        ingredient4.setQuantity(0.25);
        ingredient4.setUnit("cups");
        ingredients.add(ingredient4);

        Ingredient ingredient5 = new Ingredient();
        ingredient5.setName("Yeast");
        ingredient5.setQuantity(1);
        ingredient5.setUnit("tbsp");
        ingredients.add(ingredient5);
        return ingredients;
    }

    private static List<Instruction> createDonutInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        Instruction instruction1 = new Instruction();
        instruction1.setInstruction("Mix all ingredients to form a dough.");
        instruction1.setStepNumber(1);
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction();
        instruction2.setInstruction("Let the dough rise for 1 hour.");
        instruction2.setStepNumber(2);
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction();
        instruction3.setInstruction("Cut out donut shapes and fry until golden brown.");
        instruction3.setStepNumber(3);
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction();
        instruction4.setInstruction("Glaze with a mixture of powdered sugar and milk.");
        instruction4.setStepNumber(4);
        instructions.add(instruction4);

        return instructions;
    }

    private static Recipe createPhoRecipe(int userId) {
        Recipe recipe = new Recipe();
        recipe.setUserId(userId);
        recipe.setPinned(true);
        recipe.setTitle("Pho Bo");
        recipe.setRecipeImage("android.resource://vn.edu.tdtu.anhminh.myapplication/" + R.drawable.image_pho);
        recipe.setCategory("Lunch");
        recipe.setDietMode("None");
        recipe.setCalories(400.0);
        recipe.setProtein(30.0);
        recipe.setFat(10.0);
        recipe.setCarb(45.0);
        return recipe;
    }

    private static List<Ingredient> createPhoIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Beef Bones");
        ingredient1.setQuantity(1);
        ingredient1.setUnit("kg");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Rice Noodles");
        ingredient2.setQuantity(200);
        ingredient2.setUnit("g");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Beef Sirloin");
        ingredient3.setQuantity(200);
        ingredient3.setUnit("g");
        ingredients.add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("Onion");
        ingredient4.setQuantity(1);
        ingredient4.setUnit("pcs");
        ingredients.add(ingredient4);

        Ingredient ingredient5 = new Ingredient();
        ingredient5.setName("Ginger");
        ingredient5.setQuantity(1);
        ingredient5.setUnit("pcs");
        ingredients.add(ingredient5);

        return ingredients;
    }

    private static List<Instruction> createPhoInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        Instruction instruction1 = new Instruction();
        instruction1.setInstruction("Simmer beef bones for 6-8 hours to make the broth.");
        instruction1.setStepNumber(1);
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction();
        instruction2.setInstruction("Cook rice noodles according to package directions.");
        instruction2.setStepNumber(2);
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction();
        instruction3.setInstruction("Slice beef sirloin thinly.");
        instruction3.setStepNumber(3);
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction();
        instruction4.setInstruction("Assemble the bowl with noodles, beef, and broth.");
        instruction4.setStepNumber(4);
        instructions.add(instruction4);
        return instructions;
    }

    private static Recipe createSaladRecipe(int userId) {
        Recipe recipe = new Recipe();
        recipe.setUserId(userId);
        recipe.setTitle("Caesar Salad");
        recipe.setRecipeImage("android.resource://vn.edu.tdtu.anhminh.myapplication/" + R.drawable.image_salad);
        recipe.setCategory("Breakfast");
        recipe.setDietMode("Vegetarian");
        recipe.setCalories(250.0);
        recipe.setProtein(10.0);
        recipe.setFat(15.0);
        recipe.setCarb(20.0);
        return recipe;
    }

    private static List<Ingredient> createSaladIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Romaine Lettuce");
        ingredient1.setQuantity(1);
        ingredient1.setUnit("head");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Croutons");
        ingredient2.setQuantity(1);
        ingredient2.setUnit("cup");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Parmesan Cheese");
        ingredient3.setQuantity(0.5);
        ingredient3.setUnit("cups");
        ingredients.add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("Caesar Dressing");
        ingredient4.setQuantity(0.25);
        ingredient4.setUnit("cups");
        ingredients.add(ingredient4);

        return ingredients;
    }

    private static List<Instruction> createSaladInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        Instruction instruction1 = new Instruction();
        instruction1.setInstruction("Wash and chop the romaine lettuce.");
        instruction1.setStepNumber(1);
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction();
        instruction2.setInstruction("Toss lettuce with croutons, parmesan cheese, and Caesar dressing.");
        instruction2.setStepNumber(2);
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction();
        instruction3.setInstruction("Serve immediately.");
        instruction3.setStepNumber(3);
        instructions.add(instruction3);
        return instructions;
    }

    private static Recipe createPizzaRecipe(int userId) {
        Recipe recipe = new Recipe();
        recipe.setUserId(userId);
        recipe.setTitle("Hawaiian Pizza");
        recipe.setRecipeImage("android.resource://vn.edu.tdtu.anhminh.myapplication/" + R.drawable.image_pizza);
        recipe.setCategory("Dinner");
        recipe.setDietMode("None");
        recipe.setCalories(800.0);
        recipe.setProtein(30.0);
        recipe.setFat(35.0);
        recipe.setCarb(90.0);
        return recipe;
    }

    private static List<Ingredient> createPizzaIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Pizza Dough");
        ingredient1.setQuantity(1);
        ingredient1.setUnit("ball");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Tomato Sauce");
        ingredient2.setQuantity(0.5);
        ingredient2.setUnit("cup");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setName("Mozzarella Cheese");
        ingredient3.setQuantity(200);
        ingredient3.setUnit("g");
        ingredients.add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setName("Basil");
        ingredient4.setQuantity(1);
        ingredient4.setUnit("bunch");
        ingredients.add(ingredient4);

        return ingredients;
    }

    private static List<Instruction> createPizzaInstructions() {
        List<Instruction> instructions = new ArrayList<>();
        Instruction instruction1 = new Instruction();
        instruction1.setInstruction("Preheat oven to 475°F (245°C).");
        instruction1.setStepNumber(1);
        instructions.add(instruction1);

        Instruction instruction2 = new Instruction();
        instruction2.setInstruction("Roll out pizza dough and spread tomato sauce on top.");
        instruction2.setStepNumber(2);
        instructions.add(instruction2);

        Instruction instruction3 = new Instruction();
        instruction3.setInstruction("Top with mozzarella cheese and fresh basil.");
        instruction3.setStepNumber(3);
        instructions.add(instruction3);

        Instruction instruction4 = new Instruction();
        instruction4.setInstruction("Bake for 10-12 minutes, or until crust is golden and cheese is bubbly.");
        instruction4.setStepNumber(4);
        instructions.add(instruction4);

        return instructions;
    }
}
