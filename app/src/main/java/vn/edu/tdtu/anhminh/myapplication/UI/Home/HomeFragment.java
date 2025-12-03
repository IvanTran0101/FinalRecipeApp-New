package vn.edu.tdtu.anhminh.myapplication.UI.Home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.RecipeAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class HomeFragment extends Fragment {

    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_recipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new RecipeAdapter(new ArrayList<>(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Bundle bundle = new Bundle();

                // 2. Pass the ID (Best practice: pass ID and reload from DB,
                // but passing the whole object is easier for now if Recipe implements Serializable)
                bundle.putInt("recipe_id", recipe.getRecipeId());

                // 3. Navigate with the Bundle
                Navigation.findNavController(view).navigate(R.id.action_home_to_recipeDetail, bundle);
            }
        });
        recyclerView.setAdapter(adapter);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();

        viewModel.setCurrentUserId(userId);

        viewModel.getSearchResults().observe(getViewLifecycleOwner(), recipes -> {
            adapter.setRecipes(recipes);
        });

        viewModel.search("");

        View fab = view.findViewById(R.id.fab_add);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_home_to_recipeForm);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this screen
        if(viewModel != null) {
            viewModel.search("");
        }
    }
}