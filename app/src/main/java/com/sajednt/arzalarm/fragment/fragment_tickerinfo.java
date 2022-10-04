package com.sajednt.arzalarm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.item.itemTicker;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class fragment_tickerinfo extends Fragment {



    String timeframe, ticker;
    TextView textChangePercent, textHigh, textLow, textOpen, textClose, textChange, textVolume;
    NumberFormat formatter;
    DecimalFormat df = new DecimalFormat("#");
    LinearLayout tickerlinear;
    AVLoadingIndicatorView av;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, container, false);

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        df.setMaximumFractionDigits(8);

        FragmentPagerItem.getPosition(getArguments());
        timeframe = getArguments().getString("timeframe");
        ticker = getArguments().getString("ticker");
        textChangePercent = view.findViewById(R.id.priceChangePrecent);
        textHigh = view.findViewById(R.id.priceHigh);
        textLow = view.findViewById(R.id.priceLow);
        textOpen = view.findViewById(R.id.priceOpen);
        textClose = view.findViewById(R.id.priceClose);
        textChange = view.findViewById(R.id.priceChange);
        textVolume = view.findViewById(R.id.priceVolume);
        tickerlinear = view.findViewById(R.id.tickerinfolinear);
        av = view.findViewById(R.id.indicator_tickerinfo);

        tickerlinear.setVisibility(View.INVISIBLE);

        getTicker();

        return view;
    }

    public void getTicker(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("ticker", ticker);
        params.put("timeframe", timeframe);

        Call<itemTicker> call = RetrofitClient.getInstance().getMyApi().getsingleticker(params);

        call.enqueue(new Callback<itemTicker>() {
            @Override
            public void onResponse(Call<itemTicker> call, retrofit2.Response<itemTicker> response) {

                Log.e("Login Request" ,  response.toString());

                Double dif = response.body().close - response.body().open;
                Double difper = Double.valueOf(0);

                if(response.body().close < response.body().open) {

                    difper = ((response.body().close/response.body().open)-1)*100;
                    textChangePercent.setText( formatter.format(difper)+"%");
                    textChangePercent.setBackgroundResource(R.drawable.radius_red);
                }
                else{
                    difper = ((response.body().open/response.body().close)-1)*100;
                    textChangePercent.setText( (formatter.format(difper)+"%").replace("-",""));
                    textChangePercent.setBackgroundResource(R.drawable.radius_green);
                }


                textHigh.setText(Double.toString(response.body().high));
                textLow.setText(Double.toString(response.body().low));
                textOpen.setText(Double.toString(response.body().open));
                textClose.setText(Double.toString(response.body().close));
                textVolume.setText(formatter.format(response.body().volume));
                textHigh.setText(Double.toString(response.body().high));
                textChange.setText(formatter.format(dif));

                tickerlinear.setVisibility(View.VISIBLE);
                av.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<itemTicker> call, Throwable t) {

            }

        });

    }

}
