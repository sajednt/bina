package com.sajednt.arzalarm.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.Activity_ticker;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemSymbols;
        import com.sajednt.arzalarm.item.itemTicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapter_ticker_to_alarm extends RecyclerView.Adapter<Adapter_ticker_to_alarm.ContactViewHolder> implements Filterable {

    private List<itemSymbols> item;
    public static Context context;
    AppCompatActivity activity;
    Functions func;
    private List<itemSymbols> ListFiltered;

    public Adapter_ticker_to_alarm(List<itemSymbols> contents , Context con , AppCompatActivity act ) {
        this.item = contents;
        this.ListFiltered = contents;
        this.context =con;
        this.activity =act;
        func = new Functions(con);
    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ListFiltered = item;
                } else {
                    List<itemSymbols> filteredList = new ArrayList<>();
                    for (itemSymbols row : item) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    ListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // ListFiltered = (ArrayList<itemSymbols>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return ListFiltered.size();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ticker_to_alarm, parent, false);

        return new Adapter_ticker_to_alarm.ContactViewHolder(view) {
        };
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView textTicker;

        protected RelativeLayout linear;
        public ContactViewHolder(View v) {
            super(v);
            textTicker =  v.findViewById(R.id.textticker);

            textTicker.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent in=new Intent(context,Single_Env.class);
//            in.putExtra("myitem" , item.get(getAdapterPosition()));
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(in);
            getTickerInfo(textTicker.getText().toString().trim());
        }

        public void getTickerInfo(String ticker){
            itemTicker it = new itemTicker();
            Map<String, String> params = new HashMap<String, String>();
            params.put("ticker", ticker);
            Call<itemTicker> call = RetrofitClient.getInstance().getMyApi().getTicker(params);

            call.enqueue(new Callback<itemTicker>() {
                @Override
                public void onResponse(Call<itemTicker> call, retrofit2.Response<itemTicker> response) {

                    Log.e("Login Request" ,  response.toString());

                    it.id = response.body().id;
                    it.ticker = response.body().ticker;
                    it.symbol = response.body().symbol;
                    it.name = response.body().name;
                    it.price = response.body().price;

                    Intent in=new Intent(context, Activity_ticker.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("item",  (Serializable) it);
                    context.startActivity(in);
                }

                @Override
                public void onFailure(Call<itemTicker> call, Throwable t) {

                }

            });
        }
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub

        try {
            itemSymbols ie = ListFiltered.get(arg1);
            arg0.textTicker.setText(ie.name);

        }
        catch (Exception e){

        }
    }

    public void updateList(List<itemSymbols> data) {
        item = data;
        notifyDataSetChanged();
    }


}


