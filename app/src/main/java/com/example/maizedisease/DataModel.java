package com.example.maizedisease;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class DataModel extends Application{

    private String rec_image, predicted_class, time_stamp, crop_type;
    private double latitude, longitude;

    // Constructor
    public DataModel(String rec_image, String predicted_class, String time_stamp,
                     String crop_type, double latitude, double longitude) {
        this.rec_image = rec_image;
        this.predicted_class = predicted_class;
        this.time_stamp = time_stamp;
        this.crop_type = crop_type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter and Setter
    public Bitmap getDecoded_image() {
        byte[] imageBytes = Base64.decode(rec_image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public void setRec_image(String rec_image) {
        this.rec_image = rec_image;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getPredicted_class() {
        return predicted_class;
    }

    public void setPredicted_class(String predicted_class) {
        this.predicted_class = predicted_class;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
