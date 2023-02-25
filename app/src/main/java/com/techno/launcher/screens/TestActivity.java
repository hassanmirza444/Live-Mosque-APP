package com.techno.launcher.screens;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.GeoPoint;
import com.techno.launcher.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnLocation;
    private TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        editText = findViewById(R.id.editText);
        btnLocation = findViewById(R.id.btnLocation);
        textView = findViewById(R.id.textView);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = editText.getText().toString();
                if (zipCode != null && !TextUtils.isEmpty(zipCode)) {
                    //getLocationFromAddress(zipCode);


                } else {
                    Toast.makeText(TestActivity.this, "Please enter Zip code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
