package com.example.maizedisease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PastDataActivity extends AppCompatActivity {
    Button mainActivityBtn;
    private RecyclerView dataRV;
    private ArrayList<DataModel> dataModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_data);
        Log.d("RANDOM", "activity_past_data started");

        dataModelArrayList = new ArrayList<>();
        dataRV = findViewById(R.id.idDataCard);


        dataModelArrayList = SendPastDataReq();
        Log.d("RANDOM", "FUNCTION EXITED");
//        for(int i=0; i<dataModelArrayList.size();i++){
//            Log.d("RANDOM", Integer.toString(i));
////            Log.d("RANDOM", dataModelArrayList.get(i).getTime_stamp() );
//        }


        mainActivityBtn=findViewById(R.id.btnMainActivity);
        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "MainActivity called");
                startActivity(new Intent(PastDataActivity.this, MainActivity.class));
            }
        });
        DataAdapter dataAdapter = new DataAdapter(this, dataModelArrayList);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        dataRV.setLayoutManager(linearLayoutManager);
        dataRV.setAdapter(dataAdapter);
    }

    public ArrayList<DataModel> SendPastDataReq(){
        Log.d("RANDOM", "SendPastDataReq called");
        dataModelArrayList = new ArrayList<>();
//        dataModelArrayList.add(new DataModel("", "Northern_Leaf_Blight", "22/10/2021 16:06:16", "Maize", 0.0, 0.0));
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

//        int flag=1;
//        while(flag!=0){
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


                        for(int i=0; i<data_count; i++) {
                            String time_stamp, encoded_image, crop_type, predicted_class;
                            Double latitude, longitude;
                            time_stamp = response.getJSONObject("data" + Integer.toString(i)).getString("time_stamp");
                            encoded_image = response.getJSONObject("data" + Integer.toString(i)).getString("encoded_image");
                            crop_type = response.getJSONObject("data" + Integer.toString(i)).getString("crop_type");
                            latitude = response.getJSONObject("data" + Integer.toString(i)).getDouble("latitude");
                            longitude = response.getJSONObject("data" + Integer.toString(i)).getDouble("longitude");
                            predicted_class = response.getJSONObject("data" + Integer.toString(i)).getString("predicted_class");


                            dataModelArrayList.add(new DataModel(encoded_image, predicted_class, time_stamp, crop_type, latitude, longitude));
//                            Log.d("RANDOM", "data model array list = "+dataModelArrayList.get(i).getCrop_type() );
                            Log.d("RANDOM", "i= " + i + " " + time_stamp + crop_type + latitude + longitude + predicted_class);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

//        try {
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.d("RANDOM", "FOR LOOP EXITED");
        return dataModelArrayList;
    }
}