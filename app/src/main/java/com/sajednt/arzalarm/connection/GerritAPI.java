package com.sajednt.arzalarm.connection;

import com.sajednt.arzalarm.item.itemCondition;
import com.sajednt.arzalarm.item.itemDevice;
import com.sajednt.arzalarm.item.itemPlan;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemSinglePrice;
import com.sajednt.arzalarm.item.itemSymbols;
import com.sajednt.arzalarm.item.itemTicker;
import com.sajednt.arzalarm.item.itemUserCode;
import com.sajednt.arzalarm.item.itemUserLogin;
import com.sajednt.arzalarm.item.item_settings;
import com.sajednt.arzalarm.item.item_user_info;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GerritAPI {

    @FormUrlEncoded
    @POST("bina/home/get_home_price.php")
    Call<List<itemTicker>> getPrices(@FieldMap Map<String, String> params);

    @GET("bina/get_all_symbols.php")
    Call<List<itemSymbols>> getSymbols();


    @FormUrlEncoded
    @POST("bina/package/pay/reserve/reservebazar.php") // userid & planid
    Call<itemResponse> reservebazaar(@FieldMap Map<String, String> params);
}
