package vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.lifecycle.LiveData;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import java.util.List;

@Dao
public interface PlanDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PlanEntity plan);

    @Update
    int update(PlanEntity plan);

    @Delete
    int delete(PlanEntity plan);


    @Query("SELECT * FROM `Plan` WHERE user_id = :userId AND week_id = :weekId ORDER BY week_day ASC")
    LiveData<List<PlanEntity>> getPlanForWeek(int userId, int weekId);

    @Query("SELECT * FROM `Plan` WHERE user_id = :userId AND week_id = :weekId AND week_day = :weekDay")
    PlanEntity getPlanSlot(int userId, int weekId, int weekDay);

    @Query("SELECT * FROM `Plan` WHERE recipe_id = :recipeId")
    List<PlanEntity> getPlanEntriesByRecipe(int recipeId);
}
