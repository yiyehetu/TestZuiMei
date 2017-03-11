package com.yph.beauty.util;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by yph
 * Time is 2016/11/30 14:03
 * Good Good Study, Day Day Up
 */

public class TabLayoutUtils {
    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout linearLayout = null;
        try {
            if (tabStrip != null) {
                linearLayout = (LinearLayout) tabStrip.get(tabs);
                int left = (int) (context.getResources().getDisplayMetrics().density * leftDip);
                LogUtils.e("----->left = " + left);
                int right = (int) (context.getResources().getDisplayMetrics().density * rightDip);
                LogUtils.e("----->right = " + right);

                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = left;
                    params.rightMargin = right;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                        params.setMarginStart(left);
//                        params.setMarginEnd(right);
//                    }
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
