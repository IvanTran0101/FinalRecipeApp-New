package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;

public class RecipeViewModel extends ViewModel {
    private final ManageRecipeUseCase manageRecipeUseCase;

    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public RecipeViewModel(ManageRecipeUseCase manageRecipeUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
    }

    // Getters for UI to observe
    public LiveData<Boolean> getOperationSuccess() { return operationSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // Logic: Create Recipe
    public void createRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        manageRecipeUseCase.createRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override
            public void onSuccess() {
                operationSuccess.postValue(true);
            }

            @Override
            public void onError() {
                errorMessage.postValue("Failed to create recipe");
            }
        });
    }

    // Logic: Update Recipe
    public void updateRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        manageRecipeUseCase.updateRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override
            public void onSuccess() {
                operationSuccess.postValue(true);
            }

            @Override
            public void onError() {
                errorMessage.postValue("Failed to update recipe");
            }
        });
    }

    // Logic: Delete Recipe
    public void deleteRecipe(Recipe recipe) {
        manageRecipeUseCase.deleteRecipe(recipe, new ManageRecipeUseCase.Callback() {
            @Override
            public void onSuccess() {
                operationSuccess.postValue(true);
            }

            @Override
            public void onError() {
                errorMessage.postValue("Failed to delete recipe");
            }
        });
    }
}
