package com.wzgiceman.rxretrofitlibrary.retrofit_rx;

import android.app.Application;

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */

public class RxRetrofitApp {
    private static Application application;

    public static void init(Application app){
        setApplication(app);
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        RxRetrofitApp.application = application;
    }
}
