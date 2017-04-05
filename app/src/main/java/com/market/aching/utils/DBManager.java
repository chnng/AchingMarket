package com.market.aching.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.market.aching.model.AccountInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DBManager extends SQLiteOpenHelper
{
    private final String TAG = DBManager.class.getSimpleName();
    private static DBManager mDbManager;
    private static final String DATABASE_NAME = Global.getAppPath() + "aching.db";
    private static final int DATABASE_VISION = 1;

    private static final String TABLE_NAME = "account_info";
    private static final String TABLE_FIELD_UID = "uid";
    private static final String TABLE_FIELD_NAME = "name";
    private static final String TABLE_FIELD_ACCOUNT = "account";
    private static final String TABLE_FIELD_PASSWORD = "password";
    private static final String TABLE_FIELD_ICON = "icon_index";
    private static final String TABLE_FIELD_LOGIN_STATE = "login_state";

    public static void initDBManager(Context context)
    {
        if (null == mDbManager)
        {
            mDbManager = new DBManager(context, DATABASE_NAME, null, DATABASE_VISION);
        }
    }

    public static DBManager getInstance()
    {
        return mDbManager;
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " +
                TABLE_NAME + "("
                + TABLE_FIELD_UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TABLE_FIELD_ACCOUNT + " VARCHAR,"
                + TABLE_FIELD_PASSWORD + " BLOB,"
                + TABLE_FIELD_LOGIN_STATE + " INTEGER,"
                + TABLE_FIELD_NAME + " VARCHAR,"
                + TABLE_FIELD_ICON + " VARCHAR)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
    }

    public synchronized void updateAccount(AccountInfo accountInfo)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("update " + TABLE_NAME
                + " set " + TABLE_FIELD_LOGIN_STATE + "=0 where "
                + TABLE_FIELD_UID + "!=" + accountInfo.uid);
        int count = 0;
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select count(*) as counts from "
                            + TABLE_NAME + " where " + TABLE_FIELD_ACCOUNT + "=?",
                    new String[]{accountInfo.account});
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
        ALog.d(TAG, "count " + count);
        if (count == 0)
        {
            // 插入
            database.insert(TABLE_NAME, null, getContentValues(accountInfo));
//            database.execSQL(
//                    "INSERT INTO " + TABLE_NAME
//                            + " VALUES(null, ?, ?, ?, ?, ?)",
//                    new Object[]{accountInfo.name, accountInfo.account,
//                            accountInfo.password, accountInfo.icon,
//                            accountInfo.loginState});
        } else
        {
            database.update(TABLE_NAME, getContentValues(accountInfo), TABLE_FIELD_ACCOUNT + "=?",
                    new String[]{accountInfo.account});
        }
    }

    @NonNull
    private ContentValues getContentValues(AccountInfo accountInfo)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_FIELD_UID, accountInfo.account);
        contentValues.put(TABLE_FIELD_ACCOUNT, accountInfo.account);
        contentValues.put(TABLE_FIELD_PASSWORD, accountInfo.password);
        contentValues.put(TABLE_FIELD_LOGIN_STATE, accountInfo.loginState);
        contentValues.put(TABLE_FIELD_NAME, accountInfo.name);
        contentValues.put(TABLE_FIELD_ICON, accountInfo.icon);
        return contentValues;
    }

    /**
     * 查询当前登录账户信息,即loginState=1的账户
     *
     * @return
     */
    public synchronized AccountInfo queryCurrentAccount()
    {
        SQLiteDatabase database = getWritableDatabase();
        AccountInfo info = new AccountInfo();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from " + TABLE_NAME
                            + " where " + TABLE_FIELD_LOGIN_STATE + "=?",
                    new String[]{String.valueOf(1)});
            if (cursor.getCount() > 0)
            {
                int indexUid = cursor.getColumnIndex(TABLE_FIELD_UID);
                int indexAccount = cursor.getColumnIndex(TABLE_FIELD_ACCOUNT);
                int indexPassword = cursor.getColumnIndex(TABLE_FIELD_PASSWORD);
                int indexLoginState = cursor.getColumnIndex(TABLE_FIELD_LOGIN_STATE);
                int indexName = cursor.getColumnIndex(TABLE_FIELD_NAME);
                int indexIcon = cursor.getColumnIndex(TABLE_FIELD_ICON);
                while (cursor.moveToNext())
                {
                    info.uid = cursor.getInt(indexUid);
                    info.account = cursor.getString(indexAccount);
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
        SQLiteDatabase database = getWritableDatabase();
        ArrayList<AccountInfo> infos = new ArrayList<AccountInfo>();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor.getCount() > 0)
            {
                if (cursor.getCount() > 0)
                {
                    int indexUid = cursor.getColumnIndex(TABLE_FIELD_UID);
                    int indexAccount = cursor.getColumnIndex(TABLE_FIELD_ACCOUNT);
                    int indexPassword = cursor.getColumnIndex(TABLE_FIELD_PASSWORD);
                    int indexLoginState = cursor.getColumnIndex(TABLE_FIELD_LOGIN_STATE);
                    int indexName = cursor.getColumnIndex(TABLE_FIELD_NAME);
                    int indexIcon = cursor.getColumnIndex(TABLE_FIELD_ICON);
                    while (cursor.moveToNext())
                    {
                        AccountInfo info = new AccountInfo();
                        info.uid = cursor.getInt(indexUid);
                        info.account = cursor.getString(indexAccount);
                        info.password = cursor.getBlob(indexPassword);
                        info.loginState = cursor.getInt(indexLoginState);
                        info.name = cursor.getString(indexName);
                        info.icon = cursor.getString(indexIcon);
                        infos.add(info);
                    }
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
                cursor = null;
            }
        }
        return infos;
    }
}
