package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.RecipeAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

// 1. Extend BottomSheetDialogFragment
public class AddMealDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_DAY = "ARG_DAY";
    private RecipeViewModel viewModel;

    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe, Calendar day);
    }

    private OnRecipeSelectedListener listener;
    private Calendar selectedDay;
    private RecipeAdapter recipeAdapter;

    public void setOnRecipeSelectedListener(OnRecipeSelectedListener listener) {
        this.listener = listener;
    }

    public static AddMealDialogFragment newInstance(Calendar day) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY, day);
        AddMealDialogFragment fragment = new AddMealDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the Custom Slide-Up Theme
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            selectedDay = (Calendar) getArguments().getSerializable(ARG_DAY);
        }

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(requireActivity(), factory).get(RecipeViewModel.class);

        SearchView searchView = view.findViewById(R.id.search_view_recipes);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        recipeAdapter = new RecipeAdapter(new ArrayList<>(), recipe -> {
            if (listener != null) {
                listener.onRecipeSelected(recipe, selectedDay);
                dismiss();
            }
        }, true);
        recyclerView.setAdapter(recipeAdapter);

        viewModel.getSearchResults().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                List<Recipe> sortedList = recipes.stream()
                        .sorted((r1, r2) -> {
                            boolean p1 = r1.getPinned() != null && r1.getPinned();
                            boolean p2 = r2.getPinned() != null && r2.getPinned();
                            return Boolean.compare(p2, p1);
                        })
                        .collect(Collectors.toList());
                recipeAdapter.setRecipes(sortedList);
            }
        });

        viewModel.search("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.search(newText);
                return true;
            }
        });
    }
}