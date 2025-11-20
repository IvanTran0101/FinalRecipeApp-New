package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.InstructionDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.IngredientMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.InstructionMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class RecipeRepository {
    private final RecipeDAO recipeDAO;
    private final InstructionDAO instructionDAO;
    private final IngredientDAO ingredientDAO;

    private static class InsertRecipeTask extends android.os.AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        InsertRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.insert(entities[0]); }
            return null;
        }
    }

    private static class UpdateRecipeTask extends android.os.AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        UpdateRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.update(entities[0]); }
            return null;
        }
    }

    private static class DeleteRecipeTask extends android.os.AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        DeleteRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.delete(entities[0]); }
            return null;
        }
    }

    private static class TogglePinnedTask extends android.os.AsyncTask<Integer, Void, Void> {
        private final RecipeDAO dao;
        TogglePinnedTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(Integer... ids){
            if(ids!=null && ids.length>0){ dao.togglePinned(ids[0]); }
            return null;
        }
    }

    private static class InsertInstructionTask extends android.os.AsyncTask<InstructionEntity, Void, Void> {
        private final InstructionDAO dao;
        InsertInstructionTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(InstructionEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.insert(entities[0]);
            }
            return null;
        }
    }

    private static class InsertInstructionsTask extends android.os.AsyncTask<java.util.List<InstructionEntity>, Void, Void> {
        private final InstructionDAO dao;
        InsertInstructionsTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(java.util.List<InstructionEntity>... lists) {
            if (lists != null && lists.length > 0 && lists[0] != null && !lists[0].isEmpty()) {
                dao.insertAll(lists[0]);
            }
            return null;
        }
    }

    private static class DeleteInstructionTask extends android.os.AsyncTask<InstructionEntity, Void, Void> {
        private final InstructionDAO dao;
        DeleteInstructionTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(InstructionEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.delete(entities[0]);
            }
            return null;
        }
    }

    private static class DeleteInstructionsByRecipeTask extends android.os.AsyncTask<Integer, Void, Void> {
        private final InstructionDAO dao;
        DeleteInstructionsByRecipeTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(Integer... ids) {
            if (ids != null && ids.length > 0) {
                dao.deleteInstructionsByRecipeId(ids[0]);
            }
            return null;
        }
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

    public RecipeRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.recipeDAO = db.recipeDao();
        this.instructionDAO = db.instructionDao();
        this.ingredientDAO = db.ingredientDao();
    }
    //sync Room
    public List<Recipe> getAllRecipeSync(){
        List<RecipeEntity> entities = recipeDAO.getAllSync();
        return RecipeMapper.toModelList(entities);
    }

    public void addRecipe(Recipe recipe){
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        new InsertRecipeTask(recipeDAO).execute(entity);
    }

    public void importRecipesFromDtoList(List<RecipeDTO> dtos){
        List<RecipeEntity> entities = RecipeMapper.toEntityList(dtos);
        for (RecipeEntity entity : entities){
            new InsertRecipeTask(recipeDAO).execute(entity);
        }
    }

    public void updateRecipe(Recipe recipe){
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        new UpdateRecipeTask(recipeDAO).execute(entity);
    }

    public void deleteRecipe(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        new DeleteRecipeTask(recipeDAO).execute(entity);
    }

    public Recipe getRecipeDetail(int recipeId){
        RecipeEntity entity = recipeDAO.getRecipeById(recipeId);
        return RecipeMapper.toModel(entity);
    }

    public void togglePinned(int recipeId){
        new TogglePinnedTask(recipeDAO).execute(recipeId);
    }

    public List<Recipe> getPinnedRecipesSync() {
        List<RecipeEntity> entities = recipeDAO.getPinnedRecipesSync();
        return RecipeMapper.toModelList(entities);
    }

    // ---------------------------------------------------
    // RECIPE: search by title
    // ---------------------------------------------------
    public LiveData<List<Recipe>> searchRecipes(String searchQuery) {
        return Transformations.map(
                recipeDAO.searchRecipes(searchQuery),
                RecipeMapper::toModelList
        );
    }

    //instruction
    public LiveData<List<Instruction>> getInstructionsForRecipe(int recipeId) {
        return Transformations.map(
                instructionDAO.getInstructionsForRecipe(recipeId),
                InstructionMapper::toModelList
        );
    }
    // ---------------------------------------------------
    // INSTRUCTION: thêm 1 bước cho recipe
    // ---------------------------------------------------
    public void addInstruction(Instruction instruction) {
        if (instruction == null) return;

        InstructionEntity entity =
                InstructionMapper.toEntity(instruction);
        new InsertInstructionTask(instructionDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thêm nhiều bước cùng lúc
    // ---------------------------------------------------
    public void addInstructions(List<Instruction> instructions) {
        if (instructions == null || instructions.isEmpty()) return;
        List<InstructionEntity> entities = new ArrayList<>();
        for (Instruction instruction : instructions) {
            InstructionEntity entity = InstructionMapper.toEntity(instruction);
            if (entity != null) {
                entities.add(entity);
            }

        }
        if (!entities.isEmpty()){
            new InsertInstructionsTask(instructionDAO).execute(entities);
        }
    }

    // ---------------------------------------------------
    // INSTRUCTION: xóa 1 bước cụ thể
    // ---------------------------------------------------
    public void deleteInstruction(Instruction instruction) {
        if(instruction == null) return;
        InstructionEntity entity = InstructionMapper.toEntity(instruction);
        new DeleteInstructionTask(instructionDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thay toàn bộ steps của 1 recipe
    // (dùng khi user edit list step rồi bấm Save)
    // ---------------------------------------------------
    public void replaceInstructionsForRecipe(int recipeId, List<Instruction> newInstructions) {
        new DeleteInstructionsByRecipeTask(instructionDAO).execute(recipeId);
        if (newInstructions == null || newInstructions.isEmpty()) return;

        List<InstructionEntity> entities = new ArrayList<>();
        for (Instruction instruction : newInstructions){
            InstructionEntity entity = InstructionMapper.toEntity(instruction);
            if (entity != null) {
                entities.add(entity);
            }

        }
        if (!entities.isEmpty()){
            new InsertInstructionsTask(instructionDAO).execute(entities);
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
    public void addIngredients(List<Ingredient> ingredients) {
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
    // INGREDIENT: thay toàn bộ nguyên liệu của 1 recipe
    // (dùng khi user edit list ingredient rồi bấm Save)
    // ---------------------------------------------------
    public void replaceIngredientsForRecipe(int recipeId, List<Ingredient> newIngredients) {
        new DeleteIngredientsByRecipeTask(ingredientDAO).execute(recipeId);
        if (newIngredients == null || newIngredients.isEmpty()) return;
        List<IngredientEntity> entities = new ArrayList<>();
        for (Ingredient ingredient : newIngredients){
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
