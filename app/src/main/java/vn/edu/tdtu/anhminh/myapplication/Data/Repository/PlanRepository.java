package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.ArrayList;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.PlanDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.PlanMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.MealPlanItem;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.PlanWithRecipe;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class PlanRepository {
    private final PlanDAO planDAO;
    private final IngredientDAO ingredientDAO;

    public PlanRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.planDAO = db.planDao();
        this.ingredientDAO = db.ingredientDao();
    }

    private static class InsertPlanTask extends android.os.AsyncTask<PlanEntity, Void, Void> {
        private final PlanDAO planDAO;

        InsertPlanTask(PlanDAO planDAO) {
            this.planDAO = planDAO;
        }

        @Override
        protected Void doInBackground(PlanEntity... entities) {
            if (entities != null && entities.length > 0) {
                planDAO.insert(entities[0]);
            }
            return null;
        }
    }

    private static class DeletePlanTask extends android.os.AsyncTask<PlanEntity, Void, Void> {
        private final PlanDAO planDAO;

        DeletePlanTask(PlanDAO planDAO) {
            this.planDAO = planDAO;
        }

        @Override
        protected Void doInBackground(PlanEntity... entities) {
            if (entities != null && entities.length > 0) {
                planDAO.delete(entities[0]);
            }
            return null;
        }
    }

    private static class ClearWeekTask extends android.os.AsyncTask<Void, Void, Void> {
        private final PlanDAO planDAO;
        private final int userId;
        private final int weekId;

        ClearWeekTask(PlanDAO planDAO, int userId, int weekId) {
            this.planDAO = planDAO;
            this.userId = userId;
            this.weekId = weekId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            planDAO.deletePlansForWeek(userId, weekId);
            return null;
        }
    }

    // Get full week as domain models
    public LiveData<List<MealPlanItem>> getPlanForWeek(int userId, int weekId) {
        // 1. Get raw DB data (PlanWithRecipe contains Entities)
        LiveData<List<PlanWithRecipe>> dbData = planDAO.getPlansWithRecipesForWeek(userId, weekId);

        // 2. Map Database Entities -> Domain Models
        return Transformations.map(dbData, dbList -> {
            List<MealPlanItem> domainList = new ArrayList<>();
            if (dbList != null) {
                for (PlanWithRecipe item : dbList) {

                    // USE YOUR PLAN MAPPER HERE
                    Plan planModel = PlanMapper.toModel(item.plan);

                    // USE RECIPE MAPPER HERE
                    Recipe recipeModel = null;
                    if (item.recipe != null) {
                        recipeModel = RecipeMapper.toModel(item.recipe);
                    }

                    // Create the Clean Domain Wrapper
                    domainList.add(new MealPlanItem(planModel, recipeModel));
                }
            }
            return domainList;
        });
    }

    public List<Plan> getPlanForWeekSync(int userId, int weekId) {
        List<PlanEntity> entities = planDAO.getPlanForWeekSync(userId, weekId);
        return PlanMapper.toModelList(entities);
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
        new InsertPlanTask(planDAO).execute(plan);
    }

    // Overload: add plan slot from domain model
    public void addPlanSlot(Plan planModel) {
        if (planModel == null) return;
        PlanEntity entity = PlanMapper.toEntity(planModel);
        new InsertPlanTask(planDAO).execute(entity);
    }

    public void deleteSlot(Plan slot) {
        if (slot == null) return;
        PlanEntity entity = PlanMapper.toEntity(slot);
        new DeletePlanTask(planDAO).execute(entity);
    }

    public void clearWeek(int userId, int weekId) {
        new ClearWeekTask(planDAO, userId, weekId).execute();
    }

    public List<Plan> getPlansUsingRecipe(int recipeId) {
        List<PlanEntity> entities = planDAO.getPlanEntriesByRecipe(recipeId);
        return PlanMapper.toModelList(entities);
    }

    public List<MealPlanItem> getPlanForWeekSync_Domain(int userId, int weekId) {
        // 1. Fetch Entities (Filtered by Week ID)
        List<PlanWithRecipe> dbList = planDAO.getPlansWithRecipesForWeekSync(userId, weekId);

        List<MealPlanItem> domainList = new ArrayList<>();
        if (dbList != null) {
            for (PlanWithRecipe item : dbList) {
                // 2. Map Entities -> Domain Models
                Plan planModel = PlanMapper.toModel(item.plan);
                Recipe recipeModel = null;
                if (item.recipe != null) {
                    recipeModel = RecipeMapper.toModel(item.recipe);
                }

                // 3. Wrap in MealPlanItem
                domainList.add(new MealPlanItem(planModel, recipeModel));
            }
        }
        return domainList;
    }
}
