package com.techno.launcher.client;


import com.techno.launcher.model.PrayerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/v1/calendar")
    Call<PrayerModel> getLocalData(@Query("year") String year, @Query("month") String month, @Query("method") String method, @Query("latitude") double latitude, @Query("longitude") double longitude);


}