package vn.edu.tdtu.anhminh.myapplication.Data.Mapper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.IngredientDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;

public class IngredientMapper {
    private IngredientMapper(){
    }

    // DTO -> Entity (For syncing data from the API to Room)
    public static IngredientEntity toEntity(IngredientDTO dto){
        if(dto == null) return null;

        return new IngredientEntity(
                dto.getIngredientId(),
                dto.getRecipeId(),
                dto.getName(),
                dto.getQuantity(),
                dto.getUnit()
        );
    }

    public static List<IngredientEntity> toEntityList(List<IngredientDTO> dtos) {
        if (dtos == null) return null;

        List<IngredientEntity> list = new ArrayList<>();
        for (IngredientDTO dto : dtos) {
            IngredientEntity entity = toEntity(dto);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    // Entity -> Model (Used by Repository to give clean data to Domain/UI)
    public static Ingredient toModel(IngredientEntity entity){
        if (entity == null ) return null;

        Ingredient model = new Ingredient();

        model.setIngredientId(entity.getIngredientId());
        model.setRecipeId(entity.getRecipeId());
        model.setName(entity.getName());
        model.setQuantity(entity.getQuantity());
        model.setUnit(entity.getUnit());

        return model;
    }

    public static List<Ingredient> toModelList(List<IngredientEntity> entities) {
        if (entities == null) return null;

        List<Ingredient> list = new ArrayList<>();
        for (IngredientEntity entity : entities) {
            Ingredient model = toModel(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    // Model -> Entity (Saving/Updating from Domain/UI to Room)
    public static IngredientEntity toEntity(Ingredient model) {
        if (model == null) return null;

        return new IngredientEntity(
                model.getIngredientId(),
                model.getRecipeId(),
                model.getName(),
                model.getQuantity(),
                model.getUnit()
        );
    }
}