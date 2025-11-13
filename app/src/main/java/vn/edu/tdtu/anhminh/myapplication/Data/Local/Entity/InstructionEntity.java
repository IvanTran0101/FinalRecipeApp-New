package vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import java.util.UUID;

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
    private UUID instructionId;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private UUID recipeId;

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
            @NonNull UUID instructionId,
            @NonNull UUID recipeId,
            @NonNull Integer stepNumber,
            @NonNull String instruction) {

        this.instructionId = instructionId;
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }

    @NonNull
    public UUID getInstructionId() { return instructionId; }
    public void setInstructionId(@NonNull UUID instructionId) { this.instructionId = instructionId; }

    @NonNull
    public UUID getRecipeId() { return recipeId; }
    public void setRecipeId(@NonNull UUID recipeId) { this.recipeId = recipeId; }

    @NonNull
    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(@NonNull Integer stepNumber) { this.stepNumber = stepNumber; }

    @NonNull
    public String getInstruction() { return instruction; }
    public void setInstruction(@NonNull String instruction) { this.instruction = instruction; }
}