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

        String imagePath = currentRecipe.getRecipeImage();

        if (imagePath != null && !imagePath.isEmpty()) {
            com.bumptech.glide.Glide.with(holder.itemView.getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.image);
        } else {
            // Manually set placeholder
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