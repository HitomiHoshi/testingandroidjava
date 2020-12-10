package com.example.testingandroidjava;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(Albums result);
    void onError(JSONObject result);
}
