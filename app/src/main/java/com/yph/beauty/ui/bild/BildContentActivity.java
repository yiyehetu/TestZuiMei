package com.yph.beauty.ui.bild;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yph.beauty.R;
import com.yph.beauty.app.AppConst;
import com.yph.beauty.base.BaseActivity;
import com.yph.beauty.bean.BildContentInfo;
import com.yph.beauty.bean.HtmlText;
import com.yph.beauty.ui.bild.adapter.HtmlTextAdapter;
import com.yph.beauty.ui.bild.contract.BildContentContract;
import com.yph.beauty.ui.bild.presenter.BildContentPresenter;
import com.yph.beauty.util.LogUtils;
import com.yph.beauty.widget.CustomListView;
import com.yph.beauty.widget.GlideCircleTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 画报内容页面
 */
public class BildContentActivity extends BaseActivity implements BildContentContract.View {
    @BindView(R.id.ll_load)
    LinearLayout llLoad;
    @BindView(R.id.sv_bild)
    ScrollView svBild;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.vs_native)
    ViewStub vsNative;
    @BindView(R.id.vs_web)
    ViewStub vsWeb;
    @BindView(R.id.iv_designerAvatar)
    ImageView ivDesignerAvatar;
    @BindView(R.id.tv_designerName)
    TextView tvDesignerName;
    @BindView(R.id.tv_designerLabel)
    TextView tvDesignerLabel;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.vs_open)
    ViewStub vsOpen;

    // 当前画报id
    private int mId;
    private BildContentPresenter mContentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bild_content;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mContentPresenter.stop();
    }

    // 初始化数据
    private void initData() {
        mId = getIntent().getIntExtra(AppConst.BILD_CONTENT_ID, -1);
        LogUtils.e("---->id=" + mId);
        if (mId == -1) {
            return;
        }

        mContentPresenter = new BildContentPresenter(mId, this);
        mContentPresenter.start();
    }

    @Override
    public void showContent(BildContentInfo.DataBean data, List<HtmlText[]> list, boolean isWeb) {
        // 标题
        loadTitle(data);
        // 作者
        loadAuthor(data);
        // 内容
        loadContent(data, list, isWeb);
        // 设计师
        loadDesigner(data);
    }

    private void loadDesigner(BildContentInfo.DataBean data) {
        BildContentInfo.DataBean.DesignersBean designer = data.getDesigners().get(0);
        // designer avatar
        Glide.with(this)
                .load(designer.getAvatar_url())
                .placeholder(R.drawable.rhombus_mask_in_square)
                .transform(new GlideCircleTransform(this, 0, Color.WHITE))
                .crossFade()
                .into(ivDesignerAvatar);

        tvDesignerName.setText(designer.getName());
        tvDesignerLabel.setText(designer.getLabel());
        tvDescription.setText(designer.getDescription());

        tvDescription.post(new Runnable() {
            @Override
            public void run() {
                // 描述判断
                int count = tvDescription.getLineCount();
                LogUtils.e("----> count = " + count);
                if(count > 5){
                    tvDescription.setMaxLines(5);
                    final TextView tvOpen = (TextView) vsOpen.inflate();
                    tvOpen.setText("展开");
                    tvOpen.setSelected(false);

                    // 点击监听
                    tvOpen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(tvOpen.isSelected()){
                                tvDescription.setMaxLines(5);
                                tvOpen.setText("展开");
                                tvOpen.setSelected(false);
                            }else{
                                tvDescription.setMaxLines(100);
                                tvOpen.setText("收起");
                                tvOpen.setSelected(true);
                            }
                        }
                    });
                }
            }
        });




    }

    private void loadContent(BildContentInfo.DataBean data, List<HtmlText[]> list, boolean isWeb) {
        if (isWeb) {
            // web
            View inflate = vsWeb.inflate();
            WebView wvContent = (WebView) inflate;
            wvContent.loadUrl(data.getWeb_view_url());
        } else {
            // native
            View inflate = vsNative.inflate();
            CustomListView lvContent = (CustomListView) inflate;
            HtmlTextAdapter htmlTextAdapter = new HtmlTextAdapter(this, list);
            lvContent.setAdapter(htmlTextAdapter);
        }
    }

    private void loadAuthor(BildContentInfo.DataBean data) {
        // cover
        Glide.with(this)
                .load(data.getImage_url())
                .crossFade()
                .centerCrop()
                .into(ivCover);
        BildContentInfo.DataBean.AuthorBean author = data.getAuthor();
        // avatar
        Glide.with(this)
                .load(author.getAvatar_url())
                .transform(new GlideCircleTransform(this, 4, Color.WHITE))
                .crossFade()
                .into(ivAvatar);

        tvName.setText(author.getUsername());
        tvSign.setText(author.getSign());
    }

    private void loadTitle(BildContentInfo.DataBean data) {
        tvTitle.setText(data.getTitle());
        tvSubtitle.setText(data.getSub_title());
    }


    @Override
    public void hideLoadView() {
        llLoad.setVisibility(View.GONE);
        svBild.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_weibo)
    void weiboShare(){
        Toast.makeText(BildContentActivity.this, "微博", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_qq)
    void qqShare(){
        Toast.makeText(BildContentActivity.this, "QQ", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_weixin)
    void weixinShare(){
        Toast.makeText(BildContentActivity.this, "微信", Toast.LENGTH_SHORT).show();
    }

}
