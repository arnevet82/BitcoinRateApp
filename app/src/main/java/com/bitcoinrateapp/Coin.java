package com.bitcoinrateapp;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by natalie on 08/01/18.
 */

public class Coin {
    double oldRate = 0;
    double newRate = 0;
    double closingRate = 0;
    TextView textView;
    ImageView arrow;
    String name;

    public Coin(TextView textView, double newRate, ImageView arrow, String name) {
        this.textView = textView;
        this.newRate = newRate;
        this.arrow = arrow;
        this.name = name;
    }
}
