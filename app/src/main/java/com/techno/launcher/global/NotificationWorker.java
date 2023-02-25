package com.techno.launcher.global;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.techno.launcher.screens.HomeActivity;

public class NotificationWorker extends Worker {
    Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.i("Data", "Test is working fine");
            if (LiveMosqueSingleTone.getInstance().getPrayerTimes() != null) {
                ((HomeActivity) HomeActivity.getInstance()).implementLogics(LiveMosqueSingleTone.getInstance().getPrayerTimes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success();
    }
}
