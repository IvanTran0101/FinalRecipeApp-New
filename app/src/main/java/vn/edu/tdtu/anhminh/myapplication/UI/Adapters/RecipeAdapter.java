package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private final OnItemClickListener clickListener;
    private final OnItemLongClickListener longClickListener;
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.recipeList = recipeList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

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
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            clickListener.onItemClick(currentRecipe);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(currentRecipe);
            }
            return true;
        });

        if (selectedItems.get(currentRecipe.getRecipeId(), false)) {
            // Show the overlay
            holder.overlay.setVisibility(View.VISIBLE);
            holder.checkIcon.setVisibility(View.VISIBLE); // Optional: Show checkmark
        } else {
            // Hide the overlay
            holder.overlay.setVisibility(View.GONE);
            holder.checkIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }

    public void toggleSelection(int recipeId) {
        boolean existsInList = recipeList.stream().anyMatch(r -> r.getRecipeId() == recipeId);

        if (existsInList) {
            if (selectedItems.get(recipeId, false)) {
                selectedItems.delete(recipeId);
            } else {
                selectedItems.put(recipeId, true);
            }
            notifyDataSetChanged();
        }
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedItems.size();
    }

    public List<Recipe> getSelectedItems() {
        List<Recipe> items = new ArrayList<>();
        for (int i = 0; i < recipeList.size(); i++) {
            if (selectedItems.get(recipeList.get(i).getRecipeId(), false)) {
                items.add(recipeList.get(i));
            }
        }
        return items;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public View overlay;
        public ImageView checkIcon;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_recipe_title);
            image = itemView.findViewById(R.id.img_recipe);
            overlay = itemView.findViewById(R.id.view_overlay);
            checkIcon = itemView.findViewById(R.id.icon_check);
        }
    }
}