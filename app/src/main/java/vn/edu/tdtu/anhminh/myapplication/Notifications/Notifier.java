package vn.edu.tdtu.anhminh.myapplication.Notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifier {
    private final Context context;

    public Notifier(Context context) {
        this.context = context;
    }

    public void notify(int id, String title, String message){
        Notification notification = new NotificationCompat.Builder(context, Channels.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build();
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(id, notification);
        }
    }

    public void notify(int id, String title, String message, PendingIntent intent){
        Notification notification = new NotificationCompat.Builder(context, Channels.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();

        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(id, notification);
        }
    }
}
