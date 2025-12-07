package vn.edu.tdtu.anhminh.myapplication.Data.Mapper;


import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;

public class RecipeMapper {
    private RecipeMapper() {

    }

    //dto-> entity (sync sample tu API xuong Room)
    public static RecipeEntity toEntity(RecipeDTO dto){
        if(dto == null) return null;

        // Always assign recipes from JSON to user with ID 1 (admin)
        Integer userId = 1;

        RecipeEntity entity = new RecipeEntity(
                dto.getRecipeId(),
                userId, // Hardcoded to 1
                dto.getTitle(),
                dto.getRecipeImage(),
                dto.getCategory(),
                dto.getDietMode(),
                dto.getVideoLink(),
                dto.getCalories() != null ? dto.getCalories().doubleValue() : null,
                dto.getProtein() != null ? dto.getProtein().doubleValue() : null,
                dto.getCarb() != null ? dto.getCarb().doubleValue() : null,
                dto.getFat() != null ? dto.getFat().doubleValue() : null,
                dto.getPinned() != null ? dto.getPinned() : false
        );
        return entity;
    }

    public static List<RecipeEntity> toEntityList(List<RecipeDTO> dtos) {
        if (dtos == null) return null;
        List<RecipeEntity> list = new ArrayList<>();
        for (RecipeDTO dto :dtos) {
            RecipeEntity entity = toEntity(dto);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    //entity -> model (UI dung model, not entity)
    public static Recipe toModel(RecipeEntity entity){
        if (entity == null ) return null;

        Recipe model = new Recipe();
        model.setRecipeId(entity.getRecipeId());
        model.setUserId(entity.getUserId());
        model.setTitle(entity.getTitle());
        model.setRecipeImage(entity.getRecipeImage());
        model.setCategory(entity.getCategory());
        model.setDietMode(entity.getDietMode());
        model.setVideoLink(entity.getVideoLink());

        if (entity.getCalories() != null) {
            model.setCalories(entity.getCalories());
        }
        if (entity.getFat() != null) {
            model.setFat(entity.getFat());
        }
        if (entity.getCarb() != null) {
            model.setCarb(entity.getCarb());
        }
        if (entity.getProtein() != null) {
            model.setProtein(entity.getProtein());
        }
        model.setPinned(entity.getIsPinned());
        return model;
    }

    public static List<Recipe> toModelList(List<RecipeEntity> entities) {
        if (entities == null) return null;

        List<Recipe> list = new ArrayList<>();
        for (RecipeEntity entity : entities) {
            Recipe model = toModel(entity);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }

    //model -> entity
    public static RecipeEntity toEntity(Recipe model) {
        if (model == null) return null;
        return new RecipeEntity(
                model.getRecipeId(),
                model.getUserId(),
                model.getTitle(),
                model.getRecipeImage(),
                model.getCategory(),
                model.getDietMode(),
                model.getVideoLink(),
                model.getCalories() != null ? model.getCalories().doubleValue() : null,
                model.getProtein() != null ? model.getProtein().doubleValue() : null,
                model.getCarb() != null ? model.getCarb().doubleValue() : null,
                model.getFat() != null ? model.getFat().doubleValue() : null,
                model.getPinned()
        );
    }

}
