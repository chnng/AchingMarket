package com.market.aching.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.market.aching.R;
import com.market.aching.adapter.BookDetailAdapter;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.BookReviewsListResponse;
import com.market.aching.model.BookSeriesListResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.presenter.impl.BookDetailPresenterImpl;
import com.market.aching.presenter.IBookDetailView;
import com.market.aching.ui.base.BaseActivity;
import com.market.aching.util.Blur;
import com.market.aching.util.Global;

import butterknife.BindView;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/2/19 0019
 * Description: 图书详情页
 */
public class BookDetailActivity extends BaseActivity implements IBookDetailView
{
    private static final String COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless";
    private static final String SERIES_FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series";
    private static final int REVIEWS_COUNT = 5;
    private static final int SERIES_COUNT = 6;
    private static final int PAGE = 0;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BookDetailAdapter mDetailAdapter;
    private ImageView iv_book_img;
    private ImageView iv_book_bg;

    private BookInfoResponse mBookInfoResponse;
    private BookReviewsListResponse mReviewsListResponse;
    private BookSeriesListResponse mSeriesListResponse;

    private BookDetailPresenterImpl bookDetailPresenter;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_book_detail);
//        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_clear));
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        initEvents();
    }

    protected void initEvents() {
        bookDetailPresenter = new BookDetailPresenterImpl(this);
        mReviewsListResponse = new BookReviewsListResponse();
        mSeriesListResponse = new BookSeriesListResponse();
        mBookInfoResponse = (BookInfoResponse) getIntent().getSerializableExtra(BookInfoResponse.serialVersionName);
        mLayoutManager = new LinearLayoutManager(BookDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDetailAdapter = new BookDetailAdapter(mBookInfoResponse, mReviewsListResponse, mSeriesListResponse);
        mRecyclerView.setAdapter(mDetailAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //头部图片
        iv_book_img = (ImageView) findViewById(R.id.iv_book_img);
        iv_book_bg = (ImageView) findViewById(R.id.iv_book_bg);
        mCollapsingLayout.setTitle(mBookInfoResponse.getTitle());

        Bitmap book_img = getIntent().getParcelableExtra("book_img");
        if (book_img != null) {
            iv_book_img.setImageBitmap(book_img);
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP))
            {
                iv_book_bg.setImageBitmap(Blur.apply(book_img));
                iv_book_bg.setAlpha(0.9f);
            }
            else
            {
                iv_book_bg.setImageBitmap(book_img);
                iv_book_bg.setAlpha(0.7f);
            }
//            iv_book_bg.setImageBitmap(FastBlurUtil.doBlur(book_img, 5, true));
        } else {
            Glide.with(this)
                    .load(mBookInfoResponse.getImages().getLarge())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv_book_img.setImageBitmap(resource);
                            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP))
                            {
                                iv_book_bg.setImageBitmap(Blur.apply(resource));
//                              iv_book_bg.setImageBitmap(FastBlurUtil.doBlur(book_img, 5, true));
                                iv_book_bg.setAlpha(0.9f);
                            }
                            else
                            {
                                iv_book_bg.setImageBitmap(resource);
                                iv_book_bg.setAlpha(0.7f);
                            }
                        }
                    });
        }
//        mFab.setOnClickListener(v -> Toast.makeText(BookDetailActivity.this, "click", Toast.LENGTH_SHORT).show());
        OrderManager orderManager = new OrderManager();
        mFab.setOnClickListener(v ->
                {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.account = Global.getAccountInfo().account;
                    orderInfo.bookID = Integer.parseInt(mBookInfoResponse.getId());
                    orderInfo.orderState = 0;
                    orderInfo.quantity = 1;
                    orderInfo.time = System.currentTimeMillis();
                    orderInfo.address = Global.getAccountInfo().address;
                    orderInfo.bookInfo = mBookInfoResponse;
                    orderManager.updateOrder(orderInfo);
                    Snackbar.make(v, "添加购物车成功", Snackbar.LENGTH_SHORT).show();
                }
        );
        bookDetailPresenter.loadReviews(mBookInfoResponse.getId(), PAGE * REVIEWS_COUNT, REVIEWS_COUNT, COMMENT_FIELDS);
    }

//    @Override
//    protected int getMenuID() {
//        return R.menu.menu_book_detail;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_share:
//                StringBuilder sb = new StringBuilder();
//                sb.append(getString(R.string.your_friend));
//                sb.append(getString(R.string.share_book_1));
//                sb.append(mBookInfoResponse.getTitleID());
//                sb.append(getString(R.string.share_book_2));
//                UIUtils.share(this, sb.toString(), null);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (item.getIcon() instanceof Animatable) {
//                        ((Animatable) item.getIcon()).start();
//                    }
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(mToolbar, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFab.getDrawable() instanceof Animatable) {
                ((Animatable) mFab.getDrawable()).start();
            }
        } else {
            //低于5.0，显示其他动画
//            showMessage(getString(R.string.loading));
        }
    }

    @Override
    public void hideProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFab.getDrawable() instanceof Animatable) {
                ((Animatable) mFab.getDrawable()).stop();
            }
        } else {
            //低于5.0，显示其他动画
        }
    }

    @Override
    public void updateView(Object result) {
        if (result instanceof BookReviewsListResponse) {
            final BookReviewsListResponse response = (BookReviewsListResponse) result;
            mReviewsListResponse.setTotal(response.getTotal());
            mReviewsListResponse.getReviews().addAll(response.getReviews());
            mDetailAdapter.notifyDataSetChanged();
            if (mBookInfoResponse.getSeries() != null) {
                bookDetailPresenter.loadSeries(mBookInfoResponse.getSeries().getId(), PAGE * SERIES_COUNT, 6, SERIES_FIELDS);
            }
        } else if (result instanceof BookSeriesListResponse) {
            final BookSeriesListResponse response = (BookSeriesListResponse) result;
            mSeriesListResponse.setTotal(response.getTotal());
            mSeriesListResponse.getBooks().addAll(response.getBooks());
            mDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        bookDetailPresenter.cancelLoading();
        if (mFab.getDrawable() instanceof Animatable) {
            ((Animatable) mFab.getDrawable()).stop();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
