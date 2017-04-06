package com.yph.beauty.ui;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

import com.yph.beauty.R;
import com.yph.beauty.base.BaseActivity;
import com.yph.beauty.util.LogUtils;

import java.io.IOException;

import butterknife.BindView;

public class SplashActivty extends BaseActivity {
    @BindView(R.id.vv_splash)
    VideoView vvSplash;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("video/guide.mp4");
            LogUtils.e("---->读取视频sucess");

        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("---->读取视频error");
        }
    }
}
