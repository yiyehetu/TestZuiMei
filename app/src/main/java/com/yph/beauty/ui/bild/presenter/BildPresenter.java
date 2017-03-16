package com.yph.beauty.ui.bild.presenter;

import com.yph.beauty.api.ApiConst;
import com.yph.beauty.api.ApiManager;
import com.yph.beauty.bean.BildInfo;
import com.yph.beauty.ui.bild.contract.BildContract;
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
 * Time is 2016/12/27 17:36
 * Good Good Study, Day Day Up
 *
 * 画报P
 */

public class BildPresenter implements BildContract.Presenter{
    private static final String TAG = BildPresenter.class.getSimpleName();
    private BildContract.View mView;
    private Map<String, String> map = new HashMap<>();
    private boolean hasNext = true;
    private int currPage = 1;
    private List<Subscriber> mList = new ArrayList<>();

    public BildPresenter(BildContract.View view) {
        mView = view;
        map.putAll(ApiConst.BASE_MAP);
    }

    @Override
    public void start() {
        map.put("is_new_user", "false");
        map.put("page_size", "30");
        getStackData(1);
    }

    @Override
    public void stop() {
        for(int i = 0; i < mList.size(); i++) {
            Subscriber subscriber = mList.get(i);
            if(!subscriber.isUnsubscribed()){
                subscriber.unsubscribe();
            }
        }
    }

    @Override
    public void getStackData(int page) {
        LogUtils.e(TAG, "---->getStackData page=" + currPage);
        map.put("page", "" + page);
        Observable<BildInfo> bildInfo = ApiManager.getInstance().getApiService().getBildInfo(map);

        // 创建观察者
        Subscriber<BildInfo> subscriber = new Subscriber<BildInfo>() {
            @Override
            public void onCompleted() {
                LogUtils.e(TAG, "---->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(TAG, "---->onError:  " + e.getMessage());
                mView.showStackError();
            }

            @Override
            public void onNext(BildInfo bildInfo) {
                if (bildInfo.getResult() == 1) {
                    LogUtils.e(TAG, "---->onNext:  " + bildInfo.toString());
                    hasNext = (bildInfo.getData().getHas_next() == 1) ? true : false;
                    if (currPage == 1) {
                        mView.showStack(bildInfo);
                    } else {
                        mView.showMoreStack(bildInfo);
                    }
                } else {
                    mView.showStackError();
                }
            }
        };

        bildInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        // 添加进集合
        mList.add(subscriber);
    }

    @Override
    public void getMoreStackData() {
        if(!hasNext){
            // 没有更多
            return;
        }else{
            currPage++;
            getStackData(currPage);
        }
    }
}
