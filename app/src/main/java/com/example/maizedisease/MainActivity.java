package com.example.maizedisease;

import androidx.annotation.NonNull;
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

import java.io.File;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private ImageButton imageButton;
    private ImageView imageView;
    private Button btLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);

//        btLocation = findViewById(R.id.bt_loc);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        btLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    //When permission is granted
//                    Log.d("RANDOM", "Permission granted: ");
//                    getLocation();
//                } else {
//                    //When permission denied
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                    Log.d("RANDOM", "Permission Denied: ");
//                }
//            }
//        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission is granted
                    Log.d("RANDOM", "Permission granted: ");
                    getLocation();
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    Log.d("RANDOM", "Permission Denied: ");
                }

                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
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

    private void getLocation() {
        Log.d("RANDOM", "Function called ");
//        SendPostReq(0.0, 0.0);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override

            public void onComplete(@NonNull Task<Location> task) {
                Log.d("RANDOM", "Function oncomplete ");
                //initializing location
                Location location = task.getResult();

                Double latitude=location.getLatitude();
                Double longitude=location.getLongitude();
                Log.d("RANDOM", "Function location ");
                if (location != null) {
                    Log.d("RANDOM", "Latitude: " + Double.toString(location.getLatitude()));
                    Log.d("RANDOM", "Longitude" + Double.toString(location.getLongitude()));
                }
                SendPostReq(latitude, longitude);
                Log.d("RANDOM", "Send Post Request function called");
            }
        });

    }

    public void SendPostReq(Double latitude, Double longitude){
//        String postUrl = "http://10.197.1.213:8000/data/";
//        String postUrl = "http://0.0.0.0:8000/data/";
//        String postUrl = "http://192.168.137.1/data/";
//        String postUrl = "http://127.0.0.1:8000/data/";

//        String postUrl = "http://10.0.2.2:8000/data/";
        String postUrl = "http://0ec0-203-129-219-162.ngrok.io/data/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        JSONObject postData = new JSONObject();
        try {
            postData.put("timestamp", formatter.format(date));
            postData.put("longitude", Double.toString(longitude));
            postData.put("latitude", Double.toString(latitude));
            postData.put("predicted_class", "Nil");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,
                postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RANDOM", "response recieved");
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

        if (requestCode == 152 && resultCode == RESULT_OK && null !=data){
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            Drawable drawable =  imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            Global.setBitmap(resized);
            startActivity(new Intent(MainActivity.this, ImageClickedActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }
}