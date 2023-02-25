package com.techno.launcher.client;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.techno.launcher.model.PrayerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerRepository {
    private MutableLiveData<PrayerModel> addressResponseLiveData = new MutableLiveData<>();

    public void getLocalData(String year, String month, String method , double lat, double lng) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient();
        apiInterface.getLocalData(year, month, method, lat, lng)
                .enqueue(new Callback<PrayerModel>() {
                    @Override
                    public void onResponse(Call<PrayerModel> call, Response<PrayerModel> response) {
                        if (response.isSuccessful()) {
                            addressResponseLiveData.postValue(response.body());
                        }else{
                            addressResponseLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<PrayerModel> call, Throwable t) {
                        addressResponseLiveData.postValue(null);
                    }
                });
    }

    public LiveData<PrayerModel> getAddressResponseLiveData() {
        return addressResponseLiveData;
    }
}
