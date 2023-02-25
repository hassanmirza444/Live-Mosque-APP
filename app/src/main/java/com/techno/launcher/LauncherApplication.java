package com.techno.launcher;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techno.launcher.global.AppLifecycleListener;
import com.techno.launcher.listener.ConnectivityReceiverInternet;
import com.techno.launcher.model.AppListMain;
import com.techno.launcher.screens.HomeActivity;

import net.time4j.android.ApplicationStarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LauncherApplication extends Application {
    private Context context;
    public ArrayList<AppListMain> getAppListMainArrayList() {
        return appListMainArrayList;
    }

    public FirebaseFirestore getFireStoreReference() {
        return fireStoreReference;
    }

    private FirebaseFirestore fireStoreReference;

    public void setAppListMainArrayList(ArrayList<AppListMain> appListMainArrayList) {
        this.appListMainArrayList = appListMainArrayList;
    }

    private ArrayList<AppListMain> appListMainArrayList;

    private static LauncherApplication mInstance;

    public FirebaseMessaging getFirebaseMessaging() {
        return firebaseMessaging;
    }

    private FirebaseMessaging firebaseMessaging;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;
        FirebaseApp.initializeApp(this);
        fireStoreReference = FirebaseFirestore.getInstance();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener(this));



        firebaseMessaging = FirebaseMessaging.getInstance();
        ApplicationStarter.initialize(this, true);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Power:");
        wl.acquire();

    }

    public static synchronized LauncherApplication getInstance() {
        return mInstance;
    }

    private Map<String, Object> outputMap = null;
    private void getDataFromFieStore() {
        // now we will be getting the data from the same reference.
        getFireStoreReference().collection("imams").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    if (!value.isEmpty()) {
                        List<DocumentSnapshot> list = value.getDocuments();
                        for (DocumentSnapshot d : list) {
                            d.getData();
                            Map<String, Object> map = d.getData();
                            String name = (String) map.get("username");
                            String savedName = (String) outputMap.get("username");
                            if (savedName.equalsIgnoreCase(name)) {
                                Utils.saveMap(context, map);
                            }
                        }
                    } else {
                       // Toast.makeText(HomeActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}



