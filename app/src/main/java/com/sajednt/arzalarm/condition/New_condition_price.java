package com.sajednt.arzalarm.condition;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemSinglePrice;
import com.sajednt.arzalarm.item.itemTicker;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.pushe.plus.Pushe;
import co.pushe.plus.analytics.PusheAnalytics;
import co.pushe.plus.analytics.event.Event;
import co.pushe.plus.analytics.event.EventAction;
import retrofit2.Call;
import retrofit2.Callback;

public class New_condition_price extends AppCompatActivity {

    Functions func;
    private Toolbar mToolbar;
    SearchableSpinner sspiner;
    TextView tickerprice , tickercross , textcross, textcrossprice, texttickername;
    EditText editPrice, editMessage;
    itemTicker item;
    LinearLayout linearCondition, lineararea, Summary1 , Summary2 ;
    Handler handler = new Handler();
    List<itemTicker> TickerItems = new ArrayList<itemTicker>();
    List<String> Tickerlist = new ArrayList<String>();
    ArrayAdapter adapter;
    Double cprice;
    String ticker;
    NumberFormat formatter;
    DecimalFormat df = new DecimalFormat("#");
    int sign = 0;
    Button addCondition;
    CheckBox checkSms , checkEmail;
    Boolean sendsms = false, sendemail = false;
    AVLoadingIndicatorView avloading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_condition);

        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        func = new Functions(this);
        item = (itemTicker) getIntent().getExtras().getSerializable("tickeritem");
        ticker = item.ticker;
        sspiner =       findViewById(R.id.tickerspinner);
        tickerprice =   findViewById(R.id.textPrice);
        tickercross =   findViewById(R.id.texTickercross);
        textcross =     findViewById(R.id.textcross);
        textcrossprice= findViewById(R.id.textcrossPrice);
        editPrice =     findViewById(R.id.editTextNumberDecimal);
        editMessage =   findViewById(R.id.editMessage);
        linearCondition = findViewById(R.id.linearcondition);
        addCondition = findViewById(R.id.btnCondition);
        checkSms = findViewById(R.id.checkBoxsms);
        checkEmail = findViewById(R.id.checkBoxemail);
        lineararea = findViewById(R.id.lineararea);
        avloading = findViewById(R.id.indicator_condi);
        Summary1 = findViewById(R.id.condi_Summary1);
        Summary2 = findViewById(R.id.condi_Summary2);
        texttickername = findViewById(R.id.tickernametext);
        texttickername.setText(ticker);
        tickercross.setText(ticker);

        PusheAnalytics analytics = Pushe.getPusheService(PusheAnalytics.class);
        if (analytics != null) {
            String name = "enter new condition ";
            Map<String, Object> map = new HashMap<>();
            map.put("userid", Integer.toString(func.getDataInt("userid")));
            map.put("event", "enter new condition ");
            analytics.sendEvent(
                    new Event.Builder(name)
                            .setAction(EventAction.ACHIEVEMENT)
                            .setData(map)
                            .build()
            );
        }

        lineararea.setVisibility(View.INVISIBLE);
        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        df.setMaximumFractionDigits(8);

       // getTickersBaseSymbol();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{

                    getCurrentPrice(ticker);
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

        sspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tickercross.setText(TickerItems.get(position).ticker);
                ticker = TickerItems.get(position).ticker;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().isEmpty()){
                    Summary1.setVisibility(View.INVISIBLE);
                    Summary2.setVisibility(View.INVISIBLE);

                }
                else{
                    Summary1.setVisibility(View.VISIBLE);
                    Summary2.setVisibility(View.VISIBLE);

                    textcrossprice.setText(s.toString());
                    Double p = Double.parseDouble(s.toString());
                    if(p > cprice){
                        textcross.setText("به بیشتر از");
                        sign = 1;
                    }
                    else {
                        textcross.setText("به کمتر از");
                        sign = 2;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editPrice.getText().toString())){
                    editPrice.setError("این زمینه الزامی است");
                }
                else{
                    addCondition();

                }
            }
        });

        
        checkEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendemail = isChecked;
            }
        });
        checkSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendsms = isChecked;
                Toast.makeText(New_condition_price.this, "به زودی ...", Toast.LENGTH_SHORT).show();
                checkSms.setChecked(false);
            }
        });
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
                    cprice = Price;
                    tickerprice.setText( "قیمت لحظه‌ای : "+ df.format(Price) );
                    lineararea.setVisibility(View.VISIBLE);
                    avloading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<itemSinglePrice> call, Throwable t) {

            }

        });

    }

    public void getTickersBaseSymbol(){


        Map<String, String> params = new HashMap<String, String>();
        params.put("symbol", item.symbol);

        Call<List<itemTicker>> call = RetrofitClient.getInstance().getMyApi().getTickerBaseSymbol(params);
        call.enqueue(new Callback<List<itemTicker>>() {
            @Override
            public void onResponse(Call<List<itemTicker>> call, retrofit2.Response<List<itemTicker>> response) {

                Log.e("Repsone" ,  response.toString());
                List<itemTicker> myheroList = response.body();
                for (int i = 0; i < myheroList.size(); i++) {
                    Tickerlist.add(myheroList.get(i).ticker);
                }
                TickerItems = myheroList;
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, Tickerlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sspiner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<itemTicker>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void addCondition(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));
        params.put("ticker", ticker);
        params.put("sign", Integer.toString(sign));
        params.put("price", editPrice.getText().toString());
        params.put("message", editMessage.getText().toString());
        params.put("timeframe", "now");
        params.put("sendemail", String.valueOf(sendemail));

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().addCondition(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Repsone" ,  response.toString());
                if(response.body().success==1){
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                    New_condition_price.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
//                            Register.revertAnimation();
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