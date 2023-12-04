package com.zybooks.timer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TimerWorker extends Worker {

    public final static String KEY_MILLISECONDS_REMAINING = "com.zybooks.timer.MILLIS_LEFT";

    public TimerWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Get remaining milliseconds from MainActivity
        Data inputData = getInputData();
        long remainingMillis = inputData.getLong(KEY_MILLISECONDS_REMAINING, 0);

        // Can't continue without remaining time
        if (remainingMillis == 0) {
            return Result.failure();
        }

        // Start a new TimerModel
        TimerModel timerModel = new TimerModel();
        timerModel.start(remainingMillis);

        // Create notification channel for all notifications
        createTimerNotificationChannel();

        while (timerModel.isRunning()) {
            try {
                // New notification shows remaining time
                createTimerNotification(timerModel.toString());

                // Wait one second
                Thread.sleep(1000);

                if (timerModel.getRemainingMilliseconds() == 0) {
                    timerModel.stop();

                    // Last notification
                    createTimerNotification("Timer is finished!");
                }
            }
            catch (InterruptedException e) {
                // Ignore
            }
        }

        return Result.success();
    }

    private void createTimerNotificationChannel() {
        // TODO: Create a notification channel
    }

    private void createTimerNotification(String text) {
        // TODO: Create a notification
    }
}