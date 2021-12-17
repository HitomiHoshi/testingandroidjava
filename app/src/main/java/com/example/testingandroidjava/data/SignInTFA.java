package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInTFA {
    public Boolean tFAEnabled;
    public Integer userId;

    public SignInTFA(JSONObject result) throws JSONException {

        userId = result.getInt("UserId");
        tFAEnabled = result.getBoolean("TFAEnabled");
    }
}
