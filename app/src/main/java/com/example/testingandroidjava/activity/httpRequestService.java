package com.example.testingandroidjava.activity;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testingandroidjava.ServerCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class httpRequestService {

    private RequestQueue requestQueue;
    private String url = "https://jsonplaceholder.typicode.com/albums/1";

    public httpRequestService(Context ctx){
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public void jsonParse(final ServerCallback callback){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
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
}
