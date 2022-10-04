package com.sajednt.arzalarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.adapter.AdapterPlans;
import com.sajednt.arzalarm.adapter.ItemClickListener;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemPlan;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.util.IabHelper;
import com.sajednt.arzalarm.util.IabResult;
import com.sajednt.arzalarm.util.Purchase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.Payment;
import ir.cafebazaar.poolakey.callback.ConnectionCallback;
import ir.cafebazaar.poolakey.callback.PurchaseCallback;
import ir.cafebazaar.poolakey.config.PaymentConfiguration;
import ir.cafebazaar.poolakey.config.SecurityCheck;
import ir.cafebazaar.poolakey.entity.PurchaseInfo;
import ir.cafebazaar.poolakey.request.PurchaseRequest;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;

public class Activity_plan extends AppCompatActivity {

    private Toolbar mToolbar;
    List<itemPlan> deviceItems;
    LinearLayoutManager layoutManager;
    private RecyclerView recycle_device;
    AdapterPlans adaptDevice;
    Functions func;
    AVLoadingIndicatorView av_condition;

    // Debug tag, for logging
    static final String TAG = "qqqwwweeee";

    // SKUs for our products: the premium upgrade (non-consumable)
    static String SKU_Buy = "100";
    static String SKU_Reserve = "200";
    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_Buy = 10001;
    String pid = "1";
    boolean buy =true;
    // The helper object
   // IabHelper mHelper;
   // IabHelper.QueryInventoryFinishedListener mGotInventoryListener;
    SecurityCheck localSecurityCheck;
    PaymentConfiguration paymentConfiguration;
    Payment payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwCyPPhwje9JQuhGZgg/A2Nk28DsG5uvYqdq6Cvg8rB/C4opcV4X6A6eyj3r4htEOMx8lo9gr6+dzrX+h4THb7sP/fMmsXuSSZGfFluSZcnYypTAKYiE0mRNZzx+Y2YP+J9BjhcpXu5bQ5DdGw96T2j+IW/Vdu9kENztp3lnZJlvytSFgA0QsUmwsJGmijUJBtXs7YrtahKs70lBH38xEj/3YgnG7qUAQzm0oN/VvzUCAwEAAQ==";
       // mHelper = new IabHelper(this, base64EncodedPublicKey);
        Log.d(TAG, "Starting setup.");

//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            @Override
//            public void onIabSetupFinished(IabResult result) {
//                Log.d(TAG, "Setup finished.");
//
//                if (!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    Log.d(TAG, "مشکل در اتصال به بازار  " + result);
//                }
//                // Hooray, IAB is fully set up!
//                mHelper.queryInventoryAsync(mGotInventoryListener);
//
//            }
//        });
//        mHelper.enableDebugLogging(true);
//
//
//        mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//                Log.d(TAG, "Query inventory finished.");
//                if (result.isFailure()) {
//                    Log.d(TAG, "Failed to query inventory: " + result);
//                    return;
//                }
//                else {
//                    Log.d(TAG, "Query inventory was successful.");
//                    // does the user have the premium upgrade?
//                    mIsPremium = inventory.hasPurchase(SKU_Buy);
//
//                    // update UI accordingly
//
//                    Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
//
//                }
//
//                Log.d(TAG, "Initial inventory query finished; enabling main UI.");
//            }
//        };

         localSecurityCheck = new SecurityCheck.Enable(base64EncodedPublicKey);
         paymentConfiguration = new PaymentConfiguration(localSecurityCheck , true);
         payment = new Payment(Activity_plan.this, paymentConfiguration);





        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.planname);
        func = new Functions(getApplicationContext());

        deviceItems = new ArrayList<itemPlan>();
        recycle_device = findViewById(R.id.recyclerview);
        av_condition = findViewById(R.id.indicator_tickers);
        recycle_device.setLayoutManager(new LinearLayoutManager(this));


        adaptDevice = new AdapterPlans( deviceItems , getApplicationContext() , this  );
        recycle_device.swapAdapter(adaptDevice, false);


        adaptDevice.setBuyClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, String planid) {
                buy = true;
                pid = planid;
                SKU_Buy = "100" + Integer.toString(position+1);
//                Toast.makeText(Activity_plan.this, SKU_Buy, Toast.LENGTH_SHORT).show();


                Connection paymentconnection = payment.connect(new Function1<ConnectionCallback, Unit>() {
                    @Override
                    public Unit invoke(ConnectionCallback connectionCallback) {

                        connectionCallback.connectionSucceed(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {

                                PurchaseRequest purchaseRequest = new PurchaseRequest(SKU_Buy , "payload-string" , null);
                                payment.purchaseProduct(getActivityResultRegistry(), purchaseRequest, new Function1<PurchaseCallback, Unit>() {
                                    @Override
                                    public Unit invoke(PurchaseCallback purchaseCallback) {


                                        purchaseCallback.failedToBeginFlow(new Function1<Throwable, Unit>() {
                                            @Override
                                            public Unit invoke(Throwable throwable) {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseCanceled(new Function0<Unit>() {
                                            @Override
                                            public Unit invoke() {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseFailed(new Function1<Throwable, Unit>() {
                                            @Override
                                            public Unit invoke(Throwable throwable) {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseSucceed(new Function1<PurchaseInfo, Unit>() {
                                            @Override
                                            public Unit invoke(PurchaseInfo purchaseInfo) {

                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("userid", Integer.toString(func.getDataInt("userid")));
                                                params.put("planid", pid);

                                                Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().buybazaar(params);
                                                call.enqueue(new Callback<itemResponse>() {
                                                    @Override
                                                    public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                                                        Log.e("Token Request", response.toString());
                                                        Toast.makeText(Activity_plan.this, response.body().message, Toast.LENGTH_SHORT).show();
                                                        if (response.body().success == 1) {
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<itemResponse> call, Throwable t) {

                                                        Toast.makeText(Activity_plan.this, "مشکل در اتصال لطفا با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT).show();

                                                    }

                                                });

                                                return null;
                                            }
                                        });


                                        return null;
                                    }


                                });


                                return null;
                            }
                        });

                        return null;
                    }

                });



//                mHelper.launchSubscriptionPurchaseFlow(Activity_plan.this, SKU_Buy , RC_Buy , mPurchaseFinishedListener, "payload-string");
            }
        });

        adaptDevice.setReserveClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position , String planid) {
                pid = planid;
                buy = false;

                SKU_Buy = "100" + Integer.toString(position+1);



                Connection paymentconnection = payment.connect(new Function1<ConnectionCallback, Unit>() {
                    @Override
                    public Unit invoke(ConnectionCallback connectionCallback) {

                        connectionCallback.connectionSucceed(new Function0<Unit>() {
                            @Override
                            public Unit invoke() {

                                PurchaseRequest purchaseRequest = new PurchaseRequest(SKU_Buy , "payload-string" , null);
                                payment.purchaseProduct(getActivityResultRegistry(), purchaseRequest, new Function1<PurchaseCallback, Unit>() {
                                    @Override
                                    public Unit invoke(PurchaseCallback purchaseCallback) {


                                        purchaseCallback.failedToBeginFlow(new Function1<Throwable, Unit>() {
                                            @Override
                                            public Unit invoke(Throwable throwable) {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseCanceled(new Function0<Unit>() {
                                            @Override
                                            public Unit invoke() {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseFailed(new Function1<Throwable, Unit>() {
                                            @Override
                                            public Unit invoke(Throwable throwable) {

                                                return null;
                                            }
                                        });

                                        purchaseCallback.purchaseSucceed(new Function1<PurchaseInfo, Unit>() {
                                            @Override
                                            public Unit invoke(PurchaseInfo purchaseInfo) {


                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("userid", Integer.toString(func.getDataInt("userid")));
                                                    params.put("planid", pid);

                                                    Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().reservebazaar(params);
                                                    call.enqueue(new Callback<itemResponse>() {
                                                        @Override
                                                        public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                                                            Log.e("Token Request" ,  response.toString());
                                                            Toast.makeText(Activity_plan.this, response.body().message, Toast.LENGTH_SHORT).show();
                                                            if(response.body().success == 1){
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<itemResponse> call, Throwable t) {

                                                            Toast.makeText(Activity_plan.this, "مشکل در اتصال لطفا با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT).show();

                                                        }

                                                    });

                                                return null;
                                            }
                                        });


                                        return null;
                                    }


                                });


                                return null;
                            }
                        });

                        return null;
                    }

                });



            }
        });
        
        getplans();


    }



    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                Toast.makeText(Activity_plan.this, "Error purchasing: " + result, Toast.LENGTH_SHORT).show();
                return;
            }
            else if (purchase.getSku().equals(SKU_Buy)) {
                // give user access to premium content and update the UI

                if(buy){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", Integer.toString(func.getDataInt("userid")));
                    params.put("planid", pid);

                    Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().buybazaar(params);
                    call.enqueue(new Callback<itemResponse>() {
                        @Override
                        public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                            Log.e("Token Request" ,  response.toString());
                            Toast.makeText(Activity_plan.this, response.body().message, Toast.LENGTH_SHORT).show();
                            if(response.body().success == 1){
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<itemResponse> call, Throwable t) {

                            Toast.makeText(Activity_plan.this, "مشکل در اتصال لطفا با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT).show();

                        }

                    });
                }
                else{
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", Integer.toString(func.getDataInt("userid")));
                    params.put("planid", pid);

                    Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().reservebazaar(params);
                    call.enqueue(new Callback<itemResponse>() {
                        @Override
                        public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                            Log.e("Token Request" ,  response.toString());
                            Toast.makeText(Activity_plan.this, response.body().message, Toast.LENGTH_SHORT).show();
                            if(response.body().success == 1){
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<itemResponse> call, Throwable t) {

                            Toast.makeText(Activity_plan.this, "مشکل در اتصال لطفا با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT).show();

                        }

                    });
                }

            }
        }
    };

    public void getplans(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));

        Call<List<itemPlan>> call = RetrofitClient.getInstance().getMyApi().getPlans(params);
        call.enqueue(new Callback<List<itemPlan>>() {
            @Override
            public void onResponse(Call<List<itemPlan>> call, retrofit2.Response<List<itemPlan>> response) {

                try {
                    Log.e("Repsone" ,  response.toString());
                    List<itemPlan> myheroList = response.body();
                    for (int i = 0; i < myheroList.size(); i++) {
                        deviceItems.add(myheroList.get(i));
                    }
                    adaptDevice.updateList(deviceItems);
                    av_condition.setVisibility(View.GONE);
                }
                catch (Exception e){
                }
            }

            @Override
            public void onFailure(Call<List<itemPlan>> call, Throwable t) {

                Log.e("getDevices" , t.toString());
                av_condition.setVisibility(View.GONE);

            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mHelper != null) mHelper.dispose();
//        mHelper = null;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

        Log.d(TAG, "onActivityResult(" + SKU_Buy + "," + resultCode + "," + data);
//
//        // Pass on the activity result to the helper for handling
//        if (!mHelper.handleActivityResult(RC_Buy, resultCode, data)) {
//            super.onActivityResult(RC_Buy, resultCode, data);
//        } else {
//            Toast.makeText(Activity_plan.this, "Error purchasing: " + resultCode + "," + data, Toast.LENGTH_LONG).show();
//
//            Log.d(TAG, "onActivityResult handled by IABUtil.");
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}