package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.TimerNofification;

import vn.edu.tdtu.anhminh.myapplication.Alarms.AlarmScheduler;

public class StartCountdownTimerUseCase {
    private final AlarmScheduler alarmScheduler;

    public StartCountdownTimerUseCase(AlarmScheduler alarmScheduler) {
        this.alarmScheduler = alarmScheduler;
    }

    public interface Callback {
        void onSuccess();
        void onError();
    }

    public void execute(long durationMillis,
                        int notificationId,
                        String title,
                        String message,
                        Callback callback){
        try {
            long triggerAt =
                    System.currentTimeMillis() + durationMillis;
            alarmScheduler.schedule(triggerAt,
                    notificationId,title,message);
            if (callback != null) {
                callback.onSuccess();
            }
        }catch (Exception e){
            if (callback != null) callback.onError();
        }
    }
}
