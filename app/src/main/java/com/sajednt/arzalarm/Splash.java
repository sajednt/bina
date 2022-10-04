package com.sajednt.arzalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.pushwoosh.Pushwoosh;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemUserLogin;
import com.sajednt.arzalarm.item.item_settings;
import com.sajednt.arzalarm.user.Login;

import java.util.HashMap;
import java.util.Map;

import me.ibrahimsn.particle.ParticleView;
import retrofit2.Call;
import retrofit2.Callback;

public class Splash extends AppCompatActivity {
    Functions func;
    RelativeLayout splashlayout;
    ParticleView pv;
    int currentVersionCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        currentVersionCode = BuildConfig.VERSION_CODE;
            func = new Functions(getApplicationContext());
            splashlayout = findViewById(R.id.splashLayout);
            pv = findViewById(R.id.particleView);

            getsettings();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 0);
    }

    public void set_token(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));
        params.put("token", Pushwoosh.getInstance().getHwid());
        params.put("deviceid", func.getUniquePsuedoID());


        Call<itemUserLogin> call = RetrofitClient.getInstance().getMyApi().userSetToken(params);
        call.enqueue(new Callback<itemUserLogin>() {
            @Override
            public void onResponse(Call<itemUserLogin> call, retrofit2.Response<itemUserLogin> response) {
                Log.e("Token Request" ,  response.toString());

                if(response.body().success==1){
                    Intent i = new Intent(Splash.this, Activity_pager.class);
                    startActivity(i);
                    Splash.this.finish();
                    pv.pause();
                }
                else if(response.body().success==2){
                    SharedPreferences settings = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    Splash.this.finish();
                }
                else{
                    Toast.makeText(Splash.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<itemUserLogin> call, Throwable t) {
                Snackbar.Callback sn = new Snackbar.Callback();


                Snackbar.make(splashlayout, R.string.connection_problem, Snackbar.LENGTH_LONG)
                        .setAction(R.string.again, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                set_token();

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.darkblue))
                        .setTextColor(getResources().getColor(R.color.darkblue))
                        .setBackgroundTint(getResources().getColor(R.color.white))
                        .setDuration(50000)
                        .show();

            }

        });
    }

    public void getsettings(){

        Call<item_settings> call = RetrofitClient.getInstance().getMyApi().getSettings();
        call.enqueue(new Callback<item_settings>() {
            @Override
            public void onResponse(Call<item_settings> call, retrofit2.Response<item_settings> response) {
                Log.e("Token Request" ,  response.toString());

                if(response.body().forceupdate == 1){
                    if(response.body().lastversion > currentVersionCode) {

                        new AlertDialog.Builder(Splash.this)
                                .setTitle("آپدیت نسخه جدید ارز آلارم")
                                .setMessage("نسخه جدید نرم افزار در دسترس است ، لطفا نرم افزار را بروزرسانی بفرمایید")
                                .setCancelable(false)
                                .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                })
                                .setPositiveButton("بروزرسانی", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().updateurl));
                                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(browserIntent);
                                        finish();
                                        System.exit(0);
                                    }
                                }).show();

                    }
                    else{
                        set_token();
                    }
                }
                else  if(response.body().maintenance == 1){
                    new AlertDialog.Builder(Splash.this)
                            .setTitle("پیام سرور")
                            .setMessage(response.body().message)
                            .setCancelable(false)
                            .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            }).show();
                }
                else{

                    set_token();
                }
            }

            @Override
            public void onFailure(Call<item_settings> call, Throwable t) {
                Snackbar.Callback sn = new Snackbar.Callback();


                Snackbar.make(splashlayout, R.string.connection_problem, Snackbar.LENGTH_LONG)
                        .setAction(R.string.again, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getsettings();

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.darkblue))
                        .setTextColor(getResources().getColor(R.color.darkblue))
                        .setBackgroundTint(getResources().getColor(R.color.white))
                        .setDuration(50000)
                        .show();

            }

        });
    }
}