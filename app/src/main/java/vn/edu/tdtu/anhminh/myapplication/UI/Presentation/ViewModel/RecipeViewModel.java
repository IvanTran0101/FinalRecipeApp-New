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
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SyncRecipesUseCase; // <-- THÊM IMPORT
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;

public class RecipeViewModel extends ViewModel {
    // Dependencies
    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase;
    private final SyncRecipesUseCase syncRecipesUseCase; // <-- THÊM DEPENDENCY

    // Executor for background threads
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // --- STATUS LIVEDATA ---
    private final MutableLiveData<Boolean> isSyncing = new MutableLiveData<>(false); // <-- THÊM LIVEDATA CHO TRẠNG THÁI SYNC
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // --- SEARCH & FILTER DATA ---
    private final MutableLiveData<Pair<String, SearchRecipesUseCase.FilterOptions>> searchInput = new MutableLiveData<>();
    private final LiveData<List<Recipe>> searchResults;

    // Local State
    private SearchRecipesUseCase.FilterOptions currentFilterOptions = new SearchRecipesUseCase.FilterOptions();
    private int currentUserId = -1;

    // --- CONSTRUCTOR ---
    public RecipeViewModel(ManageRecipeUseCase manageRecipeUseCase,
                           SearchRecipesUseCase searchRecipesUseCase,
                           ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase,
                           SyncRecipesUseCase syncRecipesUseCase) { // <-- THÊM THAM SỐ
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.toggleFavoriteRecipeUseCase = toggleFavoriteRecipeUseCase;
        this.syncRecipesUseCase = syncRecipesUseCase; // <-- GÁN GIÁ TRỊ

        // Khởi tạo trạng thái ban đầu: query rỗng, filter mặc định
        searchInput.setValue(new Pair<>("", currentFilterOptions));

        // Reactive: Khi searchInput thay đổi -> gọi UseCase -> cập nhật searchResults
        searchResults = Transformations.switchMap(searchInput, input ->
                searchRecipesUseCase.execute(input.first, currentUserId, input.second)
        );

        // Tự động đồng bộ dữ liệu từ cloud khi ViewModel được tạo lần đầu
        syncData(); // <-- GỌI HÀM ĐỒNG BỘ
    }

    // --- GETTERS (OBSERVABLES) ---
    public LiveData<Boolean> isSyncing() { return isSyncing; } // <-- THÊM GETTER
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

    // --- DATA SYNC ACTION ---  // <-- THÊM PHƯƠNG THỨC MỚI
    /**
     * Kích hoạt quá trình đồng bộ dữ liệu từ cloud.
     * Báo cho UI biết khi nào bắt đầu và kết thúc.
     */
    public void syncData() {
        isSyncing.setValue(true); // Báo cho UI: Bắt đầu đồng bộ (hiện loading...)

        syncRecipesUseCase.execute(new SyncRecipesUseCase.SyncCallback() {
            @Override
            public void onSyncSuccess() {
                // Khi đồng bộ thành công, LiveData `searchResults` sẽ tự động cập nhật
                // do dữ liệu trong DB đã thay đổi.
                isSyncing.postValue(false); // Báo cho UI: Đồng bộ đã xong
            }

            @Override
            public void onSyncError(String error) {
                // Nếu có lỗi, thông báo cho UI và ghi log
                handleError("Sync failed: " + error);
                isSyncing.postValue(false); // Báo cho UI: Đồng bộ đã xong (dù thất bại)
            }
        });
    }

    // --- USER CONTEXT ---
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        refreshSearch();
    }

    // --- SEARCH & FILTER ACTIONS ---
    public void search(String query) {
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

    public void clearFilters() {
        this.currentFilterOptions = new SearchRecipesUseCase.FilterOptions();
        refreshSearch();
    }

    private void refreshSearch() {
        String currentQuery = searchInput.getValue() != null ? searchInput.getValue().first : "";
        searchInput.setValue(new Pair<>(currentQuery, currentFilterOptions));
    }

    // --- DATA RESTORATION (GETTERS FOR UI) ---
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

    // --- CUD OPERATIONS (CREATE / UPDATE / DELETE) ---
    public void createRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        if (currentUserId == -1) {
            handleError("User not logged in");
            return;
        }
        recipe.setUserId(currentUserId);
        executor.execute(() -> manageRecipeUseCase.createRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { handleSuccess(); }
            @Override public void onError() { handleError("Failed to create recipe"); }
        }));
    }

    public void updateRecipe(Recipe recipe, List<Ingredient> ingredients, List<Instruction> instructions) {
        executor.execute(() -> manageRecipeUseCase.updateRecipe(recipe, ingredients, instructions, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { handleSuccess(); }
            @Override public void onError() { handleError("Failed to update recipe"); }
        }));
    }

    public void deleteRecipe(Recipe recipe) {
        executor.execute(() -> manageRecipeUseCase.deleteRecipe(recipe, new ManageRecipeUseCase.Callback() {
            @Override public void onSuccess() { handleSuccess(); }
            @Override public void onError() { handleError("Failed to delete recipe"); }
        }));
    }

    public void toggleFavorite(int recipeId) {
        executor.execute(() -> toggleFavoriteRecipeUseCase.execute(recipeId, new ToggleFavoriteRecipeUseCase.Callback() {
            @Override public void onSuccess() { handleSuccess(); }
            @Override public void onError() { handleError("Failed to toggle favorite status"); }
        }));
    }

    // --- HELPER METHODS FOR STATUS ---
    private void handleSuccess() {
        operationSuccess.postValue(true);
    }

    private void handleError(String message) {
        errorMessage.postValue(message);
    }

    public void resetStatus() {
        operationSuccess.setValue(false);
        errorMessage.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
