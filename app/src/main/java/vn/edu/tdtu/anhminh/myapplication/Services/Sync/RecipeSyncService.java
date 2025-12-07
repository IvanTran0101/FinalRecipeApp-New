package vn.edu.tdtu.anhminh.myapplication.Services.Sync;

import android.content.Context;
import android.util.Log;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;

public class RecipeSyncService {
    private final RecipeRepository recipeRepository;

    public RecipeSyncService(Context context){
        Context appContext = context.getApplicationContext();
        this.recipeRepository = new RecipeRepository(appContext);
    }

    public void syncRecipesFromCloud() {
        recipeRepository.syncRecipesFromCloud(new RecipeRepository.SyncCallback() {
            @Override
            public void onSuccess() {
                Log.d("RecipeSyncService", "Sync successful!");
            }

            @Override
            public void onError(String message) {
                Log.e("RecipeSyncService", "Sync failed: " + message);
            }
        });
    }
}
