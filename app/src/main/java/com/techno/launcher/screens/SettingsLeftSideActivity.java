package com.techno.launcher.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;

public class SettingsLeftSideActivity extends AppCompatActivity {

    private ImageView imageBack;
    private Switch sermonSwitch;
    private Switch adhanSwitch;
    private Context context;
    private LauncherApplication launcherApplication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leftsettings);
        assignIds();

    }

    private void assignIds() {
        context = this;
        launcherApplication = (LauncherApplication)getApplicationContext();
        imageBack = findViewById(R.id.imageBack);
        adhanSwitch = findViewById(R.id.adhanSwitch);
        sermonSwitch = findViewById(R.id.sermonSwitch);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Utils.isSermonEnable(context)) {
            sermonSwitch.setChecked(true);
        } else {
            sermonSwitch.setChecked(false);
        }

        if (Utils.isAzanEnable(context)) {
            adhanSwitch.setChecked(true);
        } else {
            adhanSwitch.setChecked(false);
        }

        adhanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newTopic = Utils.loadMap(context).get("username") + "_azan";
                Utils.setAzanEnable(context, isChecked);
                if (isChecked) {
                    launcherApplication.getFirebaseMessaging().subscribeToTopic(newTopic);
                } else {
                    launcherApplication.getFirebaseMessaging().unsubscribeFromTopic(newTopic);
                }
            }
        });


        sermonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newTopic = Utils.loadMap(context).get("username") + "_sermon";
                Utils.setSermonEnable(context, isChecked);
                if (isChecked) {
                    launcherApplication.getFirebaseMessaging().subscribeToTopic(newTopic);
                } else {
                    launcherApplication.getFirebaseMessaging().unsubscribeFromTopic(newTopic);

                }
            }
        });
    }
}
