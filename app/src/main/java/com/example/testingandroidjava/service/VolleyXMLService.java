package com.example.testingandroidjava.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.testingandroidjava.callback.XMLCallback;
import com.example.testingandroidjava.data.Message;
import com.example.testingandroidjava.data.WaktuSolat;
import com.example.testingandroidjava.data.WaktuSolatList;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyXMLService {
    private final RequestQueue requestQueue;
    private final String url;
    private final Context context;

    public VolleyXMLService(Context context) {
        this.context = context;
        url = "https://www.e-solat.gov.my/index.php?r=esolatApi/xmlfeed&zon=";
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getWaktuSolat(XMLCallback xmlCallback) {



        StringRequest request = new StringRequest(Request.Method.GET, url + "SGR01", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("XML", response);

                ArrayList<WaktuSolat> waktuSolatArrayList = new ArrayList();
                WaktuSolat waktuSolat = new WaktuSolat() ;
                WaktuSolatList waktuSolatList= new WaktuSolatList();

                try {
                    XmlPullParserFactory factory;
                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( new StringReader( response ) );
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.START_DOCUMENT) {
                            System.out.println("Start document");
                        }
                        else if(eventType == XmlPullParser.START_TAG) {
                            System.out.println("Start tag "+xpp.getName());
                            if (xpp.getName().equals("date")){
                                xpp.next();
                                waktuSolatList.date = xpp.getText();
                            }
                            else if (xpp.getName().equals("item")){
                                waktuSolat = new WaktuSolat() ;
                            }
                            else if (xpp.getName().equals("title")){
                                xpp.next();
                                waktuSolat.title = xpp.getText();
                            }
                            else if (xpp.getName().equals("description")){
                                xpp.next();
                                waktuSolat.description = xpp.getText();
                            }
                        }
                        else if(eventType == XmlPullParser.END_TAG) {
                            System.out.println("End tag "+xpp.getName());
                            if (xpp.getName().equals("item")){
                                waktuSolatArrayList.add(waktuSolat);
                            }
                        }
                        else if(eventType == XmlPullParser.TEXT) {
                            System.out.println("Text "+xpp.getText());
                        }
                        eventType = xpp.next();
                    }
                    System.out.println("End document");
                    Log.e("XML 0", waktuSolatArrayList.get(0).title);
                    waktuSolatList.waktuSolatList = waktuSolatArrayList;
                    xmlCallback.onSuccess(waktuSolatList);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }


//                try {
//                    AccountResponse result = new AccountResponse(response);
//
//                    accountResponseCallback.onSuccess(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();

                try {

                    Message resp = new Message(new JSONObject(new String(error.networkResponse.data)));

                    xmlCallback.onError(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void testingXML(){
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PrintLetterBarcodeData uid=\"521007171049\" \n" +
                "name=\"Bandigari Katamaraju\"\n" +
                " gender=\"M\"\n" +
                " yob=\"1991\" \n" +
                " co=\"S/O: BANDIGARI YADAGIRI\" \n" +
                " house=\"4-141/1\"\n" +
                " loc=\"EDULLAGUDEM\" \n" +
                " vtc=\"Edullagudam\"\n" +
                " dist=\"Nalgonda\"\n" +
                " subdist=\"For New VTC\"\n" +
                " state=\"Andhra Pradesh\" \n" +
                " pc=\"508112\"/>";

        try {
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( xmlString ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag "+xpp.getName());
                    if (xpp.getName().equals("PrintLetterBarcodeData")){
                        for (int i=0; i<xpp.getAttributeCount(); i++){
                            System.out.println("attribute:"+xpp.getAttributeName(i)+" with value: "+xpp.getAttributeValue(i));
                            //Store here your values in the variables of your choice.
                        }
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    System.out.println("End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    System.out.println("Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
            System.out.println("End document");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
