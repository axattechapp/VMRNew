package com.developers.vmrapp.service;

import android.app.Application;
import android.content.Context;

public class MyContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static Context GetContext() {
        return context;
    }

}
