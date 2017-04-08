package com.yph.beauty.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.yph.beauty.app.AppManager;

import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    // 子Activity加载布局
    protected abstract int getLayoutId();

    // 初始化View
    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预处理
        doBeforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
    }

/*    protected boolean checkPermissions(int requestCode, String[] PERMS) {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            LogUtils.e("---->拥有权限");
            return true;

        } else {
            LogUtils.e("---->请求权限");
            EasyPermissions.requestPermissions(this, "App需要权限才能正常工作", requestCode, PERMS);
            return false;
        }
    }*/

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

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
//        EasyPermissions.requestPermissions(this, "需要读写权限", PERMISSION_REQ, PERMS);
    }

    // 暂时未用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "需要读写权限", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }
}
