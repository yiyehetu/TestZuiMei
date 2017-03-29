package com.yph.beauty.ui.bild.presenter;

import android.text.TextUtils;

import com.yph.beauty.api.ApiConst;
import com.yph.beauty.api.ApiManager;
import com.yph.beauty.bean.BildContentInfo;
import com.yph.beauty.bean.HtmlText;
import com.yph.beauty.ui.bild.contract.BildContentContract;
import com.yph.beauty.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yph
 * Time is 2017/1/12 11:53
 * Good Good Study, Day Day Up
 */

public class BildContentPresenter implements BildContentContract.Presenter{
    private final String TAG = BildContentPresenter.class.getSimpleName();
    private BildContentContract.View mView;
    private int mId;
    private Subscriber<BildContentInfo> subscriber;

    public BildContentPresenter(int id, BildContentContract.View view) {
        mId = id;
        mView = view;
    }

    @Override
    public void start() {
       getContentData();
    }

    @Override
    public void stop() {
        if(subscriber != null && !subscriber.isUnsubscribed()){
            subscriber.unsubscribe();
        }
    }

    @Override
    public void getContentData() {
        Observable<BildContentInfo> contentInfo = ApiManager.getInstance().getApiService().getBildContentInfo(mId, ApiConst.BASE_MAP);
        subscriber = new Subscriber<BildContentInfo>() {
            @Override
            public void onCompleted() {
                LogUtils.e(TAG, "---->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(TAG, "---->onError:" + e.getMessage());
            }

            @Override
            public void onNext(BildContentInfo bildContentInfo) {
                LogUtils.e(TAG, "---->onNext:" + bildContentInfo.toString());
                if(bildContentInfo != null && bildContentInfo.getResult() == 1){
                    BildContentInfo.DataBean data = bildContentInfo.getData();
                    if (TextUtils.isEmpty(data.getContent())) {
                        // web
//                        mView.hideLoadView();
                        mView.showContent(data, null, true);
                    }else{
                        // native
                        String content = data.getContent();
                        String[] splits = getHtmlGroup(content);
                        List<HtmlText[]> list = handleHtml(splits);
                        mView.hideLoadView();
                        mView.showContent(data, list, false);
                    }

                }
            }
        };

        contentInfo.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    // 按组分配数据
    private String[] getHtmlGroup(String html) {
        // 标记
        String flag = "<p><img";
        String[] splits = html.split(flag);

        for (int i = 1; i < splits.length; i++) {
            splits[i] = flag + splits[i];
        }

        return splits;
    }

    // 分配位置，封装对象
    private List<HtmlText[]> handleHtml(String[] htmlGroup) {
        // 标记
        String pFlag = "<p>";
        String imgFlag = "<img";
        int position = 1;
        List<HtmlText[]> list = new ArrayList<>();

        for (int i = 0; i < htmlGroup.length; i++) {
            String html = htmlGroup[i];
            String[] splits = html.split(pFlag);
            int length = splits.length;
            // 去除开头空格
            HtmlText[] htmlTexts = new HtmlText[length - 1];
            for (int j = 1; j < length; j++) {
                if (splits[j].startsWith(imgFlag)) {
                    htmlTexts[j - 1] = new HtmlText(pFlag + splits[j], HtmlText.HtmlType.IMAGE, position++);
                } else {
                    htmlTexts[j - 1] = new HtmlText(pFlag + splits[j], HtmlText.HtmlType.TEXT);
                }
            }
            list.add(htmlTexts);
        }
        return list;
    }
}
