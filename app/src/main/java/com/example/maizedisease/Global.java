package com.example.maizedisease;
import android.app.Application;
import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public class Global extends Application {
    public static Bitmap bitmap;
    public static byte[] capturedImage = new byte[0];
    public Global() {
        bitmap = null;
    }

    public static void setCapturedImage(byte[] capturedImage) {
        Global.capturedImage = capturedImage;
    }

    public static byte[] getCapturedImage() {
        return capturedImage;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        Global.bitmap = bitmap;
    }
}
