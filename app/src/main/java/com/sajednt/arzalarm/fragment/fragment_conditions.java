package com.sajednt.arzalarm.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.Activity_plan;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.adapter.adapterConditions;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemCondition;
import com.sajednt.arzalarm.user.Activity_ticker_to_alarm;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;

public class fragment_conditions extends Fragment {

    List<itemCondition> ConditionItem;
    RecyclerView recyclerCondition;
    adapterConditions adaptc;
    Functions func;
    AVLoadingIndicatorView av_condition;
    LinearLayoutManager layoutManagerC;
    TextView notCondition;
    boolean load = true;
    CircleButton addbtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conditions, container, false);

        func = new Functions(getActivity());

        ConditionItem = new ArrayList<itemCondition>();
        recyclerCondition = view.findViewById(R.id.recyclerCondition);
        av_condition = view.findViewById(R.id.indicator_condition);
        notCondition = view.findViewById(R.id.textView8);
        addbtn  =  view.findViewById(R.id.addbtn);
        layoutManagerC = new LinearLayoutManager(getActivity());
        recyclerCondition.setLayoutManager(layoutManagerC);

        adaptc = new adapterConditions( ConditionItem , getContext(), (AppCompatActivity) getActivity() , this );
        recyclerCondition.swapAdapter(adaptc, false);

        getConditions();

        Handler handler = new Handler();
        Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                try{
                    if(load) {
                       // getConditions();
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.postDelayed(runnable2, 10000);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(func.getDataInt("planstatus") == 1) {
                    Intent i = new Intent(getContext(), Activity_ticker_to_alarm.class);
                    startActivity(i);
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("اتمام اعتبار حساب")
                            .setMessage(func.getDataString("message"))
                            .setCancelable(false)
                            .setPositiveButton("خرید بسته", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i2 = new Intent(getContext(), Activity_plan.class);
                                    getActivity().startActivity(i2);                        //close drawer
                                }
                            }).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        load = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        load = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        load = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        load = true;
        getConditions();

    }

    public void getConditions(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));

        Call<List<itemCondition>> call = RetrofitClient.getInstance().getMyApi().listCondition(params);
        call.enqueue(new Callback<List<itemCondition>>() {
            @Override
            public void onResponse(Call<List<itemCondition>> call, retrofit2.Response<List<itemCondition>> response) {

                try {

                    Log.e("Repsone" ,  response.toString());
                    List<itemCondition> myheroList = response.body();
                    ConditionItem.clear();
                    for (int i = 0; i < myheroList.size(); i++) {
                        ConditionItem.add(myheroList.get(i));
                    }
                    adaptc.updateList(ConditionItem);
                    notCondition.setVisibility(View.GONE);
                    av_condition.setVisibility(View.GONE);

                    if(myheroList.size()==0){
                        notCondition.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e){
                }
            }

            @Override
            public void onFailure(Call<List<itemCondition>> call, Throwable t) {

                Log.e("getConditions" , t.toString());
                notCondition.setVisibility(View.VISIBLE);
                av_condition.setVisibility(View.GONE);

            }

        });

    }

}