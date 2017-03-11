package com.yph.beauty.ui.designer.contract;

import com.yph.beauty.base.BasePresenter;

/**
 * Created by yph
 * Time is 2016/11/28 15:49
 * Good Good Study, Day Day Up
 */

public interface DesignerContract {
    interface Presenter extends BasePresenter{
        void setViewPager();
    }

    interface View {
        void showViewPager(String[] titles);
    }
}
