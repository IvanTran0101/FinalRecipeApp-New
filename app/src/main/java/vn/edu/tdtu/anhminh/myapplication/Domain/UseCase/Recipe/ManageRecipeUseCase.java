package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

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
            recipeRepository.addRecipe(recipe);
            if (ingredients != null && !ingredients.isEmpty()) {
                ingredientRepository.addIngredients(ingredients);
            }
            if (instructions != null && !instructions.isEmpty()){
                instructionRepository.addInstructions(instructions);
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
        try{
            recipeRepository.updateRecipe(recipe);
            if (ingredients != null && !ingredients.isEmpty()) {
                ingredientRepository.replaceIngredientsForRecipe(recipe.getRecipeId(), ingredients);
            }
            if (instructions != null && !instructions.isEmpty()){
                instructionRepository.replaceInstructionsForRecipe(recipe.getRecipeId(), instructions);
            }
            if (callback != null ) callback.onSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onError();

        }
    }

    public void deleteRecipe(Recipe recipe,
                             Callback callback){
        try{
                if (recipe != null){
                    int recipeId = recipe.getRecipeId();

                    ingredientRepository.deleteIngredientsForRecipe(recipeId);;
                    instructionRepository.deleteInstructionsForRecipe(recipeId);
                    recipeRepository.deleteRecipe(recipe);
            }
                if (callback != null) callback.onSuccess();
        }catch (Exception e){
            if (callback != null) callback.onError();
        }
    }

}
