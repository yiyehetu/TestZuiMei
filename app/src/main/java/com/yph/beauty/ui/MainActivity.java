package com.yph.beauty.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yph.beauty.R;
import com.yph.beauty.base.BaseActivity;
import com.yph.beauty.ui.bild.fragment.BildFragment;
import com.yph.beauty.ui.designer.fragment.DesignerFragment;
import com.yph.beauty.ui.havething.HavethingFragment;
import com.yph.beauty.ui.me.MeFragment;
import com.yph.beauty.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String CURRENT_ID = "currentId";
    @BindView(R.id.fl_main)
    FrameLayout mFrameLayout;
    @BindView(R.id.ll_main)
    LinearLayout mLinearLayout;
    @BindView(R.id.tv_main_bild)
    TextView mTextViewBild;
    @BindView(R.id.tv_main_havething)
    TextView mTextViewHavething;
    @BindView(R.id.tv_main_designer)
    TextView mTextViewDesigner;
    @BindView(R.id.tv_main_me)
    TextView mTextViewMe;

    // 当前位置
    private int currentId;
    private BildFragment mBildFragment;
    private HavethingFragment mHavethingFragment;
    private DesignerFragment mDesignerFragment;
    private MeFragment mMeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Fragment
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentId = R.id.tv_main_bild;
        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt(CURRENT_ID);
            mBildFragment = (BildFragment) getSupportFragmentManager().findFragmentByTag("bild");
            mHavethingFragment = (HavethingFragment) getSupportFragmentManager().findFragmentByTag("havething");
            mDesignerFragment = (DesignerFragment) getSupportFragmentManager().findFragmentByTag("designer");
            mMeFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag("me");
        } else {
            mBildFragment = new BildFragment();
            mHavethingFragment = new HavethingFragment();
            mDesignerFragment = new DesignerFragment();
            mMeFragment = new MeFragment();
            transaction.add(R.id.fl_main, mBildFragment, "bild")
                    .add(R.id.fl_main, mHavethingFragment, "havething")
                    .add(R.id.fl_main, mDesignerFragment, "designer")
                    .add(R.id.fl_main, mMeFragment, "me")
                    .commit();
        }
        setBottomNavSelected(currentId);
        switchFragmentTo(currentId);
    }

    /**
     * 切换Fragment
     *
     * @param id
     */
    private void switchFragmentTo(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.tv_main_bild:
                LogUtils.e("---->switchFragmentTo bild");
                transaction.show(mBildFragment)
                        .hide(mHavethingFragment)
                        .hide(mDesignerFragment)
                        .hide(mMeFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_main_havething:
                LogUtils.e("---->switchFragmentTo havething");
                transaction.show(mHavethingFragment)
                        .hide(mBildFragment)
                        .hide(mDesignerFragment)
                        .hide(mMeFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_main_designer:
                LogUtils.e("---->switchFragmentTo designer");
                transaction.show(mDesignerFragment)
                        .hide(mBildFragment)
                        .hide(mHavethingFragment)
                        .hide(mMeFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.tv_main_me:
                LogUtils.e("---->switchFragmentTo me");
                transaction.show(mMeFragment)
                        .hide(mBildFragment)
                        .hide(mHavethingFragment)
                        .hide(mDesignerFragment)
                        .commitAllowingStateLoss();
                break;
        }
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.tv_main_bild)
    void selectBild() {
        setBottomNavSelected(R.id.tv_main_bild);
        switchFragmentTo(R.id.tv_main_bild);
    }

    @OnClick(R.id.tv_main_havething)
    void selectHavething() {
        setBottomNavSelected(R.id.tv_main_havething);
        switchFragmentTo(R.id.tv_main_havething);
    }

    @OnClick(R.id.tv_main_designer)
    void selectDesigner() {
        setBottomNavSelected(R.id.tv_main_designer);
        switchFragmentTo(R.id.tv_main_designer);
    }

    @OnClick(R.id.tv_main_me)
    void selectMe() {
        setBottomNavSelected(R.id.tv_main_me);
        switchFragmentTo(R.id.tv_main_me);
    }

    /**
     * 设置底部导航栏选中状态
     *
     * @param id
     */
    private void setBottomNavSelected(int id) {
        currentId = id;
        switch (id) {
            case R.id.tv_main_bild:
                mTextViewBild.setSelected(true);
                mTextViewHavething.setSelected(false);
                mTextViewDesigner.setSelected(false);
                mTextViewMe.setSelected(false);
                break;
            case R.id.tv_main_havething:
                mTextViewBild.setSelected(false);
                mTextViewHavething.setSelected(true);
                mTextViewDesigner.setSelected(false);
                mTextViewMe.setSelected(false);
                break;
            case R.id.tv_main_designer:
                mTextViewBild.setSelected(false);
                mTextViewHavething.setSelected(false);
                mTextViewDesigner.setSelected(true);
                mTextViewMe.setSelected(false);
                break;
            case R.id.tv_main_me:
                mTextViewBild.setSelected(false);
                mTextViewHavething.setSelected(false);
                mTextViewDesigner.setSelected(false);
                mTextViewMe.setSelected(true);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_ID, currentId);
        LogUtils.e("---->onSaveInstanceState");
    }
}
