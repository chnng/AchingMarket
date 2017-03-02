package com.market.aching.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/28.
 */

public class LoginActivity extends BaseActivity
{
    @BindView(R.id.login_layout_text_input_account)
    TextInputLayout mTextInputAccount;
    @BindView(R.id.login_layout_text_input_password)
    TextInputLayout mTextInputPassword;

    EditText mEditTextAccount, mEditTextPassword;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        mEditTextAccount = mTextInputAccount.getEditText();
        mEditTextPassword = mTextInputPassword.getEditText();
    }

    @OnClick(R.id.login_btn_confirm)
    void onClick(View v)
    {
//        Snackbar.make(v, "~~", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
