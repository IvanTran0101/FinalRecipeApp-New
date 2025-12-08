package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.UpdateAccountUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SyncSampleRecipesUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase;
    private final SyncSampleRecipesUseCase syncSampleRecipesUseCase;


    public ViewModelFactory(ManageRecipeUseCase manageRecipeUseCase,
                            AuthenticateUserUseCase authenticateUserUseCase,
                            UpdateAccountUseCase updateAccountUseCase,
                            SearchRecipesUseCase searchRecipesUseCase,
                            ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase,
                            SyncSampleRecipesUseCase syncSampleRecipesUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.toggleFavoriteRecipeUseCase = toggleFavoriteRecipeUseCase;
        this.syncSampleRecipesUseCase = syncSampleRecipesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(manageRecipeUseCase,
                                          searchRecipesUseCase,
                                          toggleFavoriteRecipeUseCase,
                                          syncSampleRecipesUseCase);
        }
        else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(authenticateUserUseCase, updateAccountUseCase);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}