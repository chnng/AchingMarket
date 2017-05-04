package com.market.aching.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.market.aching.model.BookInfoResponse;
import com.market.aching.model.OrderInfo;
import com.market.aching.util.Global;

import java.util.ArrayList;

import static com.market.aching.database.DatabaseConst.FIELD_ORDER_ADDRESS;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_BOOK_ID;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_BOOK_INFO;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_ID;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_QUANTITY;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_STATE;
import static com.market.aching.database.DatabaseConst.FIELD_ORDER_TIME;
import static com.market.aching.database.DatabaseConst.TABLE_ORDER;
import static com.market.aching.util.AchingUtil.log;

/**
 * Created by chnng on 2017/5/4.
 */

public class OrderManager
{
    /**
     * 插入订单数据
     *
     * @param orderInfo
     */
    public synchronized void updateOrder(OrderInfo orderInfo)
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        int count = 0;
        Cursor cursor = database.rawQuery("select count(*) as counts from "
                        + TABLE_ORDER + Global.getAccountInfo().account
                        + " where " + FIELD_ORDER_BOOK_ID + "=? and "
                        + FIELD_ORDER_STATE + "=?",
                new String[]{String.valueOf(orderInfo.bookID), String.valueOf(0)});
        if (cursor.moveToFirst())
        {
            count = cursor.getInt(cursor.getColumnIndex("counts"));
        }
        if (count == 0)
        {
            ContentValues contentValues = new ContentValues();
            Gson gson = new Gson();
            String bookInfoJson = gson.toJson(orderInfo.bookInfo);
            contentValues.put(FIELD_ORDER_BOOK_ID, orderInfo.bookID);
            contentValues.put(FIELD_ORDER_STATE, orderInfo.orderState);
            contentValues.put(FIELD_ORDER_QUANTITY, orderInfo.quantity);
            contentValues.put(FIELD_ORDER_ADDRESS, orderInfo.address);
            contentValues.put(FIELD_ORDER_BOOK_INFO, bookInfoJson);
            database.insert(TABLE_ORDER + orderInfo.account, null, contentValues);
        } else
        {
            database = DatabaseHelper.getInstance().getWritableDatabase();
            cursor = database.rawQuery("select * from "
                            + TABLE_ORDER + Global.getAccountInfo().account
                            + " where " + FIELD_ORDER_BOOK_ID + "=? and "
                            + FIELD_ORDER_STATE + "=?"
                    , new String[]{String.valueOf(orderInfo.bookID), "0"});
            if (cursor.moveToFirst())
            {
                int indexOrderID = cursor.getColumnIndex(FIELD_ORDER_ID);
                int indexQuantity = cursor.getColumnIndex(FIELD_ORDER_QUANTITY);
                log("index " + indexOrderID + " " + indexQuantity);
                int orderID = cursor.getInt(indexOrderID);
                int quantity = cursor.getInt(indexQuantity) + 1;
                log("quantity " + quantity);
                database.execSQL("update " +
                        TABLE_ORDER + Global.getAccountInfo().account + " set " + FIELD_ORDER_QUANTITY +
                        "=" + quantity + " where " + FIELD_ORDER_ID + "=" + orderID);
            }
        }
        cursor.close();
    }

    /**
     * 查询所有没有被提交的表单
     *
     * @return
     */
    public synchronized ArrayList<OrderInfo> queryAllOrder(int state)
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        ArrayList<OrderInfo> infos = new ArrayList<>();
        Gson gson = new Gson();
        Cursor cursor = null;
        try
        {
            cursor = database.rawQuery("select * from "
                    + TABLE_ORDER + Global.getAccountInfo().account
                    + " where " + FIELD_ORDER_STATE + "=?", new String[]{String.valueOf(state)});
            if (cursor.getCount() > 0)
            {
                int indexOrderID = cursor.getColumnIndex(FIELD_ORDER_ID);
                int indexBookID = cursor.getColumnIndex(FIELD_ORDER_BOOK_ID);
                int indexState = cursor.getColumnIndex(FIELD_ORDER_STATE);
                int indexQuantity = cursor.getColumnIndex(FIELD_ORDER_QUANTITY);
                int indexBook = cursor.getColumnIndex(FIELD_ORDER_BOOK_INFO);
                int indexTime = cursor.getColumnIndex(FIELD_ORDER_TIME);
                int indexAddress = cursor.getColumnIndex(FIELD_ORDER_ADDRESS);
                while (cursor.moveToNext())
                {
                    OrderInfo info = new OrderInfo();
                    info.orderID = cursor.getInt(indexOrderID);
                    info.bookID = cursor.getInt(indexBookID);
                    info.orderState = cursor.getInt(indexState);
                    info.quantity = cursor.getInt(indexQuantity);
                    info.time = cursor.getInt(indexTime);
                    info.address = cursor.getString(indexAddress);
                    info.bookInfo = gson.fromJson(cursor.getString(indexBook), BookInfoResponse.class);
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

    public void submitOrder(int[] oids)
    {
//        Cursor cursor;
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
//        cursor = database.rawQuery(
//                "select * from " + TABLE_ORDER
//                        + Global.getAccountInfo().account
//                        + " where " + FIELD_ORDER_STATE + "=?",
//                new String[]{String.valueOf(0)});
        for (int oid : oids)
        {
            database.execSQL("update " +
                    TABLE_ORDER + Global.getAccountInfo().account + " set " + FIELD_ORDER_STATE +
                    "=1 where " + FIELD_ORDER_ID + "=" + oid);
        }
//        cursor.close();
    }
}
