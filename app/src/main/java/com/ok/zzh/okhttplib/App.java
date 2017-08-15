package com.ok.zzh.okhttplib;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/11.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.addLogAdapter(new AndroidLogAdapter());

    }
}
