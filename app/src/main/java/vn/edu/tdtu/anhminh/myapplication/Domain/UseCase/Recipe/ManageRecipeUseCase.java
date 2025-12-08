package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.InstructionRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class ManageRecipeUseCase {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final InstructionRepository instructionRepository;

    public interface Callback {
        void onSuccess();
        void onError();
    }
    public ManageRecipeUseCase(RecipeRepository recipeRepository,
                               IngredientRepository ingredientRepository,
                               InstructionRepository instructionRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.instructionRepository = instructionRepository;

    }

    public void createRecipe(Recipe recipe,
                             List<Ingredient> ingredients,
                             List<Instruction> instructions,
                             Callback callback) {
        try{
            long newRecipeId = recipeRepository.addRecipe(recipe);

            // 2. Assign this ID to Ingredients
            if (ingredients != null && !ingredients.isEmpty()) {
                for (Ingredient item : ingredients) {
                    item.setRecipeId((int) newRecipeId); // <--- LINKING HAPPENS HERE
                }
                ingredientRepository.addMultipleIngredients(ingredients);
            }

            // 3. Assign this ID to Instructions
            if (instructions != null && !instructions.isEmpty()) {
                for (Instruction item : instructions) {
                    item.setRecipeId((int) newRecipeId); // <--- LINKING HAPPENS HERE
                }
                instructionRepository.addMultipleInstructions(instructions);
            }
            if (callback != null ) callback.onSuccess();
        }catch (Exception e){
            if (callback != null ) callback.onError();

        }
    }

    public void updateRecipe(Recipe recipe,
                             List<Ingredient> ingredients,
                             List<Instruction> instructions,
                             Callback callback) {
        try {
            if (recipe == null) {
                if (callback != null) callback.onError();
                return;
            }

            int recipeId = recipe.getRecipeId();

            // 1. Update phần header Recipe
            recipeRepository.updateRecipe(recipe);

            // 2. Chuẩn hoá list: nếu null coi như list rỗng (clear hết)
            if (ingredients == null) {
                ingredients = java.util.Collections.emptyList();
            }
            if (instructions == null) {
                instructions = java.util.Collections.emptyList();
            }

            // 3. Replace toàn bộ Ingredients / Instructions cho recipe này
            ingredientRepository.replaceIngredientsForRecipe(recipeId, ingredients);
            instructionRepository.replaceInstructionsForRecipe(recipeId, instructions);

            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onError();
        }
    }

    public void deleteRecipe(Recipe recipe,
                             Callback callback){
        try{
                if (recipe != null){
                    int recipeId = recipe.getRecipeId();

                    ingredientRepository.deleteIngredientsForRecipe(recipeId);
                    instructionRepository.deleteInstructionsForRecipe(recipeId);
                    recipeRepository.deleteRecipe(recipe);
            }
                if (callback != null) callback.onSuccess();
        }catch (Exception e){
            if (callback != null) callback.onError();
        }
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return recipeRepository.getRecipeByIdLiveData(recipeId);
    }

    public LiveData<List<Instruction>> getInstructions(int recipeId) {
        return instructionRepository.getInstructionsByRecipeId(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients(int recipeId) {
        return ingredientRepository.getQuantityAndUnitForRecipe(recipeId);
    }
}
