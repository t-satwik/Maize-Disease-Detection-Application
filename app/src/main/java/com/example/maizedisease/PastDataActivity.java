package com.example.maizedisease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PastDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_data);
        Log.d("RANDOM", "activity_past_data started");
        SendPastDataReq();

    }

    public void SendPastDataReq(){
        Log.d("RANDOM", "SendPastDataReq called");
//        String postUrl = "http://10.0.2.2:8000/data/";
//        String postUrl = "http://0ec0-203-129-219-162.ngrok.io/data/";
//        String postUrl = "http://6aed-203-129-219-162.ngrok.io/data/";

        String postUrl = Global.getURL()+"GetPastData/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        String user_name=Global.getUserName();
        try {
            postData.put("user_name",  user_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RANDOM", user_name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,
                postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RANDOM", "response received");
                try {
                    if (response.getString("message") != "Data Fetch Successful") {
                        int data_count=Integer.valueOf(response.getString("data_count"));
                        Log.d("RANDOM", "data_count= "+Integer.toString(data_count));
//                        Log.d("RANDOM", response.toString(4));
                        for(int i=0; i<data_count; i++){
                            String time_stamp, encoded_image, crop_type;
                            Double latitude, longitude;
                            time_stamp=response.getJSONObject("data"+Integer.toString(i)).getString("time_stamp");
                            encoded_image=response.getJSONObject("data"+Integer.toString(i)).getString("encoded_image");
                            crop_type=response.getJSONObject("data"+Integer.toString(i)).getString("crop_type");

                            latitude=response.getJSONObject("data"+Integer.toString(i)).getDouble("latitude");
                            longitude=response.getJSONObject("data"+Integer.toString(i)).getDouble("longitude");
                            Log.d("RANDOM", "i= "+i+" "+time_stamp+crop_type+latitude+longitude);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//                    Log.d("RANDOM", response.toString(4));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RANDOM", error.toString());
                Log.d("RANDOM", String.valueOf(error.networkResponse.statusCode));
                if(String.valueOf(error.networkResponse.statusCode).equals("401") || String.valueOf(error.networkResponse.statusCode).equals("400")){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
                Log.d("RANDOM", String.valueOf(error.getMessage()));
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}