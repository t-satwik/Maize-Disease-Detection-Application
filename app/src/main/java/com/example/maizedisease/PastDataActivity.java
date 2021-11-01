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

        mainActivityBtn=findViewById(R.id.btnMainActivity);
        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "MainActivity called");
                startActivity(new Intent(PastDataActivity.this, MainActivity.class));
            }
        });

//        while(Global.getPastDataResp()==null){
//            continue;
//        }
        Log.d("RANDOM", "Global Response="+Global.getPastDataResp().toString());
        try {
            dataModelArrayList=ConvertJSONtoArrayList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RANDOM", "FUNCTION EXITED");
//        for(int i=0; i<dataModelArrayList.size();i++){
//            Log.d("RANDOM", Integer.toString(i));
////            Log.d("RANDOM", dataModelArrayList.get(i).getTime_stamp() );
//        }



        DataAdapter dataAdapter = new DataAdapter(this, dataModelArrayList);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        dataRV.setLayoutManager(linearLayoutManager);
        dataRV.setAdapter(dataAdapter);
    }



    public ArrayList<DataModel> ConvertJSONtoArrayList() throws JSONException {
        dataModelArrayList = new ArrayList<>();
        JSONObject response=Global.getPastDataResp();
        if (response.getString("message") != "Data Fetch Successful") {
            int data_count = Integer.valueOf(response.getString("data_count"));
            for (int i = 0; i < data_count; i++) {
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
        else{
            return null;
        }
        return dataModelArrayList;
    }
}