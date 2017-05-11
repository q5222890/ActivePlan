package com.xz.activeplan.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * 计算屏幕尺寸
 * Screen size and other metrics helper
 */
public class ScreenHelper {

    private static int mWidth;
    private static int mHeight;
    //获取屏幕宽
    public static int getScreenWidth(Context context) {
        if (mWidth == 0) {
            calculateScreenDimensions(context);
        }
        return mWidth;
    }

    //获取屏幕高
    public static int getScreenHeight(Context context) {
        if (mHeight == 0) {
            calculateScreenDimensions(context);
        }
        return mHeight;
    }

    //计算屏幕尺寸
    private static void calculateScreenDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final Point point = new Point();
            display.getSize(point);
            mWidth = point.x;
            mHeight = point.y;
        } else {
            mWidth = display.getWidth();
            mHeight = display.getHeight();
        }
    }

    //把尺寸单位dp转换为px
    public static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
