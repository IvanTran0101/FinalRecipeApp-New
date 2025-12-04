package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;
import vn.edu.tdtu.anhminh.myapplication.Utils.NetworkUtils;

public class RecipeDetailFragment extends Fragment {
    private RecipeViewModel viewModel;
    private TextView tvTitle, tvCategory, tvDiet;

    // Store current recipe to handle Copy/Delete
    private Recipe currentRecipe;
    private ImageView ivRecipeImage;
    private WebView webViewVideo;

    public RecipeDetailFragment() {
        super(R.layout.fragment_recipe_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tv_recipe_title);
        ivRecipeImage = view.findViewById(R.id.iv_recipe_image);
        tvCategory = view.findViewById(R.id.tv_category);
        tvDiet = view.findViewById(R.id.tv_diet);
        View btnClose = view.findViewById(R.id.btn_close_detail);
        webViewVideo = view.findViewById(R.id.webview_video);
        webViewVideo.getSettings().setJavaScriptEnabled(true); // REQUIRED for YouTube
        webViewVideo.setWebChromeClient(new WebChromeClient());

        btnClose.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        //Keep existing Navigation logic for child fragments
        setupChildNavigation(view);

        FloatingActionButton fab = view.findViewById(R.id.fab_recipe_options);
        fab.setOnClickListener(this::showPopupMenu);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // If we just deleted, close the screen
                Navigation.findNavController(view).popBackStack();
            }
        });

        if (getArguments() != null) {
            int recipeId = getArguments().getInt("recipe_id", -1);
            if (recipeId != -1) {
                loadData(recipeId);
            }
        }
    }

    // Helper to keep onViewCreated clean
    private void setupChildNavigation(View view) {
        View btnIngredients = view.findViewById(R.id.btn_ingredients);
        btnIngredients.setOnClickListener(v -> navigateToChild(view, R.id.action_detail_to_ingredient));

        View btnInstructions = view.findViewById(R.id.btn_instructions);
        btnInstructions.setOnClickListener(v -> navigateToChild(view, R.id.action_detail_to_instruction));

        View btnNutrition = view.findViewById(R.id.btn_nutrition);
        btnNutrition.setOnClickListener(v -> navigateToChild(view, R.id.action_detail_to_nutrition));
    }

    private void navigateToChild(View view, int actionId) {
        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle.putInt("recipe_id", getArguments().getInt("recipe_id", -1));
        }
        Navigation.findNavController(view).navigate(actionId, bundle);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.recipe_detail_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (currentRecipe == null) return false;

            int itemId = item.getItemId();

            if (itemId == R.id.action_edit) {
                Bundle bundle = new Bundle();
                bundle.putInt("recipe_id", currentRecipe.getRecipeId());
                Navigation.findNavController(view).navigate(R.id.action_detail_to_form, bundle);
                return true;

            } else if (itemId == R.id.action_copy) {
                //COPY: Duplicate logic
                copyCurrentRecipe();
                return true;

            } else if (itemId == R.id.action_delete) {
                //DELETE: Confirm dialog
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Recipe")
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            viewModel.deleteRecipe(currentRecipe);
                            Toast.makeText(getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void copyCurrentRecipe() {
        // Create a new object based on current data
        Recipe copy = new Recipe();
        copy.setTitle(currentRecipe.getTitle() + " (Copy)");
        if (currentRecipe.getRecipeImage() != null) {
            copy.setRecipeImage(currentRecipe.getRecipeImage());
        }
        copy.setCategory(currentRecipe.getCategory());
        copy.setDietMode(currentRecipe.getDietMode());
        copy.setVideoLink(currentRecipe.getVideoLink());
        copy.setCalories(currentRecipe.getCalories());
        copy.setProtein(currentRecipe.getProtein());
        copy.setFat(currentRecipe.getFat());
        copy.setCarb(currentRecipe.getCarb());
        copy.setUserId(currentRecipe.getUserId());

        // NOTE: Ideally, you should also fetch and copy ingredients/instructions here.
        // For now, this copies the main recipe data.
        viewModel.createRecipe(copy, null, null);
        Toast.makeText(getContext(), "Recipe Copied to Home", Toast.LENGTH_SHORT).show();
    }

    private void loadData(int recipeId) {
        viewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                this.currentRecipe = recipe; // Save reference
                tvTitle.setText(recipe.getTitle());
                tvCategory.setText(recipe.getCategory());
                tvDiet.setText(recipe.getDietMode());
                String imagePath = recipe.getRecipeImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    com.bumptech.glide.Glide.with(this)
                            .load(imagePath) // This cannot be null
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.darker_gray)
                            .centerCrop()
                            .into(ivRecipeImage); // Make sure ivRecipeImage is initialized!
                } else {
                    // If null, manually set placeholder WITHOUT Glide
                    ivRecipeImage.setImageResource(android.R.color.darker_gray);
                }
                String videoLink = recipe.getVideoLink();
                String videoId = NetworkUtils.getYouTubeId(videoLink);

                View cardVideo = getView().findViewById(R.id.card_video); // The container

                if (videoId != null) {
                    cardVideo.setVisibility(View.VISIBLE);

                    // The YouTube Embed Player HTML
                    String embedHtml = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>";

                    webViewVideo.loadData(embedHtml, "text/html", "utf-8");
                } else {
                    // Hide the video card if no valid link
                    cardVideo.setVisibility(View.GONE);
                }
            }
        });
    }
}