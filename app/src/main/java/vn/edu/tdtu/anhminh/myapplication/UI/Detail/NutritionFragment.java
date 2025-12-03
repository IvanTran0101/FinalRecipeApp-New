
package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class NutritionFragment extends DialogFragment {

    private RecipeViewModel viewModel;
    private TextView tvCalories, tvProtein, tvCarbs, tvFat;
    private int recipeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCalories = view.findViewById(R.id.tv_calories);
        tvProtein = view.findViewById(R.id.tv_protein);
        tvCarbs = view.findViewById(R.id.tv_carbs);
        tvFat = view.findViewById(R.id.tv_fat);
        view.findViewById(R.id.btn_close).setOnClickListener(v -> dismiss());

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        if (getArguments() != null) {
            int recipeId = getArguments().getInt("recipe_id", -1);

            if (recipeId != -1) {
                loadNutritionData(recipeId);
            } else {
                Toast.makeText(getContext(), "Error: No Recipe ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadNutritionData(int recipeId) {
        viewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                String cal = recipe.getCalories() != null ? String.valueOf(recipe.getCalories()) : "--";
                String pro = recipe.getProtein() != null ? String.valueOf(recipe.getProtein()) + "g" : "--";
                String carb = recipe.getCarb() != null ? String.valueOf(recipe.getCarb()) + "g" : "--";
                String fat = recipe.getFat() != null ? String.valueOf(recipe.getFat()) + "g" : "--";

                tvCalories.setText("Calories: " + cal);
                tvProtein.setText("Protein: " + pro);
                tvCarbs.setText("Carbs: " + carb);
                tvFat.setText("Fat: " + fat);
            }
        });
    }
}
