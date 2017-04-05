package com.market.aching.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.market.aching.BaseApplication;
import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;

import java.io.File;

import static android.content.ContentValues.TAG;

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
