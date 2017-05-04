package com.market.aching.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.market.aching.R;
import com.market.aching.adapter.BookListAdapter;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.presenter.impl.BookListPresenterImpl;
import com.market.aching.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.market.aching.database.DatabaseConst.UNSUBMITTED;
import static com.market.aching.util.AchingUtil.log;

/**
 * Created by Administrator on 2017/5/4.
 */

public class OrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.rv_fragment_home)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private GridLayoutManager mLayoutManager;
    private BookListAdapter mListAdapter;
    private List<BookInfoResponse> bookInfoResponses;
    private int spanCount = 1;
    private OrderManager mOrderManager;

    public static OrderFragment newInstance()
    {
        return new OrderFragment();
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.fragment_recycler;
    }

    @Override
    protected int getTitleID()
    {
        return R.string.title_notifications;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        mFab.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(v -> {
            if (bookInfoResponses.isEmpty())
            {
                Snackbar.make(v, "您的购物车空空如也", Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                List<String> bookIDs = new ArrayList<>();
                for (BookInfoResponse bookInfoResponse : bookInfoResponses)
                {
                    if (bookInfoResponse.isChecked())
                    {
                        bookIDs.add(bookInfoResponse.getId());
                    }
                }
//                int[] array = new int[bookInfoResponses.size()];
//                for (int i = 0; i < bookInfoResponses.size(); i++)
//                {
//                    array[i] = Integer.parseInt(bookInfoResponses.get(i).getId());
//                }
                if (bookIDs.isEmpty())
                {
                    Snackbar.make(v, "请选择一本图书购买", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    mOrderManager.submitOrder(bookIDs);
                    onRefresh();
                    Snackbar.make(v, "购买成功", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        initEvents();
    }

    protected void initEvents() {
        spanCount = getResources().getInteger(R.integer.home_span_count);
        bookInfoResponses = new ArrayList<>();
        mOrderManager = new OrderManager();
//        for (OrderInfo orderInfo : mOrderManager.queryAllOrder(UNSUBMITTED))
//        {
//            bookInfoResponses.add(orderInfo.bookInfo);
//        }
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mListAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mListAdapter = new BookListAdapter(getActivity(), bookInfoResponses, spanCount);
        mRecyclerView.setAdapter(mListAdapter);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mListAdapter.getItemCount()) {
//                    onLoadMore();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh()
    {
        bookInfoResponses.clear();
        for (OrderInfo orderInfo : mOrderManager.queryAllOrder(UNSUBMITTED))
        {
            bookInfoResponses.add(orderInfo.bookInfo);
        }
        mListAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        onRefresh();
        log("onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        log("onHiddenChanged " + hidden);
    }
}
