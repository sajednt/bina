package com.sajednt.arzalarm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemCondition;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemSinglePrice;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_editCondition extends AppCompatActivity {
    EditText editPrice,editmessage;
    TextView textTicker, textTickerPrice;
    Button btnSave, btndelete, btnEnable;
    String sign , id ;
    itemCondition ic;
    ImageView imgSign;
    Functions func;
    Double currentPrice = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcondition);

        func = new Functions(getApplicationContext());
        editPrice = findViewById(R.id.editprice);
        editmessage = findViewById(R.id.textmessage);
        textTicker = findViewById(R.id.textticker);
        textTickerPrice = findViewById(R.id.textTickerPrice);
        btnSave = findViewById(R.id.btnsave);
        btndelete = findViewById(R.id.btnDelete);
        btnEnable = findViewById(R.id.btnEnable);
        imgSign = findViewById(R.id.imagesign);

        ic = (itemCondition) getIntent().getExtras().getSerializable("item");
        textTicker.setText(ic.ticker);
        editPrice.setText(ic.price);
        editmessage.setText(ic.message);
        sign = ic.sign;
        int ssign = Integer.parseInt(sign);
        if(ssign == 1){
            imgSign.setImageResource(R.mipmap.arrow_left);
        }
        else if( ssign == 2){
            imgSign.setImageResource(R.mipmap.arrow_right);
        }

        if(ic.status == 1){
            btnEnable.setText("Disable");
        }
        else if( ic.status == 0){
            btnEnable.setText("Enable");
        }

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Double inputPrice = Double.parseDouble(s.toString());

                    if(inputPrice < currentPrice){
                        imgSign.setImageResource(R.mipmap.arrow_left);
                        sign = "2";
                    }
                    else {
                        imgSign.setImageResource(R.mipmap.arrow_right);
                        sign = "1";
                    }
                }
                catch (Exception e){}

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(Integer.toString(ic.id));
            }
        });

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ic.status == 0){
                    setStatus(Integer.toString(ic.id), 1);
                }
                else {
                    setStatus(Integer.toString(ic.id), 0);
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        getCurrentPrice(ic.ticker);

    }

    public void update(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString( ic.id));
        params.put("price", editPrice.getText().toString());
        params.put("sign", sign);
        params.put("message", editmessage.getText().toString());
        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().updateCondition(params);

        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("update condition Request" ,  response.toString());

                int success = response.body().success;

                if(success == 1) {
                    Toast.makeText(getApplicationContext(), "Condition updated" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {

            }

        });

    }

    public void delete(String id){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().deleteCondition(params);

        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("delete condition Request" ,  response.toString());

                int success = response.body().success;

                if(success == 1) {
                    Toast.makeText(getApplicationContext(), "Condition deleted" , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {

            }

        });
    }

    public void setStatus(String id, int status){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("status", Integer.toString(status));

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().setConditionStatus(params);

        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Login Request" ,  response.toString());

                int success = response.body().success;

                if(success == 1) {
                    if(status == 0) {
                        Toast.makeText(getApplicationContext(), "Condition Set Disable", Toast.LENGTH_SHORT).show();
                        btnEnable.setText("Enable");
                        ic.status = 0;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Condition Set Enable", Toast.LENGTH_SHORT).show();
                        btnEnable.setText("Disable");
                        ic.status = 1;
                    }
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {

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
                    textTickerPrice.setText(Double.toString(Price));
                    currentPrice =  Price;
                }
            }

            @Override
            public void onFailure(Call<itemSinglePrice> call, Throwable t) {

            }

        });

    }
}