package vn.edu.tdtu.anhminh.myapplication.UI.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tdtu.anhminh.myapplication.R;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Get the Navigation Host
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            // Connect Bottom Navigation to the Controller
            NavigationUI.setupWithNavController(navView, navController);

            handleNotificationNavigation(navController);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            handleNotificationNavigation(navHostFragment.getNavController());
        }
    }

    private void handleNotificationNavigation(NavController navController) {
        if (getIntent() != null && getIntent().hasExtra("NAVIGATE_TO")) {
            String destination = getIntent().getStringExtra("NAVIGATE_TO");

            if ("COOKING_TIMER".equals(destination)) {
                // 3. Use the ID from your nav_graph.xml
                navController.navigate(R.id.cookingTimerFragment);

                // Clear the extra so rotating the screen doesn't re-trigger it
                getIntent().removeExtra("NAVIGATE_TO");
            }
        }
    }
}