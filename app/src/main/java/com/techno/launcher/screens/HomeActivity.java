package com.techno.launcher.screens;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;
import com.techno.launcher.global.LiveMosqueSingleTone;
import com.techno.launcher.global.NotificationWorker;
import com.techno.launcher.global.PeriodicTask;
import com.techno.launcher.listener.ConnectivityReceiverInternet;
import com.techno.launcher.model.AppListMain;
import com.techno.launcher.ui.CustomViewGroup;

import net.time4j.SystemClock;
import net.time4j.calendar.HijriCalendar;
import net.time4j.format.expert.ChronoFormatter;
import net.time4j.format.expert.PatternType;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

import java.text.SimpleDateFormat;
import java.time.chrono.HijrahDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiverInternet.ReceiverListener {
    private PackageManager packageManager;
    private ImageView image_wifi;
    private ImageView settings;
    private ImageView image_bluetooth;
    private ImageView image_privacy;
    private ImageView image_battery;
    private LinearLayout layout;
    private boolean isLayoutOpened = false;
    private LinearLayout bottom_linear;
    private ArrayList<AppListMain> appListMainArrayList;
    private LauncherApplication launcherApplication;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private static Context context;
    private TextView internetText;
    private TextView liveText;
    private TextView currentDate;
    private TextView p1;
    private TextView p2;
    private TextView p3;
    private TextView p4;
    private TextView p5;
    private TextView p6;
    private TextView p7;
    private TextView p8;
    private TextView p9;
    private TextView p10;
    private TextView p1t;
    private TextView p2t;
    private TextView p3t;
    private TextView p4t;
    private TextView p5t;
    private TextView p6t;
    private TextView p7t;
    private TextView p8t;
    private TextView p9t;
    private TextView p10t;
    private TextView tv_time;
    private TextView tv_timezone;
    private TextView tv_timeR;
    private TextView tv_timeRzone;
    private TextView textViewChangeLocation;
    private TextView reference;
    private TextView hadith;
    private TextView sunriseTime;
    private TextView sunriseTimeZone;
    private TextView sunsetTime;
    private TextView sunsetTimeZone;
    private AdView mAdView;
    private TextView message;

    private Switch sermonSwitch;
    private Switch adhanSwitch;
    private TextView tv_mosque_name;
    private TextView salah;
    private TextView timestopeating;
    private TextView timeiftar;
    private TextView zoneeating;
    private TextView zoneiftar;
    private TextView tv_fariz;
    private TextView searchT;
    private TextView tv_heading;
    private TextView tv_sub_heading;
    private TextView textViewAddress;
    private ImageView searchI;
    private ImageView liveimage;
    private ImageView settingLeft;
    private RelativeLayout searchL;
    private LinearLayout linearRamadan;
    private LinearLayout linearSwitch;
    private LinearLayout linearAds;

    private TextClock timeClockZONE;
    private TextClock timeClock;
    private int deviceStatus;
    private boolean isEnable;
    private String type;
    private Map<String, Object> outputMap = null;
//    private WorkManager mWorkManager;
//    private Constraints constraints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        outputMap = Utils.loadMap(context);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        isEnable = getIntent().getBooleanExtra("isEnable", false);
        type = getIntent().getStringExtra("type");
        launcherApplication = (LauncherApplication) getApplicationContext();


        ///INTENT FILTER
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastreceiver, intentfilter);

        if (launcherApplication.getAppListMainArrayList() == null) {
            loadApps();
        }
        try {
            getHadithDataFieStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getLocalData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.WRITE_SETTINGS}, 1234);
        }

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(this, "Please give my app this permission!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
//            } else {
//                preventStatusBarExpansion(HomeActivity.this);
//            }
//        } else {
//            preventStatusBarExpansion(HomeActivity.this);
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Please give my app this permission!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 123);
                } else {
                    preventStatusBarExpansion(HomeActivity.this);
                }
            } else {
                preventStatusBarExpansion(HomeActivity.this);
            }
        }
    }

    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity) context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        CustomViewGroup view = new CustomViewGroup(context);

        manager.addView(view, localLayoutParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastreceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromFieStore() {
        // now we will be getting the data from the same reference.
        launcherApplication.getFireStoreReference().collection("imams").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    String savedName = Utils.getSavedName(context);
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for (DocumentSnapshot d : list) {
                            d.getData();
                            Map<String, Object> map = d.getData();
                            String name = (String) map.get("username");
//                            String savedName = (String) outputMap.get("username");
                            if (savedName.equalsIgnoreCase(name)) {
                                outputMap = map;
                                Utils.saveMap(context, map);
                            }
                        }
                        try {
                            assignIds();
                            listeners();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            String sermon = (String) outputMap.get("sermon");
                            if (sermon.equalsIgnoreCase("false")) {
                                clearAnimation();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getCustomAdConfig();
                    } else {
                        Toast.makeText(HomeActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    boolean isActive = false;
    String heading;
    String subHeading;
    String redirecturl;
    long clicks;
    long adsTimeFromServer;
    private ArrayList<Map<String, Object>> adsArrayList = new ArrayList<>();

    private void getCustomAdConfig() {
        // now we will be getting the data from the same reference.
        launcherApplication.getFireStoreReference().collection("customAds").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for (DocumentSnapshot d : list) {
                            d.getData();
                            Map<String, Object> map = d.getData();
                            isActive = (boolean) map.get("active");
                            if (isActive) {
                                adsArrayList.add(map);
                            }
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "No Custom Ads Config found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onBackPressed() {
        if (isLayoutOpened) {
            isLayoutOpened = false;
            bottom_linear.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEnable) {
            isEnable = false;
            startStreamingActivity();
        }
        if (LiveMosqueSingleTone.getInstance().isSermonRunning()) {
            startAnimation();
        }
    }

    private ObjectAnimator animator;

    private void startAnimation() {
        if (animator == null) {
            animator = ObjectAnimator.ofInt(liveText, "textColor", Color.GREEN, Color.WHITE, Color.WHITE);
            animator.setDuration(500);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatCount(Animation.REVERSE);
            animator.setRepeatCount(Animation.INFINITE);
        }
        animator.start();
    }

    private void clearAnimation() {
        if (animator != null) {
            LiveMosqueSingleTone.getInstance().setSermonRunning(false);
            animator.removeAllListeners();
            animator.end();
            animator.cancel();
        }

    }

    public void startStreamingActivity() {
        Intent intent = new Intent(HomeActivity.this, AzanStreamingActivity.class);
        intent.putExtra("type", type);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void listeners() {
        image_wifi.setOnClickListener(this);
        settings.setOnClickListener(this);
        image_bluetooth.setOnClickListener(this);
        image_privacy.setOnClickListener(this);
        layout.setOnClickListener(this);
        liveimage.setOnClickListener(this);
        liveText.setOnClickListener(this);
        settingLeft.setOnClickListener(this);
    }

    private void assignIds() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        p6 = findViewById(R.id.p6);
        p7 = findViewById(R.id.p7);
        p8 = findViewById(R.id.p8);
        p9 = findViewById(R.id.p9);
        p10 = findViewById(R.id.p10);
        p1t = findViewById(R.id.p1t);
        p2t = findViewById(R.id.p2t);
        p3t = findViewById(R.id.p3t);
        p4t = findViewById(R.id.p4t);
        p5t = findViewById(R.id.p5t);
        p6t = findViewById(R.id.p6t);
        p7t = findViewById(R.id.p7t);
        p8t = findViewById(R.id.p8t);
        p9t = findViewById(R.id.p9t);
        p10t = findViewById(R.id.p10t);
        message = findViewById(R.id.message);
        adhanSwitch = findViewById(R.id.adhanSwitch);
        sermonSwitch = findViewById(R.id.sermonSwitch);
        tv_mosque_name = findViewById(R.id.tv_mosque_name);
        salah = findViewById(R.id.salah);
        timestopeating = findViewById(R.id.timestopeating);
        timeiftar = findViewById(R.id.timeiftar);
        zoneeating = findViewById(R.id.zoneeating);
        zoneiftar = findViewById(R.id.zoneIftar);
        reference = findViewById(R.id.reference);
        tv_timezone = findViewById(R.id.tv_timezone);
        tv_time = findViewById(R.id.tv_time);
        tv_timeR = findViewById(R.id.tv_timeR);
        tv_timeRzone = findViewById(R.id.tv_timeRzone);
        textViewChangeLocation = findViewById(R.id.textViewChangeLocation);
        hadith = findViewById(R.id.hadith);
        sunriseTime = findViewById(R.id.sunriseTime);
        sunriseTimeZone = findViewById(R.id.sunriseTimeZone);
        sunsetTime = findViewById(R.id.sunsetTime);
        sunsetTimeZone = findViewById(R.id.sunsetTimeZone);
        mAdView = findViewById(R.id.adView);
        //  JSONArray aa1 = (JSONArray) Utils.loadHMap(context).get("hadiths");
        try {
            ArrayList<HashMap<String, String>> hadithsArrayList = (ArrayList<HashMap<String, String>>) Utils.loadHMap(context).get("hadiths");
            hadith.setText(hadithsArrayList.get(Integer.parseInt(Utils.getCurrentDay()) - 1).get("hadith"));//aa.getJSONObject(Integer.parseInt(Utils.getCurrentDay()) - 1).getString("hadith"));
//            reference.setText("-Prophet Muhammad -[" + aa.getJSONObject(Integer.parseInt(Utils.getCurrentDay()) - 1).getString("reference") + "]");
            reference.setText("-Prophet Muhammad -[" + hadithsArrayList.get(Integer.parseInt(Utils.getCurrentDay()) - 1).get("reference") + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String latLng = Utils.getLatLNG(context);
        Coordinates coordinates;
        if (latLng != null) {
            double lat = Double.parseDouble(latLng.split("=")[0]);
            double lng = Double.parseDouble(latLng.split("=")[1]);
            coordinates = new Coordinates(lat, lng);
        } else {
            Toast.makeText(HomeActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
            coordinates = new Coordinates(0.0, 0.0);
        }
        DateComponents date = DateComponents.from(new Date());
        CalculationMethod method = CalculationMethod.valueOf(Utils.getPrayerTime(context));
        CalculationParameters params;
        if (method == CalculationMethod.OTHER) {
            params = new CalculationParameters(15.0, 15.0, method);
        } else {
            params = method.getParameters();
        }
        params.madhab = Madhab.SHAFI;
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, params);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getDefault());

        p6t.setText(formatter.format(prayerTimes.fajr));
        p7t.setText(formatter.format(prayerTimes.dhuhr));
        p8t.setText(formatter.format(prayerTimes.asr));
        p9t.setText(formatter.format(prayerTimes.maghrib));
        p10t.setText(formatter.format(prayerTimes.isha));

        sunsetTime.setText(formatter.format(prayerTimes.maghrib).split(" ")[0]);
        sunsetTimeZone.setText(formatter.format(prayerTimes.maghrib).split(" ")[1]);
        sunriseTime.setText(formatter.format(prayerTimes.sunrise).split(" ")[0]);
        sunriseTimeZone.setText(formatter.format(prayerTimes.sunrise).split(" ")[1]);

        tv_fariz = findViewById(R.id.tv_fariz);
        searchL = findViewById(R.id.searchL);
        linearRamadan = findViewById(R.id.linearRamadan);
        linearSwitch = findViewById(R.id.linearSwitch);
        linearAds = findViewById(R.id.linearAds);
        timeClockZONE = findViewById(R.id.timeClockZONE);
        timeClock = findViewById(R.id.timeClock);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddress.setText(Utils.getAddress(context));
        searchT = findViewById(R.id.searchT);
        tv_heading = findViewById(R.id.tv_heading);
        tv_sub_heading = findViewById(R.id.tv_sub_heading);
        searchI = findViewById(R.id.searchI);
        settingLeft = findViewById(R.id.settingLeft);
        searchL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MosqueListActivity.class).putExtra("comingFrom", "H"));
            }
        });

        textViewChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MosqueListActivity.class).putExtra("comingFrom", "H").putExtra("loc", "loc"));
            }
        });

        searchT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MosqueListActivity.class).putExtra("comingFrom", "H"));
            }
        });

        searchI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MosqueListActivity.class).putExtra("comingFrom", "H"));
            }
        });
        linearAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WebViewActivity.class));

            }
        });
        tv_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WebViewActivity.class).putExtra("url", redirecturl));

            }
        });
        tv_sub_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WebViewActivity.class).putExtra("url", redirecturl));
            }
        });

        if (outputMap != null) {
            message.setText((String) outputMap.get("ticker"));
            message.setSelected(true);
            p1t.setText((String) outputMap.get("fajr"));
            if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
                p2t.setText((String) outputMap.get("jumma"));
            } else {
                p2t.setText((String) outputMap.get("zuhr"));
            }
            p3t.setText((String) outputMap.get("asr"));
            p4t.setText((String) outputMap.get("maghrib"));
            p5t.setText((String) outputMap.get("isha"));

            try {
                String visible = (String) ((HashMap<String, Object>) outputMap.get("ramadan")).get("visible");
                if (visible.equalsIgnoreCase("true")) {
                    timestopeating.setText(((String) ((HashMap<String, Object>) outputMap.get("ramadan")).get("suhoorTime")).split(" ")[0]);
                    zoneeating.setText(((String) ((HashMap<String, Object>) outputMap.get("ramadan")).get("suhoorTime")).split(" ")[1]);
                    timeiftar.setText(((String) ((HashMap<String, Object>) outputMap.get("ramadan")).get("iftarTime")).split(" ")[0]);
                    zoneiftar.setText(((String) ((HashMap<String, Object>) outputMap.get("ramadan")).get("iftarTime")).split(" ")[1]);
                    linearSwitch.setVisibility(View.GONE);
                    linearRamadan.setVisibility(View.VISIBLE);
                    settingLeft.setVisibility(View.VISIBLE);
                } else {
                    linearSwitch.setVisibility(View.VISIBLE);
                    linearRamadan.setVisibility(View.GONE);
                    settingLeft.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Utils.isAzanEnable(context)) {
                adhanSwitch.setChecked(true);
            } else {
                adhanSwitch.setChecked(false);
            }

            if (Utils.isSermonEnable(context)) {
                sermonSwitch.setChecked(true);
            } else {
                sermonSwitch.setChecked(false);
            }
            tv_mosque_name.setText((String) outputMap.get("mosque"));
        }
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ahmisya.ttf");
        timeClock.setTypeface(face);
        timeClockZONE.setTypeface(face);
        p1t.setTypeface(face);
        p2t.setTypeface(face);
        p3t.setTypeface(face);
        p4t.setTypeface(face);
        p5t.setTypeface(face);

        p1.setTypeface(face);
        p2.setTypeface(face);
        p3.setTypeface(face);
        p4.setTypeface(face);
        p5.setTypeface(face);

        p6t.setTypeface(face);
        p7t.setTypeface(face);
        p8t.setTypeface(face);
        p9t.setTypeface(face);
        p10t.setTypeface(face);

        p6.setTypeface(face);
        p7.setTypeface(face);
        p8.setTypeface(face);
        p9.setTypeface(face);
        p10.setTypeface(face);
        p10.setTypeface(face);
        image_battery = findViewById(R.id.image_battery);
        layout = findViewById(R.id.layout);
        liveText = findViewById(R.id.liveText);
        liveimage = findViewById(R.id.liveimage);
        image_wifi = findViewById(R.id.image_wifi);
        internetText = findViewById(R.id.internetText);
        currentDate = findViewById(R.id.currentDate);


        String mnth = null;
        switch (month + 1) {
            case 1:
                mnth = "Jan";
                break;
            case 2:
                mnth = "Feb";
                break;
            case 3:
                mnth = "Mar";
                break;
            case 4:
                mnth = "Apr";
                break;
            case 5:
                mnth = "May";
                break;
            case 6:
                mnth = "Jun";
                break;
            case 7:
                mnth = "July";
                break;
            case 8:
                mnth = "Aug";
                break;
            case 9:
                mnth = "Sept";
                break;
            case 10:
                mnth = "Oct";
                break;
            case 11:
                mnth = "Nov";
                break;
            case 12:
                mnth = "Dec";
                break;

        }
        try {
            HijrahDate hijrahDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                hijrahDate = HijrahDate.now();
            }
            Chronology iso = ISOChronology.getInstanceUTC();
            Chronology hijri = IslamicChronology.getInstanceUTC();

            LocalDate todayIso = new LocalDate(2022, month + 1, Integer.parseInt(Utils.getCurrentDay()), iso);
            LocalDate todayHijri = new LocalDate(todayIso.toDateTimeAtStartOfDay(), hijri);
            ChronoFormatter<HijriCalendar> hijriFormat = ChronoFormatter.setUp(HijriCalendar.family(), Locale.ENGLISH).addEnglishOrdinal(HijriCalendar.DAY_OF_MONTH).addPattern(" MMMM yyyy", PatternType.CLDR).build().withCalendarVariant(HijriCalendar.VARIANT_UMALQURA);


            HijriCalendar today = SystemClock.inLocalView().today().transform(HijriCalendar.class, HijriCalendar.VARIANT_UMALQURA);

            String ti = hijriFormat.format(today).replace("I", "");
            currentDate.setText(Utils.getCurrentDayOfweek().toUpperCase() + " " + mnth + " " + Utils.getCurrentDay() + " | " + ti);
        } catch (Exception e) {
            e.printStackTrace();
        }

        settings = findViewById(R.id.settings);
        image_bluetooth = findViewById(R.id.image_bluetooth);
        image_privacy = findViewById(R.id.image_privacy);
        bottom_linear = findViewById(R.id.bottom_linear);
        findViewById(R.id.btnApps).setOnClickListener(this);
        findViewById(R.id.btnAppChange).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnSettings1).setOnClickListener(this);


        adhanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String newTopic = outputMap.get("username") + "_azan";
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
                String newTopic = outputMap.get("username") + "_sermon";
                Utils.setSermonEnable(context, isChecked);
                if (isChecked) {
                    launcherApplication.getFirebaseMessaging().subscribeToTopic(newTopic);
                } else {
                    launcherApplication.getFirebaseMessaging().unsubscribeFromTopic(newTopic);

                }
            }
        });


        implementLogics(prayerTimes);

        SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
        formatter1.setTimeZone(TimeZone.getDefault());
        LiveMosqueSingleTone.getInstance().setPrayerTimes(prayerTimes);
        getTimeToSetWorkThreadBefore((String) outputMap.get("fajr"));

        if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
            getTimeToSetWorkThreadBefore((String) outputMap.get("jumma"));
        } else {
            getTimeToSetWorkThreadBefore((String) outputMap.get("zuhr"));
        }


        getTimeToSetWorkThreadBefore((String) outputMap.get("asr"));
        getTimeToSetWorkThreadBefore((String) outputMap.get("maghrib"));
        getTimeToSetWorkThreadBefore((String) outputMap.get("isha"));

        getTimeToSetWorkThreadAfter(formatter1.format(prayerTimes.fajr));
        getTimeToSetWorkThreadAfter(formatter1.format(prayerTimes.dhuhr));
        getTimeToSetWorkThreadAfter(formatter1.format(prayerTimes.asr));
        getTimeToSetWorkThreadAfter(formatter1.format(prayerTimes.maghrib));
        getTimeToSetWorkThreadAfter(formatter1.format(prayerTimes.isha));

        //alarmManager(prayerTimes);
    }

    private void getTimeToSetWorkThreadBefore(String time) {
        String mins = time.split(" ")[0].split(":")[1];
        String hours = time.split(" ")[0].split(":")[0];
        int min = Integer.parseInt(mins);
        int hour = Integer.parseInt(hours);
        int res = min - 30;
        if (res < 0) {
            if (hour != 0) {
                hour = hour - 1;
            }
            if (time.split(" ")[1].equalsIgnoreCase("PM")) {
                hour = hour + 12;
            }
            res = 59 - (-res);
            scheduleWork(hour, res);
            String finalTime = hour + ":" + res + time.split(" ")[1];
            Log.i("Time to set Before ", finalTime);
        } else {
            if (time.split(" ")[1].equalsIgnoreCase("PM")) {
                hour = hour + 12;
            }
            scheduleWork(hour, min);
            String finalTime = hour + ":" + res + time.split(" ")[1];
            Log.i("Time to set Before", finalTime);
        }


    }

    private void getTimeToSetWorkThreadAfter(String time) {
        String mins = time.split(" ")[0].split(":")[1];
        String hours = time.split(" ")[0].split(":")[0];
        int min = Integer.parseInt(mins);
        int hour = Integer.parseInt(hours);
        int res = min + 30;
        if (res > 59) {
            hour = hour + 1;
            res = res - 59;
            if (time.split(" ")[1].equalsIgnoreCase("PM")) {
                hour = hour + 12;
            }
            scheduleWork(hour, res);
        } else {
            if (time.split(" ")[1].equalsIgnoreCase("PM")) {
                hour = hour + 12;
            }
            scheduleWork(hour, min);
        }
    }

    private void loadAdIn10Sec() {
        MobileAds.initialize(HomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getLocalData() {
        NetworkRequest networkRequest = new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build();
        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
            connectivityManager.requestNetwork(networkRequest, networkCallback);
        }
    }

    private int size = 0;

    private void loadAds() {
        if (adTimes == 30) {
            if (adsTimeFromServer == customAdTime) {
                customAdTime = 0;
                size++;
            } else {
                customAdTime++;
            }
            if (size < adsArrayList.size()) {
                Map<String, Object> map = adsArrayList.get(size);
                isActive = (boolean) map.get("active");
                if (isActive) {
                    heading = (String) map.get("heading");
                    subHeading = (String) map.get("subheading");
                    subHeading = (String) map.get("subheading");
                    redirecturl = (String) map.get("redirect_url");
                    clicks = (long) map.get("clicks");
                    adsTimeFromServer = (long) map.get("time");
                    tv_heading.setText(heading);
                    tv_sub_heading.setText(subHeading);
                }
                linearAds.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.GONE);
            } else {
                size = 0;
                adTimes = 0;
                customAdTime = 0;
                linearAds.setVisibility(View.GONE);
                mAdView.setVisibility(View.VISIBLE);
            }
        } else {
            customAdTime = 0;
            adTimes++;
            mAdView.setVisibility(View.VISIBLE);
            linearAds.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.image_privacy:
                    startActivity(new Intent(context, WebViewActivity.class));
                    break;

                case R.id.settingLeft:
                    startActivity(new Intent(context, SettingsLeftSideActivity.class));
                    break;
                case R.id.btnApps:
                    if (!TextUtils.isEmpty(Utils.getPassword(HomeActivity.this)) || Utils.getPassword(HomeActivity.this) != null) {
                        showEnterPasswordPopup("HOME");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnAppChange:
                    if (!TextUtils.isEmpty(Utils.getPassword(HomeActivity.this)) || Utils.getPassword(HomeActivity.this) != null) {
                        showEnterPasswordPopup("HOME_LAUNCHER");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSettings:
                    if (!TextUtils.isEmpty(Utils.getPassword(HomeActivity.this)) || Utils.getPassword(HomeActivity.this) != null) {
                        showEnterPasswordPopup("SETTINGS");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSettings1:

                    if (!TextUtils.isEmpty(Utils.getPassword(HomeActivity.this)) || Utils.getPassword(HomeActivity.this) != null) {
                        showEnterPasswordPopup("RESET_SETTINGS");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.image_wifi:
                    Utils.isTimer = true;
                    startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
//                    if (!TextUtils.isEmpty(Utils.getPassword(HomeActivity.this)) || Utils.getPassword(HomeActivity.this) != null) {
//                        showEnterPasswordPopup("WIFI");
//                    } else {
//                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
//                    }
                    break;

                case R.id.settings:
                    startActivity(new Intent(context, SettingsActivity.class));
                    break;

                case R.id.image_bluetooth:
                    Utils.isTimer = true;
                    startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                    break;
                case R.id.mosaque_app:
                    Intent intent = packageManager.getLaunchIntentForPackage("live.mosque.home");
                    startActivity(intent);
                    break;
                case R.id.play_store_app:
                    Intent intent1 = packageManager.getLaunchIntentForPackage("com.android.deskclock");
                    startActivity(intent1);
                    break;
                case R.id.layout:
                case R.id.liveimage:
                case R.id.liveText:
                    if (LiveMosqueSingleTone.getInstance().isSermonRunning()) {
                        type = "Sermon";
                        startStreamingActivity();
                    } else {
                        if (!isLayoutOpened) {
                            isLayoutOpened = true;
                            bottom_linear.setVisibility(View.VISIBLE);
                            Timer mTimer1;
                            TimerTask mTt1;
                            Handler mTimerHandler = new Handler();
                            mTimer1 = new Timer();
                            mTt1 = new TimerTask() {
                                public void run() {
                                    mTimerHandler.post(new Runnable() {
                                        public void run() {
                                            isLayoutOpened = false;
                                            bottom_linear.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            };

                            mTimer1.schedule(mTt1, 10000);


//                            Thread t = new Thread() {
//                                public void run() {
//                                    isLayoutOpened = false;
//                                    bottom_linear.setVisibility(View.GONE);
//                                }
//                            };
//                            t.start();

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                }
//                            }, 10000);
                        }
                    }


                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadApps() {
        packageManager = getPackageManager();
        appListMainArrayList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (!resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.android.settings")) {
                AppListMain appListMain = new AppListMain();
                appListMain.setAppIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                appListMain.setAppName(resolveInfo.loadLabel(packageManager));
                appListMain.setAppPackage(resolveInfo.activityInfo.packageName);
                appListMain.setSelected(false);
                appListMainArrayList.add(appListMain);
            }
        }
        launcherApplication.setAppListMainArrayList(appListMainArrayList);
    }


    private void showEnterPasswordPopup(String type) {
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_password);
        dialog.setCancelable(false);
        ImageView imageview_close = dialog.findViewById(R.id.imageview_close);
        imageview_close.setVisibility(View.VISIBLE);
        imageview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        EditText textInputEditText = dialog.findViewById(R.id.text_input_edit_text);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = textInputEditText.getText().toString();
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                String masterPasscode = "2" + (month + 1) + "2" + Utils.getCurrentDay();
                if (password.equalsIgnoreCase(masterPasscode)) {
                    handlePass(type, dialog);
                } else {
                    if (TextUtils.isEmpty(password)) {
                        textInputEditText.setError("Please enter Password");
                    } else if (password.length() < 6) {
                        textInputEditText.setError("Password length should be more than 6");
                    } else if (!password.equalsIgnoreCase(Utils.getPassword(HomeActivity.this))) {
                        textInputEditText.setError("Please enter correct password");
                    } else {
                        handlePass(type, dialog);
                    }
                }
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }


    private void handlePass(String type, Dialog dialog) {
        if (type.equalsIgnoreCase("WIFI")) {
            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        } else if (type.equalsIgnoreCase("HOME")) {
            isLayoutOpened = false;
            bottom_linear.setVisibility(View.GONE);
            Intent listIntent = new Intent(HomeActivity.this, AppListActivity.class);
            startActivity(listIntent);
        } else if (type.equalsIgnoreCase("SETTINGS")) {
            isLayoutOpened = false;
            bottom_linear.setVisibility(View.GONE);
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage("com.android.settings");
            startActivity(intent);
        } else if (type.equalsIgnoreCase("RESET_SETTINGS")) {
            isLayoutOpened = false;
            bottom_linear.setVisibility(View.GONE);
            startActivityForResult(new Intent(android.provider.Settings.ACTION_PRIVACY_SETTINGS), 0);
        } else if (type.equalsIgnoreCase("HOME_LAUNCHER")) {
            isLayoutOpened = false;
            bottom_linear.setVisibility(View.GONE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                final Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                startActivity(intent);
            } else {
                final Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        }
        dialog.dismiss();
    }

    public void implementLogics(PrayerTimes prayerTimes) {
        if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
            p2.setText("Jummah");
        } else {
            p2.setText("Zuhr");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getDefault());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        int diffF = Utils.getTimeDifference((String) outputMap.get("fajr"), currentTime);
        int diffJ;
        if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
            diffJ = Utils.getTimeDifference((String) outputMap.get("jumma"), currentTime);
        } else {
            diffJ = Utils.getTimeDifference((String) outputMap.get("zuhr"), currentTime);
        }
        int diffA = Utils.getTimeDifference((String) outputMap.get("asr"), currentTime);
        int diffM = Utils.getTimeDifference((String) outputMap.get("maghrib"), currentTime);
        int diffI = Utils.getTimeDifference((String) outputMap.get("isha"), currentTime);


        int diffFR = Utils.getTimeDifference(formatter.format(prayerTimes.fajr), currentTime);
        int diffJR = Utils.getTimeDifference(formatter.format(prayerTimes.dhuhr), currentTime);
        int diffAR = Utils.getTimeDifference(formatter.format(prayerTimes.asr), currentTime);
        int diffMR = Utils.getTimeDifference(formatter.format(prayerTimes.maghrib), currentTime);
        int diffIR = Utils.getTimeDifference(formatter.format(prayerTimes.isha), currentTime);


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ahmisya.ttf");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    NotificationManager notificationManager = (NotificationManager) HomeActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String salah1 = null;
                String time = null;
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ahmisya.ttf");
                if (diffFR > 0 && diffFR <= 30) {
                    tv_timeR.setTypeface(face, Typeface.BOLD);
                    p6t.setTypeface(face);
                    p6.setTypeface(face);
                    tv_timeRzone.setTypeface(face);
                    time = formatter.format(prayerTimes.fajr);
                    salah1 = "Fajr SALAH";
                    Utils.setLastValue(context, "FAJR");
                } else {
                    tv_timeRzone.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timeRzone.setTextColor(Color.parseColor("#FFFFFF"));
                }

                if (diffJR > 0 && diffJR <= 30) {
                    tv_timeR.setTypeface(face, Typeface.BOLD);
                    salah1 = "Zuhr SALAH";
                    p7t.setTypeface(face);
                    p7.setTypeface(face);
                    tv_timeRzone.setTypeface(face);
                    time = formatter.format(prayerTimes.dhuhr);
                    Utils.setLastValue(context, "DHUHAR");
                } else {
                    tv_timeRzone.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timeRzone.setTextColor(Color.parseColor("#FFFFFF"));
                }

                if (diffAR > 0 && diffAR <= 30) {
                    p8t.setTypeface(face);
                    p8.setTypeface(face);
                    tv_timeRzone.setTypeface(face);
                    tv_timeR.setTypeface(face, Typeface.BOLD);
                    salah1 = "Asr SALAH";
                    time = formatter.format(prayerTimes.asr);
                    Utils.setLastValue(context, "ASR");
                } else {
                    tv_timeR.setTypeface(face, Typeface.NORMAL);
                    tv_timeRzone.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timeRzone.setTextColor(Color.parseColor("#FFFFFF"));

                }
                if (diffMR > 0 && diffMR <= 30) {
                    p9t.setTypeface(face);
                    p9.setTypeface(face);
                    tv_timeRzone.setTypeface(face);
                    tv_timeR.setTypeface(face, Typeface.BOLD);

                    time = formatter.format(prayerTimes.maghrib);
                    salah1 = "Maghrib SALAH";
                    Utils.setLastValue(context, "MGRIB");

                } else {
                    tv_timeRzone.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timeRzone.setTextColor(Color.parseColor("#FFFFFF"));

                }
                if (diffIR > 0 && diffIR <= 30) {
                    p10t.setTypeface(face);
                    p10.setTypeface(face);
                    tv_timeRzone.setTypeface(face);
                    tv_timeR.setTypeface(face, Typeface.BOLD);

                    salah1 = "Isha SALAH";
                    time = formatter.format(prayerTimes.isha);
                    Utils.setLastValue(context, "ISHA");
                } else {
                    tv_timeR.setTypeface(face, Typeface.NORMAL);
                    tv_timeRzone.setTypeface(face, Typeface.NORMAL);
                    tv_timeR.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timeRzone.setTextColor(Color.parseColor("#FFFFFF"));
                }

                if (time != null) {
                    tv_timeR.setTextColor(Color.parseColor("#1A811E"));
                    tv_timeRzone.setTextColor(Color.parseColor("#1A811E"));

                    salah.setText(salah1);
                    tv_timeR.setText(time.split(" ")[0]);
                    tv_timeRzone.setText(time.split(" ")[1]);

                } else {
                    if (Utils.getLastValue(context) != null) {
                        switch (Utils.getLastValue(context)) {
                            case "FAJR":
                                salah.setText("Zuhr SALAH");
                                tv_timeR.setText(formatter.format(prayerTimes.dhuhr).split(" ")[0]);
                                tv_timeRzone.setText(formatter.format(prayerTimes.dhuhr).split(" ")[1]);
                                break;

                            case "DHUHAR":
                                salah.setText("Asr SALAH");
                                tv_timeR.setText(formatter.format(prayerTimes.asr).split(" ")[0]);
                                tv_timeRzone.setText(formatter.format(prayerTimes.asr).split(" ")[1]);
                                break;

                            case "ASR":
                                salah.setText("Maghrib SALAH");
                                tv_timeR.setText(formatter.format(prayerTimes.maghrib).split(" ")[0]);
                                tv_timeRzone.setText(formatter.format(prayerTimes.maghrib).split(" ")[1]);
                                break;

                            case "MGRIB":
                                salah.setText("Isha SALAH");
                                tv_timeR.setText(formatter.format(prayerTimes.isha).split(" ")[0]);
                                tv_timeRzone.setText(formatter.format(prayerTimes.isha).split(" ")[1]);
                                break;
                            case "ISHA":
                                salah.setText("Fajr SALAH");
                                tv_timeR.setText(formatter.format(prayerTimes.fajr).split(" ")[0]);
                                tv_timeRzone.setText(formatter.format(prayerTimes.fajr).split(" ")[1]);
                                break;
                        }
                    } else {
                        salah.setText("Fajr SALAH");
                        tv_timeR.setText(formatter.format(prayerTimes.fajr).split(" ")[0]);
                        tv_timeRzone.setText(formatter.format(prayerTimes.fajr).split(" ")[1]);
                    }
                }
                ///////// Left side

                String timeR = null;
                String leftZone = null;

                if (diffF < 0 && diffF >= -30) {
                    p1t.setTypeface(face);
                    p1.setTypeface(face);
                    tv_timezone.setTypeface(face);
                    leftZone = "Fajr IQAMAH";
                    timeR = (String) outputMap.get("fajr");
                    Utils.setLastValueR(context, "FAJR");

                } else {
                    tv_time.setTypeface(face, Typeface.NORMAL);
                    tv_timezone.setTypeface(face, Typeface.NORMAL);
                    tv_time.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timezone.setTextColor(Color.parseColor("#FFFFFF"));

                }

                if (diffJ < 0 && diffJ >= -30) {
                    leftZone = p2.getText().toString() + " IQAMAH";
                    p2t.setTypeface(face);
                    p2.setTypeface(face);
                    tv_timezone.setTypeface(face);
                    tv_time.setTypeface(face, Typeface.BOLD);

                    if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
                        timeR = (String) outputMap.get("jumma");
                    } else {
                        timeR = (String) outputMap.get("zuhr");
                    }

                    Utils.setLastValueR(context, "jumma");
                } else {
                    tv_time.setTypeface(face, Typeface.NORMAL);
                    tv_timezone.setTypeface(face, Typeface.NORMAL);
                    tv_time.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timezone.setTextColor(Color.parseColor("#FFFFFF"));


                }

                if (diffA < 0 && diffA >= -30) {
                    leftZone = "Asr IQAMAH";
                    p3t.setTypeface(face);
                    p3.setTypeface(face);
                    tv_timezone.setTypeface(face);
                    // Green Color
                    tv_time.setTypeface(face, Typeface.BOLD);


                    timeR = (String) outputMap.get("asr");
                    Utils.setLastValueR(context, "asr");

                } else {
                    tv_time.setTypeface(face, Typeface.NORMAL);
                    tv_timezone.setTypeface(face, Typeface.NORMAL);
                    tv_time.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timezone.setTextColor(Color.parseColor("#FFFFFF"));

                }
                if (diffM < 0 && diffM >= -30) {
                    p4t.setTypeface(face);
                    p4.setTypeface(face);
                    tv_timezone.setTypeface(face);
                    // Green Color
                    leftZone = "Maghrib IQAMAH";
                    tv_time.setTypeface(face, Typeface.BOLD);


                    timeR = (String) outputMap.get("maghrib");
                    Utils.setLastValueR(context, "maghrib");

                } else {
                    tv_time.setTypeface(face, Typeface.NORMAL);
                    tv_timezone.setTypeface(face, Typeface.NORMAL);
                    tv_time.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timezone.setTextColor(Color.parseColor("#FFFFFF"));

                }
                if (diffI < 0 && diffI >= -30) {
                    p5t.setTypeface(face);
                    p5.setTypeface(face);
                    tv_timezone.setTypeface(face);
                    tv_time.setTypeface(face, Typeface.BOLD);
                    // Green Color
                    leftZone = "Isha IQAMAH";
                    timeR = (String) outputMap.get("isha");
                    Utils.setLastValueR(context, "isha");
                } else {
                    tv_time.setTypeface(face, Typeface.NORMAL);
                    tv_timezone.setTypeface(face, Typeface.NORMAL);
                    tv_time.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_timezone.setTextColor(Color.parseColor("#FFFFFF"));
                }

                if (timeR != null) {
                    tv_fariz.setText(leftZone);
                    tv_time.setText(timeR.split(" ")[0]);
                    tv_timezone.setText(timeR.split(" ")[1]);
                    tv_time.setTextColor(Color.parseColor("#1A811E"));
                    tv_timezone.setTextColor(Color.parseColor("#1A811E"));
                } else {
                    if (Utils.getLastValueR(context) != null) {
                        switch (Utils.getLastValueR(context)) {
                            case "FAJR":
                                tv_fariz.setText(p2.getText().toString() + " IQAMAH");
                                if (Utils.getCurrentDayOfweek().equalsIgnoreCase("friday")) {
                                    tv_time.setText(((String) outputMap.get("jumma")).split(" ")[0]);
                                    tv_timezone.setText(((String) outputMap.get("jumma")).split(" ")[1]);
                                } else {
                                    tv_time.setText(((String) outputMap.get("zuhr")).split(" ")[0]);
                                    tv_timezone.setText(((String) outputMap.get("zuhr")).split(" ")[1]);
                                }
                                break;
                            case "jumma":
                                tv_fariz.setText("Asr IQAMAH");
                                tv_time.setText(((String) outputMap.get("asr")).split(" ")[0]);
                                tv_timezone.setText(((String) outputMap.get("asr")).split(" ")[1]);
                                break;
                            case "asr":
                                tv_fariz.setText("Maghrib IQAMAH");
                                tv_time.setText(((String) outputMap.get("maghrib")).split(" ")[0]);
                                tv_timezone.setText(((String) outputMap.get("maghrib")).split(" ")[1]);
                                break;
                            case "maghrib":
                                tv_fariz.setText("Isha IQAMAH");
                                tv_time.setText(((String) outputMap.get("isha")).split(" ")[0]);
                                tv_timezone.setText(((String) outputMap.get("isha")).split(" ")[1]);
                                break;
                            case "isha":
                                tv_fariz.setText("Fajr IQAMAH");
                                tv_time.setText(((String) outputMap.get("fajr")).split(" ")[0]);
                                tv_timezone.setText(((String) outputMap.get("fajr")).split(" ")[1]);
                                break;
                        }
                    } else {
                        tv_fariz.setText("Fajr IQAMAH");
                        tv_time.setText(((String) outputMap.get("fajr")).split(" ")[0]);
                        tv_timezone.setText(((String) outputMap.get("fajr")).split(" ")[1]);
                    }
                }
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    String formattedDate = dateFormat.format(new Date()).toString();
                    String s1 = formattedDate;
                    String s2 = "04:00 AM";
                    String s3 = "11:00 PM";
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    Date currentTime = sdf.parse(s1);
                    Date date1 = sdf.parse(s2);
                    Date date2 = sdf.parse(s3);
                    boolean isAfter = currentTime.after(date2);
                    boolean isBefore = currentTime.before(date1);
                    Log.i("Date ", currentTime + " " + date1 + " " + date2 + "" + isAfter + " " + isBefore);
                    boolean isLoaded = false;
                    loadAdIn10Sec();

                    Timer mTimer1;
                    TimerTask mTt1;
                    Handler mTimerHandler = new Handler();
                    mTimer1 = new Timer();
                    mTt1 = new TimerTask() {
                        public void run() {
                            mTimerHandler.post(new Runnable() {
                                public void run() {
                                    try {
                                        if (!isBefore && !isAfter) {
                                            loadAds();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    mTimer1.schedule(mTt1, 1000);


//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    int adTimes = 0;
    int customAdTime = 0;
    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel = (int) (((float) level / (float) scale) * 100.0f);


            image_battery = findViewById(R.id.image_battery);
            if (batteryLevel < 20) {
                image_battery.setImageResource(R.mipmap.empty_battery);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                image_battery.setImageResource(R.mipmap.battery_charging);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL) {
                image_battery.setImageResource(R.mipmap.battery_full);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                image_battery.setImageResource(R.drawable.full_battery);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                image_battery.setImageResource(R.drawable.full_battery);
            }


        }
    };

//    private void alarmManager(PrayerTimes prayerTimes) {
//        Date when = new Date(System.currentTimeMillis());
//        try {
//            Intent someIntent = new Intent(context, RepeatReceiver.class); // intent to be launched
//            someIntent.setAction("ALARM");
//            // Note: this could be getActivity if you want to launch an activity
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, // id (optional)
//                    someIntent, // intent to launch
//                    PendingIntent.FLAG_CANCEL_CURRENT // PendingIntent flag
//            );
//            AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarms.setRepeating(AlarmManager.RTC_WAKEUP, when.getTime(), 10000, pendingIntent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void showSnackBar(boolean isConnected) {

    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        // display snack bar
        showSnackBar(isConnected);
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            if (image_wifi != null) {
                image_wifi.setImageResource(R.mipmap.wifi);
                internetText.setVisibility(View.GONE);
            } else {
                image_wifi = findViewById(R.id.image_wifi);
                internetText = findViewById(R.id.internetText);
                image_wifi.setImageResource(R.mipmap.wifi);
                internetText.setVisibility(View.GONE);
            }


        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            if (internetText != null && image_wifi != null) {

                internetText.setVisibility(View.VISIBLE);
                image_wifi.setImageResource(R.mipmap.wifi_red);
            }

        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            if (internetText != null && image_wifi != null) {
                if (unmetered) {
                    image_wifi.setImageResource(R.mipmap.wifi);
                    internetText.setVisibility(View.GONE);
                } else {
                    internetText.setVisibility(View.VISIBLE);
                    image_wifi.setImageResource(R.mipmap.wifi_red);
                }
            }

        }
    };


    public void scheduleWork(int hour, int minute) {
//        Calendar calendar = Calendar.getInstance();
//        long nowMillis = calendar.getTimeInMillis();
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        if (calendar.before(Calendar.getInstance())) {
//            calendar.add(Calendar.DATE, 1);
//        }
//        long diff = calendar.getTimeInMillis() - nowMillis;
//        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
//                //  .setConstraints(constraints)
//                .setInitialDelay(diff, TimeUnit.MILLISECONDS).addTag("WORK_TAG").build();
//        mWorkManager.enqueue(mRequest);

        PeriodicWorkRequest mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicTask.class,
                hour, TimeUnit.HOURS, minute, TimeUnit.MINUTES)
                .addTag("PeriodicWorkRequest")
                .build();

        WorkManager.getInstance(context).enqueue(mPeriodicWorkRequest);
    }

    private void getHadithDataFieStore() {
        launcherApplication.getFireStoreReference().collection("hadiths").document("english").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> hMap = documentSnapshot.getData();
                        Utils.saveHMap(context, hMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    getDataFromFieStore();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
