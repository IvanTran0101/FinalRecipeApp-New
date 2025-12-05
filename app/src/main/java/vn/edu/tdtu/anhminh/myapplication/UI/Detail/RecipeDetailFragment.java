package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
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
    private List<Ingredient> currentIngredients = new ArrayList<>();
    private List<Instruction> currentInstructions = new ArrayList<>();
    private ImageView ivRecipeImage;
    private WebView webViewVideo;
    private ImageButton ivFavoriteToggle;
    private boolean isTogglingFavorite = false;

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
        ivFavoriteToggle = view.findViewById(R.id.iv_favorite_toggle);
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
                if (isTogglingFavorite) {
                    // It was a favorite toggle, so don't pop back.
                    // Reset the flag.
                    isTogglingFavorite = false;
                } else {
                    // For other operations like delete or copy, pop back.
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        ivFavoriteToggle.setOnClickListener(v -> {
            if (currentRecipe != null) {
                isTogglingFavorite = true;
                viewModel.toggleFavorite(currentRecipe.getRecipeId());
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
            } else if (itemId == R.id.action_timer) {
                Navigation.findNavController(view).navigate(R.id.action_detail_to_cookingTimer);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void copyCurrentRecipe() {
        // 1. Copy Basic Info
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
        copy.setPinned(currentRecipe.getPinned());

        List<Ingredient> ingredientsCopy = new ArrayList<>();
        if (currentIngredients != null) {
            for (Ingredient ing : currentIngredients) {
                Ingredient newIng = new Ingredient();
                newIng.setName(ing.getName());
                newIng.setQuantity(ing.getQuantity());
                newIng.setUnit(ing.getUnit());
                ingredientsCopy.add(newIng);
            }
        }

        List<Instruction> instructionsCopy = new ArrayList<>();
        if (currentInstructions != null) {
            for (Instruction inst : currentInstructions) {
                Instruction newInst = new Instruction();
                newInst.setInstruction(inst.getInstruction());
                newInst.setStepNumber(inst.getStepNumber());
                instructionsCopy.add(newInst);
            }
        }

        viewModel.createRecipe(copy, ingredientsCopy, instructionsCopy);
        Toast.makeText(getContext(), "Recipe Copied to Home", Toast.LENGTH_SHORT).show();
    }

    private void loadData(int recipeId) {
        viewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                this.currentRecipe = recipe; // Save reference
                tvTitle.setText(recipe.getTitle());
                tvCategory.setText(recipe.getCategory());
                tvDiet.setText(recipe.getDietMode());

                if (recipe.getPinned() != null && recipe.getPinned()) {
                    ivFavoriteToggle.setImageResource(R.drawable.ic_favorite);
                } else {
                    ivFavoriteToggle.setImageResource(R.drawable.ic_favorite_border);
                }

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
                    String embedHtml = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "?autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>";

                    webViewVideo.loadData(embedHtml, "text/html", "utf-8");
                } else {
                    // Hide the video card if no valid link
                    cardVideo.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getIngredients(recipeId).observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null) {
                this.currentIngredients = new ArrayList<>(ingredients);
            }
        });

        viewModel.getInstructions(recipeId).observe(getViewLifecycleOwner(), instructions -> {
            if (instructions != null) {
                this.currentInstructions = new ArrayList<>(instructions);
            }
        });
    }
}