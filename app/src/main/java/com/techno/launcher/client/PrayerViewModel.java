package com.techno.launcher.client;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.techno.launcher.model.PrayerModel;

import java.util.List;

public class PrayerViewModel extends AndroidViewModel {
    private PrayerRepository prayerRepository;
    private LiveData<PrayerModel> volumesResponseLiveData;

    public PrayerViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        prayerRepository = new PrayerRepository();
        volumesResponseLiveData = prayerRepository.getAddressResponseLiveData();
    }

    public void getLocalData(String year, String month, String method , double lat, double lng) {
        prayerRepository.getLocalData( year,  month,  method ,  lat,  lng);
    }

    public LiveData<PrayerModel> getLocalDataResponseLiveData() {
        return volumesResponseLiveData;
    }
}
