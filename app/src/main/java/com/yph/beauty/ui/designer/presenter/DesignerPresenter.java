package com.yph.beauty.ui.designer.presenter;

import com.yph.beauty.ui.designer.contract.DesignerContract;

/**
 * Created by yph
 * Time is 2016/11/28 17:04
 * Good Good Study, Day Day Up
 */

public class DesignerPresenter implements DesignerContract.Presenter {
    private DesignerContract.View mView;

    public DesignerPresenter(DesignerContract.View view) {
        mView = view;
    }

    @Override
    public void setViewPager() {
        String [] titles = new String[]{"我关注的", "推荐", "最受欢迎", "独立设计师", "大牌设计师"};
        mView.showViewPager(titles);
    }

    @Override
    public void start() {
        setViewPager();
    }

    @Override
    public void stop() {

    }
}
