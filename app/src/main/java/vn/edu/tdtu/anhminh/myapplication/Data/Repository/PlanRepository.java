package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.ArrayList;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.PlanDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.PlanMapper;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;

public class PlanRepository {
    private final PlanDAO planDAO;

    public PlanRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.planDAO = db.planDao();
    }

    // Get full week as domain models
    public LiveData<List<Plan>> getPlanForWeek(int userId, int weekId) {
        return Transformations.map(
                planDAO.getPlanForWeek(userId, weekId),
                entities -> PlanMapper.toModelList(entities)
        );
    }

    // Get all recipes in a given day as domain models
    public List<Plan> getPlanSlotsForDay(int userId, int weekId, int weekDay) {
        List<PlanEntity> entities = planDAO.getPlanSlotsForDay(userId, weekId, weekDay);
        return PlanMapper.toModelList(entities);
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

    // Overload: add plan slot from domain model
    public void addPlanSlot(Plan planModel) {
        if (planModel == null) return;
        PlanEntity entity = PlanMapper.toEntity(planModel);
        planDAO.insert(entity);
    }

    public void deleteSlot(Plan slot) {
        if (slot == null) return;
        PlanEntity entity = PlanMapper.toEntity(slot);
        planDAO.delete(entity);
    }

    public void clearWeek(int userId, int weekId) {
        planDAO.deletePlansForWeek(userId,weekId);
    }

    public List<Plan> getPlansUsingRecipe(int recipeId) {
        List<PlanEntity> entities = planDAO.getPlanEntriesByRecipe(recipeId);
        return PlanMapper.toModelList(entities);
    }
}
