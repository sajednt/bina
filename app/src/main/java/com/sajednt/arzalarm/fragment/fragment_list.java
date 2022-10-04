package com.sajednt.arzalarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.Activity_symbols;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.adapter.adapterTicker;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemTicker;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class fragment_list extends Fragment {

    int i = 0;
    List<itemTicker> EnvItem;

    LinearLayoutManager layoutManager;
    private RecyclerView mRecyclerEnv;
    adapterTicker rv;
    LinearLayout lheader;
    Functions func;
    String timeFrame = "1d";
    AVLoadingIndicatorView av_tickers;
    TextView notCondition;
    boolean load = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);



        EnvItem = new ArrayList<itemTicker>();
        func = new Functions(getActivity());
        mRecyclerEnv = view.findViewById(R.id.recyclerview);
        av_tickers = view.findViewById(R.id.indicator_tickers);
        lheader = view.findViewById(R.id.linearheader);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerEnv.setLayoutManager(layoutManager);

        rv = new adapterTicker( EnvItem , getContext(), (AppCompatActivity) getActivity());
        mRecyclerEnv.swapAdapter(rv, false);


        getPrices();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    if(load) {
                        getPrices();
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 10);




        return view;
    }

    public void getPrices(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString(func.getDataInt("userid")));
        params.put("timeframe", timeFrame);


        Call<List<itemTicker>> call = RetrofitClient.getInstance().getMyApi().getPrices(params);
        call.enqueue(new Callback<List<itemTicker>>() {
            @Override
            public void onResponse(Call<List<itemTicker>> call, retrofit2.Response<List<itemTicker>> response) {

                Log.e("Repsone" ,  response.toString());
                List<itemTicker> myheroList = response.body();
                EnvItem.clear();
                for (int i = 0; i < myheroList.size(); i++) {
                    EnvItem.add(myheroList.get(i));

                }

                mRecyclerEnv.getRecycledViewPool().clear();
                rv.updateList(EnvItem);
                av_tickers.setVisibility(View.GONE);
                lheader.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<itemTicker>> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
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
        //getActivity().getActionBar().setTitle("بینا");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTicker:
                Intent in = new Intent( getActivity() , Activity_symbols.class);
                startActivity(in);
                break;
        }
        return true;    }
}
