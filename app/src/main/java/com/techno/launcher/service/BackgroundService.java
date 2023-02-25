package com.techno.launcher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techno.launcher.LauncherApplication;
import com.techno.launcher.Utils;
import com.techno.launcher.screens.AzanStreamingActivity;
import com.techno.launcher.screens.MosqueListActivity;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        context = this;
        startTimer();
        return START_STICKY;
    }

    private void startAct(Context context) throws Exception{
        Utils.isTimer = false;
        Intent intent = new Intent(context, MosqueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        stopSelf();
    }


    public void startTimer() {
        try {
            initializeTimerTask();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeTimerTask() throws Exception {
        long time = 0;
        if (Utils.isTimer) {
            time = 60000;
        } else {
            time = 10000;
        }
        Timer mTimer1;
        TimerTask mTt1;
        Handler mTimerHandler = new Handler();
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        try {
                            startAct(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, time);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    startAct(context);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, time);
    }
}
