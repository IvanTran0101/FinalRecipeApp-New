package vn.edu.tdtu.anhminh.myapplication.UI.MealPlan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tdtu.anhminh.myapplication.R;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // 2. Setup RecyclerView (Skeleton)
        RecyclerView recyclerView = findViewById(R.id.recycler_shopping_list);
        // TODO: Initialize Adapter here later
        // ShoppingAdapter adapter = new ShoppingAdapter(...);
        // recyclerView.setAdapter(adapter);
    }
}