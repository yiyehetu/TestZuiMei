package com.yph.beauty.ui.bild.contract;

import com.yph.beauty.base.BasePresenter;
import com.yph.beauty.bean.BildInfo;

/**
 * Created by yph
 * Time is 2016/12/27 17:38
 * Good Good Study, Day Day Up
 *
 * 画报 契约
 */

public class BildContract {
    public interface Presenter extends BasePresenter{
        // 获得堆栈数据
        void getStackData(int page);
        // 获得更多堆栈数据
        void getMoreStackData();
    }

    public interface View{
        // 显示堆栈卡片
        void showStack(BildInfo bildInfo);
        // 显示更多堆栈卡片
        void showMoreStack(BildInfo bildInfo);
        // 显示堆栈卡片失败
        void showStackError();
    }
}
