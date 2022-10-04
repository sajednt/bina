package com.sajednt.arzalarm.user;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.arzalarm.R;
import com.sajednt.arzalarm.adapter.Adapter_ticker_to_alarm;
import com.sajednt.arzalarm.connection.RetrofitClient;
import com.sajednt.arzalarm.functions.Functions;
import com.sajednt.arzalarm.item.itemSymbols;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Activity_ticker_to_alarm extends AppCompatActivity {


    List<itemSymbols> symbolsItem;
    LinearLayoutManager layoutManager;
    private RecyclerView recyclerSymbols;
    Adapter_ticker_to_alarm rv;
    Functions func;
    AVLoadingIndicatorView av;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_to_alarm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        func = new Functions(getApplicationContext());
        recyclerSymbols = findViewById(R.id.recycler_tickers);
        av= findViewById(R.id.indicator_symbols);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSymbols.setLayoutManager(layoutManager);
        recyclerSymbols.setHasFixedSize(true);

        symbolsItem = new ArrayList<itemSymbols>();
        rv = new Adapter_ticker_to_alarm( symbolsItem , getApplicationContext(), Activity_ticker_to_alarm.this );
        recyclerSymbols.swapAdapter(rv, false);

        getSymbols();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticker_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        //searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rv.getFilter().filter(query);
//                rv.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rv.getFilter().filter(newText);
//                rv.filter(newText);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSymbols(){

        Call<List<itemSymbols>> call = RetrofitClient.getInstance().getMyApi().getSymbols();
        call.enqueue(new Callback<List<itemSymbols>>() {
            @Override
            public void onResponse(Call<List<itemSymbols>> call, retrofit2.Response<List<itemSymbols>> response) {

                Log.e("Repsone" ,  response.toString());
                List<itemSymbols> myheroList = response.body();
                for (int i = 0; i < myheroList.size(); i++) {
                    if(func.checkExistTicker(myheroList.get(i).name)){
                        symbolsItem.add(0,myheroList.get(i));
                    }
                    else{
                        symbolsItem.add(myheroList.get(i));
                    }
                }

                rv.updateList(symbolsItem);
                rv.notifyDataSetChanged(); // let your adapter know about the changes and reload view.
                av.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<itemSymbols>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }
}