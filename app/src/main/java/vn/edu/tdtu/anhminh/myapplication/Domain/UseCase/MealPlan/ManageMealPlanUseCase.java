package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.MealPlan;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.PlanRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;

public class ManageMealPlanUseCase {
    private final PlanRepository planRepository;

    public ManageMealPlanUseCase(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public LiveData<List<MealPlanItem>> getWeeklyPlan(int userId, int weekId) {
        return planRepository.getPlanForWeek(userId, weekId);
    }

    public void addRecipeToPlan(Plan newSlot) {
        if (newSlot == null || newSlot.getUserId() <= 0 || newSlot.getRecipeId() <= 0) {
            return;
        }

        planRepository.addPlanSlot(newSlot);
    }

    public void addRecipeToPlan(int userId, int weekId, int weekNumber, int weekDay, int recipeId) {
        if (userId <= 0 || weekId <= 0 || weekDay < 1 || weekDay > 7 || recipeId <= 0) {
            return;
        }

        planRepository.addPlanSlot(userId, weekId, weekNumber, weekDay, recipeId);
    }

    public void removeRecipeFromPlan(Plan slot) {
        if (slot == null || slot.getPlanId() == null) {
            return;
        }

        planRepository.deleteSlot(slot);
    }

    public void clearEntireWeek(int userId, int weekId) {
        if (userId <= 0 || weekId <= 0) {
            return;
        }

        planRepository.clearWeek(userId, weekId);
    }
}
