package vn.edu.tdtu.anhminh.myapplication.UI.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.tdtu.anhminh.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            gestureDetector = new GestureDetector(this, new SwipeGestureListener(navView));

            navView.setOnItemSelectedListener(item -> {
                NavOptions.Builder builder = new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(false);

                builder.setPopUpTo(navController.getGraph().getStartDestinationId(), false);

                Menu menu = navView.getMenu();
                int currentOrder = -1;
                int newOrder = -1;

                if (navController.getCurrentDestination() != null) {
                    for (int i = 0; i < menu.size(); i++) {
                        if (menu.getItem(i).getItemId() == navController.getCurrentDestination().getId()) {
                            currentOrder = i;
                        }
                        if (menu.getItem(i).getItemId() == item.getItemId()) {
                            newOrder = i;
                        }
                    }
                }

                if (currentOrder != -1 && newOrder != -1) {
                    if (newOrder > currentOrder) {
                        builder.setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right);
                    } else if (newOrder < currentOrder) {
                        builder.setEnterAnim(R.anim.slide_in_left)
                                .setExitAnim(R.anim.slide_out_right)
                                .setPopEnterAnim(R.anim.slide_in_right)
                                .setPopExitAnim(R.anim.slide_out_left);
                    }
                }

                try {
                    navController.navigate(item.getItemId(), null, builder.build());
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (navView.getMenu().findItem(destination.getId()) != null) {
                    navView.getMenu().findItem(destination.getId()).setChecked(true);
                }
            });

            handleNotificationNavigation(navController);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Disable Swipe on the Map Fragment
        NavDestination currentDest = navController != null ? navController.getCurrentDestination() : null;
        boolean isMapFragment = (currentDest != null && currentDest.getId() == R.id.navigation_groceries);
        boolean isStartCookingFragment = (currentDest != null && currentDest.getId() == R.id.startCookingFragment);

        if (gestureDetector != null && !isMapFragment && !isStartCookingFragment) {
            gestureDetector.onTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
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
                navController.navigate(R.id.cookingTimerFragment);
                getIntent().removeExtra("NAVIGATE_TO");
            }
        }
    }

    private static class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private final BottomNavigationView navView;

        SwipeGestureListener(BottomNavigationView navView) {
            this.navView = navView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null) return false;

            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        selectPreviousTab();
                    } else {
                        selectNextTab();
                    }
                    return true;
                }
            }
            return false;
        }

        private void selectNextTab() {
            Menu menu = navView.getMenu();
            int currentItemId = navView.getSelectedItemId();
            for (int i = 0; i < menu.size() - 1; i++) {
                if (menu.getItem(i).getItemId() == currentItemId) {
                    navView.setSelectedItemId(menu.getItem(i + 1).getItemId());
                    break;
                }
            }
        }

        private void selectPreviousTab() {
            Menu menu = navView.getMenu();
            int currentItemId = navView.getSelectedItemId();
            for (int i = 1; i < menu.size(); i++) {
                if (menu.getItem(i).getItemId() == currentItemId) {
                    navView.setSelectedItemId(menu.getItem(i - 1).getItemId());
                    break;
                }
            }
        }
    }
}