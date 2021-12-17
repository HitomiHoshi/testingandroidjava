package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIdUsername {
    public int id;
    public String username;

    public UserIdUsername(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        username = result.getString("Username");
    }
}
