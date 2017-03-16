package com.market.aching;

import android.app.Application;

/**
 * Created by Administrator on 2016/11/5.
 */

public class BaseApplication extends Application
{
    private static BaseApplication mApplication;

    public static BaseApplication getInstance()
    {
        if (null == mApplication)
        {
            synchronized (BaseApplication.class)
            {
                if (null == mApplication)
                {
                    mApplication = new BaseApplication();
                }
            }
        }
        return mApplication;
    }
}
