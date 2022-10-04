package com.sajednt.arzalarm;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.adapter.AdapterDevices;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemDevice;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_devices extends AppCompatActivity {

    private Toolbar mToolbar;
    List<itemDevice> deviceItems;
    LinearLayoutManager layoutManager;
    private RecyclerView recycle_device;
    AdapterDevices adaptDevice;
    Functions func;
    AVLoadingIndicatorView av_condition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.devicelist);
        func = new Functions(getApplicationContext());

        deviceItems = new ArrayList<itemDevice>();
        recycle_device = findViewById(R.id.recyclerview);
        av_condition = findViewById(R.id.indicator_tickers);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recycle_device.setLayoutManager(layoutManager);

        adaptDevice = new AdapterDevices( deviceItems , getApplicationContext() , this );
        recycle_device.swapAdapter(adaptDevice, false);



        getDevice();
    }

    public void getDevice(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));

        Call<List<itemDevice>> call = RetrofitClient.getInstance().getMyApi().getDevice(params);
        call.enqueue(new Callback<List<itemDevice>>() {
            @Override
            public void onResponse(Call<List<itemDevice>> call, retrofit2.Response<List<itemDevice>> response) {

                try {
                    Log.e("Repsone" ,  response.toString());
                    List<itemDevice> myheroList = response.body();
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
            public void onFailure(Call<List<itemDevice>> call, Throwable t) {

                Log.e("getDevices" , t.toString());
                av_condition.setVisibility(View.GONE);

            }

        });

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