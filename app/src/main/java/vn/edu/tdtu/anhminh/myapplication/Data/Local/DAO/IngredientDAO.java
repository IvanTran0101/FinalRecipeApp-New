package vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import java.util.List;

@Dao
public interface IngredientDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientEntity ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IngredientEntity> ingredients);

    @Update
    int update(IngredientEntity ingredient);

    @Delete
    int delete(IngredientEntity ingredient);


    @Query("DELETE FROM Ingredient WHERE recipe_id = :recipeId")
    int deleteIngredientsByRecipeId(int recipeId);

    @Query("SELECT * FROM Ingredient")
    List<IngredientEntity> getAllIngredients();

    @Query("SELECT * FROM Ingredient WHERE ingredient_id = :ingredientId")
    IngredientEntity getIngredientById(int ingredientId);

    @Query("SELECT * FROM Ingredient WHERE recipe_id = :recipeId ORDER BY name ASC")
    LiveData<List<IngredientEntity>> getIngredientsForRecipe(int recipeId);

    @Query("SELECT COUNT(*) FROM Ingredient")
    int countIngredients();
}
