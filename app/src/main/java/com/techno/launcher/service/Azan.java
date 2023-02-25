package com.techno.launcher.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techno.launcher.screens.AzanStreamingActivity;

public class Azan extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.i("Testssss", "Test");
            if(remoteMessage != null && remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null){
                startMainActivity(remoteMessage.getNotification().getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMainActivity(String type){
        Intent intent = new Intent(Azan.this, AzanStreamingActivity.class);
        intent.putExtra("type", type);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //startActivity(new Intent(Azan.this, HomeActivity.class).putExtra("isEnable", false).putExtra("type", "name"));


    }
}
