package vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import java.util.List;

@Dao
public interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecipeEntity recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<RecipeEntity> recipes);

    @Update
    int update(RecipeEntity recipe);

    @Delete
    int delete(RecipeEntity recipe);

    // SELECT ALL (UI Home)
    @Query("SELECT * FROM Recipe ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getAllRecipes();

    // SELECT BY RecipeID (Detail Screen)
    @Query("SELECT * FROM Recipe WHERE recipe_id = :recipeId LIMIT 1")
    RecipeEntity getRecipeById(int recipeId);

    @Query("SELECT * FROM Recipe WHERE recipe_id = :recipeId LIMIT 1")
    LiveData<RecipeEntity> getRecipeByIdLive(int recipeId);

    // SELECT BY USER ID (My Recipes)
    @Query("SELECT * FROM Recipe WHERE user_id = :userId ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getRecipesByCreator(int userId);

    // Select Pin
    @Query("SELECT * FROM Recipe WHERE status = 1 ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getPinnedRecipes();

    // Select Unpin
    @Query("SELECT * FROM Recipe WHERE status = 0 ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getUnpinnedRecipes();

    // SEARCH by title (for Search screen)
    @Query("SELECT * FROM Recipe " +
           "WHERE title LIKE '%' || :searchQuery || '%' " +
           "ORDER BY title ASC")
    LiveData<List<RecipeEntity>> searchRecipes(String searchQuery);

    // TOGGLE PIN (recipeDAO.togglePinned(id))
    @Query("UPDATE Recipe " +
           "SET status = CASE status WHEN 1 THEN 0 ELSE 1 END " +
           "WHERE recipe_id = :recipeId")
    void togglePinned(int recipeId);

    @Query("SELECT COUNT(*) FROM Recipe")
    int countRecipes();

    // SELECT ALL (background thread)
    @Query("SELECT * FROM Recipe ORDER BY title ASC")
    List<RecipeEntity> getAllSync();

    // SELECT PINNED (background thread)
    @Query("SELECT * FROM Recipe WHERE status = 1 ORDER BY title ASC")
    List<RecipeEntity> getPinnedRecipesSync();

    @Query("SELECT * FROM Recipe WHERE (user_id = :userId OR user_id = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY title ASC")
    LiveData<List<RecipeEntity>> searchRecipesForUser(String searchQuery, int userId);
}
