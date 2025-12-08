package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class RecipeRepository {
    private final RecipeDAO recipeDAO;

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

    public RecipeRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.recipeDAO = db.recipeDao();
    }
    //sync Room
    public List<Recipe> getAllRecipeSync(){
        List<RecipeEntity> entities = recipeDAO.getAllSync();
        return RecipeMapper.toModelList(entities);
    }

    public long addRecipe(Recipe recipe){
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        return recipeDAO.insert(entity);
    }

    public void importRecipesFromDtoList(List<RecipeDTO> dtos){
        List<RecipeEntity> entities = RecipeMapper.toEntityList(dtos);
        for (RecipeEntity entity : entities){
            new InsertRecipeTask(recipeDAO).execute(entity);
        }
    }

    public void updateRecipe(Recipe recipe){
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        recipeDAO.update(entity);
    }

    public void deleteRecipe(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        new DeleteRecipeTask(recipeDAO).execute(entity);
    }

    public Recipe getRecipeDetail(int recipeId){
        RecipeEntity entity = recipeDAO.getRecipeById(recipeId);
        return RecipeMapper.toModel(entity);
    }

    public LiveData<Recipe> getRecipeByIdLiveData(int recipeId) {
        return Transformations.map(
                recipeDAO.getRecipeByIdLive(recipeId),
                RecipeMapper::toModel
        );
    }

    public void togglePinned(int recipeId){
        new TogglePinnedTask(recipeDAO).execute(recipeId);
    }

    public List<Recipe> getPinnedRecipesSync() {
        List<RecipeEntity> entities = recipeDAO.getPinnedRecipesSync();
        return RecipeMapper.toModelList(entities);
    }

    public LiveData<List<Recipe>> searchRecipes(String searchQuery, int userId) {
        return Transformations.map(
                recipeDAO.searchRecipesForUser(searchQuery, userId),
                RecipeMapper::toModelList
        );
    }
}
