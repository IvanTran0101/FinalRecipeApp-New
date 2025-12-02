package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ManageRecipeUseCase useCase;

    public ViewModelFactory(ManageRecipeUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}