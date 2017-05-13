package com.market.aching.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.market.aching.R;
import com.market.aching.database.OrderManager;
import com.market.aching.model.AccountInfo;
import com.market.aching.model.OrderInfo;
import com.market.aching.ui.activity.OrderActivity;
import com.market.aching.util.Global;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.market.aching.database.DatabaseConst.SUBMITTED;
import static com.market.aching.util.AchingUtil.log;

/**
 * Created by chnng on 2017/5/7.
 */

public class AdminAdapter extends RecyclerView.Adapter
{
    private Context mContext;
    private List<AccountInfo> mList;

    public AdminAdapter(Context context, List<AccountInfo> list)
    {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_account_info, parent, false);
        return new AdminHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        AdminHolder adminHolder = (AdminHolder) holder;
        AccountInfo info = mList.get(position);
        adminHolder.tvAccount.setText(info.account);
        adminHolder.tvName.setText(info.name);
        adminHolder.tvAddress.setText(info.address);
        OrderInfo orderInfo = new OrderManager().queryLastOrder(info.account, SUBMITTED);
        log("onBindViewHolder ===>> " + orderInfo);
        if (null != orderInfo && null != orderInfo.bookInfo && orderInfo.bookInfo.getTime() != 0)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(orderInfo.bookInfo.getTime());
            final String formatData = simpleDateFormat.format(date);
            final String content = "最后订单生成时间：\n" + formatData
                    + "\n点击查看更多";
            final String test = "z\n";
            log(test.length());
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorAccent60)),
                    10, formatData.length() + 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(),
                    content.length() - 6, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            adminHolder.tvOrder.setText(spannableString);
        }
        adminHolder.tvOrder.setOnClickListener(v ->
        {

            Intent intent = new Intent(mContext, OrderActivity.class);
            intent.putExtra("account", info.account);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    private class AdminHolder extends RecyclerView.ViewHolder
    {
        TextView tvAccount, tvName, tvAddress, tvOrder;

        public AdminHolder(View itemView)
        {
            super(itemView);
            tvAccount = (TextView) itemView.findViewById(R.id.tv_admin_account);
            tvName = (TextView) itemView.findViewById(R.id.tv_admin_name);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_admin_address);
            tvOrder = (TextView) itemView.findViewById(R.id.tv_admin_order);
        }
    }
}
