package com.yph.beauty.ui.designer.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yph.beauty.R;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.base.BaseFragment;
import com.yph.beauty.bean.DesignType;
import com.yph.beauty.bean.DesignerInfo;
import com.yph.beauty.ui.designer.adapter.DesignTypeAdapter;
import com.yph.beauty.ui.designer.adapter.DesignerAdapter;
import com.yph.beauty.ui.designer.contract.ProjectContract;
import com.yph.beauty.ui.designer.presenter.ProjectPresenter;
import com.yph.beauty.util.ConvertUtils;
import com.yph.beauty.util.LogUtils;
import com.yph.beauty.widget.RecyclerViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yph.beauty.R.id.textView;


/**
 * Created by yph
 * Time is 2016/11/30 17:01
 * Good Good Study, Day Day Up
 * <p>
 * 设计师分类展示页面
 */

public class ProjectFragment extends BaseFragment implements ProjectContract.View {
    private final String TAG = ProjectFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(textView)
    TextView mTextView;
    @BindView(R.id.gridView)
    GridView mGridView;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private ProjectPresenter mProjectPresenter;
    private DesignerAdapter mDesignerAdapter;
    private int currFragment;
    private boolean isInitPresentered = false;
    private boolean isInitViewed = false;
    private int currId = 30;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initPresenter() {
        currFragment = getArguments().getInt(AppConst.FRAGMENT_POSITON);
        LogUtils.e(TAG, "----> onCreateView = " + currFragment);

        if (isInitPresentered) {
            return;
        }

        mProjectPresenter = new ProjectPresenter(this, currFragment);
        isInitPresentered = true;

        initNavBar();

        // 添加加载刷新
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mSwipeRefreshLayout.setRefreshing(true);
                mProjectPresenter.start();
            }
        });
    }

    @Override
    protected void initView() {
        if (isInitViewed) {
            return;
        }

        initSwipeRefreshLayout();
        initRecyclerView();
        isInitViewed = true;
    }

    /**
     * 独立设计师nav bar
     */
    private void initNavBar() {
        LogUtils.e(TAG, "----> initNavBar");
        if (currFragment == 3) {
            mTextView.setVisibility(View.VISIBLE);
        }

        mProjectPresenter.getNavBarData();
        // 展开分类
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setSelected(true);
                mGridView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initSwipeRefreshLayout() {
        // 背景色
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 旋钮色
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "----> onRefresh");
                if (currFragment == 3) {
                    mProjectPresenter.getListData(currId);
                } else {
                    mProjectPresenter.getListData();
                }
            }
        });
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerViewDivider divider = new RecyclerViewDivider(new ColorDrawable(Color.BLACK), LinearLayoutManager.VERTICAL);
        divider.setHeight(ConvertUtils.dp2px(getContext(), 8.0f));
        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisiableItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mDesignerAdapter == null) {
                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiableItem + 1 == mDesignerAdapter.getItemCount()) {
                    // 滑动到底部
                    if (currFragment == 3) {
                        mProjectPresenter.getMoreListData(currId);
                    } else {
                        mProjectPresenter.getMoreListData();
                    }
                }

                // 滑动不加载图片
                // 实际效果不行
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE :
//                        Glide.with(getContext()).resumeRequests();
//                        break;
//                    default:
//                        Glide.with(getContext()).pauseRequests();
//                        break;
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void showList(DesignerInfo designerInfo) {
        mDesignerAdapter = new DesignerAdapter(getContext(), designerInfo, currFragment, mProjectPresenter);
        mRecyclerView.setAdapter(mDesignerAdapter);
    }

    @Override
    public void showMoreList(DesignerInfo designerInfo) {
        mDesignerAdapter.addDesignerInfo(designerInfo);
        mDesignerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListError() {
        rlError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rl_error)
    void hideListError(){
        rlError.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        mProjectPresenter.start();
    }

    @Override
    public void finishRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showDesignType(final List<DesignType> designTypes) {
        final DesignTypeAdapter adapter = new DesignTypeAdapter(getContext(), designTypes);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7 || position == 8) {
                    return;
                }

                mGridView.setVisibility(View.GONE);

                DesignType designType = designTypes.get(position);
                mTextView.setText(designType.getType());
                mTextView.setSelected(false);

                currId = designType.getId();
                mProjectPresenter.getListData(currId);
                adapter.setSelectItem(position);
            }
        });
    }
}
