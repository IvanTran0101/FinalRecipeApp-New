package vn.edu.tdtu.anhminh.myapplication.UI.Home;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.RecipeAdapter;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_recipes);

        // 1. Create Dummy Data
        List<Recipe> myRecipes = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        RecipeAdapter adapter = new RecipeAdapter(myRecipes, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Navigation.findNavController(view).navigate(R.id.action_home_to_recipeDetail);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
