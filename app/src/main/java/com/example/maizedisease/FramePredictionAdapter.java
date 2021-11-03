package com.example.maizedisease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FramePredictionAdapter extends RecyclerView.Adapter<FramePredictionAdapter.Viewholder> {

    private Context context;
    private ArrayList<FramePrediction> framePredArrayList;

    // Constructor
    public FramePredictionAdapter(Context cxt, ArrayList<FramePrediction> fpArrayList) {
        this.context = cxt;
        this.framePredArrayList = fpArrayList;
    }

    @NonNull
    @Override
    public FramePredictionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_frames, parent, false);
        return new FramePredictionAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FramePredictionAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        FramePrediction model = framePredArrayList.get(position);
        holder.rec_frameIV.setImageBitmap(model.getFrameBitmap());
        holder.predicted_classTV.setText(""+model.getPredicted_class());
        holder.crop_typeTV.setText("" + model.getCrop_type());
        holder.probabilityTV.setText("" + model.getProbability());
    }

    @Override
    public int getItemCount() {
        return framePredArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public static class Viewholder extends RecyclerView.ViewHolder {
        private ImageView rec_frameIV;
        private TextView predicted_classTV, crop_typeTV, probabilityTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rec_frameIV = itemView.findViewById(R.id.rec_frameFP);
            predicted_classTV = itemView.findViewById(R.id.predicted_classFP);
            crop_typeTV = itemView.findViewById(R.id.cropTypeFP);
            probabilityTV = itemView.findViewById(R.id.probabiltyFP);
        }
    }

}

