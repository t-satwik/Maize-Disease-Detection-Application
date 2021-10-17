package com.example.maizedisease;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ImageClickedActivity extends AppCompatActivity {
    String modelFile="MN.tflite";
    String label="labels1.txt";
    Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private List<String> labels;
    String[] disease = {"Cercospora_Gray_leaf_spot","Common_rust_","Healthy","Northern_Leaf_Blight","Unhealthy"};
    TextView textView,textView2;
    Button button;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_clicked);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        try {
            tfliteModel = FileUtil.loadMappedFile(ImageClickedActivity.this, modelFile);
            labels = FileUtil.loadLabels(ImageClickedActivity.this,label);
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(Global.getBitmap());
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
        System.out.println(max);
        System.out.println(disease[index]);
        textView.setBackgroundColor(65280);
        textView.setText("Predicted Class: "+disease[index]);
        textView2.setText("Probability : "+max);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    protected int sizeOf(Bitmap data) {
        return data.getByteCount();
    }
    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
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

}