/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.view.overview.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.view.overview.misc.OverviewConfiguration;
import com.view.overview.model.OverviewAdapter;

/**
 * 包装 OverviewStackView
 */
public class Overview extends FrameLayout implements OverviewStackView.Callbacks {

    OverviewStackView mStackView;
    OverviewConfiguration mConfig;
    OverviewAdapter mAdapter;
    RecentsViewCallbacks mCallbacks;
    public Overview(Context context) {
        super(context);
        init(context);
    }

    public Overview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Overview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public Overview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mConfig = new OverviewConfiguration(context);
    }

    /**
     * Sets the callbacks
     *
     * 回调接口
     */
    public void setCallbacks(RecentsViewCallbacks cb) {
        mCallbacks = cb;
    }

    /**
     * Set/get the bsp root node
     *
     * 设置适配器传入OverviewStackView
     */
    public void setTaskStack(OverviewAdapter adapter) {
        if (mStackView != null) {
            removeView(mStackView);
        }

        mAdapter = adapter;
        // 初始化OverviewStackView
        mStackView = new OverviewStackView(getContext(), adapter, mConfig);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mStackView.setLayoutParams(params);
        mStackView.setCallbacks(this);
        mStackView.setAlpha(0);

        // 属性动画
        mStackView.animate().alpha(1.f).setDuration(500).start();

        //所以说 OverviewStackView 才是重点
        addView(mStackView);
    }

    /**
     * This is called with the full size of the window since we are handling our own insets.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mStackView != null) {
            Rect stackBounds = new Rect();
            mConfig.getOverviewStackBounds(width, height, stackBounds);
            // ???
            mStackView.setStackInsetRect(stackBounds);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onCardDismissed(int position) {
        if (mCallbacks != null) {
            mCallbacks.onCardDismissed(position);
        }
    }

    @Override
    public void onAllCardsDismissed() {
        if (mCallbacks != null) {
            mCallbacks.onAllCardsDismissed();
        }
    }

    @Override
    public void onScrollTop() {
        if (mCallbacks != null) {
            mCallbacks.onScollTop();
        }
    }

    public interface RecentsViewCallbacks {
        public void onCardDismissed(int position);

        public void onAllCardsDismissed();

        // 自定义监听
        public void onScollTop();
    }
}
