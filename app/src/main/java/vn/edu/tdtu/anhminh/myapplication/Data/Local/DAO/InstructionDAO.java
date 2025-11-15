package vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import java.util.List;

@Dao
public interface InstructionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(InstructionEntity instruction);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<InstructionEntity> instructions);

    @Update
    int update(InstructionEntity instruction);

    @Delete
    int delete(InstructionEntity instruction);


    @Query("SELECT * FROM instruction WHERE recipe_id = :recipeId ORDER BY step_number ASC")
    LiveData<List<InstructionEntity>> getInstructionsForRecipe(int recipeId);

    @Query("DELETE FROM instruction WHERE recipe_id = :recipeId")
    int deleteInstructionsByRecipeId(int recipeId);
}
