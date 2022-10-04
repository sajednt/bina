package com.sajednt.arzalarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.condition.New_condition_price;
import com.sajednt.arzalarm.condition.New_condition_timeframe;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.fragment.fragment_tickerinfo;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemSinglePrice;
import com.sajednt.arzalarm.item.itemTicker;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_ticker extends AppCompatActivity {
    Functions func;
    private Toolbar mToolbar;
    ViewPager viewPager;
    SmartTabLayout viewPagerTab;
    FragmentPagerItemAdapter adapter;
    itemTicker item;
    TextView textTicker, textPrice;
    Button basePrice , candlePrice , strabtn;
    Boolean load =true;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);

        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = (itemTicker) getIntent().getExtras().getSerializable("item");

        func = new Functions(getApplicationContext());

        viewPager = findViewById(R.id.viewpager);
        viewPagerTab = findViewById(R.id.tabindicator);
        textPrice = findViewById(R.id.textPrice);
        textTicker = findViewById(R.id.textTicker);
        basePrice = findViewById(R.id.buttonPriceBase);
        candlePrice = findViewById(R.id.buttonCandle);
        strabtn = findViewById(R.id.btnstrategy);
        webview = findViewById(R.id.tradingview);
        textTicker.setText(item.name+" ("+item.symbol+")");
        textPrice.setText("قیمت : "+item.price);
        {
            Bundler bn6h = new Bundler(); bn6h.putString("ticker" , item.ticker); bn6h.putString("timeframe", "6h");
            Bundler bn12h =new Bundler(); bn12h.putString("ticker" , item.ticker); bn12h.putString("timeframe", "12h");
            Bundler bn1d = new Bundler(); bn1d.putString("ticker" , item.ticker); bn1d.putString("timeframe", "1d");
            Bundler bn3d = new Bundler(); bn3d.putString("ticker" , item.ticker); bn3d.putString("timeframe", "3d");
            Bundler bn1w = new Bundler(); bn1w.putString("ticker" , item.ticker); bn1w.putString("timeframe", "1w");
            Bundler bn1M = new Bundler(); bn1M.putString("ticker" , item.ticker); bn1M.putString("timeframe", "1M");

            adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("6h", fragment_tickerinfo.class, bn6h.get())
                    .add("12h",fragment_tickerinfo.class, bn12h.get())
                    .add("1d", fragment_tickerinfo.class, bn1d.get())
                    .add("3d", fragment_tickerinfo.class, bn3d.get())
                    .add("1w", fragment_tickerinfo.class, bn1w.get())
                    .add("1M", fragment_tickerinfo.class, bn1M.get())
                    .create());
        }

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    if (load) {
                        getCurrentPrice(item.ticker);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 10);

        String data = "<!-- TradingView Widget BEGIN --> <div class='tradingview-widget-container'> <script type='text/javascript' src='https://s3.tradingview.com/tv.js'></script> <script type='text/javascript'> new TradingView.widget( { 'autosize': true, 'symbol': 'BINANCE:YXYXYXYX', 'interval': 'D', 'timezone': 'Etc/UTC', 'theme': 'dark', 'style': '1', 'locale': 'en', 'toolbar_bg': '#f1f3f6', 'enable_publishing': false, 'allow_symbol_change': true, 'container_id': 'tradingview_292e9' } ); </script> </div> <!-- TradingView Widget END -->";
        data = data.replace("YXYXYXYX", item.ticker);
        webview.clearCache(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setBackgroundColor(Color.TRANSPARENT);
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);



        basePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , New_condition_price.class);
                i.putExtra("tickeritem" , item);
                startActivity(i);
            }
        });

        candlePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , New_condition_timeframe.class);
                i.putExtra("tickeritem" , item);
                startActivity(i);
            }
        });
   
        strabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_ticker.this, "به زودی ...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        load = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        load = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        load = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        load = false;
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

    public void getCurrentPrice(String ticker){

        Map<String, String> params = new HashMap<String, String>();
        params.put("ticker", ticker);
        Call<itemSinglePrice> call = RetrofitClient.getInstance().getMyApi().currentSinglePrice(params);

        call.enqueue(new Callback<itemSinglePrice>() {
            @Override
            public void onResponse(Call<itemSinglePrice> call, retrofit2.Response<itemSinglePrice> response) {

                Log.e("Login Request" ,  response.toString());

                int success = response.body().success;

                if(success == 1) {

                    Double Price = response.body().price;
                    textPrice.setText( "قیمت لحظه‌ای : "+ Double.toString(Price) );
                }
            }

            @Override
            public void onFailure(Call<itemSinglePrice> call, Throwable t) {

            }

        });

    }
}