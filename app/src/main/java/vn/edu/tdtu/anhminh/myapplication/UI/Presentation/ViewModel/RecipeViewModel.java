package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;

public class RecipeViewModel extends ViewModel {
    // Dependencies
    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;

    // Executor for background threads (CUD operations)
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // CUD Status LiveData
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // --- SEARCH LOGIC VARIABLES ---
    // This holds the current Query String and Filter Options
    private final MutableLiveData<Pair<String, SearchRecipesUseCase.FilterOptions>> searchInput = new MutableLiveData<>();

    // This is the Output: It automatically updates when searchInput changes
    private final LiveData<List<Recipe>> searchResults;

    private int currentUserId = -1;

    public RecipeViewModel(ManageRecipeUseCase manageRecipeUseCase,
                           SearchRecipesUseCase searchRecipesUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;

        searchInput.setValue(new Pair<>("", null));

        // 2. UPDATE SwitchMap: Pass 'currentUserId' to the use case
        searchResults = Transformations.switchMap(searchInput, input ->
                searchRecipesUseCase.execute(input.first, currentUserId, input.second)
        );
    }

    // --- GETTERS FOR UI ---
    public LiveData<Boolean> getOperationSuccess() { return operationSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Recipe>> getSearchResults() { return searchResults; }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return manageRecipeUseCase.getRecipeById(recipeId);
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        // Trigger a refresh if needed
        search(searchInput.getValue() != null ? searchInput.getValue().first : "");
    }

    // --- SEARCH ACTIONS ---

    public void search(String query) {
        SearchRecipesUseCase.FilterOptions currentFilter =
                searchInput.getValue() != null ? searchInput.getValue().second : null;
        searchInput.setValue(new Pair<>(query, currentFilter));
    }

    public void applyFilter(SearchRecipesUseCase.FilterOptions filter) {
        String currentQuery =
                searchInput.getValue() != null ? searchInput.getValue().first : "";
        searchInput.setValue(new Pair<>(currentQuery, filter));
    }

    // --- CUD OPERATIONS (Create, Update, Delete) ---

    public void createRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        if (currentUserId != -1) {
            recipe.setUserId(currentUserId);
        }
        executor.execute(() -> {
            manageRecipeUseCase.createRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
                @Override
                public void onSuccess() { operationSuccess.postValue(true); }
                @Override
                public void onError() { errorMessage.postValue("Failed to create recipe"); }
            });
        });
    }

    public void updateRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        executor.execute(() -> {
            manageRecipeUseCase.updateRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
                @Override
                public void onSuccess() { operationSuccess.postValue(true); }
                @Override
                public void onError() { errorMessage.postValue("Failed to update recipe"); }
            });
        });
    }

    public void deleteRecipe(Recipe recipe) {
        executor.execute(() -> {
            manageRecipeUseCase.deleteRecipe(recipe, new ManageRecipeUseCase.Callback() {
                @Override
                public void onSuccess() { operationSuccess.postValue(true); }
                @Override
                public void onError() { errorMessage.postValue("Failed to delete recipe"); }
            });
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}