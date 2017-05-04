package com.market.aching.ui.fragment;

import android.os.Bundle;

import com.market.aching.R;
import com.market.aching.ui.base.BaseFragment;

/**
 * Created by Administrator on 2017/5/4.
 */

public class SettingFragment extends BaseFragment
{
    public static SettingFragment newInstance()
    {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.fragment_setting;
    }

    @Override
    protected int getTitleID()
    {
        return R.string.title_dashboard;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

    }
}
