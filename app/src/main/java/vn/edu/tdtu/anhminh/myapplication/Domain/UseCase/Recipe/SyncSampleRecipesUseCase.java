package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import vn.edu.tdtu.anhminh.myapplication.Services.Sync.RecipeSyncService;

public class SyncSampleRecipesUseCase {

    private final RecipeSyncService recipeSyncService;

    public SyncSampleRecipesUseCase(RecipeSyncService recipeSyncService) {
        this.recipeSyncService = recipeSyncService;
    }

    /**
     * Gọi sync sample recipes từ remote -> lưu xuống Room.
     * Volley + AsyncTask đã chạy nền sẵn trong RecipeSyncService.
     */
    public void execute() {
        recipeSyncService.syncSampleRecipes();
    }

    // Nếu sau này muốn callback:
    // public void execute(Callback cb) { ... }
}