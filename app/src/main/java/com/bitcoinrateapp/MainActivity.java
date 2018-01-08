package com.bitcoinrateapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    Coin bitcoin, ethereum, ripple, litecoin, bitcoinCash;
    TextView mTextView,eTextView,rTextView, lTextView, bTextView;
    ImageView bitcoinArrow, etherArrow, rippleArrow, litecoinArrow, bcashArrow;
    public static double closingBitcoin, newBitcoin, oldBitcoin;
    public static double closingEthereum, newEthereumn;
    public Coin[] coins;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getRates();
        SetCheckRatesAlarm();
        SetAlarm();
    }


    public void getRates(){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://api.coindesk.com/v1/bpi/currentprice.json";
        String url ="https://api.coinmarketcap.com/v1/ticker/?limit=10";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Locale us = new Locale("en", "US");
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(us);
                        int []indices = {0, 1, 2, 7, 3};

                        try {

                            for(int i = 0; i < coins.length; i++){
                                JSONArray jsonarray = new JSONArray(response);
                                JSONObject obj = jsonarray.getJSONObject(indices[i]);
                                String stringRate = obj.getString("price_usd");
                                double bitRate = Double.parseDouble(stringRate);
                                Coin coin = coins[i];
                                coin.oldRate = coin.newRate;
                                coin.newRate = bitRate;
                                coin.textView.setText(formatter.format(bitRate));
                                coin.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                for(Coin coin: coins){
                    coin.textView.setText("Please check internet your connection!");
                    coin.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                }
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        getRates();
    }

    public void sendNotification(String coin){

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);


        NotificationManager notificationManager = (NotificationManager) MainActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(MainActivity.this)
                .setContentTitle("Change in " + coin + " rates!!!")
                .setContentText("Click to see the curent rates!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(intent);



        Notification notificationn = notification.getNotification();
        assert notificationManager != null;
        notificationManager.notify(1, notificationn);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetAlarm()
    {
         // replace with a button from your own UI
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive( Context context, Intent _ ) {

                try {Thread.sleep(1000);}catch(InterruptedException ex) {Thread.currentThread().interrupt();}

                getRates();

                for(Coin coin: coins){
                    if(coin.newRate < coin.oldRate){
                        coin.arrow.setImageResource(R.drawable.down_arrow);
                    }else if (coin.newRate > coin.oldRate){
                        coin.arrow.setImageResource(R.drawable.up_arrow);
                    }else{

                    }

                    if(coin.closingRate != 0){
                        if(coin.newRate*0.999 > coin.closingRate || coin.newRate < coin.closingRate *0.999){
                            sendNotification(coin.name);
                        }
                    }
                }
                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.blah.somemessage"), 0 );
        AlarmManager manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        Locale aLocale = new Locale.Builder().setLanguage("iw").setRegion("IL").build();
        Calendar calendar = Calendar.getInstance(aLocale);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 50);

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        assert manager != null;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60, pintent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetCheckRatesAlarm()
    {
        // replace with a button from your own UI
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive( Context context, Intent _ ) {
                getRates();
                for(Coin coin: coins){
                    coin.closingRate = coin.newRate;
                }

                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.blah.bloo.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.bloo.somemessage"), 0 );
        AlarmManager alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        Locale aLocale = new Locale.Builder().setLanguage("iw").setRegion("IL").build();
        Calendar calendar = Calendar.getInstance(aLocale);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        assert alarmManager != null;
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pintent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pintent);
    }


    public void init(){

        mTextView = (TextView) findViewById(R.id.rate);
        eTextView = (TextView) findViewById(R.id.e_rate);
        rTextView = (TextView) findViewById(R.id.r_rate);
        lTextView = (TextView) findViewById(R.id.l_rate);
        bTextView = (TextView) findViewById(R.id.bcach_rate);

        bitcoinArrow = (ImageView)findViewById(R.id.bitcoin_arrow);
        etherArrow = (ImageView)findViewById(R.id.ether_arrow);
        rippleArrow = (ImageView)findViewById(R.id.ripple_arrow);
        litecoinArrow = (ImageView)findViewById(R.id.lite_arrow);
        bcashArrow = (ImageView)findViewById(R.id.bcash_arrow);

        bitcoin = new Coin(mTextView, 0, bitcoinArrow,"Bitcoin");
        ethereum = new Coin(eTextView, 0, etherArrow,"Ethereum");
        ripple = new Coin(rTextView, 0, rippleArrow,"Ripple");
        litecoin = new Coin(lTextView, 0, litecoinArrow,"Litecoin");
        bitcoinCash = new Coin(bTextView, 0, bcashArrow,"Bitcoin Cash");

        coins = new Coin[]{bitcoin, ethereum, ripple, litecoin, bitcoinCash};

    }


}