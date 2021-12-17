package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Sensor {
    public int id;
    public int type;
    public String name;
    public String image;
    public int status;
    public int triggerStatus;
    public int batteryStatus;
    public int batteryDaysLeft;
    public int connectionStatus;
    public int armStatus;

    public Sensor(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        name = result.getString("Name");
        image = result.optString("Image");

        if(result.optJSONObject("Type") != null) type = result.optInt("Type");
        status = result.optInt("Status");
        batteryStatus = result.optInt("BatteryStatus");
        batteryDaysLeft = result.optInt("BatteryDaysLeft");
        connectionStatus = result.optInt("ConnectionStatus");
        armStatus = result.optInt("ArmStatus");
        triggerStatus = result.optInt("TriggerStatus");
    }
}
