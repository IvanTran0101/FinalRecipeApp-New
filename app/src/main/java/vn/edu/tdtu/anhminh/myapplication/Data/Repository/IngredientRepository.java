package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.IngredientMapper;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class IngredientRepository {
    private final IngredientDAO ingredientDAO;

    public IngredientRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.ingredientDAO = db.ingredientDao();
    }

    private static class InsertIngredientTask extends android.os.AsyncTask<IngredientEntity, Void, Void> {
        private final IngredientDAO dao;
        InsertIngredientTask(IngredientDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(IngredientEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.insert(entities[0]);
            }
            return null;
        }
    }

    private static class InsertIngredientsTask extends android.os.AsyncTask<java.util.List<IngredientEntity>, Void, Void> {
        private final IngredientDAO dao;
        InsertIngredientsTask(IngredientDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(java.util.List<IngredientEntity>... lists) {
            if (lists != null && lists.length > 0 && lists[0] != null && !lists[0].isEmpty()) {
                dao.insertAll(lists[0]);
            }
            return null;
        }
    }

    private static class DeleteIngredientTask extends android.os.AsyncTask<IngredientEntity, Void, Void> {
        private final IngredientDAO dao;
        DeleteIngredientTask(IngredientDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(IngredientEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.delete(entities[0]);
            }
            return null;
        }
    }

    private static class DeleteIngredientsByRecipeTask extends android.os.AsyncTask<Integer, Void, Void> {
        private final IngredientDAO dao;
        DeleteIngredientsByRecipeTask(IngredientDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(Integer... ids) {
            if (ids != null && ids.length > 0) {
                dao.deleteIngredientsByRecipeId(ids[0]);
            }
            return null;
        }
    }

    private static class UpdateIngredientQuantityTask extends android.os.AsyncTask<Void, Void, Void> {
        private final IngredientDAO dao;
        private final int ingredientId;
        private final int newQuantity;
        UpdateIngredientQuantityTask(IngredientDAO dao, int ingredientId, int newQuantity) {
            this.dao = dao;
            this.ingredientId = ingredientId;
            this.newQuantity = newQuantity;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateIngredientQuantity(ingredientId, newQuantity);
            return null;
        }
    }

    private static class UpdateIngredientUnitTask extends android.os.AsyncTask<Void, Void, Void> {
        private final IngredientDAO dao;
        private final int ingredientId;
        private final String newUnit;
        UpdateIngredientUnitTask(IngredientDAO dao, int ingredientId, String newUnit) {
            this.dao = dao;
            this.ingredientId = ingredientId;
            this.newUnit = newUnit;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateIngredientUnit(ingredientId, newUnit);
            return null;
        }
    }

    private static class UpdateIngredientNameTask extends android.os.AsyncTask<Void, Void, Void> {
        private final IngredientDAO dao;
        private final int ingredientId;
        private final String newName;
        UpdateIngredientNameTask(IngredientDAO dao, int ingredientId, String newName) {
            this.dao = dao;
            this.ingredientId = ingredientId;
            this.newName = newName;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateIngredientName(ingredientId, newName);
            return null;
        }
    }

    // Task mới: Gộp Xóa và Thêm vào làm một để tránh xung đột
    private static class ReplaceIngredientsTask extends android.os.AsyncTask<Void, Void, Void> {
        private final IngredientDAO dao;
        private final int recipeId;
        private final List<IngredientEntity> newEntities;

        ReplaceIngredientsTask(IngredientDAO dao, int recipeId, List<IngredientEntity> newEntities) {
            this.dao = dao;
            this.recipeId = recipeId;
            this.newEntities = newEntities;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // 1. Xóa dữ liệu cũ trước (Chạy tuần tự)
            dao.deleteIngredientsByRecipeId(recipeId);

            // 2. Sau đó mới thêm dữ liệu mới (nếu có)
            if (newEntities != null && !newEntities.isEmpty()) {
                dao.insertAll(newEntities);
            }
            return null;
        }
    }

    // ---------------------------------------------------
    // INGREDIENTS for a recipe
    // ---------------------------------------------------
    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeId) {
        return Transformations.map(
                ingredientDAO.getIngredientsForRecipe(recipeId),
                IngredientMapper::toModelList
        );
    }

    // ---------------------------------------------------
    // INGREDIENT: thêm 1 nguyên liệu cho recipe
    // ---------------------------------------------------
    public void addIngredient(Ingredient ingredient) {
        if (ingredient == null) return;
        IngredientEntity entity = IngredientMapper.toEntity(ingredient);
        new InsertIngredientTask(ingredientDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INGREDIENT: thêm nhiều nguyên liệu cùng lúc
    // ---------------------------------------------------
    public void addMultipleIngredients(List<Ingredient> ingredients) {
        if(ingredients == null || ingredients.isEmpty())return;
        List<IngredientEntity> entities = new ArrayList<>();
        for (Ingredient ingredient : ingredients){
            IngredientEntity entity = IngredientMapper.toEntity(ingredient);
            if (entity != null){
                entities.add(entity);
            }
        }
        if (!entities.isEmpty()){
            new InsertIngredientsTask(ingredientDAO).execute(entities);
        }
    }

    // ---------------------------------------------------
    // INGREDIENT: xóa 1 nguyên liệu cụ thể
    // ---------------------------------------------------
    public void deleteIngredient(Ingredient ingredient) {
        if (ingredient == null) return;
        IngredientEntity entity = IngredientMapper.toEntity(ingredient);
        new DeleteIngredientTask(ingredientDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INGREDIENT: delete all ingredients for a recipe
    // (used by ManageRecipeUseCase when deleting a recipe)
    // ---------------------------------------------------
    public void deleteIngredientsForRecipe(int recipeId) {
        new DeleteIngredientsByRecipeTask(ingredientDAO).execute(recipeId);
    }

    // ---------------------------------------------------
    // INGREDIENT: thay toàn bộ nguyên liệu của 1 recipe
    // (dùng khi user edit list ingredient rồi bấm Save)
    // ---------------------------------------------------
    // ---------------------------------------------------
    // INGREDIENT: thay toàn bộ nguyên liệu của 1 recipe
    // ---------------------------------------------------
    public void replaceIngredientsForRecipe(int recipeId, List<Ingredient> newIngredients) {
        // 1. Chuẩn bị dữ liệu Entity
        List<IngredientEntity> entities = new ArrayList<>();
        if (newIngredients != null && !newIngredients.isEmpty()) {
            for (Ingredient ingredient : newIngredients) {
                IngredientEntity entity = IngredientMapper.toEntity(ingredient);
                if (entity != null) {
                    // Quan trọng: Đảm bảo ID khớp với Recipe đang sửa
                    entity.setRecipeId(recipeId);
                    entities.add(entity);
                }
            }
        }

        // 2. Gọi Task gộp để thực thi an toàn
        new ReplaceIngredientsTask(ingredientDAO, recipeId, entities).execute();
    }

    // ---------------------------------------------------
    // INGREDIENT: update quantity
    // ---------------------------------------------------
    public void updateIngredientQuantity(int ingredientId, int newQuantity) {
        new UpdateIngredientQuantityTask(ingredientDAO, ingredientId, newQuantity).execute();
    }

    // ---------------------------------------------------
    // INGREDIENT: update unit
    // ---------------------------------------------------
    public void updateIngredientUnit(int ingredientId, String newUnit) {
        new UpdateIngredientUnitTask(ingredientDAO, ingredientId, newUnit).execute();
    }

    // ---------------------------------------------------
    // INGREDIENT: update name
    // ---------------------------------------------------
    public void updateIngredientName(int ingredientId, String newName) {
        new UpdateIngredientNameTask(ingredientDAO, ingredientId, newName).execute();
    }

    // ---------------------------------------------------
    // INGREDIENT: get ingredient by ID
    // ---------------------------------------------------
    public Ingredient getIngredientById(int ingredientId) {
        IngredientEntity entity = ingredientDAO.getIngredientById(ingredientId);
        return IngredientMapper.toModel(entity);
    }

    // ---------------------------------------------------
    // INGREDIENT: get all ingredients (debug/admin)
    // ---------------------------------------------------
    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> entities = ingredientDAO.getAllIngredients();
        return IngredientMapper.toModelList(entities);
    }

    public List<Ingredient> getAllIngredientsByRecipeIdSync(int recipeId) {
        List<IngredientEntity> entities = ingredientDAO.getIngredientsForRecipeSync(recipeId);
        return IngredientMapper.toModelList(entities);
    }

    // ---------------------------------------------------
    // INGREDIENT: count ingredients
    // ---------------------------------------------------
    public int countIngredients() {
        return ingredientDAO.countIngredients();
    }

    // ---------------------------------------------------
    // INGREDIENT: get quantity + unit for recipe
    // ---------------------------------------------------

    public LiveData<List<Ingredient>> getQuantityAndUnitForRecipe(int recipeId) {
        return Transformations.map(
                ingredientDAO.getQuantityAndUnitForRecipe(recipeId),
                IngredientMapper::toModelList
        );
    }
}
