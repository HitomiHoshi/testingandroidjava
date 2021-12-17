package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserControllerUserIdUsernameController {
    public int id;
    public int userId;
    public int controllerId;
    public int isPrincipal;
    public int trusted;

    public Controller controller;
    public UserIdUsername user;

    public UserControllerUserIdUsernameController(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        userId = result.getInt("UserId");
        controllerId = result.getInt("ControllerId");
        isPrincipal = result.getInt("IsPrincipal");
        trusted = result.getInt("Trusted");

        controller = new Controller(result.getJSONObject("Controller"));
        user = new UserIdUsername(result.getJSONObject("User"));
    }
}
