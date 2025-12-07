package vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// Import đầy đủ các DTO
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.IngredientDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.InstructionDTO;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;

public class RecipeApiService {
    // URL raw từ GitHub của bạn
    private static final String SAMPLE_URL = "https://raw.githubusercontent.com/IvanTran0101/FinalRecipeApp-New/main/app/src/main/recipes.json";

    private final RequestQueue queue;

    private final Context context;

    public RecipeApiService(Context context){
        this.context = context.getApplicationContext();
        this.queue = Volley.newRequestQueue(this.context);
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
                        callback.onSuccess(parseRecipes(recipes));
                    } catch (JSONException e) {
                        e.printStackTrace(); // In lỗi ra Logcat để dễ debug
                        if (!loadFallbackFromAssets(callback)) {
                            callback.onError("JSON parse error: " + e.getMessage());
                        }
                    }
                },
                volleyError -> {
                    volleyError.printStackTrace();
                    if (!loadFallbackFromAssets(callback)) {
                        callback.onError("Volley error: " + volleyError.getMessage());
                    }
                }
        );
        queue.add(req);
    }

    private boolean loadFallbackFromAssets(ApiCallback<List<RecipeDTO>> callback) {
        try {
            InputStream inputStream = context.getAssets().open("recipes.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int readBytes = inputStream.read(buffer);
            inputStream.close();

            if (readBytes > 0) {
                String json = new String(buffer);
                JSONObject root = new JSONObject(json);
                JSONArray recipes = root.getJSONArray("recipes");
                callback.onSuccess(parseRecipes(recipes));
                return true;
            }
            callback.onError("Local fallback JSON is empty");
        } catch (IOException | JSONException e) {
            callback.onError("Failed to read fallback recipes: " + e.getMessage());
        }
        return false;
    }

    private List<RecipeDTO> parseRecipes(JSONArray recipes) throws JSONException {
        List<RecipeDTO> list = new ArrayList<>();
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject json = recipes.getJSONObject(i);

            RecipeDTO dto = new RecipeDTO();

            // Map dữ liệu cơ bản
            dto.setRecipeId(json.getInt("recipe_id"));
            dto.setTitle(json.getString("title"));
            dto.setCalories(json.getInt("calories"));
            dto.setRecipeImage(json.getString("recipe_image"));
            dto.setCarb(json.getInt("carb"));
            dto.setFat(json.getInt("fat"));
            dto.setCategory(json.getString("category"));
            dto.setDietMode(json.getString("diet_mode"));
            // Kiểm tra null cho videoLink nếu cần, hoặc để mặc định như JSON
            dto.setVideoLink(json.optString("video_link", ""));
            dto.setProtein(json.getInt("protein"));
            dto.setPinned(json.getBoolean("is_pinned"));

            // Map Ingredients (Nguyên liệu)
            if (json.has("ingredients")) {
                JSONArray ingArr = json.getJSONArray("ingredients");
                List<IngredientDTO> ingList = new ArrayList<>();
                for (int j = 0; j < ingArr.length(); j++) {
                    JSONObject ingJson = ingArr.getJSONObject(j);
                    IngredientDTO ingDto = new IngredientDTO();

                    ingDto.setName(ingJson.getString("name"));
                    ingDto.setQuantity(ingJson.getDouble("quantity"));
                    ingDto.setUnit(ingJson.getString("unit"));
                    // Set tạm ID recipe cho ingredient nếu cần logic xử lý sau này
                    ingDto.setRecipeId(dto.getRecipeId());

                    ingList.add(ingDto);
                }
                dto.setIngredients(ingList);
            }

            // Map Instructions (Hướng dẫn)
            if (json.has("instructions")) {
                JSONArray instArr = json.getJSONArray("instructions");
                List<InstructionDTO> instList = new ArrayList<>();
                for (int k = 0; k < instArr.length(); k++) {
                    JSONObject instJson = instArr.getJSONObject(k);
                    InstructionDTO instDto = new InstructionDTO();

                    instDto.setStepNumber(instJson.getInt("step_number"));
                    instDto.setInstruction(instJson.getString("instruction"));
                    // Set tạm ID recipe
                    instDto.setRecipeId(dto.getRecipeId());

                    instList.add(instDto);
                }
                dto.setInstructions(instList);
            }

            list.add(dto);
        }
        return list;
    }
}