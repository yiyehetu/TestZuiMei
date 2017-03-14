package com.yph.beauty.ui.bild.fragment;

import com.view.overview.views.Overview;
import com.yph.beauty.R;
import com.yph.beauty.base.BaseFragment;
import com.yph.beauty.bean.BildInfo;
import com.yph.beauty.ui.bild.adapter.StackAdapter;
import com.yph.beauty.ui.bild.contract.BildContract;
import com.yph.beauty.ui.bild.presenter.BildPresenter;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yph
 * Time is 2016/11/25 16:41
 * Good Good Study, Day Day Up
 * <p>
 * 画报V
 */

public class BildFragment extends BaseFragment implements BildContract.View, Overview.RecentsViewCallbacks {
    @BindView(R.id.ov_bild)
    Overview mOverview;

    private BildPresenter mBildPresenter;
    private StackAdapter mStackAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bild;
    }

    @Override
    protected void initPresenter() {
        mBildPresenter = new BildPresenter(this);
        mBildPresenter.start();
    }

    @Override
    protected void initView() {
        mOverview.setCallbacks(this);
    }

    @Override
    public void showStack(BildInfo bildInfo) {
        List<BildInfo.DataBean.ArticlesBean> articles = bildInfo.getData().getArticles();
        if (articles != null && articles.size() > 0) {
            // 反转
            Collections.reverse(articles);
            mStackAdapter = new StackAdapter(getContext(), articles);
            mOverview.setTaskStack(mStackAdapter);
        }
    }

    @Override
    public void showMoreStack(BildInfo bildInfo) {
        List<BildInfo.DataBean.ArticlesBean> articles = bildInfo.getData().getArticles();
        if (articles != null && articles.size() > 0) {
            mStackAdapter.addArticles(articles);
        }
    }

    @Override
    public void showStackError() {

    }

    @Override
    public void onStop() {
        super.onStop();
        mBildPresenter.stop();
    }

    @Override
    public void onCardDismissed(int position) {

    }

    @Override
    public void onAllCardsDismissed() {

    }

    @Override
    public void onScollTop() {
//        OverviewStackView mStakView = (OverviewStackView) mOverview.getChildAt(0);
//        int childCount = mStakView.getChildCount();
//        List<BildInfo.DataBean.ArticlesBean> articles = mStackAdapter.getArticles();
//        if(articles.size() == childCount) {
//            Log.e("TAG", "---->执行");
            mBildPresenter.getMoreStackData();
//        }
    }

}
