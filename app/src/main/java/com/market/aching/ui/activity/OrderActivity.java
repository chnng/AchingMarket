package com.market.aching.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.market.aching.R;
import com.market.aching.adapter.BookListAdapter;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.market.aching.database.DatabaseConst.SUBMITTED;
import static com.market.aching.database.DatabaseConst.UNSUBMITTED;

/**
 * Created by Administrator on 2017/5/5.
 */

public class OrderActivity extends BaseActivity
{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;
    private BookListAdapter mListAdapter;
    private List<BookInfoResponse> bookInfoResponses;
    private OrderManager mOrderManager;
    private int spanCount = 1;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        mToolbar.setTitle(R.string.order_title);

        mOrderManager = new OrderManager();
        bookInfoResponses = new ArrayList<>();
        for (OrderInfo orderInfo : mOrderManager.queryAllOrder(SUBMITTED))
        {
            bookInfoResponses.add(orderInfo.bookInfo);
        }

        spanCount = getResources().getInteger(R.integer.home_span_count);
        mLayoutManager = new GridLayoutManager(this, spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                return mListAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置adapter
        mListAdapter = new BookListAdapter(this, bookInfoResponses, spanCount);
        mListAdapter.setType(BookListAdapter.Type.ORDER);
        mRecyclerView.setAdapter(mListAdapter);
    }
}
