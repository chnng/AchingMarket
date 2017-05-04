package com.market.aching.model;

/**
 * Created by chnng on 2017/5/3.
 */

public class OrderInfo
{
    public int account;
    public int orderID;
    public int bookID;
    public int orderState;
    public int quantity;
    public long time;
    public String address;
    public BookInfoResponse bookInfo;

    @Override
    public String toString()
    {
        return "OrderInfo{" +
                "account=" + account +
                ", orderID=" + orderID +
                ", bookID=" + bookID +
                ", orderState=" + orderState +
                ", quantity=" + quantity +
                ", time=" + time +
                ", address='" + address + '\'' +
                ", bookInfo=" + bookInfo +
                '}';
    }
}
