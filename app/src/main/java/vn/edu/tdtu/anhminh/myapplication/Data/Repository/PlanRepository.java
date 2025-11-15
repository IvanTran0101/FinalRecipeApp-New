package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.PlanDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;

public class PlanRepository {
    private final PlanDAO planDAO;

    public PlanRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.planDAO = db.planDao();
    }

    //get full week
    public LiveData<List<PlanEntity>> getPlanForWeek(int userId, int weekId) {
        return planDAO.getPlanForWeek(userId,weekId);
    }

    //get all recipes in give day
    public List<PlanEntity> getPlanSlotsForDay(int userId, int weekId, int weekDay) {
        return planDAO.getPlanSlotsForDay(userId, weekId, weekDay);
    }

    public void addPlanSlot(int userId, int weekId, int weekNumber, int weekDay, int recipeId){
        PlanEntity plan = new PlanEntity(
                null,
                weekId,
                recipeId,
                userId,
                weekNumber,
                weekDay
        );
        planDAO.insert(plan);
    }

    public void deleteSlot(PlanEntity slot) {
        planDAO.delete(slot);
    }

    public void clearWeek(int userId, int weekId) {
        planDAO.deletePlansForWeek(userId,weekId);
    }

    public List<PlanEntity> getPlansUsingRecipe(int recipeId) {
        return planDAO.getPlanEntriesByRecipe(recipeId);
    }
}
