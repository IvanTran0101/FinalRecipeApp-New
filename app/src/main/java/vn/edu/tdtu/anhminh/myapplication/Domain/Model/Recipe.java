package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

import java.util.List;

public class Recipe {
    private String recipeId;
    private String userId;

    private String title;
    private String recipeImage; //uri duoi dang String
    private String category;
    private String dietMode;
    private String videoLink; //uri

    private Integer calories;
    private Integer protein;
    private Integer carb;
    private Integer fat;

    //pin/unpin
    private String status;

    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    public Recipe(){

    }
    public Recipe(String recipeId,
                  String userId,
                  String title,
                  String recipeImage,
                  String category,
                  String dietMode,
                  String videoLink,
                  Integer calories,
                  Integer protein,
                  Integer carb,
                  Integer fat,
                  String status,
                  List<Ingredient> ingredients,
                  List<Instruction> instructions){
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
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDietMode() {
        return dietMode;
    }

    public void setDietMode(String dietMode) {
        this.dietMode = dietMode;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarb() {
        return carb;
    }

    public void setCarb(Integer carb) {
        this.carb = carb;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }
}
