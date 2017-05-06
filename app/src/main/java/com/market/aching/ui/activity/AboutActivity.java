package com.market.aching.ui.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.market.aching.R;
import com.market.aching.ui.base.ToolbarActivity;

import butterknife.BindView;

/**
 * Created by chnng on 2017/5/6.
 */

public class AboutActivity extends ToolbarActivity
{
    @BindView(R.id.tv_about_version)
    TextView tvVersion;

    @BindView(R.id.tv_about_egg)
    TextView tvEgg;

    private long mTime;
    private long mCount;
    private Toast mToast;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_about;
    }

    @Override
    public int getTitleID()
    {
        return R.string.title_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        PackageManager packageManager = getPackageManager();
        String versionCode = null;
        try
        {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(versionCode))
        {
            tvVersion.setVisibility(View.VISIBLE);
            tvVersion.setText("v" + versionCode);
            int toastCount = 3;
            tvVersion.setOnClickListener(v ->
            {
                if (mCount <= toastCount)
                {
                    if (System.currentTimeMillis() - mTime < 500)
                    {
                        if (null != mToast)
                        {
                            mToast.cancel();
                        }
                        mCount++;
                        String msg;
                        if (mCount <= toastCount)
                        {
                            msg = mCount + "/" + toastCount;
                        } else
                        {
                            tvEgg.setVisibility(View.VISIBLE);
                            msg = getString(R.string.egg_0);
                        }
                        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
                        mToast.show();
                    } else
                    {
                        mTime = System.currentTimeMillis();
                    }
                }
            });
        }
    }
}
