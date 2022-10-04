package com.sajednt.arzalarm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemSymbols;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_condition extends AppCompatActivity {

    SearchableSpinner searchSpinner;
    EditText editPrice,editmessage;
    TextView textTicker;
    Button btnSave;
    List<String> Tickerlist = new ArrayList<String>();
    List<itemSymbols> TickerItems = new ArrayList<itemSymbols>();
    Double currentPrice = 0.0;
    ArrayAdapter adapter;
    Functions func;
    String sign;
    ImageView imgSign;
    String tickername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        searchSpinner = findViewById(R.id.tickerspinner);
        btnSave = findViewById(R.id.btnsave);
        textTicker = findViewById(R.id.textTicker);
        editPrice = findViewById(R.id.editprice);
        editmessage = findViewById(R.id.textmessage);
        imgSign = findViewById(R.id.imagesign);
        func = new Functions(getApplicationContext());

        getSymbols();

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textTicker.setText(TickerItems.get(position).price);
                currentPrice = Double.parseDouble(TickerItems.get(position).price);
                tickername = TickerItems.get(position).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCondition();
            }
        });
    }

    public void addCondition(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));
        params.put("ticker", tickername);
        params.put("sign", sign);
        params.put("price", editPrice.getText().toString());
        params.put("message", editmessage.getText().toString());

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().addCondition(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Repsone" ,  response.toString());

                if(response.body().success==1){
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                    Activity_condition.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "user existed", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
//                            Register.revertAnimation();
            }

        });

    }

    public void getSymbols(){



        Call<List<itemSymbols>> call = RetrofitClient.getInstance().getMyApi().getSymbols();
        call.enqueue(new Callback<List<itemSymbols>>() {
            @Override
            public void onResponse(Call<List<itemSymbols>> call, retrofit2.Response<List<itemSymbols>> response) {

                Log.e("Repsone" ,  response.toString());
                List<itemSymbols> myheroList = response.body();
                for (int i = 0; i < myheroList.size(); i++) {
                    Tickerlist.add(myheroList.get(i).getName());
                }
                TickerItems = myheroList;
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Tickerlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                searchSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<itemSymbols>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}