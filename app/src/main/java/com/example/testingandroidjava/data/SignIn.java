package com.example.testingandroidjava.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignIn {
    public int id;
    public String email;
    public String username;
    public String jwtToken;
    public String refreshToken;
    public Integer accountId;
    public Boolean systemLock;
    public ArrayList<UserControllerController> userControllers;

    public SignIn(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        email = result.getString("Email");
        username = result.getString("Username");
        jwtToken = result.getString("JWTToken");
        refreshToken = result.getString("RefreshToken");
        accountId = result.optInt("AccountId");
        systemLock = result.getBoolean("SystemLock");

        userControllers = new ArrayList<>();
        JSONArray userControllerList = result.getJSONArray("UserControllers");

        for(int i = 0; i < userControllerList.length(); i++){
            userControllers.add(new UserControllerController(userControllerList.getJSONObject(i)));
        }
    }
}
