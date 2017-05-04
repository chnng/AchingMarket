package com.market.aching.database;

import com.market.aching.util.Global;

/**
 * Created by Administrator on 2017/5/4.
 */

public class DatabaseConst
{
    static final String DATABASE_NAME = Global.getAppPath() + "aching.db";
    static final int DATABASE_VISION = 1;

    public static final int UNSUBMITTED = 0;
    public static final int SUBMITTED = 1;

    static final String TABLE_ACCOUNTS = "accounts";
    static final String FIELD_ACCOUNT = "account";
    static final String FIELD_ACCOUNT_PASSWORD = "password";
    static final String FIELD_ACCOUNT_LOGIN_STATE = "login_state";
    static final String FIELD_ACCOUNT_NAME = "name";
    static final String FIELD_ACCOUNT_ICON = "icon_index";
    static final String TABLE_ORDER = "order_";
    static final String FIELD_ORDER_ID = "order_id";
    static final String FIELD_ORDER_BOOK_ID = "book_id";
    static final String FIELD_ORDER_STATE = "state";
    static final String FIELD_ORDER_QUANTITY = "quantity";
    static final String FIELD_ORDER_TIME = "time";
    static final String FIELD_ORDER_ADDRESS = "address";
    static final String FIELD_ORDER_BOOK_INFO = "book_info";

    static final String SQL_CREATE_ACCOUNT_TABLE =
            "CREATE TABLE " +
                    TABLE_ACCOUNTS + "("
                    + FIELD_ACCOUNT + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FIELD_ACCOUNT_PASSWORD + " BLOB,"
                    + FIELD_ACCOUNT_LOGIN_STATE + " INTEGER,"
                    + FIELD_ACCOUNT_NAME + " VARCHAR,"
                    + FIELD_ACCOUNT_ICON + " VARCHAR)";

    static final String SQL_CREATE_ORDER_TABLE =
            "CREATE TABLE " + TABLE_ORDER + "%d("
                    + FIELD_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FIELD_ORDER_BOOK_ID + " VARCHAR,"
                    + FIELD_ORDER_STATE + " INTEGER,"
                    + FIELD_ORDER_QUANTITY + " INTEGER,"
                    + FIELD_ORDER_TIME + " INTEGER,"
                    + FIELD_ORDER_ADDRESS + " VARCHAR,"
                    + FIELD_ORDER_BOOK_INFO + " VARCHAR)";
}
