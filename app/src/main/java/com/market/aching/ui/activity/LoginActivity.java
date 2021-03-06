package com.market.aching.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.market.aching.R;
import com.market.aching.database.AccountManager;
import com.market.aching.model.AccountInfo;
import com.market.aching.ui.base.BaseActivity;
import com.market.aching.util.Global;
import com.market.aching.util.ScreenManager;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private ArrayList<AccountInfo> mAccountInfoList;
    private InputMethodManager mInputMethodManager;
    private AccountManager mAccountManager;

    private enum InputType
    {
        ACCOUNT, PASSWORD
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
                        return v == mEditTextAccount && !isInputValid(InputType.ACCOUNT);
                    case EditorInfo.IME_ACTION_DONE:
                        return v == mEditTextPassword && !attemptLogin();
                    default:
                        return false;
                }
            }
        };
        mEditTextAccount.setOnEditorActionListener(onEditorActionListener);
        mEditTextPassword.setOnEditorActionListener(onEditorActionListener);
        mEditTextAccount.addTextChangedListener(new LoginTextWatcher(mTextInputAccount));
        mEditTextPassword.addTextChangedListener(new LoginTextWatcher(mTextInputPassword));

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

        mAccountManager = new AccountManager();
        mAccountInfoList = mAccountManager.queryAllAccount();
        String[] accountArray = new String[mAccountInfoList.size()];
        for (int i = 0; i < mAccountInfoList.size(); i++)
        {
            final AccountInfo info = mAccountInfoList.get(i);
            accountArray[i] = String.valueOf(info.account);
            if (info.loginState == 1)
            {
                Handler handler = new Handler(getMainLooper());
                handler.postDelayed(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        mTextInputAccount.refreshDrawableState();
                        mEditTextAccount.setText(String.valueOf(info.account));
                        mEditTextPassword.setText(new String(info.password));
                    }
                }, 500);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LoginActivity.this,
                android.R.layout.simple_list_item_1, accountArray);
        mEditTextAccount.setAdapter(arrayAdapter);
        mEditTextAccount.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mEditTextPassword.setText(new String(mAccountInfoList.get(position).password));
            }
        });
    }

    private class LoginTextWatcher implements TextWatcher
    {
        private TextInputLayout textInputLayout;

        private LoginTextWatcher(TextInputLayout textInputLayout)
        {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (null != textInputLayout.getError())
            {
                textInputLayout.setError(null);
            }
        }
    }

    @OnClick({R.id.login_btn_login, R.id.login_btn_register})
    void onLoginClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_btn_login:
                attemptLogin();
                break;
            case R.id.login_btn_register:
                attemptRegister();
                break;
        }
    }

    /**
     * 检查输入内容
     *
     * @param inputType 输入类型
     * @return 是否有效
     */
    private boolean isInputValid(InputType inputType)
    {
        String inputMsg;
        String errorMsg;
        switch (inputType)
        {
            case ACCOUNT:
                mTextInputAccount.setError(null);
                Editable editable = mEditTextAccount.getText();
                inputMsg = editable.toString();
                String regex = getString(R.string.regex);
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(inputMsg);
                if (!m.matches())
                {
                    errorMsg = getString(R.string.login_error_not_match);
                    mTextInputAccount.setError(errorMsg);
                    return false;
                }
            case PASSWORD:
                mTextInputPassword.setError(null);
                errorMsg = getString(R.string.login_password);
                inputMsg = mEditTextPassword.getText().toString();
                if (TextUtils.isEmpty(inputMsg))
                {
                    errorMsg += getString(R.string.login_error_empty);
                    mTextInputPassword.setError(errorMsg);
                    return false;
                }
                else if (inputMsg.length() < 4)
                {
                    errorMsg += getString(R.string.login_error_short);
                    mTextInputPassword.setError(errorMsg);
                    return false;
                }
        }
        return true;
    }

    /**
     * 尝试登陆
     *
     * @return 成功登陆
     */
    private boolean attemptLogin()
    {
        if (!isInputValid(InputType.ACCOUNT))
        {
            mEditTextAccount.requestFocus();
            return false;
        } else if (!isInputValid(InputType.PASSWORD))
        {
            mEditTextPassword.requestFocus();
            return false;
        } else
        {
            // Store values at the time of the login attempt.
            String account = String.valueOf(mEditTextAccount.getText());
            String password = mEditTextPassword.getText().toString();
            if (mAccountInfoList.isEmpty())
            {
                showSnackBar(getString(R.string.login_error_not_exist));
                return false;
            } else
            {
                for (AccountInfo accountTemp : mAccountInfoList)
                {
                    if (accountTemp.account.equals(account))
                    {
                        if (!new String(accountTemp.password).equals(password))
                        {
                            showSnackBar(getString(R.string.login_error_incorrect));
                            return false;
                        }
                        showSnackBar(getString(R.string.login_confirm_success));
                        loginSuccess(accountTemp);
                        return true;
                    }
                }
                showSnackBar(getString(R.string.login_error_not_exist));
                return false;
            }
        }
    }

    private void attemptRegister()
    {
        if (!isInputValid(InputType.ACCOUNT))
        {
            mEditTextAccount.requestFocus();
        } else if (!isInputValid(InputType.PASSWORD))
        {
            mEditTextPassword.requestFocus();
        } else
        {
            // Store values at the time of the register attempt.
            String account = String.valueOf(mEditTextAccount.getText());
            String password = mEditTextPassword.getText().toString();
            if (!mAccountInfoList.isEmpty())
            {
                for (AccountInfo accountTemp : mAccountInfoList)
                {
                    if (accountTemp.account.equals(account))
                    {
                        showSnackBar(getString(R.string.login_error_is_exist));
                        return;
                    }
                }
            }
            showSnackBar(getString(R.string.login_register_success));
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.account = account;
            accountInfo.password = password.getBytes();
            loginSuccess(accountInfo);
        }
    }

    private void showSnackBar(String msg)
    {
        mInputMethodManager.hideSoftInputFromWindow(mEditTextAccount.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (msg.equals(getString(R.string.login_error_incorrect)))
        {
            Snackbar.make(mEditTextAccount, msg, Snackbar.LENGTH_SHORT)
//                    .setAction(R.string.login_confirm_retrieve, new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//
//                }
//            })
                    .show();
        }
        else
        {
            Snackbar.make(mEditTextAccount, msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void loginSuccess(AccountInfo account)
    {
        Global.setAccountInfo(account);
        account.loginState = 1;
        mAccountManager.updateAccount(account);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        ScreenManager.getScreenManager().popActivityFinish();
    }
}
