package com.sajednt.arzalarm.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sajednt.arzalarm.Activity_pager;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.Splash;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemResponse;
import com.sajednt.arzalarm.item.itemUserLogin;
import com.pushwoosh.Pushwoosh;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity {

//    CircularProgressButton login;
    TextView textRegister, textForgot, textPrivacy;
    Functions func;
    EditText email , password;
    Button btn;
    String userid="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        func = new Functions(this);
        if(func.userLogedin()){
            Intent i = new Intent(Login.this , Splash.class);
            startActivity(i);
            try {
                this.finish();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        btn = findViewById(R.id.button);
//        login = findViewById(R.id.btn_Login);
        textRegister = findViewById(R.id.textRegister);
        textForgot = findViewById(R.id.textForgot);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPass);
        textPrivacy = findViewById(R.id.textprivacy);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                login.startAnimation();
                loginfunc();
            }
        });
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this , Forgot_password.class);
                startActivity(i);
            }
        });

        textPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://arzalarm.com/privacy-policy/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            }
        });

    }


    public void settoken(String userid1){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid1);
        params.put("token", Pushwoosh.getInstance().getHwid());

        Call<itemUserLogin> call = RetrofitClient.getInstance().getMyApi().userSetToken(params);
        call.enqueue(new Callback<itemUserLogin>() {
            @Override
            public void onResponse(Call<itemUserLogin> call, retrofit2.Response<itemUserLogin> response) {

                Log.e("Token Request" ,  response.toString());

            }

            @Override
            public void onFailure(Call<itemUserLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });

    }

    public void loginfunc(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString().trim());
        params.put("password", password.getText().toString().trim());
        params.put("deviceid", func.getUniquePsuedoID());
        params.put("devicename", android.os.Build.MANUFACTURER +' ' + android.os.Build.MODEL);

        Call<itemUserLogin> call = RetrofitClient.getInstance().getMyApi().userlogin(params);
        Log.e("urlsss", call.request().url().toString());
        call.enqueue(new Callback<itemUserLogin>() {
            @Override
            public void onResponse(Call<itemUserLogin> call, retrofit2.Response<itemUserLogin> response) {

                Log.e("Login Request" ,  response.toString());

                int success = response.body().success;

                if(success == 1) {

                    int userid = response.body().id;
                    String tickers = response.body().tickers;
                    func.saveDataInt("userlogedin", 1);
                    func.saveDataInt("userid", userid);

                    func.saveDataString("TickersSymbol", tickers);
                    func.saveDataString("email", email.getText().toString().trim());
//                           Pushe.setUserEmail(email.getText().toString().trim());
                    Pushwoosh.getInstance().setEmail(email.getText().toString().trim());
                    //settoken(Integer.toString(userid));

                    Intent i = new Intent(Login.this, Activity_pager.class);
                    startActivity(i);

                    try {
                        Login.this.finish();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
                else if(success == 104){

                    userid = response.body().userid;
                    new AlertDialog.Builder(Login.this)
                            .setTitle("اخطار")
                            .setMessage(response.body().message)
                            .setCancelable(false)
                            .setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("حذف دستگاه ها", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    removedevices();
                                }
                            }).show();
                }
                else{

                    Toast.makeText(Login.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
//                        login.revertAnimation();
            }

            @Override
            public void onFailure(Call<itemUserLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }

        });
    }

    public void removedevices(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid);

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().removeDevices(params);
        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {
                Log.e("Token Request" ,  response.toString());
                Toast.makeText(Login.this, response.body().message, Toast.LENGTH_SHORT).show();
                if(response.body().success == 1){
                    loginfunc();
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {


            }

        });
    }
}