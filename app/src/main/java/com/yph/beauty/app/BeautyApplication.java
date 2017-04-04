package com.yph.beauty.app;

import android.app.Application;

import com.yph.beauty.util.LogUtils;

/**
 * Created by yph
 * Time is 2016/11/24 16:16
 * Good Good Study, Day Day Up
 */

public class BeautyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init(true, 'v', "TAG");

    }
}
