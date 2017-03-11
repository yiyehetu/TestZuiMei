package com.yph.beauty.ui.bild.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yph.beauty.R;
import com.yph.beauty.bean.BildContentInfo;
import com.yph.beauty.bean.HtmlText;
import com.yph.beauty.widget.CustomListView;
import com.yph.beauty.widget.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yph
 * Time is 2017/1/12 17:56
 * Good Good Study, Day Day Up
 *
 * 弃用
 */

public class BildContentAdapter extends RecyclerView.Adapter<BildContentAdapter.Holder> {
    // 标题
    private final int TITLE = 0;
    // 作者
    private final int AUTHOR = 1;
    // 内容
    private final int CONTENT = 2;
    // 分享
    private final int SHARE = 3;
    // 设计师
    private final int DESIGNER = 4;
    // 评论
    private final int COMMENT = 5;
    private BildContentInfo.DataBean mData;
    private Context mContext;

    private boolean isWeb = false;

    public BildContentAdapter(BildContentInfo.DataBean data, Context context) {
        mData = data;
        mContext = context;
        if (TextUtils.isEmpty(mData.getContent())) {
            isWeb = true;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        Holder holder = null;
        switch (viewType) {
            case TITLE:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_title_bild, parent, false);
                holder = new Holder(itemView);
                holder.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                holder.tvSubTitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
                return holder;
            case AUTHOR:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_author_bild, parent, false);
                holder = new Holder(itemView);
                holder.ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
                holder.ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
                holder.tvName = (TextView) itemView.findViewById(R.id.tv_name);
                holder.tvSign = (TextView) itemView.findViewById(R.id.tv_sign);
                return holder;
            case CONTENT:
                if (!isWeb) {
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_content_bild, parent, false);
                    holder = new Holder(itemView);
                    holder.lvContent = (CustomListView) itemView;
                } else {
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.item_web_bild, parent, false);
                    holder = new Holder(itemView);
                    holder.wvContent = (WebView) itemView;
                }
                return holder;
            case SHARE:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_share_bild, parent, false);
                holder = new Holder(itemView);
                return holder;
            case DESIGNER:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_share_bild, parent, false);
                holder = new Holder(itemView);
                return holder;
            default:
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_share_bild, parent, false);
                holder = new Holder(itemView);
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        switch (getItemViewType(position)) {
            case TITLE:
                setBildTitle(holder);
                break;
            case AUTHOR:
                setBildAuthor(holder);
                break;
            case CONTENT:
                setBildContent(holder);
                break;
            case SHARE:
                setBildShare(holder);
                break;
            case DESIGNER:
                setBildDesigner(holder);
                break;
            default:
                setBildComment(holder);
                break;
        }
    }

    // 标题
    private void setBildTitle(Holder holder) {
        holder.tvTitle.setText(mData.getTitle());
        holder.tvSubTitle.setText(mData.getSub_title());
    }

    // 作者
    private void setBildAuthor(Holder holder) {
        // cover
        Glide.with(mContext)
                .load(mData.getImage_url())
                .crossFade()
                .centerCrop()
                .into(holder.ivCover);
        BildContentInfo.DataBean.AuthorBean author = mData.getAuthor();
        // avatar
        Glide.with(mContext)
                .load(author.getAvatar_url())
                .transform(new GlideCircleTransform(mContext, 4, Color.WHITE))
                .crossFade()
                .into(holder.ivAvatar);

        holder.tvName.setText(author.getUsername());
        holder.tvSign.setText(author.getSign());
    }

    // 评论
    private void setBildComment(Holder holder) {

    }

    // 设计师
    private void setBildDesigner(Holder holder) {
    }

    // 分享
    private void setBildShare(Holder holder) {
    }

    // 详细内容
    private void setBildContent(final Holder holder) {
        if (!isWeb) {
            String content = mData.getContent();
            String[] splits = getHtmlGroup(content);
            List<HtmlText[]> list = handleHtml(splits);
            HtmlTextAdapter htmlTextAdapter = new HtmlTextAdapter(mContext, list);
            holder.lvContent.setAdapter(htmlTextAdapter);
        } else {
            holder.wvContent.loadUrl(mData.getWeb_view_url());
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TITLE;
            case 1:
                return AUTHOR;
            case 2:
                return CONTENT;
            case 3:
                return SHARE;
            case 4:
                return DESIGNER;
            default:
                return COMMENT;
        }
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

    class Holder extends RecyclerView.ViewHolder {
        // 标题
        TextView tvTitle;
        TextView tvSubTitle;

        // 作者
        ImageView ivCover;
        TextView tvName;
        TextView tvSign;
        ImageView ivAvatar;

        // 内容
        CustomListView lvContent;
        WebView wvContent;
        TextView tvText;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
