package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public LiveData<List<Recipe>> execute(String query, FilterOptions filter){
        MutableLiveData<List<Recipe>> result = new MutableLiveData<>();

        recipeRepository.searchRecipes(query).observeForever(recipes -> {
            if (recipes == null) {
                result.setValue(null);
                return;
            }

            List<Recipe> filtered = new ArrayList<>(recipes);

            if (filter != null){
                if (filter.category != null && !filter.category.isEmpty()){
                    filtered.removeIf(r -> !filter.category.equals(r.getCategory()));
                }
                if (filter.dietMode != null && !filter.dietMode.isEmpty()){
                    filtered.removeIf(r -> !filter.dietMode.equals(r.getDietMode()));
                }
                if (filter.pinned != null){
                    filtered.removeIf(r -> r.getPinned() != filter.pinned);
                }
            }

            result.setValue(filtered);
        });

        return result;
    }
}