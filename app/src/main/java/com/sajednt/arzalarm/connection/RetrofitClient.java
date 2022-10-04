package com.sajednt.arzalarm.connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String API_URL = "https://arzalarm.com/";

    private static RetrofitClient instance = null;
    GerritAPI gerritAPI;
    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gerritAPI = retrofit.create(GerritAPI.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public GerritAPI getMyApi() {
        return gerritAPI;
    }
}
