package com.sajednt.arzalarm.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.EditText;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

    Context context;

    public Functions(Context context){
        this.context = context;
    }

    public String getTickers() {
        SharedPreferences sharedPref = context.getSharedPreferences("prefID",Context.MODE_PRIVATE);
        String Tickers = sharedPref.getString("TickersSymbol", "BTCUSDT|ETHUSDT|BNBUSDT|SOLUSDT|ADAUSDT|XRPUSDT|DOTUSDT|AVAXUSDT"); //the 2 argument return default value
        //Log.e("Tickers" ,  Tickers);

        return Tickers;
    }

    public void addToTickers(String ticker) {
        String savedTicker = getTickers();

        if(!savedTicker.contains(ticker)){
            savedTicker = savedTicker + "|" + ticker;
            SharedPreferences sharedPref = context.getSharedPreferences("prefID", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("TickersSymbol", savedTicker);
            editor.apply();
            editor.commit();
        }
        //Toast.makeText(context, savedTicker, Toast.LENGTH_SHORT).show();

    }

    public void removeFromTickers(String ticker) {
        String savedTicker = getTickers();

        if(savedTicker.contains(ticker)){

            if(savedTicker.contains(ticker+"|")){

                savedTicker = savedTicker.replace(ticker+"|", "");
            }
            else if(savedTicker.contains("|"+ticker)){

                savedTicker = savedTicker.replace("|"+ticker, "");

            }
            else if(savedTicker.contains(ticker)){
                savedTicker = savedTicker.replace(ticker, "");

            }

            SharedPreferences sharedPref = context.getSharedPreferences("prefID", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("TickersSymbol", savedTicker);
            editor.apply();
            editor.commit();
           // Toast.makeText(context, savedTicker, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkExistTicker(String ticker){
        String savedTicker = getTickers();
        if(savedTicker.contains(ticker)) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean editIsEmpty(EditText ed){
        if (ed.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public boolean isValidEmail(CharSequence target) {

        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(target);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPasswordvalid(EditText et1 , EditText et2){

        if(et1.getText().toString().trim().length() < 8){
            et1.setError("Enter at least 8 characters");
            return false;
        }
        else if(!et1.getText().toString().trim().equals(et2.getText().toString().trim())){
            et2.setError("Check password equality");
            return false;
        }
        else{
            return true;
        }

    }

    public void saveDataString(String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences("prefID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public void saveDataInt(String key, int value){
        SharedPreferences sharedPref = context.getSharedPreferences("prefID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    public String getDataString(String key){
        SharedPreferences sharedPref = context.getSharedPreferences("prefID",Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, ""); //the 2 argument return default value
        return value;
    }

    public int getDataInt(String key){
        SharedPreferences sharedPref = context.getSharedPreferences("prefID",Context.MODE_PRIVATE);
        int value = sharedPref.getInt(key, 0); //the 2 argument return default value
        return value;
    }

    public boolean userLogedin(){


        int loged = getDataInt("userid");
        if(loged==0){
            return false;
        }
        else {
            return true;
        }
    }

    public String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // https://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // https://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public void add_or_check_device(){




    }
}
