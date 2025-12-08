package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Map;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DisplayNearbyStoreUseCase {
    public static class Store {
        public String name;
        public double lat;
        public double lon;
        public String address;

        public Store(String name, double lat, double lon, String address) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.address = address;
        }
    }

    public interface Callback {
        void onStoresLoaded(List<Store> stores);
        void onError(String error);
    }

    public void execute(Context context, double lat, double lon, Callback callback) {
        try {
            String query = "[out:json];" +
                    "(" +
                    "node[\"shop\"=\"supermarket\"](around:2000," + lat + "," + lon + ");" +
                    "node[\"shop\"=\"convenience\"](around:2000," + lat + "," + lon + ");" +
                    ");" +
                    "out;";

            String url = "https://overpass-api.de/api/interpreter?data=" + URLEncoder.encode(query, "UTF-8");

            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        List<Store> stores = parseOverpassResponse(response);
                        callback.onStoresLoaded(stores);
                    },
                    error -> callback.onError(error.toString()));

            queue.add(stringRequest);

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    private List<Store> parseOverpassResponse(String jsonResponse) {
        List<Store> stores = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray elements = root.getJSONArray("elements");

            for (int i = 0; i < elements.length(); i++) {
                JSONObject node = elements.getJSONObject(i);
                double lat = node.getDouble("lat");
                double lon = node.getDouble("lon");

                String name = "Unknown Store";
                String address = "Lat: " + lat + ", Lon: " + lon;

                if (node.has("tags")) {
                    JSONObject tags = node.getJSONObject("tags");
                    if (tags.has("name")) name = tags.getString("name");
                    if (tags.has("addr:street")) address = tags.getString("addr:street");
                }

                stores.add(new Store(name, lat, lon, address));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }
}