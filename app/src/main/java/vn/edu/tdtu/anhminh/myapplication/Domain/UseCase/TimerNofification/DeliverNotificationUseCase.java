package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.TimerNofification;

import android.app.PendingIntent;
import android.telecom.Call;

import vn.edu.tdtu.anhminh.myapplication.Notifications.Notifier;

public class DeliverNotificationUseCase {
    private final Notifier notifier;

    public DeliverNotificationUseCase(Notifier notifier) {
        this.notifier = notifier;
    }

    public interface Callback {
        void onSuccess();
        void onError();
    }

    public void execute(int notificationId,
                        String title,
                        String message,
                        Callback callback){
        try{
            notifier.notify(notificationId, title, message);
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onError();
        }
    }

    public void execute(int notificationId,
                        String title,
                        String message,
                        PendingIntent intent,
                        Callback callback){
        try{
            notifier.notify(notificationId, title, message, intent);
            if (callback != null) callback.onSuccess();

            }catch (Exception e){
            if (callback != null) callback.onError();
        }
    }
}
