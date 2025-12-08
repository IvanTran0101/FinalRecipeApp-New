package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref.UserPrefs;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Adapters.ShoppingListAdapter;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.MealPlanViewModel;

public class ShoppingListFragment extends Fragment {

    private MealPlanViewModel viewModel;
    private ShoppingListAdapter adapter;

    public ShoppingListFragment() {
        super(R.layout.fragment_shopping_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        viewModel = new ViewModelProvider(requireActivity()).get(MealPlanViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_shopping_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShoppingListAdapter();
        recyclerView.setAdapter(adapter);

        UserPrefs prefs = UserPrefs.getInstance(requireContext());
        int userId = prefs.getUserId();
        int weekId = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        viewModel.generateShoppingList(userId, weekId);

        viewModel.getShoppingList().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
        });
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0) {
            if (enter) {
                // When fragment is opening
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            } else {
                // When fragment is closing (popBackStack)
                return AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}