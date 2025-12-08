package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
        public List<String> categories;
        public List<String> dietModes;
        public Integer minCalories;
        public Integer maxCalories;
        public Integer minCarbs;
        public Integer maxCarbs;
        public Integer minProtein;
        public Integer maxProtein;
        public Integer minFat;
        public Integer maxFat;
        public Boolean pinned;
    }

    public LiveData<List<Recipe>> execute(String query, int userId, FilterOptions filter) {

        MediatorLiveData<List<Recipe>> result = new MediatorLiveData<>();
        LiveData<List<Recipe>> source = recipeRepository.searchRecipes(query, userId);

        result.addSource(source, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes == null) {
                    result.setValue(null);
                    return;
                }

                List<Recipe> filtered = new ArrayList<>(recipes);

                if (filter != null) {
                    if (filter.categories != null && !filter.categories.isEmpty()) {
                        filtered.removeIf(r -> !filter.categories.contains(r.getCategory()));
                    }
                    if (filter.dietModes != null && !filter.dietModes.isEmpty()) {
                        filtered.removeIf(r -> !filter.dietModes.contains(r.getDietMode()));
                    }

                    if (filter.minCalories != null) {
                        filtered.removeIf(r -> r.getCalories() == null || r.getCalories() < filter.minCalories);
                    }
                    if (filter.maxCalories != null) {
                        filtered.removeIf(r -> r.getCalories() != null && r.getCalories() > filter.maxCalories);
                    }

                    if (filter.minCarbs != null) {
                        filtered.removeIf(r -> r.getCarb() == null || r.getCarb() < filter.minCarbs);
                    }
                    if (filter.maxCarbs != null) {
                        filtered.removeIf(r -> r.getCarb() != null && r.getCarb() > filter.maxCarbs);
                    }

                    if (filter.minProtein != null) {
                        filtered.removeIf(r -> r.getProtein() == null || r.getProtein() < filter.minProtein);
                    }
                    if (filter.maxProtein != null) {
                        filtered.removeIf(r -> r.getProtein() != null && r.getProtein() > filter.maxProtein);
                    }

                    if (filter.minFat != null) {
                        filtered.removeIf(r -> r.getFat() == null || r.getFat() < filter.minFat);
                    }
                    if (filter.maxFat != null) {
                        filtered.removeIf(r -> r.getFat() != null && r.getFat() > filter.maxFat);
                    }

                    if (filter.pinned != null) {
                        filtered.removeIf(r -> {
                            boolean isPinned = r.getPinned() != null && r.getPinned();
                            return isPinned != filter.pinned;
                        });
                    }
                }

                result.setValue(filtered);
            }
        });

        return result;
    }
}