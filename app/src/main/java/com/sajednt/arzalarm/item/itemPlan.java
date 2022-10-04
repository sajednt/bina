package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemPlan {

    @SerializedName("id")
    public  int id;
    @SerializedName("name")
    public  String name;
    @SerializedName("days")
    public  String days;
    @SerializedName("alarmcount")
    public String alarmcount;
    @SerializedName("regular_price")
    public String regular_price;
    @SerializedName("sale_price")
    public String sale_price;
    @SerializedName("description")
    public String description;

}
