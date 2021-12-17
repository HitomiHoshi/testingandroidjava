package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Controller {
    public int id;
    public String name;
    public String macAddress;
    public int setupCompleted;
    public int allSensorsSynced;
    public int status;
    public int armStatus;
    public int sirenStatus;
    public int connectionStatus;
    public String latitude;
    public String longitude;
    public String emergencyContactName;
    public String emergencyContactPhone;
    public int totalSensors;


    public Controller(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        name = result.getString("Name");
        macAddress = result.getString("MacAddress");
        setupCompleted = result.getInt("SetupCompleted");
        allSensorsSynced = result.getInt("AllSensorsSynced");
        status = result.getInt("Status");
        armStatus = result.getInt("ArmStatus");
        sirenStatus = result.getInt("SirenStatus");
        connectionStatus = result.getInt("ConnectionStatus");
        latitude = result.optString("Latitude");
        longitude = result.optString("Longitude");
        emergencyContactName = result.getString("EmergencyContactName");
        emergencyContactPhone = result.getString("EmergencyContactPhone");
        totalSensors = result.optInt("TotalSensors");
    }
}
