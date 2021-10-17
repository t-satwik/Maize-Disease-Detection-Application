package com.example.maizedisease;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private ImageButton imageButton;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                @SuppressLint("IntentReset") Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 152);

            }
        });
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