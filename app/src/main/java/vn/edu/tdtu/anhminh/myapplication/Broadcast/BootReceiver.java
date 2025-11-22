package vn.edu.tdtu.anhminh.myapplication.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            // Optional: notify user/device for debugging
            Toast.makeText(context, "Reboot detected — restoring recipe alarms...", Toast.LENGTH_SHORT).show();

            // TODO: Load saved alarms from SharedPreferences or database
            // TODO: For each saved alarm, call AlarmScheduler.schedule(...)
            // Example:
            // AlarmScheduler scheduler = new AlarmScheduler(context);
            // scheduler.schedule(savedTriggerTime, savedId, savedTitle, savedMessage);
        }
    }
}
