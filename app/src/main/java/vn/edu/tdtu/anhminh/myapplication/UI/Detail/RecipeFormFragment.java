package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class RecipeFormFragment extends Fragment {

    private RecipeViewModel viewModel;

    // UI Components
    private EditText etTitle, etIngName, etIngQty, etInstruction, etCal, etPro, etFat, etCarb, etVideo;
    private Spinner spCategory, spDiet;
    private Button btnSave;

    private List<Ingredient> tempIngredients = new ArrayList<>();
    private List<Instruction> tempInstructions = new ArrayList<>();

    public RecipeFormFragment() { super(R.layout.fragment_recipe_form); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup ViewModel
        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // 2. Init Views
        etTitle = view.findViewById(R.id.et_title);
        spCategory = view.findViewById(R.id.spinner_category);
        spDiet = view.findViewById(R.id.spinner_diet);

        etIngName = view.findViewById(R.id.et_ingredient_name);
        etIngQty = view.findViewById(R.id.et_ingredient_qty);

        etInstruction = view.findViewById(R.id.et_instruction_step);

        etCal = view.findViewById(R.id.et_cal);
        etPro = view.findViewById(R.id.et_pro);
        etFat = view.findViewById(R.id.et_fat);
        etCarb = view.findViewById(R.id.et_carb);
        etVideo = view.findViewById(R.id.et_video_link);

        btnSave = view.findViewById(R.id.btn_save_recipe);

        // 3. Observe Save Success
        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "Recipe Saved Successfully!", Toast.LENGTH_SHORT).show();
                // Navigate back to Home
                Navigation.findNavController(view).popBackStack();
            }
        });

        // 4. Handle Save Button
        btnSave.setOnClickListener(v -> {
            saveRecipe();
        });

        // Optional: Logic for "Add Ingredient" button would go here
        // view.findViewById(R.id.btn_add_ingredient).setOnClickListener(...)
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("Title required");
            return;
        }

        // A. Create Recipe Object
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setCategory(spCategory.getSelectedItem().toString());
        recipe.setDietMode(spDiet.getSelectedItem().toString());
        recipe.setVideoLink(etVideo.getText().toString());
        recipe.setUserId(1); // Set current User ID (Get from Prefs in real app)
        // Set Default Image for now
        // recipe.setRecipeImage(String.valueOf(R.drawable.ic_launcher_background));

        // Parse Nutrition
        try {
            if(!etCal.getText().toString().isEmpty()) recipe.setCalories(Double.parseDouble(etCal.getText().toString()));
            if(!etPro.getText().toString().isEmpty()) recipe.setProtein(Double.parseDouble(etPro.getText().toString()));
            if(!etFat.getText().toString().isEmpty()) recipe.setFat(Double.parseDouble(etFat.getText().toString()));
            if(!etCarb.getText().toString().isEmpty()) recipe.setCarb(Double.parseDouble(etCarb.getText().toString()));
        } catch (NumberFormatException e) {
            // Handle error
        }

        // B. Create Ingredients List (From the current input fields)
        String ingName = etIngName.getText().toString();
        String ingQty = etIngQty.getText().toString();
        if (!ingName.isEmpty()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingName);
            ingredient.setQuantity(Double.parseDouble(ingQty));
            tempIngredients.add(ingredient);
        }

        // C. Create Instruction List (From the current input field)
        String stepDesc = etInstruction.getText().toString();
        if (!stepDesc.isEmpty()) {
            Instruction instruction = new Instruction();
            instruction.setInstruction(stepDesc);
            instruction.setStepNumber(1);
            tempInstructions.add(instruction);
        }

        // D. Send to ViewModel
        viewModel.createRecipe(recipe, tempIngredients, tempInstructions);
    }
}