package com.techno.launcher;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techno.launcher.global.LiveMosqueSingleTone;
import com.techno.launcher.model.AppListMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Utils {


    private static Dialog dialog;

    public static void showDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.show();
    }

    public static void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static String getLatLNG(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("latlng", null);
        return value;
    }

    public static void setLatLNG(Context context, String latlng) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latlng", latlng);
        editor.apply();

    }

    public static String getAddress(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("address", null);
        return value;
    }

    public static void setAddress(Context context, String address) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", address);
        editor.apply();

    }


    public static boolean isTimer = false;


    public static String getLastValue(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("LAST_VALUE", null);
        return value;
    }

    public static void setLastValue(Context context, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LAST_VALUE", value);
        editor.apply();

    }

    public static String getLastValueR(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString("LAST_VALUE_R", null);
        return value;
    }

    public static void setLastValueR(Context context, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LAST_VALUE_R", value);
        editor.apply();

    }


    public static String getCurrentDayOfweek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();

        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek.toLowerCase();
    }


    public static boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        return connected;
    }


    public static int getTimeDifference(String d1, String d2) {
        try {
            String firstZone = d1.split(" ")[1];
            String secondZone = d2.split(" ")[1];
            if (!firstZone.equalsIgnoreCase(secondZone)) {
                return 50;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            Date date1 = simpleDateFormat.parse(d1);
            Date date2 = simpleDateFormat.parse(d2);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            if (hours == 0) {

                return min;
            } else {

                return 50;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }

        return false;
    }


    public static String getPassword(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString("PASSWORD", null);
        return token;
    }

    public static void setPassword(Context context, String data) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PASSWORD", data);
        editor.apply();

    }
    public static String getCurrentDay() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        Log.i("Day ,", formattedDate.split("-")[0]);
        return formattedDate.split("-")[0];
    }


    public static String getPrayerTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String method = preferences.getString("PRAYER_TIME_METHOD", "OTHER");
        return method;
    }



    public static void setPrayerTime(Context context, String method) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PRAYER_TIME_METHOD", method);
        editor.apply();

    }

    public static void saveHMap(Context context, Map<String, Object> inputMap) {

        LiveMosqueSingleTone.getInstance().sethMap(inputMap);

//        SharedPreferences pSharedPref = context.getApplicationContext().getSharedPreferences("HDATA", Context.MODE_PRIVATE);
//        if (pSharedPref != null) {
//            JSONObject jsonObject = new JSONObject(inputMap);
//            String jsonString = jsonObject.toString();
//            pSharedPref.edit()
//                    .remove("H_map")
//                    .putString("H_map", jsonString)
//                    .apply();
//        }
    }


    public static Map<String, Object> loadHMap(Context context) {
        Map<String, Object> outputMap = LiveMosqueSingleTone.getInstance().gethMap();



//        SharedPreferences pSharedPref = context.getApplicationContext().getSharedPreferences("HDATA", Context.MODE_PRIVATE);
//        try {
//            if (pSharedPref != null) {
//                String jsonString = pSharedPref.getString("H_map", (new JSONObject()).toString());
//                if (jsonString != null && !jsonString.equalsIgnoreCase("{}")) {
//                    outputMap = new HashMap<>();
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    Iterator<String> keysItr = jsonObject.keys();
//                    while (keysItr.hasNext()) {
//                        String key = keysItr.next();
//                        Object value = jsonObject.get(key);
//                        outputMap.put(key, value);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return outputMap;
    }


    public static void saveMap(Context context, Map<String, Object> inputMap) {

        LiveMosqueSingleTone.getInstance().setDateMap(inputMap);

//        SharedPreferences pSharedPref = context.getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
//        if (pSharedPref != null) {
//            JSONObject jsonObject = new JSONObject(inputMap);
//            String jsonString = jsonObject.toString();
//            pSharedPref.edit()
//                    .remove("My_map")
//                    .putString("My_map", jsonString)
//                    .apply();
//        }
    }

    public static Map<String, Object> loadMap(Context context) {


        Map<String, Object> outputMap = LiveMosqueSingleTone.getInstance().getDateMap();
//        SharedPreferences pSharedPref = context.getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
//        try {
//            if (pSharedPref != null) {
//                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
//                if (jsonString != null && !jsonString.equalsIgnoreCase("{}")) {
//                    outputMap = new HashMap<>();
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                    Iterator<String> keysItr = jsonObject.keys();
//                    while (keysItr.hasNext()) {
//                        String key = keysItr.next();
//                        Object value = jsonObject.get(key);
//                        outputMap.put(key, value);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return outputMap;
    }


    public static boolean isAzanEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isFirstTime = preferences.getBoolean("AZAN", false);
        return isFirstTime;
    }

    public static void setAzanEnable(Context context, boolean isFirstTime) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("AZAN", isFirstTime);
        editor.apply();

    }


    public static boolean isSermonEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isFirstTime = preferences.getBoolean("SERMON", false);
        return isFirstTime;
    }

    public static void setSermonEnable(Context context, boolean isFirstTime) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("SERMON", isFirstTime);
        editor.apply();

    }

    public static void saveData(Context context, ArrayList<AppListMain> selectedAppsArrayList) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedAppsArrayList);
        editor.putString("apps", json);
        editor.apply();
    }

    public static ArrayList<AppListMain> loadApps(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("apps", null);
        Type type = new TypeToken<ArrayList<AppListMain>>() {
        }.getType();
        ArrayList<AppListMain> selectedAppsArrayList = gson.fromJson(json, type);
        return selectedAppsArrayList;

    }




    public static String getSavedName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = preferences.getString("SAVE_NAME", null);
        return name;
    }



    public static void saveName(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SAVE_NAME", name);
        editor.apply();

    }

}
