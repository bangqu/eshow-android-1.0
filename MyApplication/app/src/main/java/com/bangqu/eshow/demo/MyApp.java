package com.bangqu.eshow.demo;

import android.support.multidex.MultiDexApplication;

/**
 * Created by daikting on 16/2/19.
 */
public class MyApp extends MultiDexApplication {
    public static float sScale;

    @Override
    public void onCreate() {
        super.onCreate();
        sScale = getResources().getDisplayMetrics().density;

    }
}
