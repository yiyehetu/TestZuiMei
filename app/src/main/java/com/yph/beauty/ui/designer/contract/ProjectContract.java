package com.yph.beauty.ui.designer.contract;

import com.yph.beauty.base.BasePresenter;
import com.yph.beauty.bean.DesignType;
import com.yph.beauty.bean.DesignerInfo;

import java.util.List;

/**
 * Created by yph
 * Time is 2016/11/30 17:09
 * Good Good Study, Day Day Up
 *
 * 设计师接口契约
 */

public interface ProjectContract {
    interface Preseneter extends BasePresenter {
        void getListData();
        // 根据id获取分类数据
        void getListData(int id);
        void getMoreListData();
        void getMoreListData(int id);
        // 获得类型
        void getNavBarData();
        // 关注/取消
        void follow(boolean isFollow, int id);
    }

    interface View {
        // 显示列表
        void showList(DesignerInfo designerInfo);
        void showMoreList(DesignerInfo designerInfo);
        void showListError();
        // 停止刷新
        void finishRefresh();
        // 显示类型
        void showDesignType(List<DesignType> designTypes);
    }
}
