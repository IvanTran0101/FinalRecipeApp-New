package vn.edu.tdtu.anhminh.myapplication.Data.Remote.Api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import vn.edu.tdtu.anhminh.myapplication.Data.Remote.DTO.RecipeDTO;

public class RecipeApiService {
    private static final String API_URL = "https://raw.githubusercontent.com/IvanTran0101/FinalRecipeApp-New/refs/heads/main/app/src/main/recipes.json";
    private final RequestQueue requestQueue;

    public RecipeApiService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public void getSampleRecipe(ApiCallback<List<RecipeDTO>> callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL,
                response -> {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, List<RecipeDTO>>>(){}.getType();
                        Map<String, List<RecipeDTO>> map = gson.fromJson(response, type);
                        List<RecipeDTO> recipeList = map.get("recipes");

                        if (recipeList != null) {
                            callback.onSuccess(recipeList);
                        } else {
                            callback.onError("JSON Parsing Error: 'recipes' key not found or data is malformed.");
                        }
                    } catch (Exception e) {
                        callback.onError("JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> {
                    // Handle network error
                    callback.onError(error.getMessage());
                }
        );

        // Disable cache for this request to always get fresh data
        stringRequest.setShouldCache(false);

        requestQueue.add(stringRequest);
    }
}
