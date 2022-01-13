package com.example.testingandroidjava.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceService {
    private final Context context;

    public SharedPreferenceService(Context context) {
        this.context = context;
    }

    public void setJWTToken(String token) {
        SharedPreferences prefs = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwtToken", token);
        editor.apply();
    }

    public String getJWTToken() {
        SharedPreferences prefs = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        return prefs.getString("jwtToken", "");
    }
}
