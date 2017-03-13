package com.market.aching.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ISLog
{

    public static void i(String tag, String msg)
    {
        if (Global.DEBUG)
        {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg)
    {
        if (Global.DEBUG)
        {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg)
    {
        if (Global.DEBUG)
        {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg)
    {
        if (Global.DEBUG)
        {
            Log.e(tag, msg);
        }
    }

}
