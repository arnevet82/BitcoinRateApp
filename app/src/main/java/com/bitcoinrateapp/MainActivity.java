package com.bitcoinrateapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final TextView mTextView = (TextView) findViewById(R.id.rate);


// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.coindesk.com/v1/bpi/currentprice.json";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject object = jsonObject.getJSONObject("bpi");
                            JSONObject subObject = object.getJSONObject("USD");
                            double rate = subObject.getDouble("rate_float");
                            String formated_rate = String.format("%.2f", rate);
                            mTextView.setText(formated_rate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);



    }
}