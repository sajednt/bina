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
import com.sajednt.arzalarm.item.itemResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Forgot_password extends AppCompatActivity {

    EditText editemail;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        editemail = findViewById(R.id.EmailAddress);
        btn = findViewById(R.id.btnrecovery);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", editemail.getText().toString());

                Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().Recoveremail(params);
                call.enqueue(new Callback<itemResponse>() {
                    @Override
                    public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                        Log.e("Token Request" ,  response.toString());
                        Toast.makeText(Forgot_password.this, response.body().message, Toast.LENGTH_SHORT).show();
                        if(response.body().success == 1){
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<itemResponse> call, Throwable t) {

                        Toast.makeText(Forgot_password.this, "مشکل در اتصال", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });
    }
}