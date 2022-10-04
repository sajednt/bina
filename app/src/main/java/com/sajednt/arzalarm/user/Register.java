package com.sajednt.arzalarm.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemUserCode;

import java.util.HashMap;
import java.util.Map;

//import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;

public class Register extends AppCompatActivity {

//    CircularProgressButton Register;
    EditText name, email, pass, confirmPass;
    Functions func;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        func = new Functions(this);
        Register = findViewById(R.id.button2);
        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        confirmPass = findViewById(R.id.editConfirmPass);
        TextView textPrivacy = findViewById(R.id.textprivacy);

        textPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://arzalarm.com/privacy-policy/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setError(null);
                email.setError(null);
                pass.setError(null);
                confirmPass.setError(null);
                if(func.editIsEmpty(name)){
                    name.setError("Enter your name");
                }
                else if(!func.isValidEmail(email.getText().toString())){
                    email.setError("Enter valid email");
                }
                else if(!func.checkPasswordvalid(pass, confirmPass)){

                }
                else{
//                    Register.startAnimation();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("password", pass.getText().toString());

                    Call<itemUserCode> call = RetrofitClient.getInstance().getMyApi().getRegisterUserCode(params);
                    call.enqueue(new Callback<itemUserCode>() {
                        @Override
                        public void onResponse(Call<itemUserCode> call, retrofit2.Response<itemUserCode> response) {

                            Log.e("Repsone" ,  response.body().toString());

//                          Register.revertAnimation();

                            if(response.body().success==1){
                                //Toast.makeText(getApplicationContext(), response.body().message +"\n"+ Integer.toString(response.body().code), Toast.LENGTH_LONG).show();
                                func.saveDataString("TickersSymbol",response.body().tickers);
                                Intent i = new Intent(com.sajednt.arzalarm.user.Register.this , Code.class);
                                i.putExtra("code" , response.body().code);
                                i.putExtra("userid" , response.body().id);

                                startActivity(i);
                            }
                            else if(response.body().success==0 && response.body().active==0){
                                func.saveDataString("TickersSymbol",response.body().tickers);
                                //Toast.makeText(getApplicationContext(), response.body().message +"\n"+ Integer.toString(response.body().code), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(com.sajednt.arzalarm.user.Register.this , Code.class);
                                i.putExtra("code" , response.body().code);
                                i.putExtra("userid" , response.body().id);
                                startActivity(i);
                            }
                            else{

                                Toast.makeText(getApplicationContext(), "user existed", Toast.LENGTH_LONG).show();

                            }


                        }

                        @Override
                        public void onFailure(Call<itemUserCode> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "مشکل در اتصال ، لطفا اینترنت خود را بررسی کنید", Toast.LENGTH_LONG).show();
//                            Register.revertAnimation();
                        }
                    });
                }

            }
        });


    }
}