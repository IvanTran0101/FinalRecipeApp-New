package vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.TimerNofification;

import vn.edu.tdtu.anhminh.myapplication.Alarms.AlarmScheduler;

public class CancelCountdownTimerUseCase {
    private final AlarmScheduler alarmScheduler;

    public CancelCountdownTimerUseCase(AlarmScheduler alarmScheduler) {
        this.alarmScheduler = alarmScheduler;
    }

    public interface Callback {
        void onSuccess();
        void onError();
    }

    public void execute(int notificationId, Callback callback){
        try{
            alarmScheduler.cancel(notificationId);
            if (callback != null) callback.onSuccess();
        } catch (Exception e) {
            if (callback != null) callback.onError();
        }
    }
}
