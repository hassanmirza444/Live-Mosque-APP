package com.techno.launcher.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;
import com.techno.launcher.adapter.CustomAppListAdapter;
import com.techno.launcher.model.AppListMain;
import com.techno.launcher.listener.RecyclerItemClickListener;
import com.techno.launcher.ui.CustomViewGroup;
import com.techno.launcher.ui.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppListActivity extends AppCompatActivity {
    private RecyclerView rvAppList;
    private Button btn_done;
    private PackageManager packageManager;
    private CustomAppListAdapter customAppListAdapter;
    private ArrayList<AppListMain> appListMainArrayList;
    private ArrayList<AppListMain> selectedAppsArrayList;
    private AppListMain appListMain;
    private boolean isSelected = false;
    private Context context;
    private LauncherApplication launcherApplication;
    private  int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        packageManager = getPackageManager();
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        btn_done = findViewById(R.id.btn_done);
        launcherApplication = (LauncherApplication) getApplicationContext();
        appListMainArrayList = launcherApplication.getAppListMainArrayList();
        checkPermission();
        loadListView();

    }
    public void loadListView() {
        int mColumnCount = 4;
        rvAppList = findViewById(R.id.rvAppList);
        rvAppList.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        rvAppList.addItemDecoration(new GridSpacingItemDecoration(10)); // 16px. In practice, you'll want to use getDimensionPixelSize
        Collections.sort(appListMainArrayList, new Comparator<AppListMain>() {
            @Override
            public int compare(AppListMain lhs, AppListMain rhs) {
                return lhs.getAppName().toString().compareTo(rhs.getAppName().toString());
            }
        });
        customAppListAdapter = new CustomAppListAdapter(this, appListMainArrayList, isSelected);
        rvAppList.setAdapter(customAppListAdapter);
        rvAppList.addOnItemTouchListener(new RecyclerItemClickListener(this, rvAppList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                appListMain = appListMainArrayList.get(position);
                if (!isSelected) {
                    if (appListMain != null) {
                        Intent intent = packageManager.getLaunchIntentForPackage(appListMainArrayList.get(position).getAppPackage().toString());
                        startActivity(intent);
                    }
                } else {
                    if (appListMain.isSelected()) {
                        appListMainArrayList.get(position).setSelected(false);
                    } else {
                        appListMainArrayList.get(position).setSelected(true);
                    }
                    selectedAppsArrayList.add(appListMain);
                    customAppListAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAppsArrayList != null && selectedAppsArrayList.size() > 0) {
                    Utils.saveData(AppListActivity.this, selectedAppsArrayList);
                    finish();
                } else {
                    Utils.saveData(AppListActivity.this, appListMainArrayList);
                }
            }
        });
    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }else{
                preventStatusBarExpansion(context);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    checkPermission();
                } else {
                    preventStatusBarExpansion(context);
                }
            }

        }

    }
    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity)context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

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
}
