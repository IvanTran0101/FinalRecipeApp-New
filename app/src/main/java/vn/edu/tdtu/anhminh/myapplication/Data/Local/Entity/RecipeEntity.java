package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import java.util.UUID;

@Entity(tableName = "Recipe", foreignKeys = @ForeignKey(
        entity = UserEntity.class, // 1. The PARENT table entity
        parentColumns = "user_id", // 2. The PK column name in the PARENT (UserEntity)
        childColumns = "user_id",  // 3. The FK column name in the CHILD (RecipeEntity)
        onDelete = ForeignKey.CASCADE // Optional: What to do when a User is deleted
        )
)
public class RecipeEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipe_id")
    private UUID recipeId;

    @ColumnInfo(name = "user_id")
    private UUID userId;

    @ColumnInfo(name = "title")
    private String title;

    //Change to URI
    @ColumnInfo(name = "recipe_image")
    private String recipeImage;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "diet_mode")
    private String dietMode;

    //Change to URI
    @ColumnInfo(name = "video_link")
    private String videoLink;

    @ColumnInfo(name = "calories")
    private Double calories;

    @ColumnInfo(name = "protein")
    private Double protein;

    @ColumnInfo(name = "carb")
    private Double carb;

    @ColumnInfo(name = "fat")
    private Double fat;

    @ColumnInfo(name = "status", defaultValue = "Unpin")
    private String status;

    // -------------------------------------------------------------------
    // --- 1. CONSTRUCTOR (REQUIRED by Room) ---
    // Room needs a way to create an instance when reading from the database.
    // This constructor must include all columns used by Room.
    // -------------------------------------------------------------------
    public RecipeEntity(@NonNull UUID recipeId, UUID userId, String title, String recipeImage, String category,
            String dietMode, String videoLink, Double calories, Double protein, Double carb,
            Double fat, String status) {

        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.recipeImage = recipeImage;
        this.category = category;
        this.dietMode = dietMode;
        this.videoLink = videoLink;
        this.calories = calories;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
        this.status = status;
    }

    @NonNull
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(@NonNull UUID recipeId) { this.recipeId = recipeId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRecipeImage() { return recipeImage; }
    public void setRecipeImage(String recipeImage) { this.recipeImage = recipeImage; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDietMode() { return dietMode; }
    public void setDietMode(String dietMode) { this.dietMode = dietMode; }

    public String getVideoLink() { return videoLink; }
    public void setVideoLink(String videoLink) { this.videoLink = videoLink; }

    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }

    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }

    public Double getCarb() { return carb; }
    public void setCarb(Double carb) { this.carb = carb; }

    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}