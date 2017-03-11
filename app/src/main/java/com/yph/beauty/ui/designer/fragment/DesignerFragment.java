package com.yph.beauty.ui.designer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yph.beauty.R;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.base.BaseFragment;
import com.yph.beauty.ui.designer.contract.DesignerContract;
import com.yph.beauty.ui.designer.presenter.DesignerPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yph
 * Time is 2016/11/24 15:37
 * Good Good Study, Day Day Up
 *
 * 设计师页面
 */

public class DesignerFragment extends BaseFragment implements DesignerContract.View{
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private DesignerPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_designer;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DesignerPresenter(this);
        mPresenter.start();
    }

    @Override
    protected void initView() {
//        TabLayoutUtils.setIndicator(getContext(), mTabLayout, 20, 20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void showViewPager(String[] titles) {
        mViewPager.setAdapter(new DesignerAdapter(getFragmentManager(), titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class DesignerAdapter extends FragmentPagerAdapter{
        private String[] mTitles;
        private List<ProjectFragment> mFragments = new ArrayList<>();

        public DesignerAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            mTitles = titles;
            initFragments(titles.length);
        }

        private void initFragments(int length) {
            for(int index = 0; index < length; index++) {
                ProjectFragment fragment = new ProjectFragment();
                Bundle args = new Bundle();
                args.putInt(AppConst.FRAGMENT_POSITON, index);
                fragment.setArguments(args);
                mFragments.add(fragment);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }
}
