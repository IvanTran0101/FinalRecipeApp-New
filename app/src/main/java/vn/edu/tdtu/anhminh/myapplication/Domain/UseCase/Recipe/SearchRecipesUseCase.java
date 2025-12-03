package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class SearchRecipesUseCase {

    private final RecipeRepository recipeRepository;

    public SearchRecipesUseCase(RecipeRepository recipeRepository){
        this.recipeRepository = recipeRepository;
    }

    public static class FilterOptions {
        public String category;
        public String dietMode;
        public Boolean pinned;
    }

    public LiveData<List<Recipe>> execute(String query, int userId, FilterOptions filter) {

        // We use MediatorLiveData to listen to the Repository's LiveData
        MediatorLiveData<List<Recipe>> result = new MediatorLiveData<>();

        LiveData<List<Recipe>> source = recipeRepository.searchRecipes(query, userId);

        result.addSource(source, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes == null) {
                    result.setValue(null);
                    return;
                }

                // 1. Copy list to avoid modifying the original LiveData source
                List<Recipe> filtered = new ArrayList<>(recipes);

                // 2. Apply Memory Filters (Category, Diet, etc.)
                if (filter != null) {
                    if (filter.category != null && !filter.category.isEmpty()) {
                        filtered.removeIf(r -> !filter.category.equalsIgnoreCase(r.getCategory()));
                    }
                    if (filter.dietMode != null && !filter.dietMode.isEmpty()) {
                        filtered.removeIf(r -> !filter.dietMode.equalsIgnoreCase(r.getDietMode()));
                    }
                    if (filter.pinned != null) {
                        filtered.removeIf(r -> r.getPinned() != filter.pinned);
                    }
                }

                // 3. Post final result
                result.setValue(filtered);
            }
        });

        return result;
    }
}