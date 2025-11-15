package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.InstructionDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import androidx.lifecycle.LiveData;

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
    public LiveData<List<InstructionEntity>> getInstructionsForRecipe(int recipeId) {
        return instructionDAO.getInstructionsForRecipe(recipeId);
    }
    // ---------------------------------------------------
    // INSTRUCTION: thêm 1 bước cho recipe
    // ---------------------------------------------------
    public void addInstruction(InstructionEntity instruction) {
        instructionDAO.insert(instruction);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thêm nhiều bước cùng lúc
    // ---------------------------------------------------
    public void addInstructions(List<InstructionEntity> instructions) {
        instructionDAO.insertAll(instructions);
    }

    // ---------------------------------------------------
    // INSTRUCTION: xóa 1 bước cụ thể
    // ---------------------------------------------------
    public void deleteInstruction(InstructionEntity instruction) {
        instructionDAO.delete(instruction);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thay toàn bộ steps của 1 recipe
    // (dùng khi user edit list step rồi bấm Save)
    // ---------------------------------------------------
    public void replaceInstructionsForRecipe(int recipeId, List<InstructionEntity> newInstructions) {
        instructionDAO.deleteInstructionsByRecipeId(recipeId);
        instructionDAO.insertAll(newInstructions);
    }

    // ---------------------------------------------------
    // INGREDIENTS for a recipe
    // ---------------------------------------------------
    public LiveData<List<IngredientEntity>> getIngredientsForRecipe(int recipeId) {
        return ingredientDAO.getIngredientsForRecipe(recipeId);
    }

    // ---------------------------------------------------
    // INGREDIENT: thêm 1 nguyên liệu cho recipe
    // ---------------------------------------------------
    public void addIngredient(IngredientEntity ingredient) {
        ingredientDAO.insert(ingredient);
    }

    // ---------------------------------------------------
    // INGREDIENT: thêm nhiều nguyên liệu cùng lúc
    // ---------------------------------------------------
    public void addIngredients(List<IngredientEntity> ingredients) {
        ingredientDAO.insertAll(ingredients);
    }

    // ---------------------------------------------------
    // INGREDIENT: xóa 1 nguyên liệu cụ thể
    // ---------------------------------------------------
    public void deleteIngredient(IngredientEntity ingredient) {
        ingredientDAO.delete(ingredient);
    }

    // ---------------------------------------------------
    // INGREDIENT: thay toàn bộ nguyên liệu của 1 recipe
    // (dùng khi user edit list ingredient rồi bấm Save)
    // ---------------------------------------------------
    public void replaceIngredientsForRecipe(int recipeId, List<IngredientEntity> newIngredients) {
        ingredientDAO.deleteIngredientsByRecipeId(recipeId);
        ingredientDAO.insertAll(newIngredients);
    }
}
