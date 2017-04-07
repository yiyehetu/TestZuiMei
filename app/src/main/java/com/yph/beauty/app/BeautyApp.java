package com.yph.beauty.app;

import android.app.Application;
import android.content.Context;

import com.yph.beauty.util.LogUtils;
import com.yph.beauty.util.SpUtils;

/**
 * Created by yph
 * Time is 2016/11/24 16:16
 * Good Good Study, Day Day Up
 */

public class BeautyApp extends Application {
    private static volatile SpUtils spUtil = null;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init(true, 'v', "TAG");

    }

    public static SpUtils getSpUtil(Context context){
        if(spUtil == null){
            synchronized (BeautyApp.class){
                if(spUtil == null){
                    spUtil = new SpUtils("spdata", context.getApplicationContext());
                }
            }
        }
        return spUtil;
    }
}
