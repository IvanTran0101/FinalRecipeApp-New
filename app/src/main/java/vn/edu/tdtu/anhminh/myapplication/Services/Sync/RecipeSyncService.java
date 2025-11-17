package vn.edu.tdtu.anhminh.myapplication.Services.Sync;

import android.content.Context;
import android.util.Log;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api.RecipeApiService;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;

public class RecipeSyncService {
    private final RecipeRepository recipeRepository;
    private final RecipeApiService api;

    public RecipeSyncService(Context context){
        Context appContext = context.getApplicationContext();
        this.recipeRepository = new RecipeRepository(appContext);
        this.api = new RecipeApiService(appContext);
    }

    public void syncSampleRecipes() {
        api.getSampleRecipe(new RecipeApiService.ApiCallback<List<RecipeDTO>>() {
            @Override
            public void onSuccess(List<RecipeDTO> dtos) {
                recipeRepository.importRecipesFromDtoList(dtos);
            }

            @Override
            public void onError(String error) {
                Log.e("RecipeSyncService", "Sync failed:" + error);
            }
        });
    }


}
