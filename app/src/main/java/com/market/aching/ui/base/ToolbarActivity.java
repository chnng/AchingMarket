package com.market.aching.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.market.aching.R;

import butterknife.BindView;

/**
 * Created by chnng on 2017/5/6.
 */

public abstract class ToolbarActivity extends BaseActivity
{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle(getTitleID());
    }

    public abstract int getTitleID();
}
