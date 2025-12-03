package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, OnItemClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    // NEW METHOD: Updates the list and refreshes UI
    public void setRecipes(List<Recipe> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.title.setText(currentRecipe.getTitle());

        // Handle Image: If it's a default ID (integer), set it.
        // In the future, you will handle String URIs here.
        if (currentRecipe.getRecipeImage() != null) {
            // Check if it is a resource ID or a URI string
            // For now, we assume it's a resource ID or placeholder
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(currentRecipe);
        });
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_recipe_title);
            image = itemView.findViewById(R.id.img_recipe);
        }
    }
}