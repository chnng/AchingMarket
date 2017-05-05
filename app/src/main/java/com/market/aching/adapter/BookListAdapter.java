package com.market.aching.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.market.aching.BaseApplication;
import com.market.aching.R;
import com.market.aching.database.OrderManager;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.ui.activity.BookDetailActivity;
import com.market.aching.ui.base.BaseActivity;

import java.util.List;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/1/9 0009
 */
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DEFAULT = 1;
    private final List<BookInfoResponse> bookInfoResponses;
    private Context mContext;
    private int columns;
    private OrderManager mOrderManager;
    private boolean sortable;

    private Type mType;

    public void setType(Type type)
    {
        this.mType = type;
    }

    public enum Type
    {
        HOME, SHOPPING_CAR, ORDER
    }

    public BookListAdapter(Context context, List<BookInfoResponse> responses, int columns) {
        this.bookInfoResponses = responses;
        this.columns = columns;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_DEFAULT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
            return new BookListHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (bookInfoResponses == null || bookInfoResponses.isEmpty()) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DEFAULT;
        }
    }

    public int getItemColumnSpan(int position) {
        switch (getItemViewType(position)) {
            case TYPE_DEFAULT:
                return 1;
            default:
                return columns;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookListHolder) {
            final BookListHolder bookHolder = (BookListHolder) holder;
            final BookInfoResponse bookInfo = bookInfoResponses.get(position);
            if (mType == Type.SHOPPING_CAR)
            {
                bookHolder.cv_book.setCardBackgroundColor(sortable ? mContext.getResources().getColor(R.color.colorAccent60) : Color.TRANSPARENT);
                bookHolder.cv_book.setOnTouchListener((v, event) ->
                {
                    ((ViewPager) ((Activity) mContext).findViewById(R.id.container)).requestDisallowInterceptTouchEvent(sortable);
                    return false;
                });
                if (bookInfo.getQuantity() > 0)
                {
                    bookHolder.cb_book.setVisibility(View.VISIBLE);
                    bookHolder.cb_book.setChecked(bookInfo.isChecked());
                    bookHolder.cb_book.setOnClickListener(v ->
                    {
                        boolean isChecked = bookHolder.cb_book.isChecked();
                        bookInfo.setChecked(isChecked);
                        if (null == mOrderManager)
                        {
                            mOrderManager = new OrderManager();
                        }
                        mOrderManager.setCheckOrder(bookInfo.getId(), isChecked);
                    });
                }
            }
            if (mType != Type.HOME && bookInfo.getQuantity() > 0)
            {
                bookHolder.tv_book_quantity.setVisibility(View.VISIBLE);
                bookHolder.tv_book_quantity.setText("X" + bookInfo.getQuantity());
            }
            Glide.with(mContext)
                    .load(bookInfo.getImages().getLarge())
                    .into(bookHolder.iv_book_img);
            bookHolder.tv_book_title.setText(bookInfo.getTitle());
            bookHolder.ratingBar_hots.setRating(Float.valueOf(bookInfo.getRating().getAverage()) / 2);
            bookHolder.tv_hots_num.setText(bookInfo.getRating().getAverage());
            bookHolder.tv_book_info.setText(bookInfo.getInfoString());
            bookHolder.tv_book_description.setText("\u3000" + bookInfo.getSummary());
            bookHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putSerializable(BookInfoResponse.serialVersionName, bookInfo);
                    Bitmap bitmap;
                    GlideBitmapDrawable imageDrawable = (GlideBitmapDrawable) bookHolder.iv_book_img.getDrawable();
                    if (imageDrawable != null) {
                        bitmap = imageDrawable.getBitmap();
                        b.putParcelable("book_img", bitmap);
                    }
                    Intent intent = new Intent(BaseApplication.getInstance(), BookDetailActivity.class);
                    intent.putExtras(b);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (BaseActivity.activity == null) {
                            BaseActivity.activity.startActivity(intent);
                            return;
                        }
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(BaseActivity.activity, bookHolder.iv_book_img, "book_img");
                        BaseActivity.activity.startActivity(intent, options.toBundle());
                    } else {
                        BaseActivity.activity.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (bookInfoResponses.isEmpty()) {
            return 1;
        }
        return bookInfoResponses.size();
    }

    public boolean isSortable()
    {
        return sortable;
    }

    public void setSortable(boolean sortable)
    {
        this.sortable = sortable;
    }

    class BookListHolder extends RecyclerView.ViewHolder {

        private final CardView cv_book;
        private final AppCompatCheckBox cb_book;
        private final ImageView iv_book_img;
        private final TextView tv_book_title;
        private final TextView tv_book_quantity;
        private final AppCompatRatingBar ratingBar_hots;
        private final TextView tv_hots_num;
        private final TextView tv_book_info;
        private final TextView tv_book_description;

        public BookListHolder(View itemView) {
            super(itemView);
            cv_book = (CardView) itemView.findViewById(R.id.cv_book_item);
            cb_book = (AppCompatCheckBox) itemView.findViewById(R.id.checkBox);
            iv_book_img = (ImageView) itemView.findViewById(R.id.iv_book_img);
            tv_book_title = (TextView) itemView.findViewById(R.id.tv_book_title);
            tv_book_quantity = (TextView) itemView.findViewById(R.id.tv_book_quantity);
            ratingBar_hots = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBar_hots);
            tv_hots_num = (TextView) itemView.findViewById(R.id.tv_hots_num);
            tv_book_info = (TextView) itemView.findViewById(R.id.tv_book_info);
            tv_book_description = (TextView) itemView.findViewById(R.id.tv_book_description);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
