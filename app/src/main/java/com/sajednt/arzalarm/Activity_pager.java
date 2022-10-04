package com.sajednt.arzalarm;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;
import com.pushwoosh.Pushwoosh;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sajednt.arzalarm.adapter.MyPagerAdapter;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemUserLogin;
import com.sajednt.arzalarm.item.item_user_info;
import com.sajednt.arzalarm.user.Activity_contact;
import com.sajednt.arzalarm.user.Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.pushe.plus.Pushe;
import co.pushe.plus.analytics.PusheAnalytics;
import co.pushe.plus.analytics.event.Event;
import co.pushe.plus.analytics.event.EventAction;
import okhttp3.CacheControl;
import retrofit2.Call;
import retrofit2.Callback;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class Activity_pager extends AppCompatActivity {


    private Toolbar mToolbar;
    MyPagerAdapter pagerAdapter;
    ViewPager viewpager;
    BottomBar bottomBar;
    Functions func;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    TextView userName,userEmail , expdate, planname;
    Boolean loaduserdate = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        func = new Functions(getApplicationContext());



        PusheAnalytics analytics = Pushe.getPusheService(PusheAnalytics.class);
        if (analytics != null) {
            String name = "ورود به برنامه";
            Map<String, Object> map = new HashMap<>();
            map.put("userid", Integer.toString(func.getDataInt("userid")));
            map.put("event", "Activity pager opened");
            analytics.sendEvent(
                    new Event.Builder(name)
                            .setAction(EventAction.ACHIEVEMENT)
                            .setData(map)
                            .build()
            );
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        if(getIntent().hasExtra("message")) {
//            String message = getIntent().getExtras().getString("message");
//            if (!message.isEmpty()) {
//                new AlertDialog.Builder(this)
//                        .setTitle("حساب شما فعال شد")
//                        .setMessage(message)
//                        .setCancelable(false)
//                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//            }
//        }
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},
                1);
        navigationView = findViewById(R.id.navview);
        drawerLayout = findViewById(R.id.drawer);
        bottomBar = findViewById(R.id.bottomBar);
        mToolbar =  findViewById(R.id.toolbar);
        viewpager = findViewById(R.id.viewpager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout,mToolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        userName = navigationView.getHeaderView(0).findViewById(R.id.textname);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.textemail);
        expdate = navigationView.getHeaderView(0).findViewById(R.id.textExpdate);
        planname = navigationView.getHeaderView(0).findViewById(R.id.textplanname);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.setCurrentItem(1);
        bottomBar.selectTabAtPosition(1);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "iranyekan.ttf");
        bottomBar.setTabTitleTypeface(font);

        settoken();
//        addCondition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Activity_pager.this, Activity_condition.class);
//                startActivity(in);
//            }
//        });
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//               // startActivityForResult(intent, 0);
//                SharedPreferences settings = getSharedPreferences("prefID", Context.MODE_PRIVATE);
//                settings.edit().clear().commit();
//                Intent i = new Intent(Activity_pager.this, Login.class);
//                startActivity(i);
//                Activity_pager.this.finish();
//
//               // String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//                //Toast.makeText(Activity_pager.this, android.os.Build.MANUFACTURER +' ' + android.os.Build.MODEL, Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(AlarmClock.ACTION_SNOOZE_ALARM);
////
////                intent.putExtra(AlarmClock.EXTRA_HOUR, c.get(Calendar.HOUR_OF_DAY));
////                intent.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, "1000");
////                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Set alarm for morning walk");
////                startActivity(intent);
//
//
//            }
//        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_market) {
                    viewpager.setCurrentItem(1);
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

                }
                else if (tabId == R.id.tab_conditions) {
                    viewpager.setCurrentItem(0);
                    getSupportActionBar().setTitle("لیست آلارم ها");

                }
            }
        });
        Typeface typeface = ResourcesCompat.getFont(this, R.font.iranyekan);
        Resources res = this.getResources();

        if(getIntent().hasExtra("register")) {


        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.tab_market), res.getString(R.string.target_tickerlist_title), res.getString(R.string.target_tickerlist_desc))
                         .outerCircleAlpha(0.8f)
                         .transparentTarget(true)
                         .targetRadius(60)
                         .textTypeface(typeface)
                         .titleTextSize(16)
                         .descriptionTextSize(12),

                new TapTargetView.Listener() {
                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        super.onTargetDismissed(view, userInitiated);
                        viewpager.setCurrentItem(0);
                        bottomBar.selectTabAtPosition(0);

                        TapTargetView.showFor(Activity_pager.this,
                                TapTarget.forView(findViewById(R.id.tab_conditions), res.getString(R.string.target_alarmlist_title), res.getString(R.string.target_alarmlist_desc))
                                        .outerCircleAlpha(0.8f)
                                        .transparentTarget(true)
                                        .targetRadius(60)
                                        .textTypeface(typeface)
                                        .titleTextSize(16)
                                        .descriptionTextSize(12), new TapTargetView.Listener(){

                                    @Override
                                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                                        super.onTargetDismissed(view, userInitiated);
                                        func.saveDataInt("help",1);
                                    }
                                });
                    }
                });

        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.fab_plans:
                        Intent i2 = new Intent(Activity_pager.this, Activity_plan.class);
                        startActivity(i2);                        //close drawer
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.fab_devices:
                        Intent i1 = new Intent(Activity_pager.this, Activity_devices.class);
                        startActivity(i1);                        //close drawer
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.fab_logout:

                        SharedPreferences settings = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        Intent i = new Intent(Activity_pager.this, Login.class);
                        startActivity(i);
                        Activity_pager.this.finish();
                        break;

                    case R.id.fab_contact:

                        Intent i11 = new Intent(Activity_pager.this, Activity_contact.class);
                        startActivity(i11);                        //close drawer
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        if(item.getItemId() == R.id.menuTikcers){
//            Intent in = new Intent(MainActivity.this , Activity_symbols.class);
//            startActivity(in);
//        }
        if(item.getItemId() == android.R.id.home){
//            if(srn.isMenuClosed()){ srn.openMenu(); }
//            else { srn.closeMenu(); }
        }



        return super.onOptionsItemSelected(item);
    }

    public void settoken(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", Integer.toString(func.getDataInt("userid")));
        params.put("token", Pushwoosh.getInstance().getHwid());
        params.put("deviceid", func.getUniquePsuedoID());


        Call<itemUserLogin> call = RetrofitClient.getInstance().getMyApi().userSetToken(params);
        call.enqueue(new Callback<itemUserLogin>() {
            @Override
            public void onResponse(Call<itemUserLogin> call, retrofit2.Response<itemUserLogin> response) {
                Log.e("Token Request" ,  response.toString());

                if(response.body().success==2){
                    SharedPreferences settings = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    Intent i = new Intent(Activity_pager.this, Login.class);
                    startActivity(i);
                    Activity_pager.this.finish();
                }


            }

            @Override
            public void onFailure(Call<itemUserLogin> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loaduserdate)
        setUserInfo();

        loaduserdate= false;


    }

    public void setUserInfo() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Integer.toString(func.getDataInt("userid")));
                // Toast.makeText(this, Integer.toString(func.getDataInt("userid")), Toast.LENGTH_SHORT).show();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;

                Call<item_user_info> call = RetrofitClient.getInstance().getMyApi().getuserinfo(params);
                call.enqueue(new Callback<item_user_info>() {
                    @Override
                    public void onResponse(Call<item_user_info> call, retrofit2.Response<item_user_info> response) {


                        if (response.body().success == 1) {

                            userName.setText(response.body().name);
                            userEmail.setText(response.body().email);


                            func.saveDataInt("planstatus", response.body().planstatus);
                            func.saveDataString("message", response.body().message);
                            if (response.body().planstatus == 1) {

                                try {
                                    Date date = format.parse(response.body().expdate);
                                    PersianDate pdate = new PersianDate(date);
                                    PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");

                                    expdate.setText(pdformater1.format(pdate));

                                } catch (ParseException e) {
                                    e.printStackTrace();

                                }
                                planname.setText(response.body().planname);
                                func.saveDataInt("planstatus", 1);
                                loaduserdate = true;
                            } else {
                                func.saveDataInt("planstatus", 0);

                                new AlertDialog.Builder(Activity_pager.this)
                                        .setTitle("اتمام اعتبار حساب*")
                                        .setMessage(response.body().message)
                                        .setCancelable(true)
                                        .setPositiveButton("خرید بسته", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent i2 = new Intent(Activity_pager.this, Activity_plan.class);
                                                startActivity(i2);                        //close drawer
                                                drawerLayout.closeDrawer(GravityCompat.START);
                                            }
                                        }).show();
                                loaduserdate = true;

                            }
                        } else if (response.body().success == 0) {

                        }


                    }

                    @Override
                    public void onFailure(Call<item_user_info> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured user info", Toast.LENGTH_LONG).show();
                    }

                });

            }
        }, 1000);
    }
}
