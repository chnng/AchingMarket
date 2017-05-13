package com.market.aching.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.market.aching.R;
import com.market.aching.adapter.AdminAdapter;
import com.market.aching.database.AccountManager;
import com.market.aching.model.AccountInfo;
import com.market.aching.ui.base.ToolbarActivity;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chnng on 2017/5/6.
 */

public class AdminActivity extends ToolbarActivity
{
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_recycler;
    }

    @Override
    public int getTitleID()
    {
        return R.string.title_admin;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        List<AccountInfo> list = new AccountManager().queryAllAccount();
        Iterator<AccountInfo> it = list.iterator();
        while(it.hasNext()) {
            AccountInfo info = it.next();
            if (info.loginState == 1)
            {
                it.remove();
            }
        }
        mRecyclerView.setAdapter(new AdminAdapter(this, list));
    }
}
