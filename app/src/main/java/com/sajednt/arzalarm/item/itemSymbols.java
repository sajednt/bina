package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemSymbols {

    @SerializedName("id")
    public  String id;
    @SerializedName("name")
    public  String name;
    @SerializedName("price")
    public  String price;
    @SerializedName("status")
    public  Boolean status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
