package com.yph.beauty.ui.bild.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.view.overview.model.OverviewAdapter;
import com.view.overview.model.ViewHolder;
import com.yph.beauty.R;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.bean.BildInfo.DataBean.ArticlesBean;
import com.yph.beauty.ui.bild.BildContentActivity;
import com.yph.beauty.widget.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

/**
 * Created by yph
 * Time is 2016/12/28 15:00
 * Good Good Study, Day Day Up
 *
 * 卡片视图适配
 */

public class StackAdapter extends OverviewAdapter<StackAdapter.Holder, ArticlesBean> {
    private Context mContext;
    //    private BildInfo.DataBean mDataBean;
    private List<ArticlesBean> mArticles;

    public StackAdapter(Context context, List<ArticlesBean> articles) {
        super(articles);
        mContext = context;
        mArticles = articles;
    }

    public void addArticles(List<ArticlesBean> articles){
        // 逆序处理
        Collections.reverse(articles);

        notifyDataSetInsertedAll(articles);
    }

    @Override
    public Holder onCreateViewHolder(Context context, ViewGroup parent) {
        View container = View.inflate(mContext, R.layout.item_stack_bild, null);
        Holder holder = new Holder(container);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder) {
        final ArticlesBean article = holder.model;
        holder.tvName.setText(article.getAuthor().getUsername());
        holder.tvTitle.setText(article.getTitle());
        holder.tvSubtitle.setText(article.getSub_title());

        // 图片
        Glide.with(mContext)
                .load(article.getImage_url())
                .placeholder(R.drawable.rhombus_mask_in_square)
                .centerCrop()
                .crossFade()
                .into(holder.ivCover);
        // 头像
        Glide.with(mContext)
                .load(article.getAuthor().getAvatar_url())
                .transform(new GlideCircleTransform(mContext, 2, Color.WHITE))
                .crossFade()
                .into(holder.ivIcon);

        // 点击
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BildContentActivity.class);
                intent.putExtra(AppConst.BILD_CONTENT_ID, article.getId());
                mContext.startActivity(intent);
            }
        });
    }

    class Holder extends ViewHolder<View, ArticlesBean>{
        TextView tvName;
        TextView tvTitle;
        TextView tvSubtitle;
        ImageView ivCover;
        ImageView ivIcon;
        LinearLayout llContent;

        public Holder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvSubtitle = (TextView) view.findViewById(R.id.tv_subtitle);
            ivCover = (ImageView) view.findViewById(R.id.iv_cover);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        }
    }
}
