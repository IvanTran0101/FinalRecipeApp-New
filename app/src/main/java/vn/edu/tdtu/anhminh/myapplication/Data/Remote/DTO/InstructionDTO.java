package vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO;

public class InstructionDTO {
    private Integer instructionId;
    private Integer recipeId;
    private int stepNumber;
    private String instruction;

    public InstructionDTO(){

    }

    public InstructionDTO(Integer instructionId,
                          Integer recipeId,
                          int stepNumber,
                          String instruction) {
        this.instructionId = instructionId;
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }

    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
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
