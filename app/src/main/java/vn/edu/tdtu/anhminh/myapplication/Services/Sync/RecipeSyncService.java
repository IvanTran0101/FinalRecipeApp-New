package vn.edu.tdtu.anhminh.myapplication.Services.Sync;

import android.content.Context;
import android.util.Log;

import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.RecipeMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.IngredientMapper;
import vn.edu.tdtu.anhminh.myapplication.Data.Mapper.InstructionMapper;

import vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api.RecipeApiService;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.InstructionRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;

public class RecipeSyncService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final InstructionRepository instructionRepository;
    private final RecipeApiService api;
    private boolean isSyncing = false;

    public RecipeSyncService(Context context) {
        Context appContext = context.getApplicationContext();
        this.recipeRepository = new RecipeRepository(appContext);
        this.ingredientRepository = new IngredientRepository(appContext);
        this.instructionRepository = new InstructionRepository(appContext);
        this.api = new RecipeApiService(appContext);
    }

    public void syncSampleRecipes() {
        if (isSyncing) return;
        isSyncing = true;

        api.getSampleRecipe(new RecipeApiService.ApiCallback<List<RecipeDTO>>() {
            @Override
            public void onSuccess(List<RecipeDTO> dtos) {

                new Thread(() -> {
                    try {
                        if (dtos == null || dtos.isEmpty()) return;

                        for (RecipeDTO dto : dtos) {
                            RecipeEntity recipeEntity = RecipeMapper.toEntity(dto);
                            long recipeRowId = recipeRepository.addRecipe(
                                    RecipeMapper.toModel(recipeEntity)
                            );
                            int recipeId = recipeEntity.getRecipeId();

                            if (dto.getIngredients() != null) {
                                List<Ingredient> ingModels =
                                        IngredientMapper.toModelListFromDto(dto.getIngredients(), recipeId);
                                ingredientRepository.addMultipleIngredients(ingModels);
                            }

                            if (dto.getInstructions() != null) {
                                List<Instruction> insModels =
                                        InstructionMapper.toModelListFromDto(dto.getInstructions(), recipeId);
                                instructionRepository.addMultipleInstructions(insModels);
                            }
                        }

                    } finally {
                        isSyncing = false;
                    }
                }).start();
            }

            @Override
            public void onError(String error) {
                isSyncing = false;
            }
        });
    }
}
