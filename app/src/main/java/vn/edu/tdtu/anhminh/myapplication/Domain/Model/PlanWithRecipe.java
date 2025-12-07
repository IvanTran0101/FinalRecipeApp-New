package vn.edu.tdtu.anhminh.myapplication.Domain.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;

public class PlanWithRecipe {
    @Embedded
    public PlanEntity plan;

    @Relation(
            parentColumn = "recipe_id",
            entityColumn = "recipe_id",
            entity = RecipeEntity.class
    )
    public RecipeEntity recipe;
}