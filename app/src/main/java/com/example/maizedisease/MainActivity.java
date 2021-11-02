package com.example.maizedisease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.camerakit.CameraKitView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Base64;

public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private ImageButton imageButton;
    private ImageView imageView;
    private Button btnPastData;
    private Button recordVideoBtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private Uri videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        btnPastData = findViewById(R.id.btnPastData2);
        recordVideoBtn = findViewById(R.id.recordVideoBtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(isCameraPresentInPhone()){
            Log.d("RANDOM", "Camera is detected");
            getCameraPermission();
        }
        else{
            Log.d("RANDOM", "Camera is not detected");
        }
        
        recordVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("RANDOM", "Record Video btn pressed");
                getLocation();
                recordVideo();
            }
        });

        btnPastData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RANDOM", "PastDataActivity called");
                SendPastDataReq();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission is granted
                    Log.d("RANDOM", "Permission granted: ");
                    getTime();
                    getLocation();
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    Log.d("RANDOM", "Permission Denied: ");
                }
                Log.d("RANDOM", "Image button CLicked");

                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        Log.d("RANDOM", "Image captured");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                        Global.setCapturedImage(capturedImage);


                        Global.setBitmap(resized);

                        //MediaStore.Images.Media.insertImage(getContentResolver(), resized, "test.jpg" ,"New");
                        startActivity(new Intent(MainActivity.this, ImageClickedActivity.class));
                    }
                });

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                @SuppressLint("IntentReset") Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 152);

            }
        });


    }

    private void recordVideo(){
        Log.d("RANDOM", "Record Video called");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra("android.intent.extra.durationLimit", 30);
//        intent.putExtra("EXTRA_VIDEO_QUALITY", 0);
//        intent.putExtra("MediaStore.EXTRA_DURATION_LIMIT", 30);
//        intent.putExtra("MediaStore.EXTRA_VIDEO_QUALITY",0);
//        intent.putExtra("MediaStore.EXTRA_DURATION_LIMIT",10);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }


    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }
    private boolean isCameraPresentInPhone() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }




    private void getTime(){
        Log.d("RANDOM", "getTime called ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Global.setTimeStamp(formatter.format(date));
    }

    private void getLocation() {
        Log.d("RANDOM", "getLoc called ");
        Global.setLongitude(0.0);
        Global.setLatitude(0.0);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//
//            public void onComplete(@NonNull Task<Location> task) {
////                Log.d("RANDOM", "Function oncomplete ");
//                //initializing location
//                Location location = task.getResult();
//
//                Double latitude=location.getLatitude();
//                Double longitude=location.getLongitude();
////                Log.d("RANDOM", "Function location ");
//                if (location != null) {
//                    Log.d("RANDOM", "Latitude: " + Double.toString(location.getLatitude()));
//                    Global.setLatitude(location.getLatitude());
//                    Log.d("RANDOM", "Longitude" + Double.toString(location.getLongitude()));
//                    Global.setLongitude(location.getLongitude());
//                }
//                return;
//
//            }
//        });
    }

    private void getEndLocation() {
        Log.d("RANDOM", "getLoc called ");
        Global.setEndLongitude(0.0);
        Global.setEndLatitude(0.0);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//
//            public void onComplete(@NonNull Task<Location> task) {
////                Log.d("RANDOM", "Function oncomplete ");
//                //initializing location
//                Location location = task.getResult();
//
//                Double latitude=location.getLatitude();
//                Double longitude=location.getLongitude();
////                Log.d("RANDOM", "Function location ");
//                if (location != null) {
//                    Log.d("RANDOM", "Latitude: " + Double.toString(location.getLatitude()));
//                    Global.setEndLatitude(location.getLatitude());
//                    Log.d("RANDOM", "Longitude" + Double.toString(location.getLongitude()));
//                    Global.setEndLongitude(location.getLongitude());
//                }
//                return;
//
//            }
//        });
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
                        startActivity(new Intent(MainActivity.this, PastDataActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 152 && resultCode == RESULT_OK && null !=data){ // Gallery
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            Drawable drawable =  imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            Global.setBitmap(resized);
            startActivity(new Intent(MainActivity.this, ImageClickedActivity.class));
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==VIDEO_RECORD_CODE){
            if(resultCode==RESULT_OK){
                videoPath = data.getData();
                getEndLocation();
                Log.d("RANDOM", "VideoResultsActivity called");
                Uri VideoURI = data.getData();
                Global.setVideoURI(VideoURI);

                startActivity(new Intent(MainActivity.this, VideoResultsActivity.class));
                Log.d("RANDOM", "Video is Recorded at "+ videoPath);
            }

            else if(resultCode==RESULT_CANCELED){
                Log.d("RANDOM", "VIDEO is cancelled");
            }
            else{
                Log.d("RANDOM", "Error");
            }
        }
        else {
            Toast.makeText(MainActivity.this, "You have not selected and image", Toast.LENGTH_SHORT).show();
        }

    }
}