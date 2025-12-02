package vn.edu.tdtu.anhminh.myapplication.UI.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;

public class FilterFragment extends BottomSheetDialogFragment {

    private RecipeViewModel viewModel;

    // Helper to get text as Integer safely
    private Integer getIntFromEt(EditText et) {
        String text = et.getText().toString();
        if (text.isEmpty()) return null;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Get Shared ViewModel (Attached to Activity)
        viewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);

        // 2. Find Views
        Button btnApply = view.findViewById(R.id.btn_apply_filter);
        View btnReset = view.findViewById(R.id.btn_reset);

        // Checkboxes
        CheckBox cbBreakfast = view.findViewById(R.id.cb_breakfast);
        CheckBox cbLunch = view.findViewById(R.id.cb_lunch);
        CheckBox cbDinner = view.findViewById(R.id.cb_dinner);
        CheckBox cbDessert = view.findViewById(R.id.cb_dessert);
        CheckBox cbVegan = view.findViewById(R.id.cb_vegan);
        CheckBox cbKeto = view.findViewById(R.id.cb_keto);

        // Inputs
        EditText etCalMin = view.findViewById(R.id.et_cal_min);
        EditText etCalMax = view.findViewById(R.id.et_cal_max);
        EditText etCarbMin = view.findViewById(R.id.et_carb_min);
        EditText etCarbMax = view.findViewById(R.id.et_carb_max);

        // 3. Apply Logic
        btnApply.setOnClickListener(v -> {
            // A. Gather Categories
            List<String> selectedCategories = new ArrayList<>();
            if (cbBreakfast.isChecked()) selectedCategories.add("Breakfast");
            if (cbLunch.isChecked()) selectedCategories.add("Lunch");
            if (cbDinner.isChecked()) selectedCategories.add("Dinner");
            if (cbDessert.isChecked()) selectedCategories.add("Dessert");
            if (cbVegan.isChecked()) selectedCategories.add("Vegan");
            if (cbKeto.isChecked()) selectedCategories.add("Keto");

            // B. Gather Numbers
            Integer minCal = getIntFromEt(etCalMin);
            Integer maxCal = getIntFromEt(etCalMax);
            Integer minCarb = getIntFromEt(etCarbMin);
            Integer maxCarb = getIntFromEt(etCarbMax);

            // C. Send to ViewModel (You need to create this method in ViewModel!)
            // viewModel.applyFilters(selectedCategories, minCal, maxCal, minCarb, maxCarb);

            dismiss();
        });

        // 4. Reset Logic
        btnReset.setOnClickListener(v -> {
            // Clear all fields or tell ViewModel to clear filters
            // viewModel.clearFilters();
            dismiss();
        });
    }
}
