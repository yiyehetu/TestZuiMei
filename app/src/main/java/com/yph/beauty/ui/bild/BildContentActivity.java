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
