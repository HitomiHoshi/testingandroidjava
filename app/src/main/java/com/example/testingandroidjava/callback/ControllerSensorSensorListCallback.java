package com.example.testingandroidjava.callback;

import com.example.testingandroidjava.data.ControllerSensorSensor;
import com.example.testingandroidjava.data.Message;

import java.util.ArrayList;

public interface ControllerSensorSensorListCallback {
    void onSuccess(ArrayList<ControllerSensorSensor> result);
    void onError(Message result);
}
