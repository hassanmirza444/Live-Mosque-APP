package com.techno.launcher.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.techno.launcher.R;
import com.techno.launcher.Utils;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageBack;
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private RelativeLayout rl6;
    private RelativeLayout rl7;
    private RelativeLayout rl8;
    private RelativeLayout rl9;
    private RelativeLayout rl10;


    private ImageView im1;
    private ImageView im2;
    private ImageView im3;
    private ImageView im4;
    private ImageView im5;
    private ImageView im6;
    private ImageView im7;
    private ImageView im8;
    private ImageView im9;
    private ImageView im10;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context = this;
        assignIds();
        listners();
    }

    private void listners() {
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
        rl7.setOnClickListener(this);
        rl8.setOnClickListener(this);
        rl9.setOnClickListener(this);
        rl10.setOnClickListener(this);
        imageBack.setOnClickListener(this);
    }

    private void assignIds() {
        imageBack = findViewById(R.id.imageBack);
        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);
        rl3 = findViewById(R.id.rl3);
        rl4 = findViewById(R.id.rl4);
        rl5 = findViewById(R.id.rl5);
        rl6 = findViewById(R.id.rl6);
        rl7 = findViewById(R.id.rl7);
        rl8 = findViewById(R.id.rl8);
        rl9 = findViewById(R.id.rl9);
        rl10 = findViewById(R.id.rl10);

        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im3 = findViewById(R.id.im3);
        im4 = findViewById(R.id.im4);
        im5 = findViewById(R.id.im5);
        im6 = findViewById(R.id.im6);
        im7 = findViewById(R.id.im7);
        im8 = findViewById(R.id.im8);
        im9 = findViewById(R.id.im9);
        im10 = findViewById(R.id.im10);

        switch (Utils.getPrayerTime(context)) {
            case "OTHER":
                setVisibility(im1);
                break;
            case "MUSLIM_WORLD_LEAGUE":
                setVisibility(im2);
                break;
            case "EGYPTIAN":
                setVisibility(im3);
                break;
            case "UMM_AL_QURA":
                setVisibility(im4);
                break;
            case "DUBAI":
                setVisibility(im5);
                break;
            case "QATAR":
                setVisibility(im6);
                break;
            case "KUWAIT":
                setVisibility(im7);
                break;
            case "MOON_SIGHTING_COMMITTEE":
                setVisibility(im8);
                break;
            case "SINGAPORE":
                setVisibility(im9);
                break;
            case "NORTH_AMERICA":
                setVisibility(im10);
                break;

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.rl1:
//                Utils.setPrayerTime(context, "Default");
                Utils.setPrayerTime(context, "OTHER");
                setVisibility(im1);
                break;
            case R.id.rl2:
//                Utils.setPrayerTime(context, "Muslim World League");
                Utils.setPrayerTime(context, "MUSLIM_WORLD_LEAGUE");
                setVisibility(im2);
                break;
            case R.id.rl3:
//                Utils.setPrayerTime(context, "Egyptian General Authority of Survey");
                Utils.setPrayerTime(context, "EGYPTIAN");
                setVisibility(im3);
                break;
            case R.id.rl4:
//                Utils.setPrayerTime(context, "Umm al-Qura University, Makkah");
                Utils.setPrayerTime(context, "UMM_AL_QURA");
                setVisibility(im4);
                break;
            case R.id.rl5:
//                Utils.setPrayerTime(context, "Dubai, UAE");
                Utils.setPrayerTime(context, "DUBAI");
                setVisibility(im5);
                break;
            case R.id.rl6:
//                Utils.setPrayerTime(context, "Qatar");
                Utils.setPrayerTime(context, "QATAR");
                setVisibility(im6);
                break;
            case R.id.rl7:
//                Utils.setPrayerTime(context, "Kuwait");
                Utils.setPrayerTime(context, "KUWAIT");
                setVisibility(im7);
                break;
            case R.id.rl8:
//                Utils.setPrayerTime(context, "Moonsighting Committee Worldwide");
                Utils.setPrayerTime(context, "MOON_SIGHTING_COMMITTEE");
                setVisibility(im8);
                break;
            case R.id.rl9:
//                Utils.setPrayerTime(context, "Singapore");
                Utils.setPrayerTime(context, "SINGAPORE");
                setVisibility(im9);
                break;
            case R.id.rl10:
//                Utils.setPrayerTime(context, "ISNA - North America");
                Utils.setPrayerTime(context, "NORTH_AMERICA");
                setVisibility(im10);
                break;

        }
    }


    private void setVisibility(ImageView v) {
        im1.setVisibility(View.GONE);
        im2.setVisibility(View.GONE);
        im3.setVisibility(View.GONE);
        im4.setVisibility(View.GONE);
        im5.setVisibility(View.GONE);
        im6.setVisibility(View.GONE);
        im7.setVisibility(View.GONE);
        im8.setVisibility(View.GONE);
        im9.setVisibility(View.GONE);
        im10.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
    }
}
