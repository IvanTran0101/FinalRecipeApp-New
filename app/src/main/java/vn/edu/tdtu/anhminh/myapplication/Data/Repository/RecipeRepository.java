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
        recipeDAO.insert(entity);
    }

    public void updateRecipe(Recipe recipe){
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        recipeDAO.update(entity);
    }

    public void deleteRecipe(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        recipeDAO.delete(entity);
    }

    public Recipe getRecipeDetail(int recipeId){
        RecipeEntity entity = recipeDAO.getRecipeById(recipeId);
        return RecipeMapper.toModel(entity);
    }

    public void togglePinned(int recipeId){
        recipeDAO.togglePinned(recipeId);
    }

    public List<Recipe> getPinnedRecipesSync() {
        List<RecipeEntity> entities = recipeDAO.getPinnedRecipesSync();
        return RecipeMapper.toModelList(entities);
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
        instructionDAO.insert(entity);
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
            instructionDAO.insertAll(entities);
        }
    }

    // ---------------------------------------------------
    // INSTRUCTION: xóa 1 bước cụ thể
    // ---------------------------------------------------
    public void deleteInstruction(Instruction instruction) {
        if(instruction == null) return;
        InstructionEntity entity = InstructionMapper.toEntity(instruction);
        instructionDAO.delete(entity);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thay toàn bộ steps của 1 recipe
    // (dùng khi user edit list step rồi bấm Save)
    // ---------------------------------------------------
    public void replaceInstructionsForRecipe(int recipeId, List<Instruction> newInstructions) {
        instructionDAO.deleteInstructionsByRecipeId(recipeId);
        if (newInstructions == null || newInstructions.isEmpty()) return;

        List<InstructionEntity> entities = new ArrayList<>();
        for (Instruction instruction : newInstructions){
            InstructionEntity entity = InstructionMapper.toEntity(instruction);
            if (entity != null) {
                entities.add(entity);
            }

        }
        if (!entities.isEmpty()){
            instructionDAO.insertAll(entities);
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
        ingredientDAO.insert(entity);
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
            ingredientDAO.insertAll(entities);
        }
    }

    // ---------------------------------------------------
    // INGREDIENT: xóa 1 nguyên liệu cụ thể
    // ---------------------------------------------------
    public void deleteIngredient(Ingredient ingredient) {
        if (ingredient == null) return;
        IngredientEntity entity = IngredientMapper.toEntity(ingredient);
        ingredientDAO.delete(entity);
    }

    // ---------------------------------------------------
    // INGREDIENT: thay toàn bộ nguyên liệu của 1 recipe
    // (dùng khi user edit list ingredient rồi bấm Save)
    // ---------------------------------------------------
    public void replaceIngredientsForRecipe(int recipeId, List<Ingredient> newIngredients) {
        ingredientDAO.deleteIngredientsByRecipeId(recipeId);
        if (newIngredients == null || newIngredients.isEmpty()) return;
        List<IngredientEntity> entities = new ArrayList<>();
        for (Ingredient ingredient : newIngredients){
            IngredientEntity entity = IngredientMapper.toEntity(ingredient);
            if (entity != null){
                entities.add(entity);

            }
        }
        if (!entities.isEmpty()){
            ingredientDAO.insertAll(entities);
        }
    }

    // ---------------------------------------------------
    // INGREDIENT: update quantity
    // ---------------------------------------------------
    public void updateIngredientQuantity(int ingredientId, int newQuantity) {
        ingredientDAO.updateIngredientQuantity(ingredientId, newQuantity);
    }

    // ---------------------------------------------------
    // INGREDIENT: update unit
    // ---------------------------------------------------
    public void updateIngredientUnit(int ingredientId, String newUnit) {
        ingredientDAO.updateIngredientUnit(ingredientId, newUnit);
    }

    // ---------------------------------------------------
    // INGREDIENT: update name
    // ---------------------------------------------------
    public void updateIngredientName(int ingredientId, String newName) {
        ingredientDAO.updateIngredientName(ingredientId, newName);
    }

    // ---------------------------------------------------
    // INGREDIENT: get ingredient by ID
    // ---------------------------------------------------
    public Ingredient getIngredientById(int ingredientId) {
        IngredientEntity entity= ingredientDAO.getIngredientById(ingredientId);
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
