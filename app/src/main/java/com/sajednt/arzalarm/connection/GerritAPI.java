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
    @POST("bina/user/register.php")
    Call<itemUserCode> getRegisterUserCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/active.php")
    Call<itemUserCode> userActivate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/user_ticker.php")
    Call<itemResponse> setUserTickersSymbols(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/login.php")
    Call<itemUserLogin> userlogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/add_device.php") // devicetoken , userid , deviceid , devicename
    Call<itemUserLogin> useraddDevice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/set_token.php")
    Call<itemUserLogin> userSetToken(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/add.php")
    Call<itemResponse> addCondition(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/list.php") // userid
    Call<List<itemCondition>> listCondition(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/get_single_price.php") // ticker
    Call<itemSinglePrice> currentSinglePrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/set_status.php") // condition id & status
    Call<itemResponse> setConditionStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/delete.php") // condition id & status
    Call<itemResponse> deleteCondition(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/update.php") // condition id & status
    Call<itemResponse> updateCondition(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/home/get_single_ticker.php") // ticker & timeframe
    Call<itemTicker> getsingleticker(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/conditions/get_ticker_symbol.php") // get all ticker started with $symbol
    Call<List<itemTicker>> getTickerBaseSymbol(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/get_user_info.php") // userid
    Call<item_user_info> getuserinfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/home/get_ticker_info.php") // ticker
    Call<itemTicker> getTicker(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/device/list.php") // userid
    Call<List<itemDevice>> getDevice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/device/delete.php") // device id
    Call<itemResponse> deleteDevice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/package/list.php") //
    Call<List<itemPlan>> getPlans(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/remove_all_device.php") //
    Call<itemResponse> removeDevices(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/send_code.php") // userid
    Call<itemUserCode> sendCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/ticket/get.php") // userid , subject , description
    Call<itemResponse> sendTicket(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/user/request_pass.php") // Email
    Call<itemResponse> Recoveremail(@FieldMap Map<String, String> params);

    @GET("bina/user/getsettings.php") // none
    Call<item_settings> getSettings();

    @FormUrlEncoded
    @Headers("$CACHE_CONTROL_HEADER: no-cache")
    @POST("bina/package/pay/buy/buybazaar.php") // userid & planid
    Call<itemResponse> buybazaar(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bina/package/pay/reserve/reservebazar.php") // userid & planid
    Call<itemResponse> reservebazaar(@FieldMap Map<String, String> params);
}
