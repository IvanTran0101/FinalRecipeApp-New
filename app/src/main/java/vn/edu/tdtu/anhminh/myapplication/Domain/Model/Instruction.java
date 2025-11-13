package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

public class Instruction {
    private String instructionId;
    private String recipeId;
    private int stepNumber;
    private String instruction;

    public Instruction(){

    }

    public Instruction(String instructionId,
                       String recipeId,
                       int stepNumber,
                       String instruction) {
        this.instructionId = instructionId;
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
