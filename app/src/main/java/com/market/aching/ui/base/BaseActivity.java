package com.market.aching.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.market.aching.utils.ScreenManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/11/5.
 */

public abstract class BaseActivity extends AppCompatActivity
{
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ScreenManager.getScreenManager().pushActivity(this);
        unbinder = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
//        initToolBar();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

//    protected abstract void initToolBar();

    @Override
    protected void onDestroy()
    {
        unbinder.unbind();
        super.onDestroy();
    }
}
