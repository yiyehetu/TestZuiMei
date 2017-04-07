package com.yph.beauty.ui;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.yph.beauty.R;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.app.BeautyApp;
import com.yph.beauty.base.BaseActivity;
import com.yph.beauty.util.LogUtils;

import java.io.IOException;

import butterknife.BindView;

public class SplashActivty extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    @BindView(R.id.sv_splash)
    SurfaceView svSplash;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private boolean isEntered;
    private static final String[] PERMS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE};
    private static final int PERMISSION_REQ = 200;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        isEntered = BeautyApp.getSpUtil(this).getBoolean(AppConst.IS_ENTERED);

        if (isEntered) {
            ivSplash.setVisibility(View.VISIBLE);
            checkPermissions(PERMISSION_REQ, PERMS);
        } else {
            // 第一次进入
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
    public void surfaceCreated(SurfaceHolder holder) {
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(holder);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        // 读取asset视频
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("video/guide.mp4");
            LogUtils.e("---->读取视频sucess");

            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            player.prepare();
            player.start();
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
        // 播放完成便不再播放
        BeautyApp.getSpUtil(this).put(AppConst.IS_ENTERED, true);
        // 权限检查
        afterFinishPlay(player);
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        afterFinishPlay(player);
        return false;
    }

    private void afterFinishPlay(MediaPlayer player) {
        player.release();
        svSplash.setVisibility(View.GONE);
    }
}
