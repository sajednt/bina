package com.sajednt.arzalarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemSymbols;

import java.util.ArrayList;
import java.util.List;

public class AdapterSymbols extends RecyclerView.Adapter<AdapterSymbols.ContactViewHolder> implements Filterable {

    private List<itemSymbols> item;
    public static  Context context;
    AppCompatActivity activity;
    Functions func;
    private List<itemSymbols> ListFiltered;

    public AdapterSymbols(List<itemSymbols> contents , Context con , AppCompatActivity act ) {
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
                .inflate(R.layout.list_item_symbol, parent, false);

        return new AdapterSymbols.ContactViewHolder(view) {
        };
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected CheckBox checkBox;

        protected RelativeLayout linear;
        public ContactViewHolder(View v) {
            super(v);
            checkBox =  v.findViewById(R.id.checkSymbol);
        }

        @Override
        public void onClick(View v) {
//            Intent in=new Intent(context,Single_Env.class);
//            in.putExtra("myitem" , item.get(getAdapterPosition()));
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(in);
        }
    }


    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub

        try {


        itemSymbols ie = ListFiltered.get(arg1);

        arg0.checkBox.setText(ie.name);
        if(func.checkExistTicker(ie.name)){
            arg0.checkBox.setChecked(true);
        }
        else {
            arg0.checkBox.setChecked(false);
        }

        arg0.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean b = arg0.checkBox.isChecked();
                if(b){
                    func.addToTickers(ie.name);
                }
                else {
                    func.removeFromTickers(ie.name);
                }
            }
        });
        }
        catch (Exception e){

        }
//        arg0.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if(b){
//                    func.addToTickers(ie.name);
//                }
//                else {
//                    func.removeFromTickers(ie.name);
//                }
//            }
//        });


    }

    public void updateList(List<itemSymbols> data) {
        item = data;
        notifyDataSetChanged();
    }


}


