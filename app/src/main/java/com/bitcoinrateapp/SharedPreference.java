//package com.bitcoinrateapp;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import com.google.gson.Gson;
//
///**
// * Created by natalie on 10/01/18.
// */
//
//
//public class SharedPreference {
//
//    public static final String PREFS_NAME = "CRYPTO_APP";
//    public static final String RATES = "Rates";
//
//    public SharedPreference() {
//        super();
//    }
//
//    // This four methods are used for maintaining favorites.
//    public void saveRates(Context context, List<Double> rates) {
//        SharedPreferences settings;
//        Editor editor;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        editor = settings.edit();
//
//        Gson gson = new Gson();
//        String jsonFavorites = gson.toJson(rates);
//
//        editor.putString(RATES, jsonFavorites);
//
//        editor.commit();
//    }
//
//    public void addFavorite(Context context, Double rate) {
//        List<Double> rates = getRates(context);
//        if (rates == null)
//            rates = new ArrayList<Coin>();
//        rates.add(rate);
//        saveRates(context, rates);
//    }
//
//    public void removeFavorite(Context context, Coin product) {
//        ArrayList<Coin> favorites = getRates(context);
//        if (favorites != null) {
//            favorites.remove(product);
//            saveRates(context, favorites);
//        }
//    }
//
//    public ArrayList<Coin> getRates(Context context) {
//        SharedPreferences settings;
//        List<Coin> favorites;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//
//        if (settings.contains(RATES)) {
//            String jsonFavorites = settings.getString(RATES, null);
//            Gson gson = new Gson();
//            Coin[] favoriteItems = gson.fromJson(jsonFavorites,
//                    Coin[].class);
//
//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<Double>(rates);
//        } else
//            return null;
//
//        return (ArrayList<Double>) rates;
//    }
//}
