package com.example.maizedisease;

import android.app.Application;

public class FramesPrediction extends Application {
    private String rec_image, predicted_class, crop_type;
    private double probability;

    public void setPredicted_class(String predicted_class) {
        this.predicted_class = predicted_class;
    }

    public String getPredicted_class() {
        return predicted_class;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public double getProbability() {
        return probability;
    }

    public String getRec_image() {
        return rec_image;
    }

    public void setRec_image(String rec_image) {
        this.rec_image = rec_image;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
