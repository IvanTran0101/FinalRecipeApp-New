package vn.edu.tdtu.anhminh.myapplication.Data.Mapper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.PlanDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Plan;

public class PlanMapper {

    private PlanMapper() {
    }

    // DTO -> Entity (For syncing data from the API to Room)
    public static PlanEntity toEntity(PlanDTO dto){
        if(dto == null) return null;
        return new PlanEntity(
                dto.getWeekId(),
                dto.getRecipeId(),
                dto.getUserId(),
                dto.getWeekNumber(),
                dto.getWeekDay()
        );
    }

    public static List<PlanEntity> toEntityList(List<PlanDTO> dtos) {
        if (dtos == null) return null;

        List<PlanEntity> list = new ArrayList<>();
        for (PlanDTO dto : dtos) {
            PlanEntity entity = toEntity(dto);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    // Entity -> Model (Used by Repository to give clean data to Domain/UI)
    public static Plan toModel(PlanEntity entity){
        if (entity == null ) return null;

        Plan model = new Plan();

        model.setWeekId(entity.getWeekId());
        model.setRecipeId(entity.getRecipeId());
        model.setUserId(entity.getUserId());
        model.setWeekNumber(entity.getWeekNumber());
        model.setWeekDay(entity.getWeekDay());

        return model;
    }

    public static List<Plan> toModelList(List<PlanEntity> entities) {
        if (entities == null) return null;

        List<Plan> list = new ArrayList<>();
        for (PlanEntity entity : entities) {
            Plan model = toModel(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    // Model -> Entity (Saving/Updating from Domain/UI to Room)
    public static PlanEntity toEntity(Plan model) {
        if (model == null) return null;

        return new PlanEntity(
                model.getWeekId(),
                model.getRecipeId(),
                model.getUserId(),
                model.getWeekNumber(),
                model.getWeekDay()
        );
    }
}