package vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO;

public class PlanDTO {
    private String weekId;
    private Integer userId;
    private Integer recipeId;

    private int weekNumber;
    private int weekDay;

    public PlanDTO(){

    }

    public PlanDTO(String weekId,
                   Integer userId,
                   Integer recipeId,
                   int weekNumber,
                   int weekDay) {
        this.weekId = weekId;
        this.userId = userId;
        this.recipeId = recipeId;
        this.weekNumber = weekNumber;
        this.weekDay = weekDay;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
}
