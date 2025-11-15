package vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO;

public class PlanDTO {
    private Integer planId;
    private Integer weekId;
    private Integer userId;
    private Integer recipeId;

    private int weekNumber;
    private int weekDay;

    public PlanDTO(){

    }

    public PlanDTO(Integer planId,
                   Integer weekId,
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

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getWeekId() {
        return weekId;
    }

    public void setWeekId(Integer weekId) {
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
