package com.yph.beauty.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;

/**
 * Created by yita on 2016/12/3.
 */

public class BitmapUtils {
    public static Drawable createRoundImageWithBorder(Context context, Bitmap bitmap, int color, float stroke) {
        //原图宽度
        int bitmapWidth = bitmap.getWidth();
        //原图高度
        int bitmapHeight = bitmap.getHeight();
        //边框宽度 pixel
        int borderWidthHalf = ConvertUtils.dp2px(context, stroke);

        //转换为正方形后的宽高
        int bitmapSquareWidth = Math.min(bitmapWidth, bitmapHeight);
        //最终图像的宽高
        int newBitmapSquareWidth = bitmapSquareWidth + borderWidthHalf;

        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth, newBitmapSquareWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

        //裁剪后图像,注意X,Y要除以2 来进行一个中心裁剪
        canvas.drawBitmap(bitmap, x / 2, y / 2, null);
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf);
        borderPaint.setColor(color);


        //添加边框
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getWidth() / 2, newBitmapSquareWidth / 2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), roundedBitmap);
        roundedBitmapDrawable.setGravity(Gravity.CENTER);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
}
