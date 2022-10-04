package com.sajednt.arzalarm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemDevice;
import com.sajednt.arzalarm.item.itemResponse;

import java.text.NumberFormat;
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
public class AdapterDevices extends RecyclerView.Adapter<AdapterDevices.ContactViewHolder> {

    private List<itemDevice> item;
    public static  Context context;
    AppCompatActivity activity;
    NumberFormat formatter;
    Functions func;
    public AdapterDevices(List<itemDevice> contents , Context con , AppCompatActivity act ) {
        this.item = contents;
        this.context =con;
        this.activity =act;
        func = new Functions(con);
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
                .inflate(R.layout.list_item_devices, parent, false);

        return new AdapterDevices.ContactViewHolder(view) {
        };
    }


    public  class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView devicename;
        protected TextView lastvisit;
        protected TextView currentDevice;
        protected ImageView imgdelete;
        public ContactViewHolder(View v) {
            super(v);
            devicename =  (TextView) v.findViewById(R.id.textDevicename);
            lastvisit = (TextView)  v.findViewById(R.id.textDevicvisit);
            currentDevice = (TextView)  v.findViewById(R.id.textDevicecurrent);
            imgdelete = v.findViewById(R.id.imageDelete);
            imgdelete.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {

            itemDevice ic = item.get(getAdapterPosition());

            deleteDevice(ic);

        }

        public void deleteDevice(itemDevice id){



            Map<String, String> params = new HashMap<String, String>();
            params.put("id", Integer.toString(id.id));

            Call<itemResponse> call = RetrofitClient.getInstance().getMyApi().deleteDevice(params);
            call.enqueue(new Callback<itemResponse>() {
                @Override
                public void onResponse(Call<itemResponse> call, retrofit2.Response<itemResponse> response) {

                    try {
                        Log.e("Repsone" ,  response.toString());
                        if(response.body().success==1){
                            Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            item.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }
                    catch (Exception e){
                    }
                }

                @Override
                public void onFailure(Call<itemResponse> call, Throwable t) {

                    Log.e("delete device" , t.toString());

                }

            });

        }
    }



    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        final itemDevice ie = item.get(arg1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

        arg0.devicename.setText(ie.devicename);

        Date visitDate;

        try {
            visitDate = format.parse(ie.lastvisit);
            PersianDate pdate = new PersianDate(visitDate);
            PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d H:i:s");
            arg0.lastvisit.setText(pdformater1.format(pdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(ie.deviceid.equals(func.getUniquePsuedoID())){
            arg0.currentDevice.setVisibility(View.VISIBLE);
            arg0.imgdelete.setVisibility(View.GONE);
        }


    }

    public void updateList(List<itemDevice> data) {
        item = data;
        notifyDataSetChanged();
    }


}


