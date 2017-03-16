package com.market.aching.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.market.aching.BaseApplication;

/**
 * Created by Administrator on 2017/3/14.
 */

public class DBManager extends SQLiteOpenHelper
{
    private static DBManager mDbManager;

    public static DBManager getInstance(Context context)
    {
        if (null == mDbManager)
        {
            synchronized (DBManager.class)
            {
                if (null == mDbManager)
                {
                    mDbManager = new DBManager(context, Global.DATABASE_NAME, null, Global.DATABASE_VISION);
                }
            }
        }
        return mDbManager;
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE person(personid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
    }
}
