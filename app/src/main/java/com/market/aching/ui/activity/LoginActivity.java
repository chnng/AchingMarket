package com.market.aching.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.market.aching.R;
import com.market.aching.ui.base.BaseActivity;
import com.market.aching.utils.DBManager;

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

    int id;
    @OnClick(R.id.login_btn_confirm)
    void onClick(View v)
    {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
        ContentValues values1 = new ContentValues();
        values1.put("name", "呵呵~");
        //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
//        DBManager.getInstance(this).getWritableDatabase().insert("person", null, values1);
//        DBManager.getInstance(this).getWritableDatabase().execSQL("insert into person(personid,name) values(3,'da')");
        DBManager.getInstance(this).getWritableDatabase().execSQL("INSERT INTO person(personid,name) values(?,?)", new String[] {"" + id++, "大"});
    }
}
