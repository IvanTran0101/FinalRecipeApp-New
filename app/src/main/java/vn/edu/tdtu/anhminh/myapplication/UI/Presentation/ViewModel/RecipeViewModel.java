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
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;

public class RecipeViewModel extends ViewModel {
    // Dependencies
    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase;

    // Executor for background threads
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // --- STATUS LIVEDATA ---
    // Dùng để thông báo cho View (Activity/Fragment) về kết quả thao tác
    private final MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // --- SEARCH & FILTER DATA ---
    // Input: Pair<Query String, Filter Options>
    private final MutableLiveData<Pair<String, SearchRecipesUseCase.FilterOptions>> searchInput = new MutableLiveData<>();
    // Output: List of Recipes (Tự động cập nhật khi searchInput thay đổi)
    private final LiveData<List<Recipe>> searchResults;

    // Local State: Single source of truth for filters
    private SearchRecipesUseCase.FilterOptions currentFilterOptions = new SearchRecipesUseCase.FilterOptions();
    private int currentUserId = -1;

    // --- CONSTRUCTOR ---
    public RecipeViewModel(ManageRecipeUseCase manageRecipeUseCase,
                           SearchRecipesUseCase searchRecipesUseCase,
                           ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.toggleFavoriteRecipeUseCase = toggleFavoriteRecipeUseCase;

        // Khởi tạo trạng thái ban đầu: query rỗng, filter mặc định
        searchInput.setValue(new Pair<>("", currentFilterOptions));

        // Reactive: Khi searchInput thay đổi -> gọi UseCase -> cập nhật searchResults
        searchResults = Transformations.switchMap(searchInput, input ->
                searchRecipesUseCase.execute(input.first, currentUserId, input.second)
        );
    }

    // --- GETTERS (OBSERVABLES) ---
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

    // --- USER CONTEXT ---
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        // Reload lại kết quả tìm kiếm nếu User ID thay đổi (ví dụ để update trạng thái Favorite)
        refreshSearch();
    }

    // --- SEARCH & FILTER ACTIONS ---

    // 1. Chỉ update từ khóa tìm kiếm
    public void search(String query) {
        searchInput.setValue(new Pair<>(query, currentFilterOptions));
    }

    // 2. Update bộ lọc đầy đủ
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

    // 3. Xóa bộ lọc (Reset về mặc định)
    public void clearFilters() {
        this.currentFilterOptions = new SearchRecipesUseCase.FilterOptions(); // Reset object
        refreshSearch();
    }

    // Helper: Trigger lại LiveData với giá trị hiện tại
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
        // Lưu ý: Nếu UseCase cần UserId, hãy đảm bảo UseCase tự lấy hoặc truyền thêm vào đây
        executor.execute(() -> toggleFavoriteRecipeUseCase.execute(recipeId, new ToggleFavoriteRecipeUseCase.Callback() {
            @Override public void onSuccess() {
                // Không cần postSuccess(true) ở đây nếu chỉ là like/unlike để tránh reload màn hình không cần thiết
                // Nhưng nếu UI cần biết để hiển thị Toast, hãy giữ nguyên.
                handleSuccess();
                // Opsional: Refresh lại list để cập nhật icon trái tim
                // refreshSearch();
            }
            @Override public void onError() { handleError("Failed to toggle favorite status"); }
        }));
    }

    // --- HELPER METHODS FOR STATUS ---
    // Gom nhóm xử lý kết quả để code gọn hơn
    private void handleSuccess() {
        operationSuccess.postValue(true);
    }

    private void handleError(String message) {
        errorMessage.postValue(message);
    }

    // Reset status sau khi UI đã xử lý xong (tránh hiển thị lại thông báo khi xoay màn hình)
    public void resetStatus() {
        operationSuccess.setValue(false);
        errorMessage.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown(); // Dọn dẹp thread khi ViewModel bị hủy
    }
}