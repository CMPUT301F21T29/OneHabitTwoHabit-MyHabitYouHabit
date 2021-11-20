package com.example.ohthmhyh.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ohthmhyh.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location location;

    public static final int LOCATIONOK = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent location = getIntent();
        this.location = (Location)location.getExtras().get("LOCATION");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currLoc = new LatLng(this.location.getLatitude(), this.location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .title("Current Location"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                LatLng currLoc = new LatLng(latLng.latitude, latLng.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(currLoc)
                        .title("Set Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currLoc));
            }
        });

        Button set_location = findViewById(R.id.set_location_button);
        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exit = new Intent();
                exit.putExtra("LOCATION", location);

                setResult(MapActivity.LOCATIONOK, exit);
                finish();
            }
        });
    }

}