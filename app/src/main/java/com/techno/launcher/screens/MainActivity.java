package com.techno.launcher.screens;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;

import com.techno.launcher.listener.ConnectivityReceiverInternet;
import com.techno.launcher.model.AppListMain;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private PackageManager packageManager;
    private ArrayList<AppListMain> appListMainArrayList;
    private LauncherApplication launcherApplication;
    private ImageView ivAppIcon;
    private ImageView ivAppIconP;
    private TextView tvAppLabel;
    private TextView tvAppLabelP;
    private ImageView sdvWallpaper;
    private ImageView battry_image;
    private ImageView wifi_image;
    private LinearLayout mosaque_app;
    private LinearLayout app_playstore;
    private LinearLayout bottom_linear;
    private LinearLayout wifi_settings;
    private boolean isMosaqueAppExist = false;
    private TextView text_wifi;
    private TextView text_battery;
    private boolean isLayoutOpened = false;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launcherApplication = (LauncherApplication) getApplicationContext();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            mainActivity = MainActivity.this;
            bindHere();
            loadApps();
            if (TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) == null) {
                showSetPasswordPopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindHere() {
        try {
            findViewById(R.id.btnApps).setOnClickListener(this);
            findViewById(R.id.btnAppChange).setOnClickListener(this);
            findViewById(R.id.btnSettings).setOnClickListener(this);
            findViewById(R.id.btnSettings1).setOnClickListener(this);

            text_battery = findViewById(R.id.text_battery);
            battry_image = findViewById(R.id.battry_image);
            ivAppIcon = findViewById(R.id.ivAppIcon);
            sdvWallpaper = findViewById(R.id.sdvWallpaper);
            wifi_image = findViewById(R.id.wifi_image);
            text_wifi = findViewById(R.id.text_wifi);
            ivAppIconP = findViewById(R.id.ivAppIconP);
            mosaque_app = findViewById(R.id.mosaque_app);
            app_playstore = findViewById(R.id.play_store_app);
            wifi_settings = findViewById(R.id.wifi_settings);
            bottom_linear = findViewById(R.id.bottom_linear);
            mosaque_app.setOnClickListener(this);
            wifi_settings.setOnClickListener(this);
            app_playstore.setOnClickListener(this);
            sdvWallpaper.setOnClickListener(this);
            sdvWallpaper.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    isLayoutOpened = true;
                    bottom_linear.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            tvAppLabel = findViewById(R.id.tvAppLabel);
            tvAppLabelP = findViewById(R.id.tvAppLabelP);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            boolean isWifiConnected = checkWifiStatus();
            if (isWifiConnected) {
                text_wifi.setText("Wifi connected");
                wifi_image.setImageResource(R.drawable.wifi);
                text_wifi.setTextColor(Color.parseColor("#076FC1"));
            } else {
                wifi_image.setImageResource(R.drawable.wifi_red);
                text_wifi.setTextColor(Color.parseColor("#FC0000"));
                text_wifi.setText("Wifi not connected ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            NotificationManager notificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            text_battery.setText(percentage + " %");
            if (percentage <= 20) {
                battry_image.setImageResource(R.drawable.low_battery);
            } else {
                battry_image.setImageResource(R.drawable.full_battery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnApps:
                    if (!TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) != null) {
                        showEnterPasswordPopup("HOME");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnAppChange:
                    if (!TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) != null) {
                        showEnterPasswordPopup("HOME_LAUNCHER");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSettings:
                    if (!TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) != null) {
                        showEnterPasswordPopup("SETTINGS");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSettings1:

                    if (!TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) != null) {
                        showEnterPasswordPopup("RESET_SETTINGS");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.wifi_settings:
                    if (!TextUtils.isEmpty(Utils.getPassword(MainActivity.this)) || Utils.getPassword(MainActivity.this) != null) {
                        showEnterPasswordPopup("WIFI");
                    } else {
                        Toast.makeText(this, "Please Set Password first", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.mosaque_app:
                    Intent intent = packageManager.getLaunchIntentForPackage("live.mosque.home");
                    startActivity(intent);
                    break;
                case R.id.play_store_app:
//                    Intent intent1 = packageManager.getLaunchIntentForPackage("com.android.vending");
                    Intent intent1 = packageManager.getLaunchIntentForPackage("com.android.deskclock");
                    startActivity(intent1);
                    break;
                case R.id.sdvWallpaper:
                    if (isLayoutOpened) {
                        isLayoutOpened = false;
                        bottom_linear.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void showSetPasswordPopup() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_password);
        dialog.setCancelable(false);
        EditText textInputEditText = dialog.findViewById(R.id.text_input_edit_text);
        ImageView imageview_close = dialog.findViewById(R.id.imageview_close);
        imageview_close.setVisibility(View.GONE);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = textInputEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    textInputEditText.setError("Please enter Password");
                } else if (password.length() < 6) {
                    textInputEditText.setError("Password lenght should be more than 6");
                } else {
                    Utils.setPassword(MainActivity.this, password);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    private void showEnterPasswordPopup(String type) {
        Dialog dialog = new Dialog(MainActivity.this);
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
                if (TextUtils.isEmpty(password)) {
                    textInputEditText.setError("Please enter Password");
                } else if (password.length() < 6) {
                    textInputEditText.setError("Password lenght should be more than 6");
                } else if (!password.equalsIgnoreCase(Utils.getPassword(MainActivity.this))) {
                    textInputEditText.setError("Please enter correct password");
                } else {
                    if (type.equalsIgnoreCase("WIFI")) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    } else if (type.equalsIgnoreCase("HOME")) {
                        Intent listIntent = new Intent(MainActivity.this, AppListActivity.class);
                        startActivity(listIntent);
                    } else if (type.equalsIgnoreCase("SETTINGS")) {
                        PackageManager packageManager = getPackageManager();
                        Intent intent = packageManager.getLaunchIntentForPackage("com.android.settings");
                        startActivity(intent);
                    } else if (type.equalsIgnoreCase("RESET_SETTINGS")) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_PRIVACY_SETTINGS), 0);
                    } else if (type.equalsIgnoreCase("HOME_LAUNCHER")) {
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
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    public void loadApps() {
        packageManager = getPackageManager();
        appListMainArrayList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (resolveInfo.activityInfo.packageName.equalsIgnoreCase("live.mosque.home")) {
                ivAppIcon.setImageDrawable(resolveInfo.activityInfo.loadIcon(packageManager));
                tvAppLabel.setText(resolveInfo.loadLabel(packageManager));
                isMosaqueAppExist = true;
            }
            if (resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.android.deskclock")) {
                ivAppIconP.setImageDrawable(resolveInfo.activityInfo.loadIcon(packageManager));
                tvAppLabelP.setText(resolveInfo.loadLabel(packageManager));
            }
            if (!resolveInfo.activityInfo.packageName.equalsIgnoreCase("com.android.settings")) {
                AppListMain appListMain = new AppListMain();
                appListMain.setAppIcon(resolveInfo.activityInfo.loadIcon(packageManager));
                appListMain.setAppName(resolveInfo.loadLabel(packageManager));
                appListMain.setAppPackage(resolveInfo.activityInfo.packageName);
                appListMain.setSelected(false);
                appListMainArrayList.add(appListMain);
            }
        }
        if (!isMosaqueAppExist) {
            mosaque_app.setVisibility(View.GONE);
        } else {
            mosaque_app.setVisibility(View.VISIBLE);
        }
        launcherApplication.setAppListMainArrayList(appListMainArrayList);
    }

    public boolean checkWifiStatus() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }


    public void checkWifiStatus(boolean isWifiConnected) {
        if (isWifiConnected) {
            text_wifi.setText("Wifi connected");
            wifi_image.setImageResource(R.drawable.wifi);
            text_wifi.setTextColor(Color.parseColor("#076FC1"));
        } else {
            wifi_image.setImageResource(R.drawable.wifi_red);
            text_wifi.setTextColor(Color.parseColor("#FC0000"));
            text_wifi.setText("Wifi not connected ");
        }
    }


}
