package vn.edu.tdtu.anhminh.myapplication.UI;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Injection.init(this);
    }
}
