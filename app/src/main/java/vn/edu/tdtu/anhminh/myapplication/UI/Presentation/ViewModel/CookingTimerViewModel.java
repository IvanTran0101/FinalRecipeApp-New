package vn.edu.tdtu.anhminh.myapplication.UI.Presentation.ViewModel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Locale;

import vn.edu.tdtu.anhminh.myapplication.Alarms.AlarmScheduler;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.TimerNofification.CancelCountdownTimerUseCase;
import vn.edu.tdtu.anhminh.myapplication.Domain.UseCase.TimerNofification.StartCountdownTimerUseCase;
import vn.edu.tdtu.anhminh.myapplication.Notifications.Channels;

public class CookingTimerViewModel extends AndroidViewModel {

    private final StartCountdownTimerUseCase startTimerUseCase;
    private final CancelCountdownTimerUseCase cancelTimerUseCase;

    private CountDownTimer uiTimer;
    private final int NOTIFICATION_ID = 1001;

    private final MutableLiveData<String> timeLeftText = new MutableLiveData<>("00:00:00");
    private final MutableLiveData<Integer> progress = new MutableLiveData<>(100);
    private final MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<>(false);

    public CookingTimerViewModel(@NonNull Application application) {
        super(application);

        AlarmScheduler alarmScheduler = new AlarmScheduler(application);

        this.startTimerUseCase = new StartCountdownTimerUseCase(alarmScheduler);
        this.cancelTimerUseCase = new CancelCountdownTimerUseCase(alarmScheduler);

        Channels.createChannels(application);
    }

    public LiveData<String> getTimeLeftText() { return timeLeftText; }
    public LiveData<Integer> getProgress() { return progress; }
    public LiveData<Boolean> getIsTimerRunning() { return isTimerRunning; }

    public void startCookingTimer(long durationMillis) {
        if (durationMillis <= 0) return;

        startTimerUseCase.execute(
                durationMillis,
                NOTIFICATION_ID,
                "Cooking Timer",
                "Your timer is finished! Let's check the food.",
                new StartCountdownTimerUseCase.Callback() {
                    @Override
                    public void onSuccess() {
                        // Log success if needed
                    }
                    @Override
                    public void onError() {
                        // Handle error (e.g. permission missing)
                    }
                }
        );

        startUITimer(durationMillis);
    }

    public void stopCookingTimer() {
        cancelTimerUseCase.execute(NOTIFICATION_ID, null);

        if (uiTimer != null) {
            uiTimer.cancel();
        }

        isTimerRunning.setValue(false);
        timeLeftText.setValue("00:00:00");
        progress.setValue(100);
    }

    private void startUITimer(long durationTotal) {
        isTimerRunning.setValue(true);

        uiTimer = new CountDownTimer(durationTotal, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;

                long hours = totalSeconds / 3600;
                long remainder = totalSeconds % 3600;
                long minutes = remainder / 60;
                long seconds = remainder % 60;

                String timeFormatted;
                if (hours > 0) {
                    timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                } else {
                    timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                }

                timeLeftText.setValue(timeFormatted);

                int progressPercent = (int) ((millisUntilFinished * 100) / durationTotal);
                progress.setValue(progressPercent);
            }

            @Override
            public void onFinish() {
                isTimerRunning.setValue(false);
                timeLeftText.setValue("Done!");
                progress.setValue(0);
            }
        }.start();
    }
}