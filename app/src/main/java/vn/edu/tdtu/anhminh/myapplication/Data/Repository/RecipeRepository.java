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

    // =========================================================================================
    // PHẦN 1: ĐỒNG BỘ DỮ LIỆU TỪ CLOUD (API / GITHUB)
    // =========================================================================================

    public interface SyncCallback {
        void onSuccess();
        void onError(String message);
    }

    public void syncRecipesFromCloud(SyncCallback callback) {
        apiService.getSampleRecipe(new RecipeApiService.ApiCallback<List<RecipeDTO>>() {
            @Override
            public void onSuccess(List<RecipeDTO> data) {
                // Tải thành công -> Lưu vào Database Local
                new ImportRecipesTask(recipeDAO, ingredientDAO, instructionDAO, callback).execute(data);
            }

            @Override
            public void onError(String error) {
                if (callback != null) callback.onError(error);
            }
        });
    }

    public void syncRecipesFromAssets(SyncCallback callback) {
        apiService.getRecipesFromAssets(new RecipeApiService.ApiCallback<List<RecipeDTO>>() {
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

    public void syncRecipesIfEmpty(SyncCallback callback) {
        new CheckEmptyAndSyncTask(recipeDAO, this, callback).execute();
    }

    /**
     * AsyncTask để lưu danh sách Recipe + Ingredients + Instructions vào DB
     */
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
                // 👇 [SỬA LỖI TẠI ĐÂY]: Dùng trực tiếp toEntity(dto) có sẵn trong Mapper
                RecipeEntity entity = RecipeMapper.toEntity(dto);

                if (entity != null) {
                    // Insert Recipe và lấy về ID mới
                    long newRecipeId = recipeDAO.insert(entity);

                    // 2. Lưu danh sách Nguyên liệu (Ingredients)
                    if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
                        for (IngredientDTO ingDto : dto.getIngredients()) {
                            IngredientEntity ingEntity = new IngredientEntity();
                            ingEntity.setRecipeId((int) newRecipeId); // Quan trọng: Gán ID món ăn cha
                            ingEntity.setName(ingDto.getName());
                            ingEntity.setQuantity(ingDto.getQuantity());
                            ingEntity.setUnit(ingDto.getUnit());

                            ingredientDAO.insert(ingEntity);
                        }
                    }

                    // 3. Lưu danh sách Hướng dẫn (Instructions)
                    if (dto.getInstructions() != null && !dto.getInstructions().isEmpty()) {
                        for (InstructionDTO instDto : dto.getInstructions()) {
                            InstructionEntity instEntity = new InstructionEntity();
                            instEntity.setRecipeId((int) newRecipeId); // Quan trọng: Gán ID món ăn cha
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

    private static class CheckEmptyAndSyncTask extends AsyncTask<Void, Void, Boolean> {
        private final RecipeDAO recipeDAO;
        private final RecipeRepository repository;
        private final SyncCallback callback;

        CheckEmptyAndSyncTask(RecipeDAO recipeDAO, RecipeRepository repository, SyncCallback callback) {
            this.recipeDAO = recipeDAO;
            this.repository = repository;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return recipeDAO.countRecipes() == 0;
        }

        @Override
        protected void onPostExecute(Boolean isEmpty) {
            if (!isEmpty) {
                if (callback != null) callback.onSuccess();
                return;
            }

            // Ưu tiên seed từ assets để không phụ thuộc GitHub
            repository.syncRecipesFromAssets(new SyncCallback() {
                @Override
                public void onSuccess() {
                    if (callback != null) callback.onSuccess();
                }

                @Override
                public void onError(String message) {
                    // Nếu assets lỗi, tiếp tục thử GitHub
                    repository.syncRecipesFromCloud(callback);
                }
            });
        }
    }

    // =========================================================================================
    // PHẦN 2: CÁC TASK ASYNC CƠ BẢN (Insert, Update, Delete)
    // =========================================================================================

    private static class InsertRecipeTask extends AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        InsertRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.insert(entities[0]); }
            return null;
        }
    }

    private static class UpdateRecipeTask extends AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        UpdateRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.update(entities[0]); }
            return null;
        }
    }

    private static class DeleteRecipeTask extends AsyncTask<RecipeEntity, Void, Void> {
        private final RecipeDAO dao;
        DeleteRecipeTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(RecipeEntity... entities){
            if(entities!=null && entities.length>0){ dao.delete(entities[0]); }
            return null;
        }
    }

    private static class TogglePinnedTask extends AsyncTask<Integer, Void, Void> {
        private final RecipeDAO dao;
        TogglePinnedTask(RecipeDAO dao){ this.dao = dao; }
        @Override protected Void doInBackground(Integer... ids){
            if(ids!=null && ids.length>0){ dao.togglePinned(ids[0]); }
            return null;
        }
    }

    // =========================================================================================
    // PHẦN 3: CÁC HÀM PUBLIC CHO VIEWMODEL GỌI
    // =========================================================================================

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
        // Nếu userId không hợp lệ, trả về toàn bộ công thức (bao gồm dữ liệu seed từ GitHub/asset)
        if (userId <= 0) {
            return Transformations.map(
                    recipeDAO.searchRecipes(searchQuery),
                    RecipeMapper::toModelList
            );
        }

        return Transformations.map(
                recipeDAO.searchRecipesForUser(searchQuery, userId),
                RecipeMapper::toModelList
        );
    }
}