package com.techno.launcher.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrayerModel {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @SerializedName("status")
    private String status;


    @SerializedName("code")
    private int code;


    @SerializedName("data")
    private List<Data> data;




    public class Data{
        public Timings getTimings() {
            return timings;
        }

        public void setTimings(Timings timings) {
            this.timings = timings;
        }

        @SerializedName("timings")
        private Timings timings;



    }
    public class Timings{

        @SerializedName("Sunset")
        private String Sunset;


        @SerializedName("Sunrise")
        private String Sunrise;

        @SerializedName("Asr")
        private String Asr;

        @SerializedName("Dhuhr")
        private String Dhuhr;

        @SerializedName("Fajr")
        private String Fajr;

        @SerializedName("Firstthird")
        private String Firstthird;

        @SerializedName("Imsak")
        private String Imsak;

        @SerializedName("Isha")
        private String Isha;

        @SerializedName("Lastthird")
        private String Lastthird;

        @SerializedName("Maghrib")
        private String Maghrib;

        public String getSunset() {
            return Sunset;
        }

        public void setSunset(String sunset) {
            Sunset = sunset;
        }

        public String getSunrise() {
            return Sunrise;
        }

        public void setSunrise(String sunrise) {
            Sunrise = sunrise;
        }

        public String getAsr() {
            return Asr;
        }

        public void setAsr(String asr) {
            Asr = asr;
        }

        public String getDhuhr() {
            return Dhuhr;
        }

        public void setDhuhr(String dhuhr) {
            Dhuhr = dhuhr;
        }

        public String getFajr() {
            return Fajr;
        }

        public void setFajr(String fajr) {
            Fajr = fajr;
        }

        public String getFirstthird() {
            return Firstthird;
        }

        public void setFirstthird(String firstthird) {
            Firstthird = firstthird;
        }

        public String getImsak() {
            return Imsak;
        }

        public void setImsak(String imsak) {
            Imsak = imsak;
        }

        public String getIsha() {
            return Isha;
        }

        public void setIsha(String isha) {
            Isha = isha;
        }

        public String getLastthird() {
            return Lastthird;
        }

        public void setLastthird(String lastthird) {
            Lastthird = lastthird;
        }

        public String getMaghrib() {
            return Maghrib;
        }

        public void setMaghrib(String maghrib) {
            Maghrib = maghrib;
        }

        public String getMidnight() {
            return Midnight;
        }

        public void setMidnight(String midnight) {
            Midnight = midnight;
        }

        @SerializedName("Midnight")
        private String Midnight;


    }
}
