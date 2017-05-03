package com.market.aching.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.market.aching.model.AccountInfo;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.util.Global;
import com.market.aching.util.SystemUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DataBaseHelper extends SQLiteOpenHelper
{
    private final String TAG = DataBaseHelper.class.getSimpleName();
    private static DataBaseHelper mDataBaseHelper;
    private static final String DATABASE_NAME = Global.getAppPath() + "aching.db";
    private static final int DATABASE_VISION = 1;

    private static final String TABLE_ACCOUNT = "account";
    private static final String FIELD_ACCOUNT_UID = "uid";
    private static final String FIELD_ACCOUNT_NAME = "name";
    private static final String FIELD_ACCOUNT_ACCOUNT = "account";
    private static final String FIELD_ACCOUNT_PASSWORD = "password";
    private static final String FIELD_ACCOUNT_ICON = "icon_index";
    private static final String FIELD_ACCOUNT_LOGIN_STATE = "login_state";

    private static final String TABLE_ORDER = "order_";
    private static final String FIELD_ORDER_INFO = "order_info";
    private static final String FIELD_ORDER_ADDRESS = "order_address";
    private static final String FIELD_ORDER_TIME = "order_time";

    public static void initDBManager(Context context)
    {
        if (null == mDataBaseHelper)
        {
            mDataBaseHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VISION);
        }
    }

    public static DataBaseHelper getInstance()
    {
        return mDataBaseHelper;
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " +
                TABLE_ACCOUNT + "("
                + FIELD_ACCOUNT_UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FIELD_ACCOUNT_ACCOUNT + " VARCHAR,"
                + FIELD_ACCOUNT_PASSWORD + " BLOB,"
                + FIELD_ACCOUNT_LOGIN_STATE + " INTEGER,"
                + FIELD_ACCOUNT_NAME + " VARCHAR,"
                + FIELD_ACCOUNT_ICON + " VARCHAR)";
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
        database.execSQL("update " + TABLE_ACCOUNT
                + " set " + FIELD_ACCOUNT_LOGIN_STATE + "=0 where "
                + FIELD_ACCOUNT_UID + "!=" + accountInfo.uid);
        int count = 0;
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select count(*) as counts from "
                            + TABLE_ACCOUNT + " where " + FIELD_ACCOUNT_ACCOUNT + "=?",
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
        if (count == 0)
        {
            // 插入
            database.insert(TABLE_ACCOUNT, null, getContentValues(accountInfo));
//            database.execSQL(
//                    "INSERT INTO " + TABLE_ACCOUNT
//                            + " VALUES(null, ?, ?, ?, ?, ?)",
//                    new Object[]{accountInfo.name, accountInfo.account,
//                            accountInfo.password, accountInfo.icon,
//                            accountInfo.loginState});
            String sql = "CREATE TABLE " +
                    TABLE_ORDER + accountInfo.uid + "("
                    + FIELD_ACCOUNT_UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FIELD_ORDER_INFO + " VARCHAR,"
                    + FIELD_ORDER_ADDRESS + " VARCHAR,"
                    + FIELD_ORDER_TIME + " INTEGER)";
            database.execSQL(sql);
        } else
        {
            database.update(TABLE_ACCOUNT, getContentValues(accountInfo), FIELD_ACCOUNT_ACCOUNT + "=?",
                    new String[]{accountInfo.account});
        }
    }

    @NonNull
    private ContentValues getContentValues(AccountInfo accountInfo)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ACCOUNT_UID, accountInfo.account);
        contentValues.put(FIELD_ACCOUNT_ACCOUNT, accountInfo.account);
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
        SQLiteDatabase database = getWritableDatabase();
        AccountInfo info = new AccountInfo();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from " + TABLE_ACCOUNT
                            + " where " + FIELD_ACCOUNT_LOGIN_STATE + "=?",
                    new String[]{String.valueOf(1)});
            if (cursor.getCount() > 0)
            {
                int indexUid = cursor.getColumnIndex(FIELD_ACCOUNT_UID);
                int indexAccount = cursor.getColumnIndex(FIELD_ACCOUNT_ACCOUNT);
                int indexPassword = cursor.getColumnIndex(FIELD_ACCOUNT_PASSWORD);
                int indexLoginState = cursor.getColumnIndex(FIELD_ACCOUNT_LOGIN_STATE);
                int indexName = cursor.getColumnIndex(FIELD_ACCOUNT_NAME);
                int indexIcon = cursor.getColumnIndex(FIELD_ACCOUNT_ICON);
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
            cursor = database.rawQuery("select * from " + TABLE_ACCOUNT, null);
            if (cursor.getCount() > 0)
            {
                if (cursor.getCount() > 0)
                {
                    int indexUid = cursor.getColumnIndex(FIELD_ACCOUNT_UID);
                    int indexAccount = cursor.getColumnIndex(FIELD_ACCOUNT_ACCOUNT);
                    int indexPassword = cursor.getColumnIndex(FIELD_ACCOUNT_PASSWORD);
                    int indexLoginState = cursor.getColumnIndex(FIELD_ACCOUNT_LOGIN_STATE);
                    int indexName = cursor.getColumnIndex(FIELD_ACCOUNT_NAME);
                    int indexIcon = cursor.getColumnIndex(FIELD_ACCOUNT_ICON);
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
            }
        }
        return infos;
    }

    public void createOrderDataBase(int uid, BookInfoResponse bookInfo)
    {
        SQLiteDatabase database = getWritableDatabase();
//        database.create()
    }

    public synchronized void updateOrder(OrderInfo orderInfo)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ORDER_INFO, orderInfo.bookInfo.toString());
        contentValues.put(FIELD_ORDER_ADDRESS, orderInfo.address);
        contentValues.put(FIELD_ORDER_TIME, System.currentTimeMillis());
        database.insert(TABLE_ORDER + orderInfo.uid, null, contentValues);
    }
}
