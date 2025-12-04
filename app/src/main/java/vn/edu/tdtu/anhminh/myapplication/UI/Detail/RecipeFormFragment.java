package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Ingredient;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Instruction;
import vn.edu.tdtu.anhminh.myapplication.Domain.Model.Recipe;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class RecipeFormFragment extends Fragment {
    private RecipeViewModel viewModel;
    private EditText etTitle, etCal, etPro, etFat, etCarb, etVideo;
    private Spinner spCategory, spDiet;
    private ChipGroup chipGroupIngredients;
    private Button btnAddIngredient;
    private Button btnSave;
    private LinearLayout instructionContainer;
    private Button btnAddStep;
    private boolean isEditMode = false;
    private int editingRecipeId = -1;
    private ImageView imgPreview;
    private String selectedImageUri = null;
    private ActivityResultLauncher<String> pickImageLauncher;
    private final List<Ingredient> tempIngredients = new ArrayList<>();

    public RecipeFormFragment() { super(R.layout.fragment_recipe_form); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String localPath = saveImageToInternalStorage(uri);

                if (localPath != null) {
                    selectedImageUri = localPath;
                    imgPreview.setImageURI(Uri.parse(selectedImageUri));
                    imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        initViews(view);

        imgPreview.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        if (getArguments() != null && getArguments().containsKey("recipe_id")) {
            editingRecipeId = getArguments().getInt("recipe_id", -1);
            if (editingRecipeId != -1) {
                isEditMode = true;
                btnSave.setText("Update Recipe");
                loadExistingData(editingRecipeId);
            }
        }
        if (!isEditMode) {
            addInstructionInput("");
        }

        btnAddIngredient.setOnClickListener(v -> showAddIngredientDialog());
        btnAddStep.setOnClickListener(v -> addInstructionInput(""));
        btnSave.setOnClickListener(v -> saveOrUpdateRecipe());

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "Recipe Saved Successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void initViews(View view) {
        etTitle = view.findViewById(R.id.et_title);
        imgPreview = view.findViewById(R.id.img_recipe_preview);
        spCategory = view.findViewById(R.id.spinner_category);
        spDiet = view.findViewById(R.id.spinner_diet);
        etCal = view.findViewById(R.id.et_cal);
        etPro = view.findViewById(R.id.et_pro);
        etFat = view.findViewById(R.id.et_fat);
        etCarb = view.findViewById(R.id.et_carb);
        etVideo = view.findViewById(R.id.et_video_link);
        btnSave = view.findViewById(R.id.btn_save_recipe);
        instructionContainer = view.findViewById(R.id.layout_instruction_container);
        btnAddStep = view.findViewById(R.id.btn_add_step);
        chipGroupIngredients = view.findViewById(R.id.chip_group_ingredients);
        btnAddIngredient = view.findViewById(R.id.btn_add_ingredient);
    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.et_dialog_ingredient_name);
        EditText etQty = dialogView.findViewById(R.id.et_dialog_ingredient_qty);
        Spinner spUnit = dialogView.findViewById(R.id.spinner_dialog_ingredient_unit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(adapter);

        builder.setTitle("Add Ingredient");
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String qtyStr = etQty.getText().toString().trim();
            String unit = spUnit.getSelectedItem().toString();

            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(getContext(), "Enter name and quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double qty = Double.parseDouble(qtyStr);
                Ingredient ingredient = new Ingredient();
                ingredient.setName(name);
                ingredient.setQuantity(qty);
                ingredient.setUnit(unit);
                tempIngredients.add(ingredient);
                addChipToGroup(ingredient);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void loadExistingData(int recipeId) {
        viewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                etTitle.setText(recipe.getTitle());
                if (recipe.getRecipeImage() != null && !recipe.getRecipeImage().isEmpty()) {
                    selectedImageUri = recipe.getRecipeImage();
                    try {
                        Uri uri = Uri.parse(selectedImageUri);
                        imgPreview.setImageURI(uri);
                        imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } catch (Exception e) {
                        e.printStackTrace();
                        imgPreview.setImageResource(R.drawable.ic_launcher_background);
                        selectedImageUri = null;
                    }
                } else {
                    imgPreview.setImageResource(R.drawable.ic_launcher_background);
                }
                etVideo.setText(recipe.getVideoLink());
                if(recipe.getCalories() != null) etCal.setText(String.valueOf(recipe.getCalories()));
                if(recipe.getProtein() != null) etPro.setText(String.valueOf(recipe.getProtein()));
                if(recipe.getFat() != null) etFat.setText(String.valueOf(recipe.getFat()));
                if(recipe.getCarb() != null) etCarb.setText(String.valueOf(recipe.getCarb()));
                setSpinnerSelection(spCategory, recipe.getCategory());
                setSpinnerSelection(spDiet, recipe.getDietMode());
            }
        });
        viewModel.getInstructions(recipeId).observe(getViewLifecycleOwner(), instructions -> {
            if (instructions != null) {
                instructionContainer.removeAllViews();
                for (Instruction item : instructions) {
                    addInstructionInput(item.getInstruction() != null ? item.getInstruction() : item.getInstruction());
                }
            }
        });

        viewModel.getIngredients(recipeId).observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null) {
                tempIngredients.clear();
                chipGroupIngredients.removeAllViews();
                for (Ingredient item : ingredients) {
                    tempIngredients.add(item);
                    addChipToGroup(item);
                }
            }
        });
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value == null || spinner.getAdapter() == null) return;

        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void saveOrUpdateRecipe() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("Required");
            etTitle.requestFocus();
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        if (selectedImageUri != null) {
            recipe.setRecipeImage(selectedImageUri);
        }
        recipe.setCategory(spCategory.getSelectedItem().toString());
        recipe.setDietMode(spDiet.getSelectedItem().toString());
        recipe.setVideoLink(etVideo.getText().toString());

        try {
            if(!etCal.getText().toString().isEmpty()) recipe.setCalories(Double.parseDouble(etCal.getText().toString()));
            if(!etPro.getText().toString().isEmpty()) recipe.setProtein(Double.parseDouble(etPro.getText().toString()));
            if(!etFat.getText().toString().isEmpty()) recipe.setFat(Double.parseDouble(etFat.getText().toString()));
            if(!etCarb.getText().toString().isEmpty()) recipe.setCarb(Double.parseDouble(etCarb.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers for nutrition.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Instruction> instructions = new ArrayList<>();
        int stepCount = 1;
        for (int i = 0; i < instructionContainer.getChildCount(); i++) {
            View child = instructionContainer.getChildAt(i);
            if (child instanceof EditText) {
                String text = ((EditText) child).getText().toString().trim();
                if (!text.isEmpty()) {
                    Instruction item = new Instruction();
                    item.setInstruction(text);
                    item.setStepNumber(stepCount++);
                    instructions.add(item);
                }
            }
        }

        if (isEditMode) {
            recipe.setRecipeId(editingRecipeId);
            UserPrefs prefs = UserPrefs.getInstance(requireContext());
            recipe.setUserId(prefs.getUserId());

            viewModel.updateRecipe(recipe, tempIngredients, instructions);
        } else {
            UserPrefs prefs = UserPrefs.getInstance(requireContext());
            recipe.setUserId(prefs.getUserId());

            viewModel.createRecipe(recipe, tempIngredients, instructions);
        }
    }

    private void addInstructionInput(String content) {
        int stepCount = instructionContainer.getChildCount() + 1;
        EditText etStep = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);
        etStep.setLayoutParams(params);
        etStep.setHint("Step " + stepCount + "...");
        etStep.setText(content);
        etStep.setPadding(30, 30, 30, 30);
        etStep.setBackgroundResource(android.R.drawable.edit_text);
        etStep.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etStep.setSingleLine(true);

        etStep.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                addInstructionInput("");
                return true;
            }
            return false;
        });

        instructionContainer.addView(etStep);
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            String fileName = "recipe_" + System.currentTimeMillis() + ".jpg";
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            File file = new File(requireContext().getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return Uri.fromFile(file).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addChipToGroup(Ingredient ingredient) {
        Chip chip = new Chip(getContext());
        chip.setText(ingredient.getName() + " : " + ingredient.getQuantity() + " " + ingredient.getUnit());
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);

        chip.setOnCloseIconClickListener(v -> {
            chipGroupIngredients.removeView(chip);
            tempIngredients.remove(ingredient);
        });

        chipGroupIngredients.addView(chip);
    }
}
