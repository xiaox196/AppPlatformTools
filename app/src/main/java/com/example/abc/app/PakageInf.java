package com.example.abc.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by xiaoming on 2015/9/17.
 */
public class PakageInf {
    private String pakage_name;
    private String mainactivity;
    private Drawable appicon;
    private Intent intent;
    private  String activiy;

    public String getActiviy() {
        return activiy;
    }

    public void setActiviy(String activiy) {
        this.activiy = activiy;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public Drawable getAppicon() {
        return appicon;
    }

    public String getPakage_name() {
        return pakage_name;
    }

    public String getMainactivity() {
        return mainactivity;
    }

    public void setPakage_name(String pakage_name) {
        this.pakage_name = pakage_name;
    }

    public void setMainactivity(String mainactivity) {
        this.mainactivity = mainactivity;
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = appicon;
    }
}
