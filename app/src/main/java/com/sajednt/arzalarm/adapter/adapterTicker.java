package com.sajednt.arzalarm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.Activity_plan;
import com.sajednt.arzalarm.Activity_ticker;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemTicker;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class adapterTicker extends RecyclerView.Adapter<adapterTicker.ContactViewHolder> implements Filterable {

    private List<itemTicker> item;
    public static  Context context;
    private int lastPosition = -1;
    AppCompatActivity activity;
    NumberFormat formatter;
    DecimalFormat df = new DecimalFormat("#");
    private List<itemTicker> ListFiltered;
    Functions func;
    public adapterTicker(List<itemTicker> contents , Context con , AppCompatActivity act ) {
        this.item = contents;
        this.context =con;
        this.activity =act;
        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        df.setMaximumFractionDigits(8);
        func = new Functions(context);

    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ticker, parent, false);

        return new adapterTicker.ContactViewHolder(view) {
        };
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
                    List<itemTicker> filteredList = new ArrayList<>();
                    for (itemTicker row : item) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
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
                ListFiltered = (ArrayList<itemTicker>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView name;
        protected TextView price;
        protected TextView diffrent;
        protected TextView difper , symbol;
        protected LinearLayout linear;
        public ContactViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.textName);
            price = (TextView)  v.findViewById(R.id.textPrice);
            diffrent = (TextView)  v.findViewById(R.id.textDifference);
            difper = (TextView)  v.findViewById(R.id.textDifPer);
            symbol = (TextView)  v.findViewById(R.id.textsymbol);
            linear = v.findViewById(R.id.maintikcerlayout);
            linear.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {

            if(func.getDataInt("planstatus") == 1) {
                itemTicker ic = item.get(getAdapterPosition());
                Intent in = new Intent(context, Activity_ticker.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("item", (Serializable) ic);
                context.startActivity(in);
            }
            else{
                new AlertDialog.Builder(context)
                        .setTitle("اتمام اعتبار حساب")
                        .setMessage(func.getDataString("message"))
                        .setCancelable(false)
                        .setPositiveButton("خرید بسته", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i2 = new Intent(context, Activity_plan.class);
                                context.startActivity(i2);                        //close drawer
                            }
                        }).show();
            }

        }
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        final itemTicker ie = item.get(arg1);

        arg0.name.setText(ie.name);
        arg0.symbol.setText(ie.symbol);

        arg0.price.setText(df.format(ie.price));
        Double dif = ie.price - ie.open;
        Double difper = Double.valueOf(0);
         if(ie.price < ie.open){

             arg0.diffrent.setText( formatter.format(dif));

             difper = ((ie.price/ie.open)-1)*100;

             arg0.difper.setText( formatter.format(difper)+"%");

             arg0.difper.setBackgroundResource(R.drawable.radius_red);
             arg0.diffrent.setTextColor(context.getResources().getColor(R.color.red));

         }
        else if(ie.price > ie.open){

             arg0.diffrent.setText(formatter.format(dif));

             difper = ((ie.open/ie.price)-1)*100;
             arg0.difper.setText( (formatter.format(difper)+"%").replace("-",""));


             arg0.difper.setBackgroundResource(R.drawable.radius_green);

             arg0.diffrent.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if( ie.price == ie.open){

             arg0.diffrent.setText(formatter.format(dif));


             arg0.diffrent.setTextColor(context.getResources().getColor(R.color.white));
             arg0.difper.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    public void updateList(List<itemTicker> data) {
        item = data;
        notifyDataSetChanged();

    }


}


