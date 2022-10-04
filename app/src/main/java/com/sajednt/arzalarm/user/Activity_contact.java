package com.sajednt.arzalarm.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_contact extends AppCompatActivity {

    EditText editSubject , editDescription;
    Button btn;
    Functions func;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        editSubject = findViewById(R.id.editSubject);
        editDescription = findViewById(R.id.editDescription);
        btn = findViewById(R.id.button3);
        func = new Functions(Activity_contact.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editSubject.getText().toString().trim().length() == 0){
                    editSubject.setError("لطفا این زمینه را پر کنید");
                }
                else if(editDescription.getText().toString().trim().length() == 0){
                    editDescription.setError("لطفا این زمینه را پر کنید");
                }
                else{
                   settoken();
                }

            }
        });
    }

    public void settoken(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));
        params.put("subject", editSubject.getText().toString());
        params.put("description", editDescription.getText().toString());



        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().sendTicket(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                Log.e("Token Request" ,  response.toString());
                Toast.makeText(Activity_contact.this, response.body().message , Toast.LENGTH_LONG).show();
                if(response.body().success ==1){
                    finish();
                }

            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });

    }
}