package com.market.aching.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.market.aching.R;
import com.market.aching.adapter.BookListAdapter;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.ui.base.BaseFragment;
import com.market.aching.util.Global;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.market.aching.database.DatabaseConst.UNSUBMITTED;
import static com.market.aching.util.AchingUtil.log;

/**
 * Created by Administrator on 2017/5/4.
 */

public class ShoppingCarFragment extends BaseFragment
{
    @BindView(R.id.layout_recycler_parent)
    CoordinatorLayout mLayoutParent;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
//    @BindView(R.id.swipe_refresh_widget)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private GridLayoutManager mLayoutManager;
    private BookListAdapter mListAdapter;
    private List<BookInfoResponse> bookInfoResponses;
    private int spanCount = 1;
    private OrderManager mOrderManager;
    private ItemTouchHelper touchHelper;
    private boolean isSortable;

    public static ShoppingCarFragment newInstance()
    {
        return new ShoppingCarFragment();
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.fragment_shopping_car;
    }

    @Override
    protected int getTitleID()
    {
        return R.string.title_notifications;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(Toolbar toolbar)
    {
        toolbar.inflateMenu(R.menu.menu_order);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_edit:
                        if (!bookInfoResponses.isEmpty())
                        {
                            isSortable = !isSortable;
                            switchSortable(isSortable);
                            if (isSortable)
                            {
                                showMessage(R.string.order_delete_tip);
                            }
                        } else
                        {
                            showMessage(R.string.order_list_empty);
                        }
                        break;
                }
                return true;
            }
        });
        initFloatingButton();
        initRecyclerView();
    }

    private void initFloatingButton()
    {
//        mFab.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(v ->
        {
            if (bookInfoResponses.isEmpty())
            {
                showMessage(R.string.order_list_empty);
            } else
            {
                List<String> bookIDs = new ArrayList<>();
                for (BookInfoResponse bookInfoResponse : bookInfoResponses)
                {
                    if (bookInfoResponse.isChecked())
                    {
                        bookIDs.add(bookInfoResponse.getId());
                    }
                }
                if (bookIDs.isEmpty())
                {
                    showMessage(R.string.order_check_one);
                } else
                {
                    mOrderManager.submitOrder(bookIDs);
                    onRefresh();
                    showMessage(R.string.order_success);
                }
            }
        });
    }

    private void showMessage(int msgID)
    {
        Snackbar.make(mRecyclerView, msgID, Snackbar.LENGTH_SHORT).show();
    }

    protected void initRecyclerView()
    {
        spanCount = getResources().getInteger(R.integer.home_span_count);
        bookInfoResponses = new ArrayList<>();
        mOrderManager = new OrderManager();
//        for (OrderInfo orderInfo : mOrderManager.queryAllOrder(UNSUBMITTED))
//        {
//            bookInfoResponses.add(orderInfo.bookInfo);
//        }
//        mSwipeRefreshLayout.setEnabled(false);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
//                R.color.recycler_color3, R.color.recycler_color4);
//        mSwipeRefreshLayout.setEnabled(false);
        mLayoutManager = new GridLayoutManager(getActivity(), spanCount);
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

        //设置adapter
        mListAdapter = new BookListAdapter(getActivity(), bookInfoResponses, spanCount);
        mListAdapter.setType(BookListAdapter.Type.SHOPPING_CAR);
        mRecyclerView.setAdapter(mListAdapter);

        mLayoutParent.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                log("setOnTouchListener-mRecyclerView");
                ((ViewPager) getActivity().findViewById(R.id.container)).requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mListAdapter.getItemCount()) {
//                    onLoadMore();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                log("onMove");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                if (bookInfoResponses.isEmpty())
                {
                    return;
                }
                int position = viewHolder.getAdapterPosition();
                mOrderManager.deleteOrder(bookInfoResponses.get(position).getId());
                bookInfoResponses.remove(position);
                mListAdapter.notifyItemRemoved(position);
                if (bookInfoResponses.isEmpty())
                {
                    switchSortable(false);
                }
            }
        });
//        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    //    @Override
    public void onRefresh()
    {
        bookInfoResponses.clear();
        for (OrderInfo orderInfo : mOrderManager.queryAllOrder(Global.getAccountInfo().account, UNSUBMITTED))
        {
            bookInfoResponses.add(orderInfo.bookInfo);
        }
        if (bookInfoResponses.isEmpty())
        {
            switchSortable(false);
        }
        mListAdapter.notifyDataSetChanged();
//        mSwipeRefreshLayout.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void switchSortable(boolean isSortable)
    {
        this.isSortable = isSortable;
        touchHelper.attachToRecyclerView(isSortable ? mRecyclerView : null);

        boolean mListAdapterSortable = mListAdapter.isSortable();
        mListAdapter.setSortable(isSortable);
        if (isSortable != mListAdapterSortable)
        {
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        onRefresh();
    }
}
