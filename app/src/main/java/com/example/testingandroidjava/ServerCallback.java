package com.example.testingandroidjava;

import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result);
    void onError(JSONObject result);
}
