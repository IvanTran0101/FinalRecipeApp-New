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

    @Update
    int update(RecipeEntity recipe);

    @Delete
    int delete(RecipeEntity recipe);


    @Query("SELECT * FROM Recipe WHERE recipe_id = :recipeId")
    RecipeEntity getRecipeById(int recipeId);

    @Query("SELECT * FROM Recipe ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getAllRecipes();

    @Query("SELECT * FROM Recipe WHERE user_id = :userId ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getRecipesByCreator(int userId);

    @Query("SELECT * FROM Recipe WHERE status = 1 ORDER BY title ASC")
    LiveData<List<RecipeEntity>> getPinnedRecipes();
}
