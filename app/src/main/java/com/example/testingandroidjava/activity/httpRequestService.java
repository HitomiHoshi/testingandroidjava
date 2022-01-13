package com.example.testingandroidjava.activity;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testingandroidjava.Albums;
import com.example.testingandroidjava.ServerCallback;
import com.example.testingandroidjava.callback.ControllerSensorSensorListCallback;
import com.example.testingandroidjava.callback.SignInCallback;
import com.example.testingandroidjava.data.ControllerSensorSensor;
import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.SignIn;
import com.example.testingandroidjava.data.SignInTFA;
import com.example.testingandroidjava.service.SharedPreferenceService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class httpRequestService {

    private final RequestQueue requestQueue;
    private final SharedPreferenceService sharedPreferenceService;

    private final String url = "http://primeguard.com.my:2010/mobile/";

    public httpRequestService(Context ctx){
        requestQueue = Volley.newRequestQueue(ctx);
        sharedPreferenceService = new SharedPreferenceService(ctx);
    }

    public void jsonParse(final ServerCallback callback){

        String url = "https://jsonplaceholder.typicode.com/albums/1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(new Albums(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                try {
//                    int userId = response.getInt("userId");
//                    int id = response.getInt("id");
//                    String title = response.getString("title");
//                    Log.i("JSON",title);
//                    textView.setText(String.valueOf(userId) + " " + String.valueOf(id) + " " + title);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("JSON","Error");
            }
        });

        requestQueue.add(request);
    }

    public void signIn(String username, String password, final SignInCallback signInCallback) {

        JSONObject data = new JSONObject();
        try {
            data.put("Username", username);
            data.put("KeypadCode", password);
            data.put("FCMToken", "testFCM");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + "user/login/", data, response -> {
            Log.e("JSON", String.valueOf(response));
            try {
                if (response.optBoolean("TFAEnabled")) {
                    SignInTFA result = new SignInTFA(response);

                    signInCallback.onSuccessTFA(result);
                } else {
                    SignIn result = new SignIn(response);

                    sharedPreferenceService.setJWTToken(result.jwtToken);
                    Log.e("TAG", "signIn: " + sharedPreferenceService.getJWTToken() );
                    signInCallback.onSuccess(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            error.getMessage();

            try {
                Message resp = new Message(new JSONObject(new String(error.networkResponse.data)));

                signInCallback.onError(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void getSensorByControllerId(String controllerId, ControllerSensorSensorListCallback controllerSensorSensorListCallback) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + "controller/sensors/" + controllerId, null, response -> {
            Log.e("JSON", String.valueOf(response));
            try {

                ArrayList<ControllerSensorSensor> result = new ArrayList<>();

                for(int i = 0; i < response.length(); i++){
                    result.add(new ControllerSensorSensor(response.getJSONObject(i)));
                }

                controllerSensorSensorListCallback.onSuccess(result);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            error.getMessage();

            try {
                Message resp = new Message(new JSONObject(new String(error.networkResponse.data)));

                controllerSensorSensorListCallback.onError(resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + sharedPreferenceService.getJWTToken());
                return headers;
            }
        };
        requestQueue.add(request);
    }

}
