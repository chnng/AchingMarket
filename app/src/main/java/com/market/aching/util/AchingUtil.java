package com.market.aching.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.market.aching.BaseApplication;

/**
 * Created by chnng on 2017/4/29.
 */

public class AchingUtil
{
    public static void log(Object msg)
    {
        if (Global.DEBUG)
        {
            Log.d("Aching", msg.toString());
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue)
    {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 得到屏幕的宽度
     */
    public static int getScreenWidth()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 得到屏幕的高度
     */
    public static int getScreenHeight()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据图片名称获取图片资源ID
     *
     * @param resName
     * @return
     */
    public static int getResource(String resName)
    {
        return BaseApplication.getInstance().getResources().getIdentifier(resName,
                "drawable", BaseApplication.getInstance().getPackageName());
    }

    /**
     * 判断网络是否连接
     * @return
     */
    public static boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
