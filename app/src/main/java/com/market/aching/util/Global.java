package com.market.aching.util;

import android.os.Environment;
import android.text.TextUtils;

import com.market.aching.database.AccountManager;
import com.market.aching.model.AccountInfo;

import java.io.File;

/**
 * Created by Administrator on 2017/3/9.
 */

public class Global
{
    public static boolean DEBUG = true;
    private static String APP_NAME = "AchingMarket";
    private static AccountInfo accountInfo;

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

    public static AccountInfo getAccountInfo()
    {
        if (null == accountInfo)
        {
            accountInfo = new AccountManager().queryCurrentAccount();
        }
        return accountInfo;
    }

    public static void setAccountInfo(AccountInfo accountInfo)
    {
        Global.accountInfo = accountInfo;
    }
}
