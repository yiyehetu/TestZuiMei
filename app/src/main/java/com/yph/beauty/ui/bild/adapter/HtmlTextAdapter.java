package com.yph.beauty.ui.bild.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.yph.beauty.bean.HtmlText;
import com.yph.beauty.widget.SingleGroupView;

import java.util.List;

/**
 * Created by yph
 * Time is 2017/2/22 14:18
 * Good Good Study, Day Day Up
 */
public class HtmlTextAdapter extends BaseAdapter {
    private Context mContext;
    private List<HtmlText[]> mList;

    public HtmlTextAdapter(Context context, List<HtmlText[]> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HtmlText[] htmlTexts = mList.get(position);
        SingleGroupView singleGroupView = new SingleGroupView(mContext);
        singleGroupView.initView(htmlTexts);
        singleGroupView.setOnClickImageListener(new SingleGroupView.OnClickImageListener() {
            @Override
            public void onClick(int positon) {
                Toast.makeText(mContext, "positon = " + positon, Toast.LENGTH_SHORT).show();
            }
        });

        return singleGroupView;
    }

}
