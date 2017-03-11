package com.yph.beauty.ui.designer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yph.beauty.R;
import com.yph.beauty.bean.DesignerInfo;
import com.yph.beauty.ui.designer.presenter.ProjectPresenter;
import com.yph.beauty.widget.GlideCircleTransform;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yph
 * Time is 2016/12/2 10:39
 * Good Good Study, Day Day Up
 */

public class DesignerAdapter extends RecyclerView.Adapter<DesignerAdapter.CustomViewHolder> {
    private final int HEADER = 0;
    private final int FOOTER = 1;
    private final int DEFAULT = 2;
    private final String ADDFOCUS = "+ 关注";
    private final String ISFOCUSED = "已关注";
    private Context mContext;
    private boolean hasNext;
    private int currFragment;
    private ProjectPresenter mProjectPresenter;
    private Map<Integer, String> map = new HashMap();
    // 类别
    private List<DesignerInfo.DataBean.CategoriesBean> mCategories;
    // 设计师
    private List<DesignerInfo.DataBean.DesignersBean> mDesigners;

    public DesignerAdapter(Context context, DesignerInfo designerInfo, int currFragment, ProjectPresenter projectPresenter) {
        mContext = context;
        this.currFragment = currFragment;
        DesignerInfo.DataBean data = designerInfo.getData();
        hasNext = (data.getHas_next() == 1) ? true : false;
        mCategories = data.getCategories();
        mDesigners = data.getDesigners();
        mProjectPresenter = projectPresenter;
    }

    public void addDesignerInfo(DesignerInfo designerInfo) {
        DesignerInfo.DataBean data = designerInfo.getData();
        hasNext = (data.getHas_next() == 1) ? true : false;
        mDesigners.addAll(data.getDesigners());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new CustomViewHolder(View.inflate(mContext, R.layout.recycler_header_recommend, null));
            case FOOTER:
                return new CustomViewHolder(View.inflate(mContext, R.layout.recycler_footer_recommend, null));
            default:
                return new CustomViewHolder(View.inflate(mContext, R.layout.recycler_body_recommend, null));
        }
    }

    @Override
    public int getItemCount() {
        switch (currFragment) {
            case 1:
                // 推荐
                return mDesigners.size() + 2;
            default:
                // 其他
                return mDesigners.size() + 1;
        }
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        switch (currFragment) {
            case 1:
                // 推荐
                if (position == 0) {
                    setHeaderItem(holder);
                } else if (position < getItemCount() - 1) {
                    setDefaultItem(holder, position - 1);
                } else if (position == getItemCount() - 1) {
                    setFooterItem(holder);
                }
                break;
            default:
                // 其他
                if (position < getItemCount() - 1) {
                    setDefaultItem(holder, position);
                } else if (position == getItemCount() - 1) {
                    setFooterItem(holder);
                }
                break;
        }

    }

    /**
     * 设置尾部
     *
     * @param holder
     */
    private void setFooterItem(CustomViewHolder holder) {
        if (hasNext) {
            holder.footerTextView.setText("加载更多...");
        } else {
            holder.footerTextView.setText("没有更多啦");
        }
    }

    /**
     * 设置一般Item
     *
     * @param holder
     * @param index
     */
    private void setDefaultItem(final CustomViewHolder holder, int index) {
        DesignerInfo.DataBean.DesignersBean designer = mDesigners.get(index);
        final int id = designer.getId();
        // 点击监听
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "id = " + id, Toast.LENGTH_SHORT).show();
            }
        });

        // cover
        Glide.with(mContext)
                .load(designer.getRecommend_images().get(0))
                .centerCrop()
                .placeholder(R.drawable.rhombus_mask_in_rectangle)
                .crossFade()
                .into(holder.coverImageView);

        // 加载圆形图片
        Glide.with(mContext)
                .load(designer.getAvatar_url())
                .placeholder(R.drawable.rhombus_mask_in_square)
                .transform(new GlideCircleTransform(mContext, 1, Color.BLACK))
                .into(holder.iconImageView);

        holder.nameTextView.setText(designer.getName());
        holder.jobTextView.setText(designer.getLabel());

        setFocusNum(holder, designer);
        setFoucsText(holder, designer, id);
    }

    /**
     * 设置关注人数
     *
     * @param holder
     * @param designer
     */
    private void setFocusNum(CustomViewHolder holder, DesignerInfo.DataBean.DesignersBean designer) {
        switch (currFragment) {
            case 2:
                holder.numTextView.setText(designer.getFollow_num() + " 关注");
                break;
            default:
                holder.numTextView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置关注/取消关注
     *
     * @param holder
     * @param designer
     */
    private void setFoucsText(CustomViewHolder holder, DesignerInfo.DataBean.DesignersBean designer, final int id) {
        switch (currFragment) {
            // 我关注的
            case 0:
                holder.focusTextView.setVisibility(View.GONE);
                break;
            default:
                String focusStr = map.get(id);
                if (focusStr == null) {
                    // 第一次
                    int is_followed = designer.getIs_followed();
                    if (is_followed == 0) {
                        map.put(id, ADDFOCUS);
                        focusStr = ADDFOCUS;
                    } else {
                        map.put(id, ISFOCUSED);
                        focusStr = ISFOCUSED;
                    }
                }

                if (ISFOCUSED.equals(focusStr)) {
                    holder.focusTextView.setSelected(true);
                } else {
                    holder.focusTextView.setSelected(false);
                }
                holder.focusTextView.setText(focusStr);

                holder.focusTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = (TextView) v;
                        if (ISFOCUSED.equals(textView.getText())) {
                            map.put(id, ADDFOCUS);
                            textView.setText(ADDFOCUS);
                            textView.setSelected(false);
                            // 取消关注
                            mProjectPresenter.follow(false, id);
                        } else {
                            map.put(id, ISFOCUSED);
                            textView.setText(ISFOCUSED);
                            textView.setSelected(true);
                            // 关注
                            mProjectPresenter.follow(true, id);
                        }
                    }
                });
                break;
        }

    }

    /**
     * 设置头部
     *
     * @param holder
     */
    private void setHeaderItem(CustomViewHolder holder) {
        holder.mTagFlowLayout.setAdapter(new TagAdapter<DesignerInfo.DataBean.CategoriesBean>(mCategories) {
            @Override
            public View getView(FlowLayout parent, int position, final DesignerInfo.DataBean.CategoriesBean categoriesBean) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.recycler_header_tag, null);
                textView.setText(categoriesBean.getName());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "id = " + categoriesBean.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
                return textView;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        switch (currFragment) {
            case 1:
                // 推荐
                if (position == 0) {
                    return HEADER;
                } else if (position == getItemCount() - 1) {
                    return FOOTER;
                } else {
                    return DEFAULT;
                }
            default:
                if (position == getItemCount() - 1) {
                    return FOOTER;
                } else {
                    return DEFAULT;
                }
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        // 头部Tag
        TagFlowLayout mTagFlowLayout;
        // item容器
        RelativeLayout mRelativeLayout;
        // item cover
        ImageView coverImageView;
        // item icon
        ImageView iconImageView;
        // item name
        TextView nameTextView;
        // item job
        TextView jobTextView;
        // item focus
        TextView focusTextView;
        // footer
        TextView footerTextView;
        // focus num
        TextView numTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mTagFlowLayout = (TagFlowLayout) itemView.findViewById(R.id.tagFlowLayout);
            coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover);
            iconImageView = (ImageView) itemView.findViewById(R.id.iv_icon);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            jobTextView = (TextView) itemView.findViewById(R.id.tv_job);
            focusTextView = (TextView) itemView.findViewById(R.id.tv_focus);
            numTextView = (TextView) itemView.findViewById(R.id.tv_num);
            // container
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.mRelativeLayout);
            footerTextView = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
}
