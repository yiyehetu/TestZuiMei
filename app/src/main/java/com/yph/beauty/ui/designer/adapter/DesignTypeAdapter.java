package com.yph.beauty.ui.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yph.beauty.R;
import com.yph.beauty.bean.DesignType;

import java.util.List;

/**
 * Created by yph
 * Time is 2016/12/9 11:10
 * Good Good Study, Day Day Up
 */
public class DesignTypeAdapter extends BaseAdapter {
    private Context mContext;
    private List<DesignType> designTypes;
    private int mPositon = 0;

    public DesignTypeAdapter(Context context, List<DesignType> designTypes) {
        mContext = context;
        this.designTypes = designTypes;
    }

    @Override
    public int getCount() {
        return designTypes.size();
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
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.designer_item_type, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_type);
        textView.setText(designTypes.get(position).getType());

        if(position == mPositon) {
            textView.setSelected(true);
        }else{
            textView.setSelected(false);
        }
        return convertView;
    }

    public void setSelectItem(int positon) {
        mPositon = positon;
        notifyDataSetChanged();
    }
}
