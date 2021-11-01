package com.example.maizedisease;

import static org.bytedeco.javacpp.opencv_highgui.cvNamedWindow;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.io.File;

public class VideoResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("RANDOM", "Video Results started called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_results);
        VideoView simpleVideoView = (VideoView) findViewById(R.id.simpleVideoView);
        String filePath = getRealPathFromURI(Global.getVideoURI());
        Log.d("RANDOM", "Real Path= "+ filePath);

        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(simpleVideoView);
        simpleVideoView.setMediaController(mediaController);
        simpleVideoView.setVideoPath(filePath);
        simpleVideoView.requestFocus();
        simpleVideoView.start();





//        grabFrames(filePath);
    }

    private String getRealPathFromURI(Uri videoURI) {
        String result;
        Cursor cursor = getContentResolver().query(videoURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = videoURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void grabFrames(String filePath) {
        Log.d("RANDOM", "Grab Frames function called");
        FrameGrabber grabber = new OpenCVFrameGrabber(filePath);

        if (grabber == null)
        {
//            System.out.println("!!! Failed OpenCVFrameGrabber");
            Log.d("RANDOM", "!!! Failed OpenCVFrameGrabber");
            return;
        }

//        cvNamedWindow("video_demo");

        try
        {
            Log.d("RANDOM", "Try Statement");
            grabber.start();
            Log.d("RANDOM", "grabber.start()");
            Frame frame = null;

            int frame_counter = 1;
            Log.d("RANDOM", "loop entered");
            while (true)
            {
                Log.d("RANDOM", "loop entered");
                frame = grabber.grab();
                Log.d("RANDOM", "fram grab");
                if (frame == null) {
                    Log.d("RANDOM", "!!! Failed grab");
//                    System.out.println();
                    break;
                }

                if ((frame_counter % 5) == 0) Log.d("RANDOM", "Frame number:"+frame);
                    // do something pretty with frame 5, 10, 15, 20, 25, ...

//                    cvShowImage("video_demo", frame);
//                int key = cvWaitKey(33);
//                if (key == 27)
//                {
//                    break;
//                }

                frame_counter++;
            }
            Log.d("RANDOM", "While Loop Exited");
        }
        catch (Exception e)
        {
            Log.d("RANDOM", "!!! Exception");
        }
    }

}