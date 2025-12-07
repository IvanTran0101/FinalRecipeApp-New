package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

import java.util.List;

public class Recipe {
    private Integer recipeId;
    private Integer userId;

    private String title;
    private String recipeImage;
    private String category;
    private String dietMode;
    private String videoLink; //uri

    private Double calories;
    private Double protein;
    private Double carb;
    private Double fat;

    //pin/unpin
    private Boolean isPinned;

    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    public Recipe(){

    }
    public Recipe(Integer recipeId,
                  Integer userId,
                  String title,
                  String recipeImage,
                  String category,
                  String dietMode,
                  String videoLink,
                  Double calories,
                  Double protein,
                  Double carb,
                  Double fat,
                  Boolean isPinned,
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
        this.isPinned = isPinned;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarb() {
        return carb;
    }

    public void setCarb(Double carb) {
        this.carb = carb;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Boolean getPinned() {
        return isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
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
