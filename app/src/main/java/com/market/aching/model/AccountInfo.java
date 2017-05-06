package com.market.aching.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chnng on 2017/4/2.
 */

public class AccountInfo implements Parcelable
{
    public String account;
    public byte[] password;
    public int loginState;
    public String name;
    public String address;
    public String icon;

    public AccountInfo()
    {
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.account);
        dest.writeByteArray(this.password);
        dest.writeInt(this.loginState);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.icon);
    }

    protected AccountInfo(Parcel in)
    {
        this.account = in.readString();
        this.password = in.createByteArray();
        this.loginState = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.icon = in.readString();
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
