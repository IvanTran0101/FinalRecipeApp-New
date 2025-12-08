package vn.edu.tdtu.anhminh.myapplication.Data.Mapper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.InstructionDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;

public class InstructionMapper {
    private InstructionMapper() {
    }

    // DTO -> Entity (For syncing data from the API to Room)
    public static InstructionEntity toEntity(InstructionDTO dto){
        if(dto == null) return null;

        return new InstructionEntity(
                dto.getInstructionId(),
                dto.getRecipeId(),
                dto.getStepNumber(),
                dto.getInstruction()
        );
    }

    public static List<InstructionEntity> toEntityList(List<InstructionDTO> dtos) {
        if (dtos == null) return null;

        List<InstructionEntity> list = new ArrayList<>();
        for (InstructionDTO dto : dtos) {
            InstructionEntity entity = toEntity(dto);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    // Entity -> Model (Used by Repository to give clean data to Domain/UI)
    public static Instruction toModel(InstructionEntity entity){
        if (entity == null ) return null;

        Instruction model = new Instruction();

        model.setInstructionId(entity.getInstructionId());
        model.setRecipeId(entity.getRecipeId());
        model.setStepNumber(entity.getStepNumber());
        model.setInstruction(entity.getInstruction());

        return model;
    }

    public static List<Instruction> toModelList(List<InstructionEntity> entities) {
        if (entities == null) return null;

        List<Instruction> list = new ArrayList<>();
        for (InstructionEntity entity : entities) {
            Instruction model = toModel(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    // Model -> Entity (Saving/Updating from Domain/UI to Room)
    public static InstructionEntity toEntity(Instruction model) {
        if (model == null) return null;

        return new InstructionEntity(
                model.getInstructionId(),
                model.getRecipeId(),
                model.getStepNumber(),
                model.getInstruction()
        );
    }
    public static List<Instruction> toModelListFromDto(List<InstructionDTO> dtos, int recipeId) {
        List<Instruction> result = new ArrayList<>();
        if (dtos == null) return result;
        for (InstructionDTO dto : dtos) {
            Instruction ins = new Instruction();
            ins.setRecipeId(recipeId);
            ins.setStepNumber(dto.getStepNumber());
            ins.setInstruction(dto.getInstruction());
            result.add(ins);
        }
        return result;
    }
}