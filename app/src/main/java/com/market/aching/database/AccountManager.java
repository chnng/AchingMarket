package com.market.aching.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.market.aching.model.AccountInfo;
import com.market.aching.util.Global;

import java.util.ArrayList;
import java.util.Locale;

import static com.market.aching.database.DatabaseConst.FIELD_ORDER_BOOK_ID;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_CHECK;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_STATE;
import static com.market.aching.database.DatabaseConst.SQL_CREATE_ORDER_TABLE;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_ICON;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_LOGIN_STATE;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_NAME;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_PASSWORD;
import static com.market.aching.database.DatabaseConst.TABLE_ACCOUNTS;
import static com.market.aching.database.DatabaseConst.TABLE_ORDER;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AccountManager
{
    /**
     * 更新账户信息
     *
     * @param accountInfo
     */
    public synchronized void updateAccount(AccountInfo accountInfo)
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        database.execSQL("update " + TABLE_ACCOUNTS
                + " set " + FIELD_ACCOUNT_LOGIN_STATE + "=0 where "
                + FIELD_ACCOUNT + "!=" + accountInfo.account);
        int count = 0;
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select count(*) as counts from "
                            + TABLE_ACCOUNTS + " where " + FIELD_ACCOUNT + "=?",
                    new String[]{String.valueOf(accountInfo.account)});
            if (cursor.moveToFirst())
            {
                count = cursor.getInt(cursor.getColumnIndex("counts"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        if (count == 0)
        {
            // 插入
            database.insert(TABLE_ACCOUNTS, null, getContentValues(accountInfo));
            database.execSQL(String.format(Locale.getDefault(), SQL_CREATE_ORDER_TABLE, accountInfo.account));
        } else
        {
            database.update(TABLE_ACCOUNTS, getContentValues(accountInfo), FIELD_ACCOUNT + "=?",
                    new String[]{String.valueOf(accountInfo.account)});
        }
    }

    @NonNull
    private ContentValues getContentValues(AccountInfo accountInfo)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ACCOUNT, accountInfo.account);
        contentValues.put(FIELD_ACCOUNT_PASSWORD, accountInfo.password);
        contentValues.put(FIELD_ACCOUNT_LOGIN_STATE, accountInfo.loginState);
        contentValues.put(FIELD_ACCOUNT_NAME, accountInfo.name);
        contentValues.put(FIELD_ACCOUNT_ICON, accountInfo.icon);
        return contentValues;
    }

    /**
     * 查询当前登录账户信息,即loginState=1的账户
     *
     * @return
     */
    public synchronized AccountInfo queryCurrentAccount()
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        AccountInfo info = new AccountInfo();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from " + TABLE_ACCOUNTS
                            + " where " + FIELD_ACCOUNT_LOGIN_STATE + "=?",
                    new String[]{String.valueOf(1)});
            if (cursor.getCount() > 0)
            {
                int indexAccount = cursor.getColumnIndex(FIELD_ACCOUNT);
                int indexPassword = cursor.getColumnIndex(FIELD_ACCOUNT_PASSWORD);
                int indexLoginState = cursor.getColumnIndex(FIELD_ACCOUNT_LOGIN_STATE);
                int indexName = cursor.getColumnIndex(FIELD_ACCOUNT_NAME);
                int indexIcon = cursor.getColumnIndex(FIELD_ACCOUNT_ICON);
                while (cursor.moveToNext())
                {
                    info.account = cursor.getInt(indexAccount);
                    info.password = cursor.getBlob(indexPassword);
                    info.loginState = cursor.getInt(indexLoginState);
                    info.name = cursor.getString(indexName);
                    info.icon = cursor.getString(indexIcon);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return info;
    }

    /**
     * 查询所有没有被清理的账户
     *
     * @return
     */
    public synchronized ArrayList<AccountInfo> queryAllAccount()
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        ArrayList<AccountInfo> infos = new ArrayList<>();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from " + TABLE_ACCOUNTS, null);
            if (cursor.getCount() > 0)
            {
                int indexAccount = cursor.getColumnIndex(FIELD_ACCOUNT);
                int indexPassword = cursor.getColumnIndex(FIELD_ACCOUNT_PASSWORD);
                int indexLoginState = cursor.getColumnIndex(FIELD_ACCOUNT_LOGIN_STATE);
                int indexName = cursor.getColumnIndex(FIELD_ACCOUNT_NAME);
                int indexIcon = cursor.getColumnIndex(FIELD_ACCOUNT_ICON);
                while (cursor.moveToNext())
                {
                    AccountInfo info = new AccountInfo();
                    info.account = cursor.getInt(indexAccount);
                    info.password = cursor.getBlob(indexPassword);
                    info.loginState = cursor.getInt(indexLoginState);
                    info.name = cursor.getString(indexName);
                    info.icon = cursor.getString(indexIcon);
                    infos.add(info);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return infos;
    }

    public void loginOut()
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_ACCOUNTS
                + " SET " + FIELD_ACCOUNT_LOGIN_STATE + "=0" + " WHERE " + FIELD_ACCOUNT
                + "=" + Global.getAccountInfo().account + " AND " + FIELD_ACCOUNT_LOGIN_STATE + "=1");
        Global.setAccountInfo(null);
    }
}
