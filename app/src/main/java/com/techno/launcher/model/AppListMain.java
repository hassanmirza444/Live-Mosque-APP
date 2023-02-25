package com.techno.launcher.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppListMain implements Serializable {
    Drawable appIcon;
    CharSequence appName;
    CharSequence appPackage;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public CharSequence getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(CharSequence appPackage) {
        this.appPackage = appPackage;
    }
}
