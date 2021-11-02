package com.example.maizedisease;

//import static org.bytedeco.javacpp
//        .opencv_highgui.cvNamedWindow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.FrameGrabber;
//import org.bytedeco.javacv.OpenCVFrameGrabber;

import org.checkerframework.checker.units.qual.A;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

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
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("RANDOM", "Video Results started called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_results);
        VideoView simpleVideoView = (VideoView) findViewById(R.id.simpleVideoView);

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(simpleVideoView);
        simpleVideoView.setMediaController(mediaController);
//        simpleVideoView.setVideoPath(filePath);
        simpleVideoView.setVideoURI(Global.getVideoURI());
        simpleVideoView.start();

        ArrayList<Bitmap> frames=grabFrames();
//        ArrayList<FramesPrediction> framesPredictions=processFrames(frames);
        processFrames(frames);


    }

    private void processFrames(ArrayList<Bitmap> frames) {
        Log.d("RANDOM", "process frames function called ");
//        ArrayList<FramesPrediction> framesPredictions = new ArrayList<FramesPredictions>();



        for(int i=0; i<frames.size(); i++){
//            FramePrediction framePrediction = new FramePrediction();

            try {
                tfliteModel = FileUtil.loadMappedFile(VideoResultsActivity.this, modelFile);
                labels = FileUtil.loadLabels(VideoResultsActivity.this,label);
                tflite = new Interpreter(tfliteModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("RANDOM", "tflite models loaded");
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(frames.get(i));
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
            Log.d("RANDOM", "Disease : "+disease[index]);



        }
//        return framesPredictions;
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


//    private String getRealPathFromURI(Uri videoURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(videoURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = videoURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

    private ArrayList<Bitmap> grabFrames() {
        Log.d("RANDOM", "Grab Frames function called");

        FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();

        Log.d("RANDOM", "duration=" + Global.getVideoURI());
        med.setDataSource(this, Global.getVideoURI());

//        MediaPlayer mp = MediaPlayer.create(getBaseContext(), Global.getVideoURI());
//        int millis = mp.getDuration(); //Duration of Video

        String time = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time)-150496000;
//        150497000 150498000 150502000
        Log.d("RANDOM", "duration=" + timeInMillisec);
        ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
        for (int i = 0; i < timeInMillisec*1000; i += 500000) {           //Loop for every half second, i.e 2 frames per second
            Bitmap bmp = med.getFrameAtTime(i, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if(bmp!=null){
                frames.add(bmp);
            }
            else{
                Log.d("RANDOM", "null");
            }
            Log.d("RANDOM", "i=" + i);
        }
        return frames;
    }
}