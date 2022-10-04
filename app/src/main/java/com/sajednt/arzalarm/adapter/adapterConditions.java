package com.sajednt.arzalarm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.Activity_plan;
import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.condition.Edit_condition_price;
import com.sajednt.arzalarm.condition.Edit_condition_timeframe;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.fragment.fragment_conditions;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemCondition;
import com.sajednt.arzalarm.item.itemResponse;
import com.wang.avi.AVLoadingIndicatorView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

@SuppressWarnings("unchecked")
public class adapterConditions extends RecyclerView.Adapter<adapterConditions.ContactViewHolder> {

    private List<itemCondition> item;
    public static  Context context;
    private int lastPosition = -1;
    AppCompatActivity activity;
    com.sajednt.arzalarm.fragment.fragment_conditions fragment_conditions;
    Functions func;
    public adapterConditions(List<itemCondition> contents , Context con , AppCompatActivity act , fragment_conditions fc ) {
        this.item = contents;
        this.context =con;
        this.activity =act;
        this.fragment_conditions = fc;
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
                .inflate(R.layout.list_item_conditions2, parent, false);

        return new adapterConditions.ContactViewHolder(view) {
        };
    }



    public  class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView  symbol , name , price , modifydate, logtext, timeframe;
        AVLoadingIndicatorView avstatus;
        Switch Switch;
        LinearLayout ll, btnlog;
        ExpandableLayout logLayout;

        public ContactViewHolder(View v) {
            super(v);
            symbol =  (TextView) v.findViewById(R.id.textsymbol);
            name =  (TextView) v.findViewById(R.id.textName);
            price =  (TextView) v.findViewById(R.id.textprice);
            modifydate =  (TextView) v.findViewById(R.id.textmodifydate);
            avstatus = v.findViewById(R.id.indicator_symbols2);
            Switch = v.findViewById(R.id.switch1);
            timeframe = v.findViewById(R.id.texttimeframe);

            ll = v.findViewById(R.id.linearclick);
            btnlog = v.findViewById(R.id.btnlog);
            logLayout = v.findViewById(R.id.expandable_layout);
            logtext = v.findViewById(R.id.logtext);
            ll.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {

            itemCondition ic = item.get(getAdapterPosition());

            if(ic.timeframe.equals("now")){
                Intent in=new Intent(context, Edit_condition_price.class);
                in.putExtra("item",  (Serializable) ic);

                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
            else{
                Intent in=new Intent(context, Edit_condition_timeframe.class);
                in.putExtra("item",  (Serializable) ic);

                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }




        }
    }


    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        final itemCondition ie = item.get(arg1);
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

        try {
            date = format.parse(ie.dateModify);
            PersianDate pdate = new PersianDate(date);
            PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d H:i:s");

            arg0.modifydate.setText(pdformater1.format(pdate));

        } catch (ParseException e) {
            e.printStackTrace();

        }
        arg0.symbol.setText(ie.ticker);
        arg0.name.setText(ie.ticker);
        arg0.price.setText(ie.price);

        if(ie.status > 0){
            arg0.avstatus.setVisibility(View.VISIBLE);
            arg0.avstatus.setIndicatorColor(context.getResources().getColor(R.color.green));
            arg0.Switch.setChecked(true);
        }
        else{

            arg0.avstatus.setIndicatorColor(context.getResources().getColor(R.color.red));
            arg0.Switch.setChecked(false);
            arg0.avstatus.setVisibility(View.GONE);

        }

        if(ie.timeframe.equals("now")){
            arg0.timeframe.setText("قیمت لحظه ای");

        }
        else{
            arg0.timeframe.setText("دوره زمانی "+ ie.timeframe);
        }

        String logcondition = "";

        for (itemCondition.itemLog il : ie.log) {
            Date logdate = new Date();

            try {
                logdate = format.parse(il.date);
                PersianDate pdate = new PersianDate(logdate);
                PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d H:i:s");

               // arg0.modifydate.setText(pdformater1.format(pdate));
                logcondition += "\n" + pdformater1.format(pdate);

            } catch (ParseException e) {
                e.printStackTrace();

            }

            logcondition += "\n" + il.message;
            logcondition += " | قیمت : " + il.price;
            logcondition += "\n";
        }

        arg0.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(func.getDataInt("planstatus") == 1) {
                    int myInt = isChecked ? 1 : 0;
                    setStatus(ie, myInt);
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
        });

        arg0.logtext.setText(logcondition);
        arg0.btnlog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arg0.logLayout.toggle();
            }
        });

    }

    public void setStatus(itemCondition ic, int status){

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", Integer.toString(ic.id));
        params.put("status", Integer.toString(status));
        params.put("ticker", ic.ticker);
        params.put("price", ic.price);
        params.put("message", ic.message);

        Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().setConditionStatus(params);

        call.enqueue(new Callback<itemResponse>() {
            @Override
            public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                Log.e("Login Request" ,  response.toString());

                int success = response.body().success;
                fragment_conditions.getConditions();
                if(success == 1) {
                    if(status == 0) {
                        Toast.makeText(context, "آلارم غیرفعال شد", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "آلارم فعال شد", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<itemResponse> call, Throwable t) {

            }

        });

    }

    public void updateList(List<itemCondition> data) {
        item = data;
        notifyDataSetChanged();

    }


}


