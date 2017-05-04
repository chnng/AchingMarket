package com.market.aching.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.market.aching.database.DatabaseConst.DATABASE_NAME;
import static com.market.aching.database.DatabaseConst.DATABASE_VISION;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_ICON;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_LOGIN_STATE;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_NAME;
import static com.market.aching.database.DatabaseConst.FIELD_ACCOUNT_PASSWORD;
import static com.market.aching.database.DatabaseConst.SQL_CREATE_ACCOUNT_TABLE;
import static com.market.aching.database.DatabaseConst.TABLE_ACCOUNTS;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper mDatabaseHelper;

    public static void initDBManager(Context context)
    {
        if (null == mDatabaseHelper)
        {
            mDatabaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VISION);
        }
    }

    public static DatabaseHelper getInstance()
    {
        return mDatabaseHelper;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
    }
}
