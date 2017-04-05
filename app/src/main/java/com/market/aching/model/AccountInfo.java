package com.market.aching.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chnng on 2017/4/2.
 */

public class AccountInfo implements Parcelable
{
    public int uid;
    public String account;
    public byte[] password;
    public String name;
    public String icon;
    public int loginState;


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.uid);
        dest.writeString(this.account);
        dest.writeByteArray(this.password);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.loginState);
    }

    public AccountInfo()
    {
    }

    protected AccountInfo(Parcel in)
    {
        this.uid = in.readInt();
        this.account = in.readString();
        this.password = in.createByteArray();
        this.name = in.readString();
        this.icon = in.readString();
        this.loginState = in.readInt();
    }

    public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>()
    {
        @Override
        public AccountInfo createFromParcel(Parcel source)
        {
            return new AccountInfo(source);
        }

        @Override
        public AccountInfo[] newArray(int size)
        {
            return new AccountInfo[size];
        }
    };
}
