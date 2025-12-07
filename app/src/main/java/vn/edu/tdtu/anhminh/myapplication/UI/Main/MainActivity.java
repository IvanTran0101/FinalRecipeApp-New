package vn.edu.tdtu.anhminh.myapplication.UI.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // <-- 1. THÊM IMPORT
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.tdtu.anhminh.myapplication.Data.Repository.IngredientRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.InstructionRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.RecipeRepository;
import vn.edu.tdtu.anhminh.myapplication.Data.Repository.UserRepository;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.AuthenticateUserUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Account.UpdateAccountUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ManageRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SearchRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.SyncRecipesUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.Recipe.ToggleFavoriteRecipeUseCase;
import vn.edu.tdtu.anhminh.myapplication.R;
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.RecipeViewModel; // <-- 1. THÊM IMPORT
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.UserViewModel;   // <-- 1. THÊM IMPORT
import vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel.ViewModelFactory; // <-- 1. THÊM IMPORT

public class MainActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private NavController navController;

    // <-- 2. KHAI BÁO VIEWMODELS
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // <-- 3. GỌI HÀM KHỞI TẠO VIEWMODELS
        initViewModels();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        // ... phần còn lại của onCreate không thay đổi ...

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

    // <-- 4. THÊM PHƯƠNG THỨC KHỞI TẠO VIEWMODELS
    private void initViewModels() {
        // --- Khởi tạo Repository ---
        RecipeRepository recipeRepository = new RecipeRepository(getApplication());
        IngredientRepository ingredientRepository = new IngredientRepository(getApplication());
        InstructionRepository instructionRepository = new InstructionRepository(getApplication());
        UserRepository userRepository = new UserRepository(getApplication());

        // --- Khởi tạo tất cả các UseCase ---
        ManageRecipeUseCase manageRecipeUseCase = new ManageRecipeUseCase(recipeRepository, ingredientRepository, instructionRepository);
        SearchRecipesUseCase searchRecipesUseCase = new SearchRecipesUseCase(recipeRepository);
        ToggleFavoriteRecipeUseCase toggleFavoriteRecipeUseCase = new ToggleFavoriteRecipeUseCase(recipeRepository);
        SyncRecipesUseCase syncRecipesUseCase = new SyncRecipesUseCase(recipeRepository);

        AuthenticateUserUseCase authenticateUserUseCase = new AuthenticateUserUseCase(userRepository);
        UpdateAccountUseCase updateAccountUseCase = new UpdateAccountUseCase(userRepository);

        // --- Tạo Factory với đầy đủ các UseCase ---
        ViewModelFactory factory = new ViewModelFactory(
                manageRecipeUseCase,
                searchRecipesUseCase,
                toggleFavoriteRecipeUseCase,
                syncRecipesUseCase,
                authenticateUserUseCase,
                updateAccountUseCase
        );

        // --- Lấy ViewModel từ factory ---
        // ViewModelProvider sẽ đảm bảo ViewModel tồn tại qua các thay đổi cấu hình (như xoay màn hình)
        this.recipeViewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);
        this.userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        // Bây giờ, bạn có thể bắt đầu theo dõi dữ liệu từ ViewModel nếu cần
        // Ví dụ: Hiển thị loading bar khi đang đồng bộ
        recipeViewModel.isSyncing().observe(this, isSyncing -> {
            if (isSyncing) {
                // Hiển thị một ProgressBar
            } else {
                // Ẩn ProgressBar
            }
        });
    }

    // ... các phương thức còn lại không thay đổi ...

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        NavDestination currentDest = navController != null ? navController.getCurrentDestination() : null;
        boolean isMapFragment = (currentDest != null && currentDest.getId() == R.id.navigation_groceries);
        boolean isStartCookingFragment = (currentDest != null && currentDest.getId() == R.id.startCookingFragment);
        boolean isRecipeDetailFragment = (currentDest != null && currentDest.getId() == R.id.recipeDetailFragment);

        if (gestureDetector != null && !isMapFragment && !isStartCookingFragment && !isRecipeDetailFragment) {
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
