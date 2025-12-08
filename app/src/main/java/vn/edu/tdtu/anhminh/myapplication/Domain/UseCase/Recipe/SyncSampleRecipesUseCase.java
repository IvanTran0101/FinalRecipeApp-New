package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import vn.edu.tdtu.anhminh.myapplication.Services.Sync.RecipeSyncService;

public class SyncSampleRecipesUseCase {

    private final RecipeSyncService recipeSyncService;

    public SyncSampleRecipesUseCase(RecipeSyncService recipeSyncService) {
        this.recipeSyncService = recipeSyncService;
    }

    public void execute() {
        recipeSyncService.syncSampleRecipes();
    }
}