package vn.edu.tdtu.anhminh.myapplication.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.edu.tdtu.anhminh.myapplication.Notifications.Notifier;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);

        if (title == null || title.isEmpty()){
            title = "Recipe reminder";
        }
        if (message == null || message.isEmpty()) {
            message = "Your timer is finished";
        }

        Notifier notifier = new Notifier(context);
        notifier.notify(notificationId, title, message);
    }
}
