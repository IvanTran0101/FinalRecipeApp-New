package vn.edu.tdtu.anhminh.myapplication.Data.Local.Pref;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefs {
    private static final String PREF_NAME = "vn.edu.tdtu.anhminh.recipe_app_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static volatile UserPrefs INSTANCE;
    private final SharedPreferences sharedPreferences;

    private UserPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static UserPrefs getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserPrefs.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserPrefs(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public void saveUserId(int id) {
        sharedPreferences.edit().putInt(KEY_USER_ID, id).apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearPrefs() {
        sharedPreferences.edit().clear().apply();
    }
}
