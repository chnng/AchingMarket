package com.market.aching.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.market.aching.R;
import com.market.aching.adapter.BookListAdapter;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.ui.base.ToolbarActivity;
import com.market.aching.util.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.market.aching.database.DatabaseConst.SUBMITTED;

/**
 * Created by Administrator on 2017/5/5.
 */

public class OrderActivity extends ToolbarActivity
{
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private BookListAdapter mListAdapter;
    private int spanCount = 1;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_recycler;
    }

    @Override
    public int getTitleID()
    {
        return R.string.title_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        OrderManager orderManager = new OrderManager();
        List<BookInfoResponse> bookInfoResponses = new ArrayList<>();
        String account = getIntent().getStringExtra("account");
        for (OrderInfo orderInfo : orderManager.queryAllOrder(account, SUBMITTED))
        {
            bookInfoResponses.add(orderInfo.bookInfo);
        }

        spanCount = getResources().getInteger(R.integer.home_span_count);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                return mListAdapter.getItemColumnSpan(position);
            }
        });
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置adapter
        mListAdapter = new BookListAdapter(this, bookInfoResponses, spanCount);
        mListAdapter.setType(BookListAdapter.Type.ORDER);
        mRecyclerView.setAdapter(mListAdapter);
    }
}
