package com.sajednt.arzalarm.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.sajednt.arzalarm.Activity_pager;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemUserCode;

import java.util.HashMap;
import java.util.Map;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;

public class Code extends AppCompatActivity {

    private Handler mHandler = new Handler();
    Functions func;
    private OtpTextView otpTextView;
    int validcode= 0 ,userid  =0 ;
    TextView textsendcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        int validcode  = getIntent().getExtras().getInt("code");
        userid  = getIntent().getExtras().getInt("userid");
        otpTextView = findViewById(R.id.otp_view);
        textsendcode = findViewById(R.id.textsendcode);
        func = new Functions(this);
        //Toast.makeText(getApplicationContext(), Integer.toString(validcode), Toast.LENGTH_LONG).show();

        textsendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(Integer.toString(userid));
            }
        });
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String code) {
                // fired when user has entered the OTP fully.
                try {
                    int input = Integer.parseInt(code);
                    if(validcode == input){

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", Integer.toString(userid));
                        params.put("deviceid", func.getUniquePsuedoID());
                        params.put("devicename", android.os.Build.MANUFACTURER +' ' + android.os.Build.MODEL);

                        Call<itemUserCode> call = RetrofitClient.getInstance().getMyApi().userActivate(params);
                        call.enqueue(new Callback<itemUserCode>() {
                            @Override
                            public void onResponse(Call<itemUserCode> call, retrofit2.Response<itemUserCode> response) {


                                if(response.body().success==1){
                                    func.saveDataInt("userlogedin" , 1);
                                    func.saveDataInt("userid" , userid);
                                    func.saveDataInt("planstatus" , 1);


                                    new AlertDialog.Builder(Code.this)
                                            .setTitle("حساب شما فعال شد")
                                            .setMessage(response.body().message)
                                            .setCancelable(false)
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                    Intent i = new Intent(Code.this , Activity_pager.class);
                                                    i.putExtra("register", 1);
                                                    startActivity(i);
                                                    Code.this.finish();
                                                }
                                            })
                                            .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent i = new Intent(Code.this , Activity_pager.class);
                                                    startActivity(i);
                                                    Code.this.finish();
                                                }
                                            }).show();




                                }
                                else if(response.body().success==0 && response.body().active==0){
                                    Toast.makeText(getApplicationContext(), "کد اشتباه است", Toast.LENGTH_LONG).show();
                                    otpTextView.setOTP("");
                                }


                            }

                            @Override
                            public void onFailure(Call<itemUserCode> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                            }

                        });

                    }
                    else{
                        Toast.makeText(Code.this, "Error!", Toast.LENGTH_SHORT).show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "کد اشتباه است", Toast.LENGTH_LONG).show();
                                otpTextView.setOTP("");

                            }
                        }, 2000);
                    }
                }
                catch (Exception e){

                }
            }
        });
    }

    public void sendCode(String id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", id);
        Toast.makeText(Code.this,  id , Toast.LENGTH_SHORT).show();


        Call<itemUserCode> call = RetrofitClient.getInstance().getMyApi().sendCode(params);
        call.enqueue(new Callback<itemUserCode>() {
            @Override
            public void onResponse(Call<itemUserCode> call, retrofit2.Response<itemUserCode> response) {
                Log.e("Token Request" ,  response.toString());

                if(response.body().success==1){
                    Toast.makeText(Code.this,  response.body().message , Toast.LENGTH_SHORT).show();
                    validcode = response.body().code;
                }
                else{
                    Toast.makeText(Code.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<itemUserCode> call, Throwable t) {

            }

        });
    }
}