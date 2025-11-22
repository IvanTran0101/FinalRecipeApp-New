package vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO;

public class IngredientDTO {
    private Integer ingredientId;
    private Integer recipeId;
    private String name;
    private double quantity;
    private String unit;

    public IngredientDTO(){

    }

    public IngredientDTO(Integer ingredientId,
                         Integer recipeId,
                         String name,
                         double quantity,
                         String unit) {
        this.ingredientId = ingredientId;
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
