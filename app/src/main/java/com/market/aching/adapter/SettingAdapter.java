package com.market.aching.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.market.aching.R;
import com.market.aching.database.AccountManager;
import com.market.aching.ui.activity.AboutActivity;
import com.market.aching.ui.activity.AdminActivity;
import com.market.aching.ui.activity.LoginActivity;
import com.market.aching.ui.activity.OrderActivity;
import com.market.aching.util.Global;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/5/5.
 */

public class SettingAdapter extends RecyclerView.Adapter
{
    private Context mContext;
    private String[] mOptionArray;

    private enum Type
    {
        NAME, ADDRESS
    }

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
//            ((RecyclerView.LayoutParams) optionHolder.mCvOption.getLayoutParams()).topMargin = dip2px(120);
            optionHolder.mTvOption.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        optionHolder.itemView.setOnClickListener(v ->
        {
            Intent intent;
            if (mOptionArray[position].equals(mContext.getString(R.string.setting_name)))
            {
                showDialog(Type.NAME);

            } else if (mOptionArray[position].equals(mContext.getString(R.string.setting_address)))
            {
                showDialog(Type.ADDRESS);
            } else if (mOptionArray[position].equals(mContext.getString(R.string.setting_order)))
            {
                intent = new Intent(mContext, OrderActivity.class);
                intent.putExtra("account", Global.getAccountInfo().account);
                mContext.startActivity(intent);
            } else if (mOptionArray[position].equals(mContext.getString(R.string.setting_admin)))
            {
                intent = new Intent(mContext, AdminActivity.class);
                mContext.startActivity(intent);
            } else if (mOptionArray[position].equals(mContext.getString(R.string.setting_about)))
            {
                intent = new Intent(mContext, AboutActivity.class);
                mContext.startActivity(intent);
            } else if (mOptionArray[position].equals(mContext.getString(R.string.setting_login_out)))
            {
                AccountManager accountManager = new AccountManager();
                accountManager.loginOut();
                intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        });
    }

    private void showDialog(Type type)
    {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting, null);
        TextInputLayout inputLayout = (TextInputLayout) dialogView.findViewById(R.id.layout_setting_dialog_input);
        EditText editText = inputLayout.getEditText();
        int maxLength;
        String text = null;
        switch (type)
        {
            case NAME:
                maxLength = 10;
                inputLayout.setHint(mContext.getString(R.string.setting_name));
                text = Global.getAccountInfo().name;
                break;
            case ADDRESS:
                maxLength = 20;
                inputLayout.setHint(mContext.getString(R.string.setting_address));
                text = Global.getAccountInfo().address;
                break;
            default:
                maxLength = 10;
                break;
        }
        if (!TextUtils.isEmpty(text))
        {
            String finalText = text;
            editText.postDelayed(() -> editText.setText(finalText), 300);
        }
        editText.setOnEditorActionListener((v, actionId, event) ->
        {
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_ENTER:
                    updateInfo(type, inputLayout);
                    return true;
                default:
                    return false;
            }
        });
        inputLayout.setCounterMaxLength(maxLength);
        editText.setMaxEms(maxLength);
        new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setPositiveButton(R.string.positive, (dialog, which) ->
                {
                    if (!updateInfo(type, inputLayout))
                    {
                        setDialogShow(dialog, false);
                        inputLayout.post(() -> setDialogShow(dialog, true));
                    }
                })
                .setNegativeButton(R.string.negative, null)
                .create().show();
    }

    private void setDialogShow(DialogInterface dialog, boolean isShow)
    {
        try
        {
            Field field = dialog.getClass().getSuperclass()
                    .getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, isShow);//设定为true,则可以关闭对话框
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private boolean updateInfo(Type type, TextInputLayout inputLayout)
    {
        String text = inputLayout.getEditText().getText().toString();
        if (!TextUtils.isEmpty(text))
        {
            switch (type)
            {
                case NAME:
                    if (!text.equals(Global.getAccountInfo().name))
                    {
                        new AccountManager().updateName(text.trim());
                    }
                    break;
                case ADDRESS:
                    if (!text.equals(Global.getAccountInfo().address))
                    {
                        new AccountManager().updateAddress(text.trim());
                    }
                    break;
            }
            return true;
        }
        else
        {
            inputLayout.setError(inputLayout.getHint() + mContext.getString(R.string.login_error_empty));
            return false;
        }
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
