package com.example.testingandroidjava;

import org.json.JSONException;
import org.json.JSONObject;

public class Albums {

    public int userId;
    public int id;
    public String title;

    public Albums(JSONObject result) throws JSONException {
        id = result.getInt("id");
        title = result.getString("title");
        userId = result.getInt("userId");
    }
}
