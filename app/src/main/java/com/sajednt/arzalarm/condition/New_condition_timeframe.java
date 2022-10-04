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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemTicker;
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

public class New_condition_timeframe extends AppCompatActivity {

    Functions func;
    private Toolbar mToolbar;
    //SearchableSpinner sspiner;
    TextView textPrice , textcross, texttickername;
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
    Spinner timeframe;
    String time = "6h";
    CheckBox checkSms , checkEmail;
    Boolean sendsms = false, sendemail = false;
    AVLoadingIndicatorView avloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_condition_timeframe);

        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        func = new Functions(this);
        item = (itemTicker) getIntent().getExtras().getSerializable("tickeritem");
        ticker = item.ticker;
        //sspiner =       findViewById(R.id.tickerspinner);
        textPrice =   findViewById(R.id.textPrice);
        textcross =   findViewById(R.id.textcondition);
        editPrice =     findViewById(R.id.editTextNumberDecimal);
        editMessage =   findViewById(R.id.editMessage);
        linearCondition = findViewById(R.id.linearcondition);
        addCondition = findViewById(R.id.btnCondition);
        texttickername = findViewById(R.id.texttickername);
        timeframe = findViewById(R.id.timespinner);
        checkSms = findViewById(R.id.checkBoxsms);
        checkEmail = findViewById(R.id.checkBoxemail);
        lineararea = findViewById(R.id.lineararea);
        avloading = findViewById(R.id.indicator_condi);
        Summary1 = findViewById(R.id.condi_Summary1);
        Summary2 = findViewById(R.id.condi_Summary2);
        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        df.setMaximumFractionDigits(8);
        lineararea.setVisibility(View.INVISIBLE);
        texttickername.setText(ticker);
        PusheAnalytics analytics = Pushe.getPusheService(PusheAnalytics.class);
        if (analytics != null) {
            String name = "enter new timeframe condition ";
            Map<String, Object> map = new HashMap<>();
            map.put("userid", Integer.toString(func.getDataInt("userid")));
            map.put("event", "enter timeframe new condition ");
            analytics.sendEvent(
                    new Event.Builder(name)
                            .setAction(EventAction.ACHIEVEMENT)
                            .setData(map)
                            .build()
            );
            Log.e("pushe", "enter new timeframe condition" );
        }
        //getTickersBaseSymbol();

//        sspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ticker = sspiner.getSelectedItem().toString();
//
//                getTicker();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        timeframe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = parent.getSelectedItem().toString();
                getTicker();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                }

                try {
                    Double p = Double.parseDouble(s.toString());
                    if(p > cprice){
                        textcross.setText("اگر قیمت "+ ticker + " در بسته شدن کندل "+ timeframe.getSelectedItem().toString() + " به " + s.toString() + " افزایش پیدا کرد");
                        sign = 1;
                    }
                    else {
                        textcross.setText("اگر قیمت "+ ticker + " در بسته شدن کندل "+ timeframe.getSelectedItem().toString() + " به " + s.toString() + " کاهش پیدا کرد");
                        sign = 2;
                    }
                }
                catch (Exception e){}

            }

            @Override
            public void afterTextChanged(Editable s) {

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
                Toast.makeText(New_condition_timeframe.this, "به زودی ...", Toast.LENGTH_SHORT).show();
                checkSms.setChecked(false);
            }
        });

    }

    public void getTicker(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("ticker", ticker);
        params.put("timeframe", time);

        Call<itemTicker> call = RetrofitClient.getInstance().getMyApi().getsingleticker(params);

        call.enqueue(new Callback<itemTicker>() {
            @Override
            public void onResponse(Call<itemTicker> call, retrofit2.Response<itemTicker> response) {

                Log.e("Login Request" ,  response.toString());
                lineararea.setVisibility(View.VISIBLE);
                avloading.setVisibility(View.GONE);
                if(response.body().close < response.body().open) {

                }
                else{

                }
                cprice = response.body().open;
                textPrice.setText("قیمت شروع کندل : "+formatter.format(response.body().open));

            }

            @Override
            public void onFailure(Call<itemTicker> call, Throwable t) {

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
        params.put("timeframe", timeframe.getSelectedItem().toString());
        params.put("sendemail", String.valueOf(sendemail));

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().addCondition(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Repsone" ,  response.toString());
                if(response.body().success==1){
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                    New_condition_timeframe.this.finish();
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

//    public void getTickersBaseSymbol(){
//
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("symbol", item.symbol);
//
//        Call<List<itemTicker>> call = RetrofitClient.getInstance().getMyApi().getTickerBaseSymbol(params);
//        call.enqueue(new Callback<List<itemTicker>>() {
//            @Override
//            public void onResponse(Call<List<itemTicker>> call, retrofit2.Response<List<itemTicker>> response) {
//
//                Log.e("Repsone" ,  response.toString());
//                List<itemTicker> myheroList = response.body();
//                for (int i = 0; i < myheroList.size(); i++) {
//                    Tickerlist.add(myheroList.get(i).ticker);
//                }
//                TickerItems = myheroList;
//                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, Tickerlist);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sspiner.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<itemTicker>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
//            }
//
//        });
//    }

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