package com.market.aching.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.market.aching.R;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.ui.activity.BookDetailActivity;
import com.market.aching.ui.base.BaseActivity;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/1/26 0026
 * Description:useless
 */
public class BookSeriesCeilHolder
{
    private View mContentView;
    private ImageView iv_book_img;
    private TextView tv_title;
    private AppCompatRatingBar ratingBar_hots;
    private TextView tv_hots_num;
    private BookInfoResponse mBookInfoResponse;

    public BookSeriesCeilHolder(BookInfoResponse bookInfoResponse) {
        this.mBookInfoResponse = bookInfoResponse;
        initView();
        initEvent();
    }

    private void initView() {
        mContentView = LayoutInflater.from(BaseActivity.activity).inflate(R.layout.item_book_series_ceil, null, false);
        iv_book_img = (ImageView) mContentView.findViewById(R.id.iv_book_img);
        tv_title = (TextView) mContentView.findViewById(R.id.tv_title);
        ratingBar_hots = (AppCompatRatingBar) mContentView.findViewById(R.id.ratingBar_hots);
        tv_hots_num = (TextView) mContentView.findViewById(R.id.tv_hots_num);
    }

    private void initEvent() {
        Glide.with(BaseActivity.activity)
                .load(mBookInfoResponse.getImages().getLarge())
                .into(iv_book_img);
        tv_title.setText(mBookInfoResponse.getTitle());
        ratingBar_hots.setRating(Float.valueOf(mBookInfoResponse.getRating().getAverage()) / 2);
        tv_hots_num.setText(mBookInfoResponse.getRating().getAverage());
        mContentView.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putSerializable(BookInfoResponse.serialVersionName, mBookInfoResponse);
            Bitmap bitmap;

            bitmap = ((GlideBitmapDrawable) (iv_book_img.getDrawable())).getBitmap();
            b.putParcelable("book_img", bitmap);
            Intent intent = new Intent(BaseActivity.activity, BookDetailActivity.class);
            intent.putExtras(b);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (BaseActivity.activity == null) {
                    BaseActivity.activity.startActivity(intent);
                    return;
                }
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(BaseActivity.activity, iv_book_img, "book_img");
                BaseActivity.activity.startActivity(intent, options.toBundle());
            } else {
                BaseActivity.activity.startActivity(intent);
            }
        });
    }

    public View getContentView() {
        return mContentView;
    }
}
