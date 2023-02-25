package com.techno.launcher.global;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.techno.launcher.screens.HomeActivity;

public class PeriodicTask extends Worker {

    private static final String TAG = PeriodicTask.class.getSimpleName();

    public PeriodicTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        try {
            //TODO HASSAN
            if (LiveMosqueSingleTone.getInstance().getPrayerTimes() != null) {
                ((HomeActivity) HomeActivity.getInstance()).implementLogics(LiveMosqueSingleTone.getInstance().getPrayerTimes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}