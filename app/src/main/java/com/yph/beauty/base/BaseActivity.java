package com.yph.beauty.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.yph.beauty.api.ApiConst;
import com.yph.beauty.app.AppManager;
import com.yph.beauty.util.LogUtils;

import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    // 子Activity加载布局
    protected abstract int getLayoutId();

    // 初始化View
    protected abstract void initView();

    private static final int PERMISSION_REQ = 200;
    private static final String[] PERMS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预处理
        doBeforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        checkPermissions();
    }

    @AfterPermissionGranted(PERMISSION_REQ)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            LogUtils.e("---->拥有写权限");
            // 获取基本信息
            if(ApiConst.BASE_MAP.isEmpty()){
                setNetBaseMap();
            }
        } else {
            LogUtils.e("---->请求写权限");
            EasyPermissions.requestPermissions(this, "需要读写权限", PERMISSION_REQ, PERMS);
        }
    }

    protected void doBeforeSetContentView() {
        // 添加Activity至栈中
        AppManager.getInstance().addActivity(this);
        // 透明状态栏(延伸)
        /*
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        */
        // 沉浸状态栏
        /**
         * 布局设置属性
         *  android:fitsSystemWindows="true"
         *  android:clipToPadding="true"
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        ScreenUtils.setTranslucentStatus(this, true);


        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    // 通过Class跳转界面
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    // 通过Class跳转界面
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    // 含有Bundle通过Class跳转界面
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    // 含有Bundle通过Class跳转界面
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除Activity
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.e("---->权限允许");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogUtils.e("---->权限拒绝");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
//        EasyPermissions.requestPermissions(this, "需要读写权限", PERMISSION_REQ, PERMS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "需要读写权限", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
