package com.example.maizedisease;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageClickedActivity extends AppCompatActivity {
    String modelFile="MN.tflite";

    String label="labels1.txt";
    Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private List<String> labels;
    String[] disease = {"Cercospora_Gray_leaf_spot","Common_rust_","Healthy","Northern_Leaf_Blight","Unhealthy"};
    TextView textView,textView2;
    Button button,btnPastData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Global.setCropType("Maize");
        Log.d("RANDOM", "ImageClickedActivity called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_clicked);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        btnPastData = findViewById(R.id.btnPastData);

        btnPastData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "PastDataActivity called");
                SendPastDataReq();
            }
        });

        try {
            tfliteModel = FileUtil.loadMappedFile(ImageClickedActivity.this, modelFile);
            labels = FileUtil.loadLabels(ImageClickedActivity.this,label);
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(Global.getBitmap());
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(4*5);
        outputBuffer.order(ByteOrder.nativeOrder());
        float[][] out = new float[1][5];
        Object[] input = {byteBuffer};
        Map<Integer, Object> outputs = new HashMap();
        outputs.put(0, out);
        tflite.runForMultipleInputsOutputs(input, outputs);
        String Out = Arrays.deepToString(out);
        System.out.println(Out.toCharArray());
        Double[] fin = {0.0,0.0,0.0,0.0,0.0};
        int k = 0;
        StringBuilder temp = new StringBuilder();
        for(int j=0;j<Out.toCharArray().length;j++){
            if(Out.toCharArray()[j]=='[' || Out.toCharArray()[j]==' '){
                continue;
            }
            else if(Out.toCharArray()[j]==']' && j==Out.toCharArray().length-1){
                fin[k] = Double.parseDouble(String.valueOf(temp));
                k++;
            }
            else if(Out.toCharArray()[j]==']' && j==Out.toCharArray().length-2){
                continue;
            }
            else if(Out.toCharArray()[j]==','){
                fin[k] = Double.parseDouble(String.valueOf(temp));
                k++;
                temp = new StringBuilder();
            }
            else{
                temp.append(Out.toCharArray()[j]);
            }
        }
        Double max = fin[0];
        int index = 0;
        for (int j=0;j<fin.length;j++) {
            if(fin[j]>max){
                max = fin[j];
                index = j;
            }
        }
        System.out.println(max);
        System.out.println(disease[index]);
        textView.setBackgroundColor(65280);
        textView.setText("Predicted Class: "+disease[index]);
        Global.setPredictedClass(disease[index]);

        textView2.setText("Probability : "+max);
        Log.d("RANDOM", "Send Post Request called");
        SendPostReq();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected int sizeOf(Bitmap data) {
        return data.getByteCount();
    }
    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer;
        byteBuffer = ByteBuffer.allocateDirect(
                4 * 1 * 224* 224 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[224 * 224];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < 224; ++i) {
            for (int j = 0; j < 224; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat(
                        (((val >> 16) & 255))/255F);
                byteBuffer.putFloat(
                        (((val >> 8) & 255))/255F);
                byteBuffer.putFloat((((val) & 255))/255F);
            }
        }
        return byteBuffer;
    }



    public void SendPostReq(){

        String postUrl = Global.getURL()+"SetData/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        Global.setTimeStamp(formatter.format(date));

        JSONObject postData = new JSONObject();
        try {
            postData.put("time_stamp",  Global.getTimeStamp());
            postData.put("latitude", Double.toString(Global.getLatitude()));
            postData.put("longitude", Double.toString(Global.getLongitude()));
            postData.put("user_name", Global.getUserName());
            postData.put("encoded_image", Global.getEncodedImage());
            postData.put("predicted_class", Global.getPredictedClass());
            postData.put("crop_type", Global.getCropType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,
                postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RANDOM", "response received");
                System.out.println(response);
                try {
                    if (response.getString("message") != "Data Sent") {
                        Toast.makeText(getApplicationContext(),
                                "Data is sent to server", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Request Failed", Toast.LENGTH_LONG).show();
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
                Log.d("RANDOM", String.valueOf(error.getMessage()));
//                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void SendPastDataReq(){
        Log.d("RANDOM", "SendPastDataReq called");
//        dataModelArrayList = new ArrayList<>();
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
        Log.d("RANDOM", "user_name="+user_name);


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


//                            dataModelArrayList.add(new DataModel(encoded_image, predicted_class, time_stamp, crop_type, latitude, longitude));
//                            Log.d("RANDOM", "data model array list = "+dataModelArrayList.get(i).getCrop_type() );
                            Log.d("RANDOM", "i= " + i + " " + time_stamp + crop_type + latitude + longitude + predicted_class);
                        }
                        Global.setPastDataResp(response);
                        startActivity(new Intent(ImageClickedActivity.this, PastDataActivity.class));;
//                        Log.d("RANDOM", Global.getPastDataResp().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
//                        Global.setPastDataResp(response);
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
                            "Error in request", Toast.LENGTH_LONG).show();
                }
                Log.d("RANDOM", String.valueOf(error.getMessage()));
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);

        Log.d("RANDOM", "Request function exited");
    }
}