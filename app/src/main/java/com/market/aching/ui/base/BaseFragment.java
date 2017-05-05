package com.market.aching.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.market.aching.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/5.
 */

public abstract class BaseFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, rootView);
        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(getTitleID()));
//        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        initViews(toolbar);
        return rootView;
    }

//    protected abstract int getToolBar();

    protected abstract int getLayoutID();

    protected abstract int getTitleID();

    protected abstract void initViews(Toolbar toolbar);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
