package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.UpdateAccountUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SyncRecipesUseCase; // <-- 1. THÊM IMPORT
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase;
    private final SyncRecipesUseCase syncRecipesUseCase; // <-- 2. THÊM THUỘC TÍNH

    // <-- 3. CẬP NHẬT CONSTRUCTOR
    public ViewModelFactory(ManageRecipeUseCase manageRecipeUseCase,
                            SearchRecipesUseCase searchRecipesUseCase,
                            ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase,
                            SyncRecipesUseCase syncRecipesUseCase,
                            AuthenticateUserUseCase authenticateUserUseCase,
                            UpdateAccountUseCase updateAccountUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.toggleFavoriteRecipeUseCase = toggleFavoriteRecipeUseCase;
        this.syncRecipesUseCase = syncRecipesUseCase; // <-- GÁN GIÁ TRỊ
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            // <-- 4. CẬP NHẬT CÁCH TẠO RECIPE VIEWMODEL
            // Đảm bảo truyền đủ 4 UseCase theo đúng thứ tự trong constructor của RecipeViewModel
            return (T) new RecipeViewModel(
                    manageRecipeUseCase,
                    searchRecipesUseCase,
                    toggleFavoriteRecipeUseCase,
                    syncRecipesUseCase);
        }
        else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            // Phần này không thay đổi
            return (T) new UserViewModel(authenticateUserUseCase, updateAccountUseCase);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
