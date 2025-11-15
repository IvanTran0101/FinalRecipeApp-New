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

    @Query("SELECT quantity, unit FROM Ingredient WHERE recipe_id = :recipeId")
    LiveData<List<IngredientEntity>> getQuantityAndUnitForRecipe(int recipeId);

    @Query("UPDATE Ingredient SET quantity = :newQuantity WHERE ingredient_id = :ingredientId")
    int updateIngredientQuantity(int ingredientId, int newQuantity);

    @Query("UPDATE Ingredient SET unit = :newUnit WHERE ingredient_id = :ingredientId")
    int updateIngredientUnit(int ingredientId, String newUnit);

    @Query("UPDATE Ingredient SET name = :newName WHERE ingredient_id = :ingredientId")
    int updateIngredientName(int ingredientId, String newName);
}
