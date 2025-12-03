package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ManageRecipeUseCase manageRecipeUseCase;
    private final SearchRecipesUseCase searchRecipesUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public ViewModelFactory(ManageRecipeUseCase manageRecipeUseCase,
                            AuthenticateUserUseCase authenticateUserUseCase,
                            SearchRecipesUseCase searchRecipesUseCase) {
        this.manageRecipeUseCase = manageRecipeUseCase;
        this.searchRecipesUseCase = searchRecipesUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // 3. Logic for RecipeViewModel
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(manageRecipeUseCase, searchRecipesUseCase);
        }
        // 4. Logic for LoginViewModel (New!)
        else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(authenticateUserUseCase);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}