package com.yph.beauty.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.yph.beauty.R;
import com.yph.beauty.api.ApiConst;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.app.BeautyApp;
import com.yph.beauty.base.BaseActivity;
import com.yph.beauty.util.LogUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.functions.Action1;

public class SplashActivty extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    @BindView(R.id.sv_splash)
    SurfaceView svSplash;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private MediaPlayer mediaPlayer;
    private boolean isEntered;
    private static final String[] PERMS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE};
    private static final String[] PERMS_DETAIL = {"存储", "系统信息"};
    private static final int PERMISSION_REQ = 200;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        isEntered = BeautyApp.getSpUtil(this).getBoolean(AppConst.IS_ENTERED);

        if (isEntered) {
            checkPermissions();
        } else {
            // 第一次进入
            BeautyApp.getSpUtil(this).put(AppConst.IS_ENTERED, true);
            ivSplash.setVisibility(View.GONE);
            svSplash.setVisibility(View.VISIBLE);
            SurfaceHolder holder = svSplash.getHolder();
            holder.addCallback(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            // 释放资源
            mediaPlayer.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(holder);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        // 读取asset视频
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("video/guide.mp4");
            LogUtils.e("---->读取视频sucess");

            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("---->读取视频error");
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    // 播放完成
    @Override
    public void onCompletion(MediaPlayer player) {
        checkPermissions();
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        svSplash.setVisibility(View.GONE);
        ivSplash.setVisibility(View.VISIBLE);
        checkPermissions();
        return false;
    }

    // 权限检查
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            skipToMain();
        } else {
            EasyPermissions.requestPermissions(this, "需要存储和系统信息权限应用才能正常工作", PERMISSION_REQ, PERMS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (PERMISSION_REQ == requestCode && EasyPermissions.hasPermissions(this, PERMS)) {
            // 权限全部允许
            LogUtils.e("---->权限全部允许");
            skipToMain();
        }
    }

    // 跳转
    private void skipToMain() {
        setNetBaseMap();
        if (isEntered) {
            Observable.timer(1500, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            startActivity(MainActivity.class);
                            finish();
                        }
                    });
        } else {
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (PERMISSION_REQ == requestCode) {
            // 读取拒绝权限
            int size = perms.size();
            String[] permsDetail = new String[size];
            for (int i = 0; i < size; i++) {
                String perm = perms.get(i);
                if (perm.equals(PERMS[0])) {
                    permsDetail[i] = PERMS_DETAIL[0];
                } else if (perm.equals(PERMS[1])) {
                    permsDetail[i] = PERMS_DETAIL[1];
                }
            }

            // 拼接提示
            StringBuilder builder = new StringBuilder("需要");
            for (int i = 0; i < size; i++) {
                if (i == 1) {
                    builder.append("和");
                }
                builder.append(permsDetail[i]);
            }

            builder.append("权限应用才能正常工作");
            String[] deniedPerms = new String[size];
            perms.toArray(deniedPerms);

            EasyPermissions.requestPermissions(SplashActivty.this, builder.toString(), PERMISSION_REQ, deniedPerms);
        }
    }

    // 设置网络请求参数
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
