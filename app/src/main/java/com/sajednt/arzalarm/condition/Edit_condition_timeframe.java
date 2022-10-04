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
import com.sajednt.arzalarm.item.itemCondition;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemTicker;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Edit_condition_timeframe extends AppCompatActivity {

    Functions func;
    private Toolbar mToolbar;
    //SearchableSpinner sspiner;
    TextView textPrice , textcross, texttickername;
    EditText editPrice, editMessage;
    itemCondition ic;
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
    Button addCondition, btnDelete;
    Spinner timeframe;
    String time = "6h";
    CheckBox checkSms , checkEmail;
    Boolean sendsms = false, sendemail = false;
    AVLoadingIndicatorView avloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_condition_timeframe);

        mToolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        func = new Functions(this);
        ic = (itemCondition) getIntent().getExtras().getSerializable("item");
        ticker = ic.ticker;

        textPrice =   findViewById(R.id.textPrice);
        textcross =   findViewById(R.id.textcondition);
        editPrice =     findViewById(R.id.editTextNumberDecimal);
        editMessage =   findViewById(R.id.editMessage);
        linearCondition = findViewById(R.id.linearcondition);
        addCondition = findViewById(R.id.btnupdate);
        texttickername = findViewById(R.id.texttickername);
        timeframe = findViewById(R.id.timespinner);
        checkSms = findViewById(R.id.checkBoxsms);
        checkEmail = findViewById(R.id.checkBoxemail);
        lineararea = findViewById(R.id.lineararea);
        avloading = findViewById(R.id.indicator_condi);
        Summary1 = findViewById(R.id.condi_Summary1);
        Summary2 = findViewById(R.id.condi_Summary2);
        btnDelete = findViewById(R.id.btnDelete);
        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        df.setMaximumFractionDigits(8);
        lineararea.setVisibility(View.INVISIBLE);



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
                    updateCondition();

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
                Toast.makeText(Edit_condition_timeframe.this, "به زودی ...", Toast.LENGTH_SHORT).show();
                checkSms.setChecked(false);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(Integer.toString(ic.id));
            }
        });
        texttickername.setText(ticker);
        selectValue(timeframe, ic.timeframe);
        editPrice.setText(ic.price);
        editMessage.setText(ic.message);
        if(ic.sendemail.equals("1")){
            checkEmail.setChecked(true);
            sendemail = true;
        }
        else{
            checkEmail.setChecked(false);
            sendemail = false;
        }
    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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
                editPrice.setText(ic.price);

            }

            @Override
            public void onFailure(Call<itemTicker> call, Throwable t) {

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
                    Toast.makeText(getApplicationContext(), response.body().message , Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {

            }

        });
    }

    public void updateCondition(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(ic.id));
        params.put("sign", Integer.toString(sign));
        params.put("price", editPrice.getText().toString());
        params.put("timeframe", time);
        params.put("message", editMessage.getText().toString());
        params.put("sendemail", String.valueOf(sendemail));

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().updateCondition(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Repsone" ,  response.toString());
                if(response.body().success==1){
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                    Edit_condition_timeframe.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "connection problem", Toast.LENGTH_LONG).show();
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
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}