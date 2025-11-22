package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;

public class ToggleFavoriteRecipeUseCase {
    private final RecipeRepository recipeRepository;

    public ToggleFavoriteRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public interface Callback {
        void onSuccess();
        void onError();
    }

    public void execute(int recipeId, Callback callback){
        try{
            recipeRepository.togglePinned(recipeId);
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onError();
        }
    }
}
