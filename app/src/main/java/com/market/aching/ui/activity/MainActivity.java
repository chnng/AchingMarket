package com.market.aching.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setSupportActionBar(toolbar);
    }
}
