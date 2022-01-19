package com.example.maizedisease;

//import static org.bytedeco.javacpp
//        .opencv_highgui.cvNamedWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.FrameGrabber;
//import org.bytedeco.javacv.OpenCVFrameGrabber;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoResultsActivity extends AppCompatActivity {

    String modelFile="MN.tflite";

    String label="labels1.txt";
    Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private List<String> labels;
    String[] disease = {"Cercospora_Gray_leaf_spot","Common_rust_","Healthy","Northern_Leaf_Blight","Unhealthy"};
//    ImageView testImageView1, testImageView2, testImageView3, testImageView4;

    Button mainActivityBtn;
    private RecyclerView frameRV;
    private ArrayList<FramePrediction> framePredArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("RANDOM", "Video Results started called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_results);


        framePredArrayList = new ArrayList<>();
        frameRV = findViewById(R.id.idFramePredCard);

        mainActivityBtn=findViewById(R.id.btnMainActivity);
        mainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "MainActivity called");
                startActivity(new Intent(VideoResultsActivity.this, MainActivity.class));
            }
        });

        String filePath=getRealPathFromURI(Global.getVideoURI());
        ArrayList<Bitmap> frames=grabFrames(filePath);

        framePredArrayList=processFrames(frames);




        FramePredictionAdapter frameAdapter = new FramePredictionAdapter(this, framePredArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        frameRV.setLayoutManager(linearLayoutManager);
        frameRV.setAdapter(frameAdapter);

    }

    private ArrayList<FramePrediction> processFrames(ArrayList<Bitmap> frames) {
        Log.d("RANDOM", "process frames function called ");
        framePredArrayList = new ArrayList<>();

        try {
            tfliteModel = FileUtil.loadMappedFile(VideoResultsActivity.this, modelFile);
            labels = FileUtil.loadLabels(VideoResultsActivity.this,label);
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<frames.size(); i++){


            Log.d("RANDOM", "tflite models loaded");
            Bitmap resized_bitmap = Bitmap.createScaledBitmap(frames.get(i), 224, 224, true);
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(resized_bitmap);




            Log.d("RANDOM", "bitmap converted into byte buffer");
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
//            Log.d("RANDOM", max+disease[index]);
//            Global.setPredictedClass(disease[index]);
            Log.d("RANDOM", "Probability : "+max);
            Log.d("RANDOM", "Disease : "+disease[index]+i);
            Global.setCropType("Maize");
            SendPostReq(resized_bitmap, disease[index], Global.getCropType(), max);

            framePredArrayList.add(new FramePrediction(resized_bitmap, disease[index], Global.getCropType(), max));
        }


        return framePredArrayList;
    }

    private void SendPostReq(Bitmap bmp, String predicted_class, String crop_type, Double probability){
        String postUrl = Global.getURL()+"SetVideoFrame/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded_image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String time_stamp=formatter.format(date);
        Log.d("RANDOM", time_stamp);
        JSONObject postData = new JSONObject();
        try {
            postData.put("startLatitude", Double.toString(Global.getLatitude()));
            postData.put("startLongitude", Double.toString(Global.getLongitude()));
            postData.put("endLatitude", Double.toString(Global.getEndLatitude()));
            postData.put("endLongitude", Double.toString(Global.getEndLongitude()));
            postData.put("user_name", Global.getUserName());
            postData.put("encoded_image", encoded_image);
            postData.put("predicted_class", predicted_class);
            postData.put("crop_type", crop_type);
            postData.put("probability", probability);
            postData.put("time_stamp", time_stamp);

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
                    Log.d("RANDOM", response.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RANDOM", error.toString());
                Log.d("RANDOM", String.valueOf(error.networkResponse.statusCode));
                Log.d("RANDOM", String.valueOf(error.getMessage()));
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
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


    private String getRealPathFromURI(Uri videoURI) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            return "Permission Failed";
        }

        String result;
        Cursor cursor = getContentResolver().query(videoURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = videoURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private ArrayList<Bitmap> grabFrames(String filePath) {
        Log.d("RANDOM", "Grab Frames function called");

        MediaMetadataRetriever  retriever = new MediaMetadataRetriever ();

        retriever.setDataSource(filePath);

        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);

        Log.d("RANDOM", "Duration in milliseconds"+timeInMilliSec);
        ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
        timeInMilliSec=5000;
        for (int i = 0; i < timeInMilliSec*1000; i += 500000) {           //Loop for every half second, i.e 2 frames per second
            Bitmap bmp = retriever.getFrameAtTime(i);
            if(bmp!=null){
                frames.add(bmp);
                Log.d("RANDOM", "BMP added to frames"+frames.size());
            }
            else{
                Log.d("RANDOM", "null");
            }
            Log.d("RANDOM", "i=" + i);
        }
        retriever.release();
        return frames;
    }
}