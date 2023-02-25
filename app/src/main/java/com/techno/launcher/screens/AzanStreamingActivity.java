package com.techno.launcher.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;
import com.techno.launcher.global.LiveMosqueSingleTone;
import com.techno.launcher.service.Azan;

import java.util.List;
import java.util.Map;

import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;

public class AzanStreamingActivity extends AppCompatActivity {
    private Context context;
    private String appId = "435dcb295adc42b2aaae78a37a07e150";
    private String channelId = "malafsay";
    private RtcEngine agoraEngine;
    private ImageView imageCancel;
    private ImageView imageBack;
    private TextView tvMosqueName;
    private TextView textViewChangeLocation;
    private String type;
    private String coming;
    private LauncherApplication launcherApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azan_streaming);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        launcherApplication = (LauncherApplication) getApplicationContext();
        LiveMosqueSingleTone.getInstance().setStreaming(true);
        tvMosqueName = findViewById(R.id.tvMosqueName);
        try {
            type = getIntent().getStringExtra("type");
            coming = getIntent().getStringExtra("coming");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Utils.loadMap(context) != null) {
            channelId = (String) Utils.loadMap(context).get("username");
            tvMosqueName.setText((String) Utils.loadMap(context).get("mosque"));
        }

        imageCancel = findViewById(R.id.imageCancel);
        textViewChangeLocation = findViewById(R.id.textViewChangeLocation);
        textViewChangeLocation.setVisibility(View.GONE);
        imageBack = findViewById(R.id.imageBack);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.contains("Azan")) {
                    LiveMosqueSingleTone.getInstance().setSermonRunning(true);
                }
                leaveChannel();
                if (coming != null && coming.equalsIgnoreCase("background")) {
                    startMainActivity();
                } else {
                    onBackPressed();
                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveChannel();
                onBackPressed();
            }
        });
        setupVideoSDKEngine();
        getDataFromFieStore();
        loadAds();
    }

    public void startMainActivity() {
        Intent intent = new Intent(context, MosqueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        LiveMosqueSingleTone.getInstance().setStreaming(false);
        if (coming != null && coming.equalsIgnoreCase("background")) {
            startMainActivity();
        } else {
            super.onBackPressed();
        }
    }

    private void setupVideoSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
            agoraEngine.setChannelProfile(1);
            agoraEngine.setClientRole(2);
            agoraEngine.adjustPlaybackSignalVolume(400);
            agoraEngine.enableAudio();

            agoraEngine.joinChannel(null, channelId, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void leaveChannel() {
        try {
            agoraEngine.leaveChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            Log.i("Data", "");
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

            Log.i("Data", "");
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            Log.i("Data", "");
            // leaveChannel();
            //LiveMosqueSingleTone.getInstance().setStreaming(false);
        }
    };

    private void loadAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("Load", "Completed");

            }
        });

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void getDataFromFieStore() {
        // now we will be getting the data from the same reference.
        launcherApplication.getFireStoreReference().collection("imams").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for (DocumentSnapshot d : list) {
                            d.getData();
                            Map<String, Object> map = d.getData();
                            String name = (String) map.get("username");
                            if (Utils.loadMap(context) != null) {
                                String savedName = (String) Utils.loadMap(context).get("username");
                                if (savedName.equalsIgnoreCase(name)) {
                                    String azan = (String) map.get("azan");
                                    String sermon = (String) map.get("sermon");
                                    boolean isStreaming = false;
                                    if (!azan.equalsIgnoreCase("false")) {
                                        isStreaming = true;
                                    }
                                    if (!sermon.equalsIgnoreCase("false")) {
                                        isStreaming = true;
                                    }
                                    if (!isStreaming) {
                                        leaveChannel();
                                        if (coming != null && coming.equalsIgnoreCase("background")) {
                                            startMainActivity();
                                        } else {
                                            finish();
                                        }


                                    }
                                }
                            }
                        }


                    } else {
                        Toast.makeText(AzanStreamingActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
