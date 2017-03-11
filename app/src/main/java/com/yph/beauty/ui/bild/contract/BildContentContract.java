package com.yph.beauty.ui.bild.contract;

import com.yph.beauty.base.BasePresenter;
import com.yph.beauty.bean.BildContentInfo;
import com.yph.beauty.bean.HtmlText;

import java.util.List;

/**
 * Created by yph
 * Time is 2017/1/12 11:36
 * Good Good Study, Day Day Up
 *
 * 画报内容契约
 */

public class BildContentContract {
    public interface Presenter extends BasePresenter{
        // 获得内容数据
        void getContentData();
    }

    public interface View{
        // 显示内容
        void showContent(BildContentInfo.DataBean data, List<HtmlText[]> list, boolean isWeb);
        // 隐藏加载视图
        void hideLoadView();
    }
}
