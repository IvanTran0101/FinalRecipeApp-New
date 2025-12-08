package vn.edu.tdtu.anhminh.myapplication.Data.Local.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.IngredientDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.InstructionDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.PlanDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.RecipeDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.DAO.UserDAO;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.IngredientEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.InstructionEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.PlanEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.RecipeEntity;
import vn.edu.tdtu.anhminh.myapplication.Data.Local.Entity.UserEntity;

@Database(entities = {
                RecipeEntity.class,
                UserEntity.class,
                PlanEntity.class,
                IngredientEntity.class,
                InstructionEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDao();
    public abstract UserDAO userDao();
    public abstract PlanDAO planDao();
    public abstract IngredientDAO ingredientDao();
    public abstract InstructionDAO instructionDao();

    private static volatile AppDatabase INSTANCE;

    private static final String DATABASE_NAME = "recipe_app_db";

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                UserDAO dao = INSTANCE.userDao();

                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPasswordHash("1234");
                dao.insert(admin);

                UserEntity user = new UserEntity();
                user.setUsername("user");
                user.setPasswordHash("1234");
                dao.insert(user);
            });
        }
    };
}
