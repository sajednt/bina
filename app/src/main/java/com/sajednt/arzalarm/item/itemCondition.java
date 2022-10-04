package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class itemCondition implements Serializable {

    @SerializedName("id")
    public  int id;
    @SerializedName("userid")
    public  int userid;
    @SerializedName("status")
    public  int status;
    @SerializedName("ticker")
    public String ticker;
    @SerializedName("name")
    public String name;
    @SerializedName("dateCreate")
    public String dateCreate;
    @SerializedName("dateExpire")
    public String dateExpire;
    @SerializedName("dateModify")
    public String dateModify;
    @SerializedName("sign")
    public String sign;
    @SerializedName("price")
    public String price;
    @SerializedName("message")
    public String message;
    @SerializedName("timeframe")
    public String timeframe;
    @SerializedName("sendemail")
    public String sendemail;
    @SerializedName("log")
    public List<itemLog> log;

    public class itemLog implements Serializable{
        @SerializedName("id")
        public  int id;
        @SerializedName("price")
        public  String price;
        @SerializedName("date")
        public  String date;
        @SerializedName("message")
        public  String message;
    }
}
