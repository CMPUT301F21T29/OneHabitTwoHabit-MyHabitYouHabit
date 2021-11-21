package com.example.ohthmhyh.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ohthmhyh.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location location;
    private FloatingActionButton getLocationButton;

    public static final int LOCATIONOK = -2; // Required for result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get location from the EventActivity
        Intent location = getIntent();
        this.location = (Location)location.getExtras().get("LOCATION");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationButton = findViewById(R.id.get_location);
    }

    /**
     * When map is ready, sets the location
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currLoc = new LatLng(this.location.getLatitude(), this.location.getLongitude());
        updateMarker(new LatLng(this.location.getLatitude(), this.location.getLongitude()));

        // Making sure it's zoomed in properly
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15.0f));

        // If a location is selected on the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                updateMarker(latLng);
            }
        });

        Button set_location = findViewById(R.id.set_location_button);

        // If set location is clicked
        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exit = new Intent();
                exit.putExtra("LOCATION", location);

                setResult(MapActivity.LOCATIONOK, exit);
                finish();
            }
        });

        // If current location button is clicked
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


    }

    /**
     * Updates the marker on the map
     * @param currLoc location of the new marker
     */
    private void updateMarker(LatLng currLoc) {
        location.setLatitude(currLoc.latitude);
        location.setLongitude(currLoc.longitude);
        mMap.clear();   // Make sure the other markers are cleared.
        mMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .title("Set Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(currLoc));
    }

    /**
     * gets the current location and sets it to the location variable
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    /**
                     * Obtains the user's phone's location.
                     */
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        // Initialize location
                        Location currentLocation = task.getResult();
                        if (currentLocation != null) {
                            try {
                                // Get GeoCoder
                                Geocoder geocoder = new Geocoder(MapActivity.this,
                                        Locale.getDefault());
                                List<Address> addressList = geocoder.getFromLocation(
                                        currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

                                updateMarker(new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude()));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}