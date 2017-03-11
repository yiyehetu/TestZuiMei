package com.yph.beauty.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yph.beauty.bean.HtmlText;
import com.yph.beauty.util.ConvertUtils;

/**
 * 组View
 */

public class SingleGroupView extends LinearLayout {
    private Context mContext;

    public interface OnClickImageListener{
        void onClick(int positon);
    }
    private OnClickImageListener mOnClickImageListener;

    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        mOnClickImageListener = onClickImageListener;
    }

    public SingleGroupView(Context context) {
        super(context);
        mContext = context;
        setOrientation(VERTICAL);
        setBackgroundColor(Color.WHITE);
    }

    public SingleGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(VERTICAL);
    }

    public void initView(HtmlText[] htmlTexts){
        if(htmlTexts == null || htmlTexts.length == 0){
            return;
        }

        for(int i = 0; i < htmlTexts.length; i++) {
            HtmlText.HtmlType type = htmlTexts[i].getType();
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = ConvertUtils.dp2px(mContext, 20);
            switch (type) {
                case TEXT:
                    TextView textView = new TextView(mContext);
                    addView(textView);
                    // 布局参数
                    layoutParams.leftMargin = margin;
                    layoutParams.rightMargin = margin;
                    textView.setLayoutParams(layoutParams);
                    Spanned spanned  = Html.fromHtml(htmlTexts[i].getHtml());
                    // trim()去除空数据
                    textView.setText("\n" + spanned.toString().trim());
                    textView.setTextSize(16);

                    break;
                case IMAGE:
                    ImageView imageView = new ImageView(mContext);
                    addView(imageView);
                    String html = htmlTexts[i].getHtml();
                    // 图片url
                    int urlStart = html.indexOf("http");
                    int urlEnd = html.indexOf("jpeg") + 4;
                    String picUrl = html.substring(urlStart, urlEnd);
                    // 图片size
                    int sizeStart = html.indexOf("_") + 1;
                    int sizeEnd = html.indexOf(".jpeg");
                    String[] size = html.substring(sizeStart, sizeEnd).split("x");
                    int width = Integer.valueOf(size[0]);
                    int height = Integer.valueOf(size[1]);
                    // 布局参数
                    layoutParams.leftMargin = margin;
                    layoutParams.rightMargin = margin;
                    int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
                    layoutParams.width = screenWidth - 2 * margin;
                    layoutParams.height = layoutParams.width * height / width;
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setLayoutParams(layoutParams);
                    // 位置
                    final int imgPositon = htmlTexts[i].getImgPositon();
                    // 点击监听
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mOnClickImageListener != null){
                                mOnClickImageListener.onClick(imgPositon);
                            }
                        }
                    });

                    Glide.with(mContext)
                            .load(picUrl)
                            .crossFade()
                            .fitCenter()
                            .into(imageView);
                    break;
                default:
                    break;
            }
        }
    }
}
