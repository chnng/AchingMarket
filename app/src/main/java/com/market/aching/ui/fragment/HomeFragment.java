package com.market.aching.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.market.aching.R;
import com.market.aching.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by chnng on 2017/4/30.
 */

public class HomeFragment extends BaseFragment
{
    @BindView(R.id.rv_fragment_home)
    RecyclerView mRecyclerView;

    public static HomeFragment getInstance(int index)
    {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    protected int getToolBar()
    {
        return R.id.toolbar;
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        int index = getArguments().getInt("index");
        switch (index)
        {
            case 0:
                setTitle(getString(R.string.title_home));
                break;
            case 1:
                setTitle(getString(R.string.title_notifications));
                break;
            case 2:
                setTitle(getString(R.string.title_dashboard));
                break;
        }
    }
}
