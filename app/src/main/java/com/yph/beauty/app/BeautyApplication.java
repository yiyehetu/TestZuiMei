package com.yph.beauty.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.yph.beauty.api.ApiConst;
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

        setNetBaseMap();
    }

    /**
     * 设置网络请求参数
     */
    private void setNetBaseMap() {
        ApiConst.BASE_MAP.put("page_size", "30");
        // http://design.zuimeia.com/api/v1/designers/recommend/?page=1
        // &page_size=30&device_id=357329076420100&platform=android
        // &lang=zh&appVersion=1.2.6&appVersionCode=10260&systemVersion=23
        // &countryCode=CN&user_id=88087
        // &token=4hb6bf196967c803e626106&package_name=com.zuiapps.zuiworld
        // READ_PHONE_STATE权限
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        ApiConst.BASE_MAP.put("device_id", deviceId);
        ApiConst.BASE_MAP.put("platform", "android");
        ApiConst.BASE_MAP.put("lang", "zh");
        // appVersion
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            ApiConst.BASE_MAP.put("appVersion", packageInfo.versionName);
            ApiConst.BASE_MAP.put("appVersionCode", packageInfo.versionCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ApiConst.BASE_MAP.put("systemVersion", Build.VERSION.SDK_INT + "");
        ApiConst.BASE_MAP.put("countryCode", "CN");
        ApiConst.BASE_MAP.put("user_id", "88087");
        ApiConst.BASE_MAP.put("token", "4hb6bf196967c803e626106");
        ApiConst.BASE_MAP.put("package_name", "com.zuiapps.zuiworld");

    }
}
