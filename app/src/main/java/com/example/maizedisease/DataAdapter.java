package com.example.maizedisease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maizedisease.DataModel;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Viewholder> {

    private Context context;
    private ArrayList<DataModel> dataModelArrayList;

    // Constructor
    public DataAdapter(Context cxt, ArrayList<DataModel> dmArrayList) {
        this.context = cxt;
        this.dataModelArrayList = dmArrayList;
    }

    @NonNull
    @Override
    public DataAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        DataModel model = dataModelArrayList.get(position);
        holder.rec_imageIV.setImageBitmap(model.getDecoded_image());
        holder.predicted_classTV.setText(""+model.getPredicted_class());
        holder.time_stampTV.setText("" + model.getTime_stamp());
        holder.crop_typeTV.setText("" + model.getCrop_type());
        holder.latitudeTV.setText("" + model.getLatitude());
        holder.longitudeTV.setText("" + model.getLongitude());
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public static class Viewholder extends RecyclerView.ViewHolder {
        private ImageView rec_imageIV;
        private TextView time_stampTV, predicted_classTV, crop_typeTV, latitudeTV, longitudeTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rec_imageIV = itemView.findViewById(R.id.rec_image);
            time_stampTV = itemView.findViewById(R.id.time_stamp);
            predicted_classTV = itemView.findViewById(R.id.predicted_class);
            crop_typeTV = itemView.findViewById(R.id.crop_type);
            latitudeTV = itemView.findViewById(R.id.latitude);
            longitudeTV = itemView.findViewById(R.id.longitude);
        }
    }
}
