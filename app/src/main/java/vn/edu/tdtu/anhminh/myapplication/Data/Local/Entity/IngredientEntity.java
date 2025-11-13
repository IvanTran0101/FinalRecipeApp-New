package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import java.util.UUID;

@Entity(tableName = "Ingredient", foreignKeys = @ForeignKey(
        entity = RecipeEntity.class, // The PARENT table entity
        parentColumns = "recipe_id", // PK column name in the PARENT
        childColumns = "recipe_id",  // FK column name in the CHILD
        onDelete = ForeignKey.CASCADE // 1. If a Recipe is deleted, delete all its Ingredients.
        )
)
public class IngredientEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ingredient_id")
    private UUID ingredientId;

    @NonNull
    @ColumnInfo(name = "recipe_id", index = true)
    private UUID recipeId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "quantity")
    private Double quantity;

    @NonNull
    @ColumnInfo(name = "unit")
    private String unit;

    // -------------------------------------------------------------------
    // --- CONSTRUCTOR (REQUIRED by Room) ---
    // -------------------------------------------------------------------
    public IngredientEntity(@NonNull UUID ingredientId, @NonNull UUID recipeId, @NonNull String name, @NonNull Double quantity, @NonNull String unit) {
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public void setIngredientId(@NonNull UUID ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setRecipeId(@NonNull UUID recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(@NonNull Double quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    @NonNull
    public UUID getIngredientId() { return ingredientId; }
    @NonNull
    public UUID getRecipeId() { return recipeId; }
}