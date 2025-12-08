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
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SyncSampleRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;

public class RecipeViewModel extends ViewModel {
    // Dependencies
    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final SyncSampleRecipesUseCase syncSampleRecipesUseCase;

    private final ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Pair<String, SearchRecipesUseCase.FilterOptions>> searchInput = new MutableLiveData<>();
    private final LiveData<List<Recipe>> searchResults;
    private SearchRecipesUseCase.FilterOptions currentFilterOptions = new SearchRecipesUseCase.FilterOptions();
    private int currentUserId = -1;

    public RecipeViewModel(ManageRecipeUseCase manageRecipeUseCase,
                           SearchRecipesUseCase searchRecipesUseCase,
                           ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase,
                           SyncSampleRecipesUseCase syncSampleRecipesUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.toggleFavoriteRecipeUseCase = toggleFavoriteRecipeUseCase;
        this.syncSampleRecipesUseCase = syncSampleRecipesUseCase;

        searchInput.setValue(new Pair<>("", currentFilterOptions));

        searchResults = Transformations.switchMap(searchInput, input ->
                searchRecipesUseCase.execute(input.first, currentUserId, input.second)
        );
    }
    public void syncSampleRecipes() {
        syncSampleRecipesUseCase.execute();
    }
    public LiveData<Boolean> getOperationSuccess() { return operationSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<List<Recipe>> getSearchResults() { return searchResults; }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return manageRecipeUseCase.getRecipeById(recipeId);
    }

    public LiveData<List<Instruction>> getInstructions(int recipeId) {
        return manageRecipeUseCase.getInstructions(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients(int recipeId) {
        return manageRecipeUseCase.getIngredients(recipeId);
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        refreshSearch();
    }

    public void search(String query) {
        // Reuse the existing filter object
        searchInput.setValue(new Pair<>(query, currentFilterOptions));
    }

    public void setFilters(List<String> categories, List<String> dietModes,
                           Integer minCalories, Integer maxCalories,
                           Integer minCarbs, Integer maxCarbs,
                           Integer minProtein, Integer maxProtein,
                           Integer minFat, Integer maxFat) {

        currentFilterOptions.categories = categories;
        currentFilterOptions.dietModes = dietModes;
        currentFilterOptions.minCalories = minCalories;
        currentFilterOptions.maxCalories = maxCalories;
        currentFilterOptions.minCarbs = minCarbs;
        currentFilterOptions.maxCarbs = maxCarbs;
        currentFilterOptions.minProtein = minProtein;
        currentFilterOptions.maxProtein = maxProtein;
        currentFilterOptions.minFat = minFat;
        currentFilterOptions.maxFat = maxFat;

        refreshSearch();
    }

    private void refreshSearch() {
        String currentQuery = searchInput.getValue() != null ? searchInput.getValue().first : "";
        searchInput.setValue(new Pair<>(currentQuery, currentFilterOptions));
    }

    public List<String> getCurrentCategories() { return currentFilterOptions.categories; }
    public List<String> getCurrentDietModes() { return currentFilterOptions.dietModes; }
    public Integer getMinCalories() { return currentFilterOptions.minCalories; }
    public Integer getMaxCalories() { return currentFilterOptions.maxCalories; }
    public Integer getMinCarbs() { return currentFilterOptions.minCarbs; }
    public Integer getMaxCarbs() { return currentFilterOptions.maxCarbs; }
    public Integer getMinProtein() { return currentFilterOptions.minProtein; }
    public Integer getMaxProtein() { return currentFilterOptions.maxProtein; }
    public Integer getMinFat() { return currentFilterOptions.minFat; }
    public Integer getMaxFat() { return currentFilterOptions.maxFat; }

    public void createRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        if (currentUserId != -1) recipe.setUserId(currentUserId);
        executor.execute(() -> manageRecipeUseCase.createRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { operationSuccess.postValue(true); }
            @Override public void onError() { errorMessage.postValue("Failed to create recipe"); }
        }));
    }

    public void updateRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        executor.execute(() -> manageRecipeUseCase.updateRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { operationSuccess.postValue(true); }
            @Override public void onError() { errorMessage.postValue("Failed to update recipe"); }
        }));
    }

    public void deleteRecipe(Recipe recipe) {
        executor.execute(() -> manageRecipeUseCase.deleteRecipe(recipe, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { operationSuccess.postValue(true); }
            @Override public void onError() { errorMessage.postValue("Failed to delete recipe"); }
        }));
    }

    public void toggleFavorite(int recipeId) {
        executor.execute(() -> toggleFavoriteRecipeUseCase.execute(recipeId, new ToggleFavoriteRecipeUseCase.Callback() {
            @Override public void onSuccess() { operationSuccess.postValue(true); }
            @Override public void onError() { errorMessage.postValue("Failed to toggle favorite status"); }
        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}