package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class RecipeDetailFragment extends Fragment {
    private RecipeViewModel viewModel;
    private TextView tvTitle, tvCategory, tvDiet;

    public RecipeDetailFragment() {
        super(R.layout.fragment_recipe_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tv_recipe_title);
        tvCategory = view.findViewById(R.id.tv_category);
        tvDiet = view.findViewById(R.id.tv_diet);

        View btnClose = view.findViewById(R.id.btn_close_detail);
        btnClose.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        View btnIngredients = view.findViewById(R.id.btn_ingredients);
        btnIngredients.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("recipe_id", getArguments().getInt("recipe_id", -1));
            }
            Navigation.findNavController(view).navigate(R.id.action_detail_to_ingredient, bundle);
        });

        View btnInstructions = view.findViewById(R.id.btn_instructions);
        btnInstructions.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("recipe_id", getArguments().getInt("recipe_id", -1));
            }
            Navigation.findNavController(view).navigate(R.id.action_detail_to_instruction, bundle);
        });

        View btnNutrition = view.findViewById(R.id.btn_nutrition);
        btnNutrition.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (getArguments() != null) {
                bundle.putInt("recipe_id", getArguments().getInt("recipe_id", -1));
            }
            Navigation.findNavController(view).navigate(R.id.action_detail_to_nutrition, bundle);
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_recipe_options);
        fab.setOnClickListener(this::showPopupMenu);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        if (getArguments() != null) {
            int recipeId = getArguments().getInt("recipe_id", -1);
            if (recipeId != -1) {
                loadData(recipeId);
            }
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.recipe_detail_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                // Handle Edit
                Toast.makeText(getContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_copy) {
                // Handle Copy
                Toast.makeText(getContext(), "Copy clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_timer) {
                // Handle Timer
                Toast.makeText(getContext(), "Timer clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_delete) {
                // Handle Delete
                Toast.makeText(getContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void loadData(int recipeId) {
        viewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                tvTitle.setText(recipe.getTitle());
                tvCategory.setText(recipe.getCategory());
                tvDiet.setText(recipe.getDietMode());
            } else {
                Toast.makeText(getContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
