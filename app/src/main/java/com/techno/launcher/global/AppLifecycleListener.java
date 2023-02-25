package com.techno.launcher.global;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.techno.launcher.Utils;
import com.techno.launcher.screens.HomeActivity;
import com.techno.launcher.screens.MosqueListActivity;
import com.techno.launcher.service.Azan;
import com.techno.launcher.service.BackgroundService;

public class AppLifecycleListener implements DefaultLifecycleObserver {
    Context context;
    Intent backGroundService;
    public AppLifecycleListener(Context context) {
        this.context = context;
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onDestroy(owner);
        Log.d("Destroy", "Going into Killed");

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d("Start", "Going into foreground");

    }


    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d("Stop", "Going into background");
        context.startService(new Intent(context, BackgroundService.class));

//        long time = 0;
//        PackageManager packageManager = context.getPackageManager();
//        if (Utils.isTimer) {
//            time = 60000;
//        } else {
//            time = 10000;
//        }
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(context, MosqueListActivity.class);
//                if (intent != null) {
//                    Utils.isTimer = false;
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            }
//        }, time);


    }


}
