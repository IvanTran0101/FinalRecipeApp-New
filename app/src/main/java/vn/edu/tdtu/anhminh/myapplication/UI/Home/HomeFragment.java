package vn.edu.tdtu.anhminh.myapplication.UI.Home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.RecipeAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Filter.FilterFragment;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class HomeFragment extends Fragment {

    private RecipeViewModel viewModel;
    private RecipeAdapter favoritesAdapter;
    private RecipeAdapter otherRecipesAdapter;
    private ActionMode actionMode;
    private EditText searchBar;
    private ImageView filterButton;
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_home_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<Recipe> selectedRecipes = getSelectedRecipes();
            int itemId = item.getItemId();

            if (itemId == R.id.action_delete) {
                for (Recipe recipe : selectedRecipes) {
                    viewModel.deleteRecipe(recipe);
                }
                Toast.makeText(getContext(), "Recipes Deleted", Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            } else if (itemId == R.id.action_copy) {
                for (Recipe recipe : selectedRecipes) {
                    copyMultipleRecipes(recipe);
                }
                Toast.makeText(getContext(), "Recipes Copied", Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            } else if (itemId == R.id.action_pin) {
                for (Recipe recipe : selectedRecipes) {
                    viewModel.toggleFavorite(recipe.getRecipeId());
                }
                Toast.makeText(getContext(), "Favorites Updated", Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            favoritesAdapter.clearSelection();
            otherRecipesAdapter.clearSelection();
        }
    };

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup UI components
        setupRecyclerViews(view);
        setupSearchAndFilter(view);
        setupViewModel();

        View fab = view.findViewById(R.id.fab_add);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_home_to_recipeForm);
            });
        }
    }

    private void setupRecyclerViews(View view) {
        // Setup Favorites RecyclerView
        RecyclerView favoritesRecyclerView = view.findViewById(R.id.recycler_favorites);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoritesAdapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeClick, this::onRecipeLongClick);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Setup Other Recipes RecyclerView
        RecyclerView otherRecipesRecyclerView = view.findViewById(R.id.recycler_recipes);
        otherRecipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        otherRecipesAdapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeClick, this::onRecipeLongClick);
        otherRecipesRecyclerView.setAdapter(otherRecipesAdapter);
    }

    private void setupSearchAndFilter(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        filterButton = view.findViewById(R.id.btn_filter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        filterButton.setOnClickListener(v -> {
            FilterFragment filterFragment = new FilterFragment();
            filterFragment.show(getChildFragmentManager(), "FilterFragment");
        });
    }

    private void setupViewModel() {
        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(requireActivity(), factory).get(RecipeViewModel.class);

        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();

        viewModel.setCurrentUserId(userId);

        viewModel.getSearchResults().observe(getViewLifecycleOwner(), recipes -> {
            List<Recipe> favoriteRecipes = recipes.stream()
                    .filter(r -> r.getPinned() != null && r.getPinned())
                    .collect(Collectors.toList());

            List<Recipe> otherRecipes = recipes.stream()
                    .filter(r -> r.getPinned() == null || !r.getPinned())
                    .collect(Collectors.toList());

            favoritesAdapter.setRecipes(favoriteRecipes);
            otherRecipesAdapter.setRecipes(otherRecipes);
        });

        viewModel.search(""); // Initial search
    }

    private void onRecipeClick(Recipe recipe) {
        if (actionMode != null) {
            toggleSelection(recipe);
        } else {
            navigateToDetail(recipe);
        }
    }

    private void onRecipeLongClick(Recipe recipe) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(recipe);
    }

    private void toggleSelection(Recipe recipe) {
        favoritesAdapter.toggleSelection(recipe.getRecipeId());
        otherRecipesAdapter.toggleSelection(recipe.getRecipeId());

        int selectedCount = favoritesAdapter.getSelectedCount() + otherRecipesAdapter.getSelectedCount();

        if (selectedCount == 0 && actionMode != null) {
            actionMode.finish();
        } else if (actionMode != null) {
            actionMode.setTitle(selectedCount + " selected");
        }
    }

    private List<Recipe> getSelectedRecipes() {
        List<Recipe> selected = new ArrayList<>(favoritesAdapter.getSelectedItems());
        selected.addAll(otherRecipesAdapter.getSelectedItems());
        return selected;
    }

    private void navigateToDetail(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipe.getRecipeId());
        Navigation.findNavController(requireView()).navigate(R.id.action_home_to_recipeDetail, bundle);
    }

    private void copyMultipleRecipes(Recipe original) {
        Recipe copy = new Recipe();
        copy.setTitle(original.getTitle() + " (Copy)");
        if (original.getRecipeImage() != null) {
            copy.setRecipeImage(original.getRecipeImage());
        }
        copy.setCategory(original.getCategory());
        copy.setDietMode(original.getDietMode());
        copy.setVideoLink(original.getVideoLink());
        copy.setCalories(original.getCalories());
        copy.setProtein(original.getProtein());
        copy.setFat(original.getFat());
        copy.setCarb(original.getCarb());
        copy.setUserId(original.getUserId());
        viewModel.createRecipe(copy, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.search("");
        }
    }
}
