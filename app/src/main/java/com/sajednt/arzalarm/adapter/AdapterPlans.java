package com.sajednt.arzalarm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemPlan;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.pushe.plus.Pushe;
import co.pushe.plus.analytics.PusheAnalytics;
import co.pushe.plus.analytics.event.Event;
import co.pushe.plus.analytics.event.EventAction;

@SuppressWarnings("unchecked")
public class AdapterPlans extends RecyclerView.Adapter<AdapterPlans.ContactViewHolder> {

    private List<itemPlan> item;
    public static  Context context;
    AppCompatActivity activity;
    NumberFormat formatter;
    Functions func;
    private ItemClickListener buyclickListener;
    private ItemClickListener reserveclickListener;

    public AdapterPlans(List<itemPlan> contents , Context con , AppCompatActivity act ) {
        this.item = contents;
        this.context =con;
        this.activity =act;
        func = new Functions(con);
    }

    public void setBuyClickListener(ItemClickListener itemClickListener) {
        this.buyclickListener = itemClickListener;
    }

    public void setReserveClickListener(ItemClickListener itemClickListener) {
        this.reserveclickListener = itemClickListener;
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
                .inflate(R.layout.list_item_plan2, parent, false);

        return new AdapterPlans.ContactViewHolder(view) {
        };
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView name;
        protected TextView days;
        protected TextView alarmcount;
        protected TextView regularPrice;
        protected Button btnselect, btnreserve;
        LinearLayout textcontainer , cornerback , maincorner;
        public ContactViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.textplanename);
            days = (TextView)  v.findViewById(R.id.textmodat);
            alarmcount = (TextView)  v.findViewById(R.id.textalarmcount);
            regularPrice = v.findViewById(R.id.textalarmprice);
            btnselect = v.findViewById(R.id.buttonselect);
            btnreserve = v.findViewById(R.id.buttonreserve);
            if(func.getDataInt("planstatus")!=1){
                btnreserve.setEnabled(false);
            }
            btnselect.setOnClickListener(this);
            btnreserve.setOnClickListener(this);
            textcontainer = v.findViewById(R.id.textlayout);
            cornerback = v.findViewById(R.id.cornerback);
            maincorner = v.findViewById(R.id.awdawdawf);
        }

        @Override
        public void onClick(View v) {
            itemPlan ic = item.get(getAdapterPosition());
            int userid = func.getDataInt("userid");
            if(v.getId() == R.id.buttonselect){

                PusheAnalytics analytics = Pushe.getPusheService(PusheAnalytics.class);
                if (analytics != null) {
                    String name = "buy button clicked";
                    Map<String, Object> map = new HashMap<>();
                    map.put("userid", Integer.toString(func.getDataInt("userid")));
                    map.put("event", "buy button clicked");
                    analytics.sendEvent(
                            new Event.Builder(name)
                                    .setAction(EventAction.ACHIEVEMENT)
                                    .setData(map)
                                    .build()
                    );
                }

                if(func.getDataInt("planstatus")==1) {
                    new AlertDialog.Builder(activity)
                            .setTitle("توجه")
                            .setMessage("حساب کاربری شما دارای اعتبار می باشد ، در صورت خرید بسته جدید ، بسته قبلی شما حذف می گردد")
                            .setCancelable(false)
                            .setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .setPositiveButton("خرید بسته", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    String url = "https://arzalarm.com/bina/package/pay/buy/buy.php?userid="+ userid +"&planid="+ic.id;
//                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url+"&buy=1"));
//                                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(browserIntent);
                                     if (buyclickListener != null) buyclickListener.onClick(v, getAdapterPosition() , Integer.toString(ic.id));

                                }
                            }).show();
                }
                else {
//                    String url = "https://arzalarm.com/bina/package/pay/buy/buy.php?userid="+ userid +"&planid="+ic.id;
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(browserIntent);
                    if (buyclickListener != null) buyclickListener.onClick(v, getAdapterPosition(),Integer.toString(ic.id));

                }
            }
            else if(v.getId() == R.id.buttonreserve){

                PusheAnalytics analytics = Pushe.getPusheService(PusheAnalytics.class);
                if (analytics != null) {
                    String name = "reserve button clicked";
                    Map<String, Object> map = new HashMap<>();
                    map.put("userid", Integer.toString(func.getDataInt("userid")));
                    map.put("event", "reserve button clicked");
                    analytics.sendEvent(
                            new Event.Builder(name)
                                    .setAction(EventAction.ACHIEVEMENT)
                                    .setData(map)
                                    .build()
                    );
                }
                new AlertDialog.Builder(activity)
                        .setTitle("توجه")
                        .setMessage("در صورت رزرو بسته ، تاریخ فعال شدن بسته انتخابی به صورت خودکار به بعد از تمام شدن بسته فعلی شما انتقال داده می شود")
                        .setCancelable(false)
                        .setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .setPositiveButton("خرید بسته", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                String url = "https://arzalarm.com/bina/package/pay/reserve/reserve.php?userid="+ userid +"&planid="+ic.id;
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(browserIntent);
                                if (reserveclickListener != null) reserveclickListener.onClick(v, getAdapterPosition() ,Integer.toString(ic.id));

                            }
                        }).show();
            }
        }
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        final itemPlan ie = item.get(arg1);
        arg0.name.setText(ie.name);
        arg0.days.setText(ie.days + " روز");
        arg0.alarmcount.setText(ie.alarmcount);
        arg0.regularPrice.setText(ie.regular_price+ " تومان");

        arg0.textcontainer.post(new Runnable() {
            @Override
            public void run() {
                int height = arg0.textcontainer.getHeight();
                ViewGroup.LayoutParams params = arg0.maincorner.getLayoutParams();
                ViewGroup.LayoutParams params1 = arg0.cornerback.getLayoutParams();
                params.height = height+50;
                params1.height = height+50;
                arg0.cornerback.setLayoutParams(params1);
                arg0.maincorner.setLayoutParams(params);

            }
        });

    }

    public void updateList(List<itemPlan> data) {
        item = data;
        notifyDataSetChanged();
    }


}


