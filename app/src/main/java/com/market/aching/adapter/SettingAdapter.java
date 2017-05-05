package com.market.aching.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.market.aching.R;
import com.market.aching.database.AccountManager;
import com.market.aching.ui.activity.LoginActivity;
import com.market.aching.ui.activity.OrderActivity;

import java.util.List;

import static com.market.aching.util.AchingUtil.dip2px;

/**
 * Created by Administrator on 2017/5/5.
 */

public class SettingAdapter extends RecyclerView.Adapter
{
    private Context mContext;
    private String[] mOptionArray;

    public SettingAdapter(Context context, String[] optionArray)
    {
        this.mContext = context;
        this.mOptionArray = optionArray;
    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting_option, parent, false);
        return new OptionHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        OptionHolder optionHolder = (OptionHolder) holder;
        optionHolder.mTvOption.setText(mOptionArray[position]);
        if (position == mOptionArray.length - 1)
        {
            ((RecyclerView.LayoutParams) optionHolder.mCvOption.getLayoutParams()).topMargin = dip2px(150);
            optionHolder.mTvOption.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        optionHolder.itemView.setOnClickListener(v ->
        {
            switch (position)
            {
                case 0:
                case 1:
                case 3:
                    Snackbar.make(optionHolder.itemView, "施工中", Snackbar.LENGTH_SHORT).show();
                    break;
                case 2:
                    Intent intentOrder = new Intent(mContext, OrderActivity.class);
                    mContext.startActivity(intentOrder);
                    break;
                case 4:
                    AccountManager accountManager = new AccountManager();
                    accountManager.loginOut();
                    Intent intentLoginOut = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intentLoginOut);
                    ((Activity) mContext).finish();
                    break;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mOptionArray.length;
    }

    private class OptionHolder extends RecyclerView.ViewHolder
    {
        private CardView mCvOption;
        private TextView mTvOption;
        private OptionHolder(View itemView)
        {
            super(itemView);
            mCvOption = (CardView) itemView.findViewById(R.id.cv_setting_option);
            mTvOption = (TextView) itemView.findViewById(R.id.tv_setting_option);
        }
    }
}
