package com.market.aching.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.market.aching.R;
import com.market.aching.model.AccountInfo;
import com.market.aching.ui.base.BaseActivity;
import com.market.aching.utils.DBManager;
import com.market.aching.utils.ScreenManager;

import java.util.ArrayList;

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
    private AutoCompleteTextView mEditTextAccount;
    private EditText mEditTextPassword;

    private static final int INPUT_TYPE_ACCOUNT = 0, INPUT_TYPE_PASSWORD = 1;

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        mEditTextAccount = (AutoCompleteTextView) mTextInputAccount.getEditText();
        mEditTextPassword = mTextInputPassword.getEditText();
        TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_NEXT:
                        return v == mEditTextAccount && !isInputValid(INPUT_TYPE_ACCOUNT);
                    case EditorInfo.IME_ACTION_DONE:
                        return v == mEditTextPassword && !attemptLogin();
                    default:
                        return false;
                }
            }
        };
        mEditTextAccount.setOnEditorActionListener(onEditorActionListener);
        mEditTextPassword.setOnEditorActionListener(onEditorActionListener);

        // 过滤特殊字符
//        InputFilter filter = new InputFilter()
//        {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
//            {
//                String speChat = "[ `~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//                Pattern pattern = Pattern.compile(speChat);
//                Matcher matcher = pattern.matcher(source.toString());
//                if (matcher.find()) return "";
//                else return null;
//            }
//        };
//        mEditTextAccount.setFilters(new InputFilter[]{filter});

        final ArrayList<AccountInfo> infos = DBManager.getInstance().queryAllAccount();
        String[] accounts = new String[infos.size()];
        for (int i = 0; i < infos.size(); i++)
        {
            final AccountInfo info = infos.get(i);
            accounts[i] = info.account;
            if (info.loginState == 1)
            {
                mEditTextAccount.setText(info.account);
                mEditTextPassword.setText(new String(info.password));
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accounts);
        mEditTextAccount.setAdapter(arrayAdapter);
        mEditTextAccount.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mEditTextPassword.setText(new String(infos.get(position).password));
            }
        });
    }

    @OnClick(R.id.login_btn_confirm)
    void onClick()
    {
        attemptLogin();
    }

    /**
     * 检查输入内容
     *
     * @param inputType 输入类型
     * @return 是否有效
     */
    private boolean isInputValid(int inputType)
    {
        EditText editText;
        String errorMsg;
        switch (inputType)
        {
            case INPUT_TYPE_ACCOUNT:
                editText = mEditTextAccount;
                errorMsg = getString(R.string.login_account);
                break;
            case INPUT_TYPE_PASSWORD:
                editText = mEditTextPassword;
                errorMsg = getString(R.string.login_password);
                break;
            default:
                return false;
        }
        editText.setError(null);
        String inputMsg = editText.getText().toString();
        if (TextUtils.isEmpty(inputMsg))
        {
            errorMsg += getString(R.string.login_error_empty);
            editText.setError(errorMsg);
            return false;
        } else if (inputMsg.length() < 4)
        {
            errorMsg += getString(R.string.login_error_short);
            editText.setError(errorMsg);
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 尝试登陆
     *
     * @return 成功登陆
     */
    private boolean attemptLogin()
    {
        if (!isInputValid(INPUT_TYPE_ACCOUNT))
        {
            mEditTextAccount.requestFocus();
            return false;
        } else if (!isInputValid(INPUT_TYPE_PASSWORD))
        {
            mEditTextPassword.requestFocus();
            return false;
        } else
        {
            // Store values at the time of the login attempt.
            String account = mEditTextAccount.getText().toString();
            String password = mEditTextPassword.getText().toString();
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.uid = Integer.parseInt(account);
            accountInfo.account = account;
            accountInfo.password = password.getBytes();
            accountInfo.loginState = 1;
            DBManager.getInstance().updateAccount(accountInfo);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            ScreenManager.getScreenManager().popActivityFinish();
            return true;
        }
    }
}
