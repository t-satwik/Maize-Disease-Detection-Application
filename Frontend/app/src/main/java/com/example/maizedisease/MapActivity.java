package com.example.maizedisease;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.Mapbox;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private LatLng dest_point;
    private MapboxMap map;
    private Marker dest_marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        mapView=findViewById(R.id.mapView);
        Log.d("RANDOM", "on create of map activity");
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    public void onMapReady(MapboxMap mapboxMap){
        Log.d("RANDOM", "map is ready");
        map=mapboxMap;
        dest_point=new LatLng(17.5013888, 78.3843328);
        dest_marker=map.addMarker(new MarkerOptions().position(dest_point));
    }

    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}