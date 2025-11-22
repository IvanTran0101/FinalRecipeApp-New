package vn.edu.tdtu.anhminh.myapplication.Notifications;

import android.app.NotificationManager;
import android.os.Build;

public class Channels {
    public static final String CHANNEL_ID = "recipe_notifications";
    private static final String CHANNEL_NAME = "Recipe Alerts";
    private static final String CHANNEL_DESC = "Notifications for recipe timers and reminders";

    public static void createChannels(android.content.Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            android.app.NotificationManager manager = context.getSystemService(android.app.NotificationManager.class);
            if (manager != null){
                manager.createNotificationChannel(channel);
            }

        }
    }
}
