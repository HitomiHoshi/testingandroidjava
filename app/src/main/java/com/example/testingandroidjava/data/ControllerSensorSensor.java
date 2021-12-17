package com.example.testingandroidjava.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerSensorSensor {
    public int id;
    public int controllerId;
    public int sensorId;
    public int sync;

    public Sensor sensor;

    public ControllerSensorSensor(JSONObject result) throws JSONException {
        id = result.getInt("Id");
        sensorId = result.getInt("SensorId");
        controllerId = result.getInt("ControllerId");
        sync = result.getInt("Synchronized");

        sensor = new Sensor(result.getJSONObject("Sensor"));
    }
}
