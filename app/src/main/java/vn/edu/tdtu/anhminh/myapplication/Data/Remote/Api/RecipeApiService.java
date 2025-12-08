package vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.IngredientDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.InstructionDTO;

public class RecipeApiService {
    private static final String SAMPLE_URL = "https://raw.githubusercontent.com/IvanTran0101/FinalRecipeApp-New/refs/heads/main/app/src/main/recipes.json";
    private final RequestQueue queue;
    public RecipeApiService(Context context){
        this.queue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public interface ApiCallback<T>{
        void onSuccess(T data);
        void onError(String error);
    }

    public void getSampleRecipe(ApiCallback<List<RecipeDTO>> callback){
        StringRequest req = new StringRequest(
                Request.Method.GET,
                SAMPLE_URL,
                response -> {
                    try {
                        JSONObject root = new JSONObject(response);
                        JSONArray recipes =  root.getJSONArray("recipes");
                        List<RecipeDTO> list =  new ArrayList<>();
                        for (int i = 0; i< recipes.length(); i++){
                            JSONObject json = recipes.getJSONObject(i);

                            RecipeDTO dto = new RecipeDTO();
                            dto.setRecipeId(json.getInt("recipe_id"));
                            dto.setTitle(json.getString("title"));
                            dto.setCalories(json.getInt("calories"));
                            dto.setRecipeImage(json.getString("recipe_image"));
                            dto.setCarb(json.getInt("carb"));
                            dto.setFat(json.getInt("fat"));
                            dto.setCategory(json.getString("category"));
                            dto.setDietMode(json.getString("diet_mode"));
                            dto.setVideoLink(json.getString("video_link"));
                            dto.setProtein(json.getInt("protein"));
                            dto.setPinned(json.getBoolean("is_pinned"));

                            // Parse ingredients array if present
                            if (json.has("ingredients")) {
                                JSONArray ingredientsArray = json.getJSONArray("ingredients");
                                List<IngredientDTO> ingredientDTOs = new ArrayList<>();
                                for (int j = 0; j < ingredientsArray.length(); j++) {
                                    JSONObject ingJson = ingredientsArray.getJSONObject(j);
                                    IngredientDTO ingDto = new IngredientDTO();
                                    ingDto.setName(ingJson.getString("name"));
                                    // quantity in JSON may be int or double, use getDouble to cover both
                                    ingDto.setQuantity(ingJson.getDouble("quantity"));
                                    ingDto.setUnit(ingJson.getString("unit"));
                                    ingredientDTOs.add(ingDto);
                                }
                                dto.setIngredients(ingredientDTOs);
                            }

                            // Parse instructions array if present
                            if (json.has("instructions")) {
                                JSONArray instructionsArray = json.getJSONArray("instructions");
                                List<InstructionDTO> instructionDTOs = new ArrayList<>();
                                for (int k = 0; k < instructionsArray.length(); k++) {
                                    JSONObject insJson = instructionsArray.getJSONObject(k);
                                    InstructionDTO insDto = new InstructionDTO();
                                    insDto.setStepNumber(insJson.getInt("step_number"));
                                    insDto.setInstruction(insJson.getString("instruction"));
                                    instructionDTOs.add(insDto);
                                }
                                dto.setInstructions(instructionDTOs);
                            }

                            list.add(dto);

                        }
                        callback.onSuccess(list);
                    } catch (JSONException e) {
                        callback.onError("JSON parse error");
                    }
                },
                volleyError -> callback.onError("Volley error")
        );
        queue.add(req);
    }

}
