package com.yph.beauty.ui.designer.presenter;

import com.yph.beauty.api.ApiConst;
import com.yph.beauty.api.ApiManager;
import com.yph.beauty.api.ApiService;
import com.yph.beauty.bean.DesignType;
import com.yph.beauty.bean.DesignerInfo;
import com.yph.beauty.ui.designer.contract.ProjectContract;
import com.yph.beauty.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yph
 * Time is 2016/12/1 12:00
 * Good Good Study, Day Day Up
 *
 * 设计师分类展示Presenter
 */

public class ProjectPresenter implements ProjectContract.Preseneter {
    private final String TAG = ProjectPresenter.class.getSimpleName();
    private ProjectContract.View mView;
    private int currFragment;
    private Map<String, String> map = new HashMap();
    private boolean hasNext;
    private int currPage = 1;

    public ProjectPresenter(ProjectContract.View view, int currFragment) {
        mView = view;
        this.currFragment = currFragment;
        map.putAll(ApiConst.BASE_MAP);
    }

    @Override
    public void start() {
        getListData();
    }

    @Override
    public void stop() {

    }

    // 根据位置获取被观察者对象
    private Observable<DesignerInfo> getObservable(boolean isFirst) {
        if (isFirst) {
            // 第1页
            map.put("page", "1");
            currPage = 1;
        } else {
            // 更多页
            if (!hasNext) {
                return null;
            }
            map.put("page", String.valueOf(++currPage));
        }

        ApiService apiService = ApiManager.getInstance().getApiService();
        switch (currFragment) {
            // 我关注的
            case 0:
                return apiService.getFollowUserInfo(ApiConst.BASE_MAP.get("user_id"), map);
            // 推荐
            case 1:
                return apiService.getRecommendInfo(map);
            // 最受欢迎
            case 2:
                return apiService.getMostFavorInfo(map);
            // 独立设计师
            case 3:
                return apiService.getDesignerInfo(30, map);
            // 大牌设计师
            default:
                return apiService.getDesignerInfo(31, map);
        }
    }

    // 根据id获取被观察者对象
    private Observable<DesignerInfo> getObservable(boolean isFirst, int id) {
        if (isFirst) {
            // 第1页
            map.put("page", "1");
            currPage = 1;
        } else {
            // 更多页
            if (!hasNext) {
                return null;
            }
            map.put("page", String.valueOf(++currPage));
        }

        ApiService apiService = ApiManager.getInstance().getApiService();
        return apiService.getDesignerInfo(id, map);
    }

    // 根据id获取被观察者对象
    private Observable<DesignerInfo> getObservable2(boolean isFollow, int id) {
        ApiService apiService = ApiManager.getInstance().getApiService();
        String userId = ApiConst.BASE_MAP.get("user_id");
        if(isFollow) {
            return apiService.followDesigner(id, ApiConst.BASE_MAP, userId);
        }else {
            return apiService.unFollowDesigner(id, ApiConst.BASE_MAP, userId);
        }
    }

    @Override
    public void getListData() {
        Observable<DesignerInfo> designerInfo = getObservable(true);
        designerInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DesignerInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "---->onCompleted");
                        mView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "---->" + e.getMessage());
                        mView.showListError();
                        mView.finishRefresh();
                    }

                    @Override
                    public void onNext(DesignerInfo designerInfo) {
                        LogUtils.e(TAG, "---->" + designerInfo.toString());
                        if (designerInfo.getResult() == 1) {
                            // 赋值是否有更多
                            hasNext = (designerInfo.getData().getHas_next() == 1) ? true : false;
                            mView.showList(designerInfo);
                        } else {
                            mView.showListError();
                        }
                    }
                });

    }

    @Override
    public void getListData(int id) {
        Observable<DesignerInfo> designerInfo = getObservable(true, id);
        designerInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DesignerInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "---->onCompleted");
                        mView.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "---->" + e.getMessage());
                        mView.showListError();
                        mView.finishRefresh();
                    }

                    @Override
                    public void onNext(DesignerInfo designerInfo) {
                        LogUtils.e(TAG, "---->" + designerInfo.toString());
                        if (designerInfo.getResult() == 1) {
                            // 赋值是否有更多
                            hasNext = (designerInfo.getData().getHas_next() == 1) ? true : false;
                            mView.showList(designerInfo);
                        } else {
                            mView.showListError();
                        }
                    }
                });
    }

    @Override
    public void getMoreListData() {
        Observable<DesignerInfo> designerInfo = getObservable(false);
        if (designerInfo == null) {
            // 没有更多
            return;
        }
        designerInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DesignerInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "---->onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "---->" + e.getMessage());
                        mView.showListError();
                    }

                    @Override
                    public void onNext(DesignerInfo designerInfo) {
                        LogUtils.e(TAG, "---->" + designerInfo.toString());
                        if (designerInfo.getResult() == 1) {
                            // 赋值是否有更多
                            hasNext = (designerInfo.getData().getHas_next() == 1) ? true : false;
                            mView.showMoreList(designerInfo);
                        } else {
                            mView.showListError();
                        }
                    }
                });
    }

    @Override
    public void getMoreListData(int id) {
        Observable<DesignerInfo> designerInfo = getObservable(false, id);
        if (designerInfo == null) {
            // 没有更多
            return;
        }
        designerInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DesignerInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "---->onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "---->" + e.getMessage());
                        mView.showListError();
                    }

                    @Override
                    public void onNext(DesignerInfo designerInfo) {
                        LogUtils.e(TAG, "---->" + designerInfo.toString());
                        if (designerInfo.getResult() == 1) {
                            // 赋值是否有更多
                            hasNext = (designerInfo.getData().getHas_next() == 1) ? true : false;
                            mView.showMoreList(designerInfo);
                        } else {
                            mView.showListError();
                        }
                    }
                });
    }

    @Override
    public void getNavBarData() {
        List<DesignType> designTypes = new ArrayList<>();
        designTypes.add(new DesignType("全部", 30));
        designTypes.add(new DesignType("首饰", 1));
        designTypes.add(new DesignType("包袋", 3));
        designTypes.add(new DesignType("鞋履", 4));
        designTypes.add(new DesignType("配饰", 2));
        designTypes.add(new DesignType("家居", 27));
        designTypes.add(new DesignType("其他", 26));
        designTypes.add(new DesignType("", -1));
        designTypes.add(new DesignType("", -1));
        mView.showDesignType(designTypes);
    }

    @Override
    public void follow(boolean isFollow, int id) {
        Observable<DesignerInfo> designerInfo = getObservable2(isFollow, id);
        designerInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DesignerInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "---->onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "---->" + e.getMessage());
                    }

                    @Override
                    public void onNext(DesignerInfo designerInfo) {
                        LogUtils.e(TAG, "---->" + designerInfo.toString());
                    }
                });
    }
}
