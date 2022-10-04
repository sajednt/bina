package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class item_settings {

    @SerializedName("success")
    public int success;

    @SerializedName("lastversion")
    public int lastversion;

    @SerializedName("updateurl")
    public String updateurl;

    @SerializedName("forceupdate")
    public int forceupdate;

    @SerializedName("message")
    public String message;

    @SerializedName("maintenance")
    public int maintenance;

}
