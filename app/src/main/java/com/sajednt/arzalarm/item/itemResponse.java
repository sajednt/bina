package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemResponse {

    @SerializedName("id")
    public int id;

    @SerializedName("message")
    public String message;

    @SerializedName("success")
    public int success;
}
