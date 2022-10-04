package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class itemTicker  implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("ticker")
    public String ticker;
    @SerializedName("symbol")
    public String symbol;
    @SerializedName("open")
    public Double open;
    @SerializedName("status")
    public Boolean status;
    @SerializedName("price")
    public Double price;

    @SerializedName("close")
    public Double close;
    @SerializedName("high")
    public Double high;
    @SerializedName("low")
    public Double low;
    @SerializedName("volume")
    public Double volume;
}
