package vn.edu.tdtu.anhminh.myapplication.Alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import vn.edu.tdtu.anhminh.myapplication.Broadcast.AlarmReceiver;

public class AlarmScheduler {
    private final Context context;

    public AlarmScheduler(Context context) {
        this.context = context;
    }
    public void schedule(long triggerAtMillis, int notificationId, String title, String message){
        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title);

        intent.putExtra(AlarmReceiver.EXTRA_MESSAGE, message);

        intent.putExtra(AlarmReceiver.EXTRA_NOTIFICATION_ID,notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Android 12+ cần check quyền exact alarm
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    return;
                }
            }

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );
        }
    }

    public void cancel(int notificationId) {
        Intent intent = new Intent(context,
                AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }
}
