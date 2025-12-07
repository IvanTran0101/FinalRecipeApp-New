package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.IngredientAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class IngredientFragment extends DialogFragment {
    private RecipeViewModel viewModel;
    private IngredientAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FilterDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            params.width = (int) (displayMetrics.widthPixels * 0.70);

            params.height = (int) (displayMetrics.heightPixels * 0.50);

            window.setAttributes(params);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_close).setOnClickListener(v -> dismiss());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IngredientAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        if (getArguments() != null) {
            int recipeId = getArguments().getInt("recipe_id", -1);
            if (recipeId != -1) {
                viewModel.getIngredients(recipeId).observe(getViewLifecycleOwner(), ingredients -> {
                    if (ingredients != null) {
                        adapter = new IngredientAdapter(ingredients);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }
    }
}
