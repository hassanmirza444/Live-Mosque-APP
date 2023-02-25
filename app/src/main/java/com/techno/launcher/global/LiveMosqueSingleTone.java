package com.techno.launcher.global;

import com.batoulapps.adhan.PrayerTimes;

import java.util.Map;

public class LiveMosqueSingleTone {

    public Map<String, Object> getDateMap() {
        return dateMap;
    }

    public void setDateMap(Map<String, Object> dateMap) {
        this.dateMap = dateMap;
    }

    private Map<String, Object> dateMap;

    public Map<String, Object> gethMap() {
        return hMap;
    }

    public void sethMap(Map<String, Object> hMap) {
        this.hMap = hMap;
    }

    private Map<String, Object> hMap;

    public boolean isSermonRunning() {
        return isSermonRunning;
    }

    public void setSermonRunning(boolean sermonRunning) {
        isSermonRunning = sermonRunning;
    }

    private boolean isSermonRunning = false;

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    private boolean isStreaming = false;
    private static LiveMosqueSingleTone liveMosqueSingleTone;

    public PrayerTimes getPrayerTimes() {
        return prayerTimes;
    }

    public void setPrayerTimes(PrayerTimes prayerTimes) {
        this.prayerTimes = prayerTimes;
    }

    private PrayerTimes prayerTimes;

    public static LiveMosqueSingleTone getInstance() {
        if (liveMosqueSingleTone == null) {
            liveMosqueSingleTone = new LiveMosqueSingleTone();
        }
        return liveMosqueSingleTone;
    }

}
