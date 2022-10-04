package com.sajednt.arzalarm.item;

import com.google.gson.annotations.SerializedName;

public class itemUserCode {

    @SerializedName("success")
    public int success;
    @SerializedName("message")
    public String message;
    @SerializedName("code")
    public int code;

    @SerializedName("active")
    public int active;
    @SerializedName("id")
    public int id;

    @SerializedName("tickers")
    public String tickers;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
