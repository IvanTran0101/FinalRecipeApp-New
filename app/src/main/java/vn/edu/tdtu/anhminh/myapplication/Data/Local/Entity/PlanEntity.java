package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(
        tableName = "Plan",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "user_id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = RecipeEntity.class,
                        parentColumns = "recipe_id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "user_id"),
                @Index(value = "recipe_id")
        }
)
public class PlanEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plan_id")
    private Integer planId;

    @NonNull
    @ColumnInfo(name = "week_id")
    private Integer weekId;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private Integer recipeId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private Integer userId;

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
    public PlanEntity(Integer planId, @NonNull Integer weekId, @NonNull Integer recipeId, @NonNull Integer userId, @NonNull Integer weekNumber, @NonNull Integer weekDay) {
        this.planId = planId;
        this.weekId = weekId;
        this.recipeId = recipeId;
        this.userId = userId;
        this.weekNumber = weekNumber;
        this.weekDay = weekDay;
    }

    public Integer getPlanId() { return planId; }
    public void setPlanId(Integer planId) { this.planId = planId; }

    @NonNull
    public Integer getWeekId() { return weekId; }
    public void setWeekId(@NonNull Integer weekId) { this.weekId = weekId; }

    @NonNull
    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(@NonNull Integer recipeId) { this.recipeId = recipeId; }

    @NonNull
    public Integer getUserId() { return userId; }
    public void setUserId(@NonNull Integer userId) { this.userId = userId; }

    @NonNull
    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(@NonNull Integer weekNumber) { this.weekNumber = weekNumber; }

    @NonNull
    public Integer getWeekDay() { return weekDay; }
    public void setWeekDay(@NonNull Integer weekDay) { this.weekDay = weekDay; }
}