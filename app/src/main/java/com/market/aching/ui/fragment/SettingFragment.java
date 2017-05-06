package com.market.aching.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.market.aching.R;
import com.market.aching.adapter.SettingAdapter;
import com.market.aching.ui.base.BaseFragment;
import com.market.aching.util.Blur;
import com.market.aching.util.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

import static com.market.aching.R.id.iv_book_bg;

/**
 * Created by Administrator on 2017/5/4.
 */

public class SettingFragment extends BaseFragment
{
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout;
    @BindView(R.id.iv_book_img)
    ImageView iv_book_img;
    @BindView(R.id.iv_book_bg)
    ImageView iv_book_bg;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    public static SettingFragment newInstance()
    {
        return new SettingFragment();
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
    protected void initViews(Toolbar toolbar)
    {
//        mSwipeRefreshLayout.setEnabled(false);
        String[] optionArray = getResources().getStringArray(
                Global.getAccountInfo().account.equals(
                        getString(R.string.account_administrator_0))
                        ? R.array.options_admin : R.array.options_user);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new SettingAdapter(getContext(), optionArray));

//        mCollapsingLayout.setTitle(getString(R.string.title_dashboard));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bear, options);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP))
        {
            iv_book_bg.setImageBitmap(Blur.apply(bitmap));
            iv_book_bg.setAlpha(0.9f);
        } else
        {
            iv_book_bg.setImageBitmap(bitmap);
            iv_book_bg.setAlpha(0.7f);
        }
    }
}
