// Trong thư mục Domain/UseCase/Sync/ hoặc tương tự
package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;

public class SyncRecipesUseCase {
    private final RecipeRepository repository;

    public SyncRecipesUseCase(RecipeRepository repository) {
        this.repository = repository;
    }

    // Định nghĩa callback để ViewModel biết khi nào xong
    public interface SyncCallback {
        void onSyncSuccess();
        void onSyncError(String error);
    }

    public void execute(SyncCallback callback) {
        repository.syncRecipesFromCloud(new RecipeRepository.SyncCallback() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onSyncSuccess();
                }
            }

            @Override
            public void onError(String message) {
                if (callback != null) {
                    callback.onSyncError(message);
                }
            }
        });
    }
}
