package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.InstructionDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api.RecipeApiService;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.IngredientDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.InstructionDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class RecipeRepository {
    private final RecipeDAO recipeDAO;
    private final IngredientDAO ingredientDAO;
    private final InstructionDAO instructionDAO;
    private final RecipeApiService apiService;

    public RecipeRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.recipeDAO = db.recipeDao();
        this.ingredientDAO = db.ingredientDao();
        this.instructionDAO = db.instructionDao();
        this.apiService = new RecipeApiService(context);
    }

    // Sync from Cloud
    public interface SyncCallback {
        void onSuccess();
        void onError(String message);
    }

    public void syncRecipesFromCloud(SyncCallback callback) {
        apiService.getSampleRecipe(new RecipeApiService.ApiCallback<List<RecipeDTO>>() {
            @Override
            public void onSuccess(List<RecipeDTO> data) {
                new ImportRecipesTask(recipeDAO, ingredientDAO, instructionDAO, callback).execute(data);
            }

            @Override
            public void onError(String error) {
                if (callback != null) callback.onError(error);
            }
        });
    }

    private static class ImportRecipesTask extends AsyncTask<List<RecipeDTO>, Void, Void> {
        private final RecipeDAO recipeDAO;
        private final IngredientDAO ingredientDAO;
        private final InstructionDAO instructionDAO;
        private final SyncCallback callback;

        ImportRecipesTask(RecipeDAO rDao, IngredientDAO iDao, InstructionDAO instDao, SyncCallback cb) {
            this.recipeDAO = rDao;
            this.ingredientDAO = iDao;
            this.instructionDAO = instDao;
            this.callback = cb;
        }

        @Override
        protected Void doInBackground(List<RecipeDTO>... lists) {
            if (lists == null || lists.length == 0) return null;
            List<RecipeDTO> dtos = lists[0];

            for (RecipeDTO dto : dtos) {
                RecipeEntity entity = RecipeMapper.toEntity(dto);
                if (entity != null) {
                    long newRecipeId = recipeDAO.insert(entity);

                    if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
                        for (IngredientDTO ingDto : dto.getIngredients()) {
                            IngredientEntity ingEntity = new IngredientEntity();
                            ingEntity.setRecipeId((int) newRecipeId);
                            ingEntity.setName(ingDto.getName());
                            ingEntity.setQuantity(ingDto.getQuantity());
                            ingEntity.setUnit(ingDto.getUnit());
                            ingredientDAO.insert(ingEntity);
                        }
                    }

                    if (dto.getInstructions() != null && !dto.getInstructions().isEmpty()) {
                        for (InstructionDTO instDto : dto.getInstructions()) {
                            InstructionEntity instEntity = new InstructionEntity();
                            instEntity.setRecipeId((int) newRecipeId);
                            instEntity.setStepNumber(instDto.getStepNumber());
                            instEntity.setInstruction(instDto.getInstruction());
                            instructionDAO.insert(instEntity);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (callback != null) callback.onSuccess();
        }
    }

    // Synchronous CUD Operations for UseCases
    public long addRecipeSync(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        return recipeDAO.insert(entity);
    }

    public void updateRecipe(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        recipeDAO.update(entity);
    }

    public void deleteRecipe(Recipe recipe) {
        RecipeEntity entity = RecipeMapper.toEntity(recipe);
        recipeDAO.delete(entity);
    }

    // LiveData Getters for ViewModel
    public LiveData<Recipe> getRecipeByIdLiveData(int recipeId) {
        return Transformations.map(
                recipeDAO.getRecipeByIdLive(recipeId),
                RecipeMapper::toModel
        );
    }

    public LiveData<List<Recipe>> searchRecipes(String searchQuery, int userId) {
        return Transformations.map(
                recipeDAO.searchRecipesForUser(searchQuery, userId),
                RecipeMapper::toModelList
        );
    }
    
    // AsyncTask for pinning
    public void togglePinned(int recipeId){
        new TogglePinnedTask(recipeDAO).execute(recipeId);
    }

    private static class TogglePinnedTask extends AsyncTask<Integer, Void, Void> {
        private final RecipeDAO dao;
        TogglePinnedTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(Integer... ids){
            if(ids!=null && ids.length>0){ dao.togglePinned(ids[0]); }
            return null;
        }
    }
}
