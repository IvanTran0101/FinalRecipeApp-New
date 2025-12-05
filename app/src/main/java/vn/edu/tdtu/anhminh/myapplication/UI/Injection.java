package vn.edu.tdtu.anhminh.myapplication.UI;

import android.content.Context;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.*;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan.GenerateMealFrequencyReportUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan.GenerateShoppingListUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan.ManageMealPlanUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class Injection {
    private static RecipeRepository recipeRepo;
    private static IngredientRepository ingredientRepo;
    private static InstructionRepository instructionRepo;
    private static UserRepository userRepo;
    private static PlanRepository planRepository;

    public static void init(Context context) {
        recipeRepo = new RecipeRepository(context);
        ingredientRepo = new IngredientRepository(context);
        instructionRepo = new InstructionRepository(context);
        userRepo = new UserRepository(context);
        planRepository = new PlanRepository(context);
    }

    public static ManageRecipeUseCase provideManageRecipeUseCase() {
        return new ManageRecipeUseCase(recipeRepo, ingredientRepo, instructionRepo);
    }

    public static SearchRecipesUseCase provideSearchRecipesUseCase() {
        return new SearchRecipesUseCase(recipeRepo);
    }

    public static ToggleFavoriteRecipeUseCase provideToggleFavoriteRecipeUseCase() {
        return new ToggleFavoriteRecipeUseCase(recipeRepo);
    }

    public static ManageMealPlanUseCase provideManageMealPlanUseCase() {
        return new ManageMealPlanUseCase(planRepository);
    }

    public static GenerateShoppingListUseCase provideGenerateShoppingListUseCase() {
        return new GenerateShoppingListUseCase(planRepository, ingredientRepo);
    }

    public static GenerateMealFrequencyReportUseCase provideGenerateMealFrequencyReportUseCase() {
        return new GenerateMealFrequencyReportUseCase();
    }

    public static AuthenticateUserUseCase provideAuthenticateUserUseCase() {
        return new AuthenticateUserUseCase(userRepo);
    }

    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(
                provideManageRecipeUseCase(),
                provideAuthenticateUserUseCase(),
                provideSearchRecipesUseCase(),
                provideToggleFavoriteRecipeUseCase()
        );
    }
}