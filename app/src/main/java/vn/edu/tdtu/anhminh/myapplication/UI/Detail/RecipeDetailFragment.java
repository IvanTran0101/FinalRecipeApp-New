package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
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
    private Recipe currentRecipe;
    private List<Ingredient> currentIngredients = new ArrayList<>();
    private List<Instruction> currentInstructions = new ArrayList<>();
    private ImageView ivRecipeImage;
    private YouTubePlayerView youTubePlayerView;
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
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);

        btnClose.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        setupChildNavigation(view);

        FloatingActionButton fab = view.findViewById(R.id.fab_recipe_options);
        fab.setOnClickListener(this::showPopupMenu);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                if (isTogglingFavorite) {
                    isTogglingFavorite = false;
                } else {
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

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            if (enter) {
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            } else {
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

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
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
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
                copyCurrentRecipe();
                return true;

            } else if (itemId == R.id.action_delete) {
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
                this.currentRecipe = recipe;
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
                            .load(imagePath)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.darker_gray)
                            .centerCrop()
                            .into(ivRecipeImage);
                } else {
                    ivRecipeImage.setImageResource(android.R.color.darker_gray);
                }

                String videoLink = recipe.getVideoLink();
                String videoId = NetworkUtils.getYouTubeId(videoLink);

                View cardVideo = getView().findViewById(R.id.card_video);

                if (videoId != null && !videoId.isEmpty()) {
                    cardVideo.setVisibility(View.VISIBLE);

                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
                } else {
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
