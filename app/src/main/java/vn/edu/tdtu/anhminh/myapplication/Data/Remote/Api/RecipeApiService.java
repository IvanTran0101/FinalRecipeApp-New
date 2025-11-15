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

public class RecipeApiService {
    private static final String SAMPLE_URL = "https://raw.githubusercontent.com/username/repo/main/recipes.json";
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
