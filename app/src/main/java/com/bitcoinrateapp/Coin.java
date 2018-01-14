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
    String state;
    double changePercent;

    public Coin(TextView textView, double oldRate, double newRate, ImageView arrow, String name, double changePercent) {
        this.textView = textView;
        this.oldRate = oldRate;
        this.newRate = newRate;
        this.arrow = arrow;
        this.name = name;
        this.changePercent = changePercent;
    }
}
