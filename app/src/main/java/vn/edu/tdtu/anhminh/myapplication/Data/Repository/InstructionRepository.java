package vn.edu.tdtu.anhminh.myapplication.Data.Repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.InstructionDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Database.AppDatabase;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.IngredientMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.InstructionMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class InstructionRepository {
    private final InstructionDAO instructionDAO;

    public InstructionRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.instructionDAO = db.instructionDao();
    }

    private static class InsertInstructionTask extends android.os.AsyncTask<InstructionEntity, Void, Void> {
        private final InstructionDAO dao;
        InsertInstructionTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(InstructionEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.insert(entities[0]);
            }
            return null;
        }
    }

    private static class InsertInstructionsTask extends android.os.AsyncTask<java.util.List<InstructionEntity>, Void, Void> {
        private final InstructionDAO dao;
        InsertInstructionsTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(java.util.List<InstructionEntity>... lists) {
            if (lists != null && lists.length > 0 && lists[0] != null && !lists[0].isEmpty()) {
                dao.insertAll(lists[0]);
            }
            return null;
        }
    }

    private static class DeleteInstructionTask extends android.os.AsyncTask<InstructionEntity, Void, Void> {
        private final InstructionDAO dao;
        DeleteInstructionTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(InstructionEntity... entities) {
            if (entities != null && entities.length > 0) {
                dao.delete(entities[0]);
            }
            return null;
        }
    }

    private static class DeleteInstructionsByRecipeTask extends android.os.AsyncTask<Integer, Void, Void> {
        private final InstructionDAO dao;
        DeleteInstructionsByRecipeTask(InstructionDAO dao) { this.dao = dao; }
        @Override
        protected Void doInBackground(Integer... ids) {
            if (ids != null && ids.length > 0) {
                dao.deleteInstructionsByRecipeId(ids[0]);
            }
            return null;
        }
    }

    //instruction
    public LiveData<List<Instruction>> getInstructionsForRecipe(int recipeId) {
        return Transformations.map(
                instructionDAO.getInstructionsForRecipe(recipeId),
                InstructionMapper::toModelList
        );
    }

    // ---------------------------------------------------
    // INSTRUCTION: thêm 1 bước cho recipe
    // ---------------------------------------------------
    public void addInstruction(Instruction instruction) {
        if (instruction == null) return;

        InstructionEntity entity =
                InstructionMapper.toEntity(instruction);
        new InsertInstructionTask(instructionDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thêm nhiều bước cùng lúc
    // ---------------------------------------------------
    public void addInstructions(List<Instruction> instructions) {
        if (instructions == null || instructions.isEmpty()) return;
        List<InstructionEntity> entities = new ArrayList<>();
        for (Instruction instruction : instructions) {
            InstructionEntity entity = InstructionMapper.toEntity(instruction);
            if (entity != null) {
                entities.add(entity);
            }

        }
        if (!entities.isEmpty()){
            new InsertInstructionsTask(instructionDAO).execute(entities);
        }
    }

    // ---------------------------------------------------
    // INSTRUCTION: xóa 1 bước cụ thể
    // ---------------------------------------------------
    public void deleteInstruction(Instruction instruction) {
        if(instruction == null) return;
        InstructionEntity entity = InstructionMapper.toEntity(instruction);
        new DeleteInstructionTask(instructionDAO).execute(entity);
    }

    // ---------------------------------------------------
    // INSTRUCTION: thay toàn bộ steps của 1 recipe
    // (dùng khi user edit list step rồi bấm Save)
    // ---------------------------------------------------
    public void replaceInstructionsForRecipe(int recipeId, List<Instruction> newInstructions) {
        new DeleteInstructionsByRecipeTask(instructionDAO).execute(recipeId);
        if (newInstructions == null || newInstructions.isEmpty()) return;

        List<InstructionEntity> entities = new ArrayList<>();
        for (Instruction instruction : newInstructions){
            InstructionEntity entity = InstructionMapper.toEntity(instruction);
            if (entity != null) {
                entities.add(entity);
            }

        }
        if (!entities.isEmpty()){
            new InsertInstructionsTask(instructionDAO).execute(entities);
        }
    }
}
