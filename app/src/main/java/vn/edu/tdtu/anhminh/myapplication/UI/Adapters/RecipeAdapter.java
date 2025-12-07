package vn.edu.tdtu.anhminh.myapplication.UI.Adapters;

import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private final OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private OnFavoriteClickListener favoriteClickListener;

    private boolean isSimpleMode = false;

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Recipe recipe);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.recipeList = recipeList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.isSimpleMode = false;
    }

    public RecipeAdapter(List<Recipe> recipeList, OnItemClickListener clickListener, boolean isSimpleMode) {
        this.recipeList = recipeList;
        this.clickListener = clickListener;
        this.longClickListener = null;
        this.isSimpleMode = isSimpleMode;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    public void setRecipes(List<Recipe> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isSimpleMode ? R.layout.item_recipe_for_mealplan : R.layout.item_recipe_card;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RecipeViewHolder(view, isSimpleMode);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);

        if (holder.title != null) {
            holder.title.setText(currentRecipe.getTitle());
        }

        if (isSimpleMode) {
            if (holder.pinIcon != null) {
                boolean isPinned = currentRecipe.getPinned() != null && currentRecipe.getPinned();
                holder.pinIcon.setVisibility(isPinned ? View.VISIBLE : View.GONE);
            }
            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(currentRecipe));
        } else {
            String imagePath = currentRecipe.getRecipeImage();
            if (holder.image != null) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    if (imagePath.startsWith("http")) {
                        // It's a URL, load from the web
                        Glide.with(holder.itemView.getContext())
                                .load(imagePath)
                                .placeholder(android.R.color.darker_gray)
                                .centerCrop()
                                .into(holder.image);
                    } else {
                        // It's a local file path, load from Uri
                        Glide.with(holder.itemView.getContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(android.R.color.darker_gray)
                                .centerCrop()
                                .into(holder.image);
                    }
                } else {
                    holder.image.setImageResource(android.R.color.darker_gray);
                }
            }

            if (holder.favoriteIcon != null) {
                if (currentRecipe.getPinned() != null && currentRecipe.getPinned()) {
                    holder.favoriteIcon.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
                }
                holder.favoriteIcon.setOnClickListener(v -> {
                    if (favoriteClickListener != null) favoriteClickListener.onFavoriteClick(currentRecipe);
                });
            }

            if (selectedItems.get(currentRecipe.getRecipeId(), false)) {
                if (holder.overlay != null) holder.overlay.setVisibility(View.VISIBLE);
                if (holder.checkIcon != null) holder.checkIcon.setVisibility(View.VISIBLE);
            } else {
                if (holder.overlay != null) holder.overlay.setVisibility(View.GONE);
                if (holder.checkIcon != null) holder.checkIcon.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(currentRecipe));
            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) longClickListener.onItemLongClick(currentRecipe);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    public void toggleSelection(int recipeId) {
        if (isSimpleMode) return;
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
        if (recipeList == null) return items;
        for (Recipe r : recipeList) {
            if (selectedItems.get(r.getRecipeId(), false)) {
                items.add(r);
            }
        }
        return items;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public View overlay;
        public ImageView checkIcon;
        public ImageView favoriteIcon;
        public ImageView pinIcon;

        public RecipeViewHolder(@NonNull View itemView, boolean isSimpleMode) {
            super(itemView);
            if (isSimpleMode) {
                title = itemView.findViewById(R.id.tv_recipe_title);
                pinIcon = itemView.findViewById(R.id.iv_pin_icon);
            } else {
                title = itemView.findViewById(R.id.txt_recipe_title);
                image = itemView.findViewById(R.id.img_recipe);
                overlay = itemView.findViewById(R.id.view_overlay);
                checkIcon = itemView.findViewById(R.id.icon_check);
                favoriteIcon = itemView.findViewById(R.id.iv_pin_icon);
            }
        }
    }
}
