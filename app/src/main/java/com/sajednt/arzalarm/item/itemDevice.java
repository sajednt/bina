package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemDevice {

    @SerializedName("id")
    public  int id;
    @SerializedName("userid")
    public  int userid;
    @SerializedName("lastvisit")
    public  String lastvisit;
    @SerializedName("deviceid")
    public String deviceid;
    @SerializedName("devicename")
    public String devicename;

}
