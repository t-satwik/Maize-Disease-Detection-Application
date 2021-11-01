package com.example.maizedisease;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import android.net.Uri;
import java.nio.ByteBuffer;
import java.sql.Time;

public class Global extends Application {
    public static Bitmap bitmap;
    public static byte[] capturedImage = new byte[0];
    public static double Latitude, Longitude, EndLatitude, EndLongitude;
    public static String TimeStamp, UserName, PredictedClass, CropType, URL;
    public static Uri VideoURI;
    public static JSONObject PastDataResp=null;
    public Global() {
        bitmap = null;
    }

    public  static  void setPastDataResp(JSONObject resp){ Global.PastDataResp=resp;}
    public static  JSONObject getPastDataResp() { return PastDataResp;}

    public static void setURL(String u) {
        Global.URL = u;
    }

    public static String getURL() {
        return URL;
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

    public static void setUserName(String uname) {
        Global.UserName = uname;
    }

    public static String getUserName() {
        return UserName;
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



    public static String getEncodedImage() {
        return Base64.encodeToString(capturedImage, Base64.DEFAULT);
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        Global.bitmap = bitmap;
    }

    public static void setEndLongitude(double v) {
        EndLatitude=v;
    }

    public static void setEndLatitude(double v) {
        EndLongitude=v;
    }
    public static void setVideoURI(Uri s){
        VideoURI=s;
    }

    public static Uri getVideoURI() {
        return VideoURI;
    }
}
