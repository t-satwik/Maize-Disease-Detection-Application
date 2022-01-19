package com.example.maizedisease;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class FramePrediction {
    private String predicted_class, crop_type;
    private double probability;
    private Bitmap frame_bmp;

    public FramePrediction(Bitmap bmp, String predicted_class, String crop_type, double prob) {
        this.frame_bmp = bmp;
        this.predicted_class = predicted_class;
        this.crop_type = crop_type;
        this.probability = prob;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public String getPredicted_class() {
        return predicted_class;
    }

    public void setPredicted_class(String predicted_class) {
        this.predicted_class = predicted_class;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public Bitmap getFrameBitmap() {
        return frame_bmp;
    }

    public void setFrame_bmp(Bitmap bmp) {
        this.frame_bmp = bmp;
    }

}

