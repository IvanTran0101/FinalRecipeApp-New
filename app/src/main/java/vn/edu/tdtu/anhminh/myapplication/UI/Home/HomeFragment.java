package vn.edu.tdtu.anhminh.myapplication.UI.Home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
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
    private SearchView searchView;
    private ImageView filterButton;

    // --- ACTION MODE CALLBACK ---
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

            if (selectedRecipes.isEmpty()) {
                mode.finish();
                return true;
            }

            if (itemId == R.id.action_delete) {
                // Xóa nhiều recipe
                for (Recipe recipe : selectedRecipes) {
                    viewModel.deleteRecipe(recipe);
                }
                return true; // Action Mode sẽ được đóng trong Observer khi thành công

            } else if (itemId == R.id.action_copy) {
                // Copy nhiều recipe
                for (Recipe recipe : selectedRecipes) {
                    fetchAndCopyRecipe(recipe);
                }
                Toast.makeText(getContext(), "Processing copy...", Toast.LENGTH_SHORT).show();
                mode.finish(); // Copy tốn thời gian lấy dữ liệu, nên đóng luôn UI chọn
                return true;

            } else if (itemId == R.id.action_pin) {
                // Ghim/Bỏ ghim nhiều recipe
                for (Recipe recipe : selectedRecipes) {
                    viewModel.toggleFavorite(recipe.getRecipeId());
                }
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

        // 1. Setup UI
        setupRecyclerViews(view);
        setupSearchAndFilter(view);

        // 2. Setup Logic
        setupViewModel();

        // 3. Setup FAB
        View fab = view.findViewById(R.id.fab_add);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_home_to_recipeForm);
            });
        }
    }

    private void setupRecyclerViews(View view) {
        // Favorites
        RecyclerView favoritesRecyclerView = view.findViewById(R.id.recycler_favorites);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        favoritesAdapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeClick, this::onRecipeLongClick);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Other Recipes
        RecyclerView otherRecipesRecyclerView = view.findViewById(R.id.recycler_recipes);
        otherRecipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        otherRecipesAdapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeClick, this::onRecipeLongClick);
        otherRecipesRecyclerView.setAdapter(otherRecipesAdapter);
    }

    private void setupSearchAndFilter(View view) {
        searchView = view.findViewById(R.id.search_view);
        filterButton = view.findViewById(R.id.btn_filter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                searchView.clearFocus(); // Ẩn bàn phím
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.search(newText);
                return false;
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

        // Set User ID context
        // For now, let's hardcode user ID 1 to see the sample recipes.
        // A proper implementation would get this from a login session.
        viewModel.setCurrentUserId(1);

        // 1. Observe Search Results
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes == null) return;

            List<Recipe> favoriteRecipes = recipes.stream()
                    .filter(r -> r.getPinned() != null && r.getPinned())
                    .collect(Collectors.toList());

            List<Recipe> otherRecipes = recipes.stream()
                    .filter(r -> r.getPinned() == null || !r.getPinned())
                    .collect(Collectors.toList());

            favoritesAdapter.setRecipes(favoriteRecipes);
            otherRecipesAdapter.setRecipes(otherRecipes);
        });

        // 2. Observe Operations Status (Success)
        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Nếu đang ở trong chế độ chọn (Action Mode), hãy đóng nó lại khi thao tác xong
                if (actionMode != null) {
                    actionMode.finish();
                    Toast.makeText(getContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                }
                // Reset status để tránh Toast hiện lại khi xoay màn hình
                viewModel.resetStatus();
            }
        });

        // 3. Observe Error Messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.resetStatus();
            }
        });

        // Initial load (Refresh list)
        viewModel.search("");
    }

    // --- INTERACTION LOGIC ---

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

    // --- COPY LOGIC (DEEP COPY) ---

    private void fetchAndCopyRecipe(Recipe original) {
        // Lấy Ingredients
        LiveData<List<Ingredient>> ingredientsLiveData = viewModel.getIngredients(original.getRecipeId());
        ingredientsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                ingredientsLiveData.removeObserver(this); // Quan trọng: Ngắt lắng nghe ngay sau khi lấy được

                // Lấy Instructions
                LiveData<List<Instruction>> instructionsLiveData = viewModel.getInstructions(original.getRecipeId());
                instructionsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Instruction>>() {
                    @Override
                    public void onChanged(List<Instruction> instructions) {
                        instructionsLiveData.removeObserver(this); // Ngắt lắng nghe

                        // Thực hiện copy
                        performDeepCopy(original, ingredients, instructions);
                    }
                });
            }
        });
    }

    private void performDeepCopy(Recipe original, List<Ingredient> ingredients, List<Instruction> instructions) {
        // 1. Copy thông tin cơ bản
        Recipe copy = new Recipe();
        copy.setTitle(original.getTitle() + " (Copy)");
        copy.setRecipeImage(original.getRecipeImage());
        copy.setCategory(original.getCategory());
        copy.setDietMode(original.getDietMode());
        copy.setVideoLink(original.getVideoLink());

        // Null checks
        copy.setCalories(original.getCalories() != null ? original.getCalories() : 0.0);
        copy.setProtein(original.getProtein() != null ? original.getProtein() : 0.0);
        copy.setFat(original.getFat() != null ? original.getFat() : 0.0);
        copy.setCarb(original.getCarb() != null ? original.getCarb() : 0.0);

        copy.setPinned(false); // Bản copy không nên tự động Pin

        // UserId sẽ được ViewModel tự set, nhưng set ở đây cũng an toàn
        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        copy.setUserId(prefs.getUserId());

        // 2. Copy Ingredients (Tạo object mới hoàn toàn)
        List<Ingredient> newIngredients = new ArrayList<>();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                Ingredient newIng = new Ingredient();
                newIng.setName(ing.getName());
                newIng.setQuantity(ing.getQuantity());
                newIng.setUnit(ing.getUnit());
                newIngredients.add(newIng);
            }
        }

        // 3. Copy Instructions (Tạo object mới hoàn toàn)
        List<Instruction> newInstructions = new ArrayList<>();
        if (instructions != null) {
            for (Instruction inst : instructions) {
                Instruction newInst = new Instruction();
                newInst.setInstruction(inst.getInstruction());
                newInst.setStepNumber(inst.getStepNumber());
                newInstructions.add(newInst);
            }
        }

        // 4. Gọi ViewModel để lưu
        viewModel.createRecipe(copy, newIngredients, newInstructions);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Không cần gọi viewModel.search("") ở đây nếu ViewModel giữ trạng thái tốt.
        // Tuy nhiên, để đảm bảo list update khi quay lại từ màn hình Detail (ví dụ: đã xóa ở màn Detail),
        // ta có thể gọi lại search để refresh dữ liệu từ DB.
        if (viewModel != null) {
            // Dùng chuỗi rỗng để giữ nguyên bộ lọc hiện tại, chỉ refresh data
            viewModel.search(searchView != null ? searchView.getQuery().toString() : "");
        }
    }
}
