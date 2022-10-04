package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class item_user_info {

    @SerializedName("success")
    public int success;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("planname")
    public String planname;
    @SerializedName("expdate")
    public String expdate;
    @SerializedName("message")
    public String message;
    @SerializedName("planstatus")
    public int planstatus;
}
