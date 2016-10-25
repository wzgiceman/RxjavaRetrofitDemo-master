package com.example.retrofit;

import android.app.Application;
import android.content.Context;

/**
 * Created by WZG on 2016/10/25.
 */

public class MyApplication extends Application{
    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=getApplicationContext();
    }
}
