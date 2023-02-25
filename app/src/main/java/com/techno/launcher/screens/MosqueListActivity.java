package com.techno.launcher.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techno.launcher.LauncherApplication;
import com.techno.launcher.R;
import com.techno.launcher.Utils;
import com.techno.launcher.adapter.MosqueAdapter;
import com.techno.launcher.ui.CustomViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class MosqueListActivity extends AppCompatActivity {
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private Context context;
    private EditText searchEditText;
    private LauncherApplication launcherApplication;
    private ArrayList<Map<String, Object>> dataArrayList;
    private ArrayList<Map<String, Object>> filterDataArrayList;
    private CheckBox mosqueCheckbox;
    private CheckBox zipCheckbox;
    private CheckBox cityCheckbox;
    private RecyclerView recyclerView;
    private String type = "name";
    private MosqueAdapter adapter;
    private ImageView imageBack;
    private NestedScrollView nestedScroll;
    private boolean isKeyboardOpen = false;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private TextView textViewChangeLocation;
    private String url = "https://maps.googleapis.com/maps/api/geocode/json?address=$$&sensor=false&key=AIzaSyBbcCEAUPSY2FJh2Q_cdcBrxSzSiIxl_is";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosquelist);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        context = this;
        dataArrayList = new ArrayList<>();
        textViewChangeLocation = findViewById(R.id.textViewChangeLocation);
        if (Utils.getLatLNG(MosqueListActivity.this) != null) {
            textViewChangeLocation.setVisibility(View.VISIBLE);
            textViewChangeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZipDialog(true);
                }
            });
        }

        //   checkLocationPermission();
        launcherApplication = (LauncherApplication) getApplicationContext();
        String h = getIntent().getStringExtra("comingFrom");
        String loc = getIntent().getStringExtra("loc");
        if (h != null) {
            if (!h.equalsIgnoreCase("H")) {
                if (Utils.getSavedName(context) != null) {
                    startActivity(new Intent(context, HomeActivity.class).putExtra("isEnable", false).putExtra("type", type));
                    onBackPressed();
                    return;
                }
            }
        } else {
            if (Utils.getSavedName(context) != null) {
                startActivity(new Intent(context, HomeActivity.class).putExtra("isEnable", false).putExtra("type", type));
                onBackPressed();
                return;
            }
        }
        assignIds();
        try {
            getDataFromFieStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(Utils.getPassword(MosqueListActivity.this)) || Utils.getPassword(MosqueListActivity.this) == null) {
            showSetPasswordPopup();
        } else if (Utils.getLatLNG(MosqueListActivity.this) == null) {
            showZipDialog(false);
        }

        if (loc != null) {
            showZipDialog(true);
        }
    }

    private void assignIds() {
        nestedScroll = findViewById(R.id.nestedScroll);
        searchEditText = findViewById(R.id.searchEditText);
        mosqueCheckbox = findViewById(R.id.mosqueCheckbox);
        mosqueCheckbox.setChecked(true);
        zipCheckbox = findViewById(R.id.zipCheckBox);
        cityCheckbox = findViewById(R.id.cityCheckBox);
        recyclerView = findViewById(R.id.recyclerView);
        imageBack = findViewById(R.id.imageBack);
        String h = getIntent().getStringExtra("comingFrom");
        if (h != null) {
            imageBack.setVisibility(View.VISIBLE);
            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            imageBack.setVisibility(View.GONE);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mosqueCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "name";
                    zipCheckbox.setChecked(false);
                    cityCheckbox.setChecked(false);
                }
            }
        });
        zipCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "zip";
                    mosqueCheckbox.setChecked(false);
                    cityCheckbox.setChecked(false);
                }
            }
        });
        cityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "city";
                    zipCheckbox.setChecked(false);
                    mosqueCheckbox.setChecked(false);
                }
            }
        });
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootView);

//        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (hasFocus) {
//                            isKeyboardOpen = true;
//                            nestedScroll.smoothScrollTo(0, 100);
//                        } else {
//                            nestedScroll.smoothScrollTo(0, 0);
//
//                        }
//                    }
//                }, 1000);
//
//
//            }
//        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (dataArrayList.size() > 0) {
                        if (s != null) {
                            String typeVal = s.toString();
                            if (!TextUtils.isEmpty(s.toString())) {
                                filterDataArrayList = new ArrayList<>();
                                // zip  mosque city
                                for (int i = 0; i < dataArrayList.size(); i++) {
                                    switch (type) {
                                        case "name":
                                            String val = (String) dataArrayList.get(i).get("mosque");
                                            if (val.toLowerCase().contains(typeVal.toLowerCase())) {
                                                Log.i("VAL ", val);
                                                filterDataArrayList.add(dataArrayList.get(i));
                                            }
                                            break;
                                        case "zip":
                                            String valZip = (String) dataArrayList.get(i).get("zip");
                                            if (valZip.toLowerCase().contains(typeVal.toLowerCase())) {
                                                Log.i("VAL ", valZip);
                                                filterDataArrayList.add(dataArrayList.get(i));
                                            }
                                            break;
                                        case "city":
                                            String valCity = (String) dataArrayList.get(i).get("city");
                                            if (valCity.toLowerCase().contains(typeVal.toLowerCase())) {
                                                Log.i("VAL ", valCity);
                                                filterDataArrayList.add(dataArrayList.get(i));
                                            }
                                            break;
                                    }
                                    adapter = new MosqueAdapter(context, filterDataArrayList);
                                    recyclerView.swapAdapter(adapter, true);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getHadithDataFieStore() {
        launcherApplication.getFireStoreReference().collection("hadiths").document("english").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> hMap = documentSnapshot.getData();
                    Utils.saveHMap(context, hMap);
                }

            }
        });
    }


    private void getDataFromFieStore() {
        // now we will be getting the data from the same reference.
        launcherApplication.getFireStoreReference().collection("imams").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                d.getData();
                                Map<String, Object> aa = d.getData();// d.toObject(JSONObject.class);
                                dataArrayList.add(aa);
                            }
                            adapter = new MosqueAdapter(context, dataArrayList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(MosqueListActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            getHadithDataFieStore();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(MosqueListActivity.this, "Fail to get the data." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        try {
                            getHadithDataFieStore();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }


    private void showZipDialog(boolean isChange) {
        Dialog dialog = new Dialog(MosqueListActivity.this);
        dialog.setContentView(R.layout.dialog_password);
        dialog.setCancelable(false);
        EditText textInputEditText = dialog.findViewById(R.id.text_input_edit_text);
        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputEditText.setHint("Enter Your Home Zip Code");
        ImageView imageview_close = dialog.findViewById(R.id.imageview_close);
        if (isChange) {
            imageview_close.setVisibility(View.VISIBLE);
            imageview_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            imageview_close.setVisibility(View.GONE);
        }
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        btnSubmit.setText("Get Location");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = textInputEditText.getText().toString();
                if (TextUtils.isEmpty(zip)) {
                    textInputEditText.setError("Enter Your Home Zip Code");
                } else {
                    dialog.dismiss();
                    try {
                        Utils.showDialog(context);
                        url = url.replace("$$", zip);
                        getLatLngsFromZip(isChange);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    private void showSetPasswordPopup() {
        Dialog dialog = new Dialog(MosqueListActivity.this);
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
                    Utils.setPassword(MosqueListActivity.this, password);
                    dialog.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.getLatLNG(MosqueListActivity.this) == null) {
                                showZipDialog(false);
                            }
                        }
                    }, 0);
                }
            }
        });
        dialog.show();
        //TODO HASSAN
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                preventStatusBarExpansion(context);
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

    public void callNextScreen(ArrayList<Map<String, Object>> listData, int position) {

        if (Utils.loadMap(context) != null) {
            String newTopicS = Utils.loadMap(context).get("username") + "_sermon";
            String newTopicA = Utils.loadMap(context).get("username") + "_azan";
            launcherApplication.getFirebaseMessaging().unsubscribeFromTopic(newTopicA);
            launcherApplication.getFirebaseMessaging().unsubscribeFromTopic(newTopicS);

        }
        Map<String, Object> selectedMap = listData.get(position);
        Utils.saveMap(context, selectedMap);
        String newTopic = Utils.loadMap(context).get("username") + "_sermon";
        launcherApplication.getFirebaseMessaging().subscribeToTopic(newTopic);
        String newTopic1 = Utils.loadMap(context).get("username") + "_azan";
        launcherApplication.getFirebaseMessaging().subscribeToTopic(newTopic1);
        Utils.setSermonEnable(context, true);
        Utils.setAzanEnable(context, true);
        String sermon = (String) Utils.loadMap(context).get("sermon");
        String azan = (String) Utils.loadMap(context).get("azan");
        boolean isEnable = false;
        String type = "Done";
        if (sermon.equalsIgnoreCase("true")) {
            isEnable = true;
            type = "Sermon";
        }
        if (azan.equalsIgnoreCase("true")) {
            isEnable = true;
            type = "Azan";
        }
        String name = (String) Utils.loadMap(context).get("username");
        Utils.saveName(context, name);
        startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("isEnable", isEnable).putExtra("type", type));
        finish();
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

    private void getLatLngsFromZip(boolean isChange) {
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Utils.dismissDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                    String address = jsonObject1.getString("formatted_address");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
                    double lat = jsonObject3.getDouble("lat");
                    double lng = jsonObject3.getDouble("lng");
                    Utils.setLatLNG(context, lat + "=" + lng);
                    Utils.setAddress(context, address);
                    Toast.makeText(getApplicationContext(), "Lat " + lat + " Long " + lng + "  Address : " + address, Toast.LENGTH_LONG).show();
                    if (isChange) {
                        startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("isEnable", false).putExtra("type", type));
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Utils.dismissDialog();
                    Log.i("TAG", "Error :" + error.toString());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
