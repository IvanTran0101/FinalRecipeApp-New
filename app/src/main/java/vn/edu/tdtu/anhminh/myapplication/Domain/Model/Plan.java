package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

public class Plan {
    private String weekId;
    private String userId;
    private String recipeId;

    private int weekNumber;
    private int weekDay;

    public Plan(){

    }

    public Plan(String weekId, String userId, String recipeId, int weekNumber, int weekDay) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
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
