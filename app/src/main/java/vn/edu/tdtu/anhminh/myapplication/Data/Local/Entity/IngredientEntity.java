package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

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
    private Integer ingredientId;

    @NonNull
    @ColumnInfo(name = "recipe_id", index = true)
    private Integer recipeId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "quantity")
    private double quantity;

    @NonNull
    @ColumnInfo(name = "unit")
    private String unit;

    // -------------------------------------------------------------------
    // --- CONSTRUCTOR (REQUIRED by Room) ---
    // -------------------------------------------------------------------
    public IngredientEntity(@NonNull Integer ingredientId, @NonNull Integer recipeId, @NonNull String name, @NonNull double quantity, @NonNull String unit) {
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public void setIngredientId(@NonNull Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setRecipeId(@NonNull Integer recipeId) {
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
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(@NonNull double quantity) {
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
    public Integer getIngredientId() { return ingredientId; }
    @NonNull
    public Integer getRecipeId() { return recipeId; }
}