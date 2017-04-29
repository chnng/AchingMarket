package com.market.aching.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/9.
 */

public class Global
{
    public static boolean DEBUG = true;
    private static String APP_NAME = "AchingMarket";

    private static String appPath;

    public static String getAppPath()
    {
        if (TextUtils.isEmpty(appPath))
        {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED))
            {
                appPath = Environment.getExternalStorageDirectory().getPath()
                        + File.separator + APP_NAME + File.separator;
            } else
            {
                appPath = "/mnt/sdcard/";
            }
            File file = new File(appPath);
            if (!file.exists())
            {
                file.mkdir();
            }
        }
        return appPath;
    }
}
