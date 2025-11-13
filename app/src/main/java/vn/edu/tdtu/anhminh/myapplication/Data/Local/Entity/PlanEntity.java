package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import java.util.UUID;

@Entity(tableName = "Plan", primaryKeys = {"week_id", "user_id", "week_day"},
        foreignKeys = {
                // FK 1: Link to UserEntity
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "user_id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE // If the User is deleted, delete their Plan entries.
                ),
                // FK 2: Link to RecipeEntity
                @ForeignKey(
                        entity = RecipeEntity.class,
                        parentColumns = "recipe_id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE // Prevent deleting a Recipe that is currently scheduled.
                )
        }
)
public class PlanEntity {
    @NonNull
    @ColumnInfo(name = "week_id")
    private UUID weekId;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private UUID recipeId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private UUID userId;

    @NonNull
    @ColumnInfo(name = "week_number")
    private Integer weekNumber;

    //Change to Enum for Week Day
    @NonNull
    @ColumnInfo(name = "week_day")
    private Integer weekDay;

    // -------------------------------------------------------------------
    // --- CONSTRUCTOR (REQUIRED by Room) ---
    // -------------------------------------------------------------------
    public PlanEntity(@NonNull UUID weekId, @NonNull UUID recipeId, @NonNull UUID userId,@NonNull Integer weekNumber, @NonNull Integer weekDay) {
        this.weekId = weekId;
        this.recipeId = recipeId;
        this.userId = userId;
        this.weekNumber = weekNumber;
        this.weekDay = weekDay;
    }

    @NonNull
    public UUID getWeekId() { return weekId; }
    public void setWeekId(@NonNull UUID weekId) { this.weekId = weekId; }

    @NonNull
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(@NonNull UUID recipeId) { this.recipeId = recipeId; }

    @NonNull
    public UUID getUserId() { return userId; }
    public void setUserId(@NonNull UUID userId) { this.userId = userId; }

    @NonNull
    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(@NonNull Integer weekNumber) { this.weekNumber = weekNumber; }

    @NonNull
    public Integer getWeekDay() { return weekDay; }
    public void setWeekDay(@NonNull Integer weekDay) { this.weekDay = weekDay; }
}