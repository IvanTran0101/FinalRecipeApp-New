package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.PlanRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.ShoppingListItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan.GenerateShoppingListUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan.ManageMealPlanUseCase;

public class MealPlanViewModel extends AndroidViewModel {

    private final ManageMealPlanUseCase manageMealPlanUseCase;
    private final GenerateShoppingListUseCase generateShoppingListUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<ShoppingListItem>> shoppingList = new MutableLiveData<>();

    public MealPlanViewModel(@NonNull Application application) {
        super(application);
        PlanRepository planRepo = new PlanRepository(application);
        IngredientRepository ingredientRepository = new IngredientRepository(application);

        this.manageMealPlanUseCase = new ManageMealPlanUseCase(planRepo);
        this.generateShoppingListUseCase = new GenerateShoppingListUseCase(planRepo, ingredientRepository);
    }

    public LiveData<List<MealPlanItem>> getWeeklyPlan(int userId, int weekId) {
        return manageMealPlanUseCase.getWeeklyPlan(userId, weekId);
    }

    public void addRecipeToPlan(int userId, int weekId, int weekNumber, int weekDay, int recipeId) {
        executor.execute(() -> {
            manageMealPlanUseCase.addRecipeToPlan(userId, weekId, weekNumber, weekDay, recipeId);
        });
    }

    public void removeRecipeFromPlan(Plan slot) {
        executor.execute(() -> {
            manageMealPlanUseCase.removeRecipeFromPlan(slot);
        });
    }

    public LiveData<List<ShoppingListItem>> getShoppingList() {
        return shoppingList;
    }

    public void generateShoppingList(int userId, int weekId) {
        executor.execute(() -> {
            List<ShoppingListItem> items = generateShoppingListUseCase.executeGeneration(userId, weekId);
            shoppingList.postValue(items);
        });
    }

    public void clearEntireWeek(int userId, int weekId) {
        executor.execute(() -> {
            manageMealPlanUseCase.clearEntireWeek(userId, weekId);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}