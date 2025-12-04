package vn.edu.tdtu.anhminh.myapplication.UI.Filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment; // Changed from BottomSheetDialogFragment
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

// Change inheritance to DialogFragment
public class FilterFragment extends DialogFragment {

    private RecipeViewModel viewModel;

    private CheckBox cbBreakfast, cbLunch, cbDinner, cbDessert, cbVegan, cbKeto;
    private EditText etCalMin, etCalMax, etCarbMin, etCarbMax, etProteinMin, etProteinMax, etFatMin, etFatMax;
    private Button btnApplyFilter;
    private TextView btnReset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    // This makes the Dialog look like a popup in the center
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // Set width to 90% of screen, Height to Wrap Content
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawableResource(android.R.color.transparent); // Optional: for rounded corners in XML
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        // Use requireActivity() so we share data with HomeFragment
        viewModel = new ViewModelProvider(requireActivity(), factory).get(RecipeViewModel.class);

        initViews(view);

        // Load previous data immediately when opening
        loadCurrentFilters();

        btnApplyFilter.setOnClickListener(v -> {
            applyFilters();
            dismiss();
        });

        btnReset.setOnClickListener(v -> {
            resetFilters();
            applyFilters();
            dismiss();
        });
    }

    private void initViews(View view) {
        cbBreakfast = view.findViewById(R.id.cb_breakfast);
        cbLunch = view.findViewById(R.id.cb_lunch);
        cbDinner = view.findViewById(R.id.cb_dinner);
        cbDessert = view.findViewById(R.id.cb_dessert);
        cbVegan = view.findViewById(R.id.cb_vegan);
        cbKeto = view.findViewById(R.id.cb_keto);

        etCalMin = view.findViewById(R.id.et_cal_min);
        etCalMax = view.findViewById(R.id.et_cal_max);
        etCarbMin = view.findViewById(R.id.et_carb_min);
        etCarbMax = view.findViewById(R.id.et_carb_max);
        etProteinMin = view.findViewById(R.id.et_protein_min);
        etProteinMax = view.findViewById(R.id.et_protein_max);
        etFatMin = view.findViewById(R.id.et_fat_min);
        etFatMax = view.findViewById(R.id.et_fat_max);

        btnApplyFilter = view.findViewById(R.id.btn_apply_filter);
        btnReset = view.findViewById(R.id.btn_reset);
    }

    private void loadCurrentFilters() {
        // Restore Checkboxes
        List<String> cats = viewModel.getCurrentCategories();
        if (cats != null) {
            cbBreakfast.setChecked(cats.contains("Breakfast"));
            cbLunch.setChecked(cats.contains("Lunch"));
            cbDinner.setChecked(cats.contains("Dinner"));
            cbDessert.setChecked(cats.contains("Dessert"));
        }

        List<String> modes = viewModel.getCurrentDietModes();
        if (modes != null) {
            cbVegan.setChecked(modes.contains("Vegan"));
            cbKeto.setChecked(modes.contains("Keto"));
        }

        // Restore Numbers (Helper function to safely convert Integer to String)
        setTextSafely(etCalMin, viewModel.getMinCalories());
        setTextSafely(etCalMax, viewModel.getMaxCalories());
        setTextSafely(etCarbMin, viewModel.getMinCarbs());
        setTextSafely(etCarbMax, viewModel.getMaxCarbs());
        setTextSafely(etProteinMin, viewModel.getMinProtein());
        setTextSafely(etProteinMax, viewModel.getMaxProtein());
        setTextSafely(etFatMin, viewModel.getMinFat());
        setTextSafely(etFatMax, viewModel.getMaxFat());
    }

    private void setTextSafely(EditText editText, Integer value) {
        if (value != null) {
            editText.setText(String.valueOf(value));
        } else {
            editText.setText("");
        }
    }

    private void applyFilters() {
        List<String> categories = new ArrayList<>();
        if (cbBreakfast.isChecked()) categories.add("Breakfast");
        if (cbLunch.isChecked()) categories.add("Lunch");
        if (cbDinner.isChecked()) categories.add("Dinner");
        if (cbDessert.isChecked()) categories.add("Dessert");

        List<String> dietModes = new ArrayList<>();
        if (cbVegan.isChecked()) dietModes.add("Vegan");
        if (cbKeto.isChecked()) dietModes.add("Keto");

        Integer minCalories = etCalMin.getText().toString().isEmpty() ? null : Integer.parseInt(etCalMin.getText().toString());
        Integer maxCalories = etCalMax.getText().toString().isEmpty() ? null : Integer.parseInt(etCalMax.getText().toString());
        Integer minCarbs = etCarbMin.getText().toString().isEmpty() ? null : Integer.parseInt(etCarbMin.getText().toString());
        Integer maxCarbs = etCarbMax.getText().toString().isEmpty() ? null : Integer.parseInt(etCarbMax.getText().toString());
        Integer minProtein = etProteinMin.getText().toString().isEmpty() ? null : Integer.parseInt(etProteinMin.getText().toString());
        Integer maxProtein = etProteinMax.getText().toString().isEmpty() ? null : Integer.parseInt(etProteinMax.getText().toString());
        Integer minFat = etFatMin.getText().toString().isEmpty() ? null : Integer.parseInt(etFatMin.getText().toString());
        Integer maxFat = etFatMax.getText().toString().isEmpty() ? null : Integer.parseInt(etFatMax.getText().toString());

        viewModel.setFilters(categories, dietModes, minCalories, maxCalories, minCarbs, maxCarbs, minProtein, maxProtein, minFat, maxFat);
    }

    private void resetFilters() {
        cbBreakfast.setChecked(false);
        cbLunch.setChecked(false);
        cbDinner.setChecked(false);
        cbDessert.setChecked(false);
        cbVegan.setChecked(false);
        cbKeto.setChecked(false);

        etCalMin.setText("");
        etCalMax.setText("");
        etCarbMin.setText("");
        etCarbMax.setText("");
        etProteinMin.setText("");
        etProteinMax.setText("");
        etFatMin.setText("");
        etFatMax.setText("");
    }
}