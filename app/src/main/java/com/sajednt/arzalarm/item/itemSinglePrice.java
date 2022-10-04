package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemSinglePrice {

    @SerializedName("price")
    public Double price;

    @SerializedName("message")
    public String message;

    @SerializedName("success")
    public int success;
}
