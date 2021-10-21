package com.example.maizedisease;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.nio.ByteBuffer;
import java.sql.Time;

public class Global extends Application {
    public static Bitmap bitmap;
    public static byte[] capturedImage = new byte[0];
    public static double Latitude, Longitude;
    public static String TimeStamp, UserID, PredictedClass, CropType;
    public Global() {
        bitmap = null;
    }




    public static void setCapturedImage(byte[] capturedImage) {
        Global.capturedImage = capturedImage;
    }

    public static byte[] getCapturedImage() {
        return capturedImage;
    }

    public static void setLatitude(double lat) {
        Global.Latitude = lat;
    }

    public static double getLatitude() {
        return Latitude;
    }

    public static void setLongitude(double lon) {
        Global.Longitude = lon;
    }

    public static double getLongitude() {
        return Longitude;
    }
    public static void setTimeStamp(String ts) {
        Global.TimeStamp = ts;
    }

    public static String getTimeStamp() {
        return TimeStamp;
    }

    public static void setUserId(String uid) {
        Global.UserID = uid;
    }

    public static String getUserID() {
        return UserID;
    }
    public static void setPredictedClass(String pc) {
        Global.PredictedClass = pc;
    }

    public static String getPredictedClass() {
        return PredictedClass;
    }
    public static void setCropType(String ct) {
        Global.CropType = ct;
    }

    public static String getCropType() {
        return CropType;
    }

//    public static void setEncodedImage() {
//        Global.EncodedImage = Base64.encodeToString(capturedImage, Base64.DEFAULT);
////        Log.d("RANDOM", EncodedImage);
//    }

    public static String getEncodedImage() {
        return Base64.encodeToString(capturedImage, Base64.DEFAULT);
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        Global.bitmap = bitmap;
    }
}
