package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

@Entity(tableName = "Instruction", foreignKeys = @ForeignKey(
        entity = RecipeEntity.class,     // The PARENT table entity
        parentColumns = "recipe_id",     // PK column name in the PARENT
        childColumns = "recipe_id",      // FK column name in the CHILD
        onDelete = ForeignKey.CASCADE    // If a Recipe is deleted, delete all its Instructions.
        ),
        indices = {@androidx.room.Index(value = {"recipe_id", "step_number"}, unique = true)}
)
public class InstructionEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "instruction_id")
    private Integer instructionId;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private Integer recipeId;

    @NonNull
    @ColumnInfo(name = "step_number")
    private Integer stepNumber;

    @NonNull
    @ColumnInfo(name = "instruction")
    private String instruction;

    // -------------------------------------------------------------------
    // --- CONSTRUCTOR (REQUIRED by Room) ---
    // -------------------------------------------------------------------
    public InstructionEntity(
            @NonNull Integer instructionId,
            @NonNull Integer recipeId,
            @NonNull Integer stepNumber,
            @NonNull String instruction) {

        this.instructionId = instructionId;
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }

    @NonNull
    public Integer getInstructionId() { return instructionId; }
    public void setInstructionId(@NonNull Integer instructionId) { this.instructionId = instructionId; }

    @NonNull
    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(@NonNull Integer recipeId) { this.recipeId = recipeId; }

    @NonNull
    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(@NonNull Integer stepNumber) { this.stepNumber = stepNumber; }

    @NonNull
    public String getInstruction() { return instruction; }
    public void setInstruction(@NonNull String instruction) { this.instruction = instruction; }
}