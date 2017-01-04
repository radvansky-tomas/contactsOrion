package com.radvansky.orion.contacts;

import android.app.Application;

import com.anupcowkur.reservoir.Reservoir;


public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Reservoir.init(this, 1000000); //cache size in bytes
            Reservoir.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
