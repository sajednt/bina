package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemUserLogin {


    @SerializedName("id")
    public int id;

    @SerializedName("tickers")
    public String tickers;

    @SerializedName("success")
    public int success;

    @SerializedName("userid")
    public String userid;

    @SerializedName("message")
    public String message;
}
