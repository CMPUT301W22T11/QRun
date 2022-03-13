//https://pragmaapps.com/add-custom-image-in-google-maps-marker-android/

package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.qrun.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, ImagePopup.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private ArrayList<QRGame> points = new ArrayList<>();
    private HashMap<Marker, String> images = new HashMap<>();
    FirebaseFirestore QrRun;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QrRun = FirebaseFirestore.getInstance();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        String image = "test String"; // will be replaced by resource from the firestore


        points.add(new QRGame("one", "test", 53.523220, -113.526321, image));
        points.add(new QRGame("two", "test", 53.427920, -113.528660, image));
        points.add(new QRGame("three", "test", 53.518050, -113.446460, image));



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                ImagePopup.newInstance(images.get("images")).show(getSupportFragmentManager(), "Image");

                return false;
            }
        });

        QRGame qr;



        // Add a marker
        for(int i = 0; i < points.size(); i++){
            qr = points.get(i);
            LatLng add = new LatLng(qr.getLat(), qr.getLon());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(add)
                    .title(String.valueOf(qr.getPoints()))
            );

            images.put(marker, qr.getPath());
            Log.d("--------------------------------------------------------------------------------------------------------------", String.valueOf(qr.getPoints()));

        }



    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng me = new LatLng(location.getLatitude(), location.getLongitude());


    }

    @Override
    public void onOkPressed() {

    }

    @Override
    public void onDiscard() {

    }



}