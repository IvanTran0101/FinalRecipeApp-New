package vn.edu.tdtu.anhminh.myapplication.UI.Detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation; // Changed to match your standard navigation style
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.InstructionAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Injection;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory;

public class InstructionFragment extends DialogFragment {

    private RecipeViewModel viewModel;
    private InstructionAdapter adapter;
    private int recipeId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_instructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InstructionAdapter();
        recyclerView.setAdapter(adapter);

        ViewModelFactory factory = Injection.provideViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        if (getArguments() != null) {
            this.recipeId = getArguments().getInt("recipe_id", -1);

            if (recipeId != -1) {
                viewModel.getInstructions(recipeId).observe(getViewLifecycleOwner(), instructions -> {
                    if (instructions != null) {
                        adapter.setInstructions(instructions);

                        // Optional: Debugging Toast if list is empty
                        if (instructions.isEmpty()) {
                            Toast.makeText(getContext(), "0 Instructions found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Error: Invalid Recipe ID", Toast.LENGTH_SHORT).show();
            }
        }

        view.findViewById(R.id.btn_close).setOnClickListener(v -> dismiss());

        Button btnStartCook = view.findViewById(R.id.btn_start_cooking); // Ensure ID matches XML
        btnStartCook.setOnClickListener(v -> {
            if (recipeId != -1) {
                Bundle args = new Bundle();
                args.putInt("recipe_id", recipeId);

                NavHostFragment.findNavController(this).navigate(R.id.action_instruction_to_startCooking, args);
                dismiss();
            }
        });
    }
}