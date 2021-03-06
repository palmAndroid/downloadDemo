package com.downloaddemo;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }
    public void setConnectivityListener(ConnectivityReceiverInternet.ConnectivityReceiverListener listener) {
        ConnectivityReceiverInternet.connectivityReceiverListener = listener;
    }
}
