package com.market.aching.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.market.aching.BaseApplication;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtil
{
    private static Toast toast;

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
     * 得到屏幕的密度dpi
     */
    public static int getScreenDendityDpi()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    /**
     * 得到屏幕的像素密度
     */
    public static float getScreenDendity()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 得到屏幕状态栏高度
     */
    public static int getStatusBarHeight(Context context)
    {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try
        {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 测量view的宽高,这种方法可以在控件显示之前得到其宽高
     *
     * @param view 测量的控件
     * @return 测量后的控件
     */
    public static View getMeasuredWidthHeight(View view)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view;
    }

    /**
     * 获取应用versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        String versionName = "";
        try
        {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return versionName;

    }

    /**
     * 获取应用versionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = 0;
        try
        {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 判断应用是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isForegroudProgram(Context context)
    {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0)
        {
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity
                    .getPackageName()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个activity是否在栈的顶层
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isTopActivity(Context context, String className)
    {
        boolean isTop = false;
        String topActivityName = null;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(1).get(0);
        if (runningTaskInfo != null)
        {
            ComponentName componentName = runningTaskInfo.topActivity;
            if (componentName != null)
            {
                topActivityName = componentName.getClassName();
                isTop = topActivityName.equals(className);
            }
        }

        return isTop;
    }


    public static void sleepMillis(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return 网络类型字符串
     */
    public static String getNetType(Context context)
    {
        String netType = "";
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conn.getActiveNetworkInfo();
        if (netInfo != null)
        {
            switch (netInfo.getType())
            {
                case ConnectivityManager.TYPE_WIFI:
                    netType = "wifi";
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    netType = netInfo.getSubtypeName();
                    break;
                default:
                    netType = "unknow";
                    break;
            }
        }
        return netType;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion()
    {
        return android.os.Build.VERSION.RELEASE;
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
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue)
    {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp的单位 转成为 px(像素)
     */
    public static float spTopx(Context context, float sp)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     */
    public static String addComma(String str)
    {
        // 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++)
        {
            if (i * 3 + 3 > reverseStr.length())
            {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将[789,456,] 中最后一个[,]去除
        if (strTemp.endsWith(","))
        {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }

    /**
     * 同上，货币型数字加千位逗号
     *
     * @param num
     * @return
     */
    public static String displayComma(long num)
    {
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(num);
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles)
    {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 测量文本内容高度
     *
     * @param paint 画笔
     * @return 内容高度
     */
    public static float measureTextHeight(Paint paint)
    {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top;

        return height;
    }

    /**
     * 测量文本内容宽度
     *
     * @param text  文本内容
     * @param paint 画笔
     * @return 内容宽度
     */
    public static float measureTextWidth(String text, Paint paint)
    {
        float width = paint.measureText(text);

        return width;
    }

    /**
     * 文本内容相对于某一点居中
     *
     * @param canvas  画布
     * @param paint   画笔
     * @param text    文本内容
     * @param centerX x轴方向中心点
     * @param centerY y轴方向中心点
     */
    public static void drawTextInCenter(Canvas canvas, Paint paint, String text, float centerX, float centerY)
    {
        float bottom = paint.getFontMetrics().bottom;
        canvas.drawText(text, (float) (centerX - measureTextWidth(text, paint) * 0.5),
                (float) (0.5 * measureTextHeight(paint) - bottom + centerY), paint);
    }
}
