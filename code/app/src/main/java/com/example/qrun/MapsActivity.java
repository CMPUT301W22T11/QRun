//https://pragmaapps.com/add-custom-image-in-google-maps-marker-android/

package com.example.qrun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.qrun.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, ImagePopup.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private ArrayList<QRGame> points = new ArrayList<>();
    private HashMap<Marker, String> images = new HashMap<>();
    private ArrayList<Marker> markers = new ArrayList<>();
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


        QRStorage storage = new QRStorage(FirebaseFirestore.getInstance());
        //points.add(new QRGame("one", "test", 53.523220, -113.526321, image));
        //points.add(new QRGame("two", "test", 53.427920, -113.528660, image));
        //points.add(new QRGame("three", "test", 53.518050, -113.446460, image));
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

                ImagePopup.newInstance(images.get(marker)).show(getSupportFragmentManager(), "Image");

                return false;
            }
        });

        //get the QRs from storage
        QrRun.collection("QR").get()
                .addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                points.add(new QRGame(document));

                            }
                            QRGame qr;

                            Random rand = new Random();

                            // Add a marker
                            for(int i = 0; i < points.size(); i++){
                                qr = points.get(i);
                                if(qr.getLat() != null && qr.getLon() != null){
                                    LatLng add = new LatLng(qr.getLat(), qr.getLon());
                                    if(samePointMarkerHandler(add)){
                                        add = new LatLng(qr.getLat() + (rand.nextInt(10)*0.01), qr.getLon() + (rand.nextInt(10) * 0.01));
                                    }
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(add)
                                            .title(String.valueOf(qr.getPoints()))
                                    );
                                    Log.d("Storage get", "-------------------------------------------------------" + add.toString());
                                    images.put(marker, qr.getPath());
                                    markers.add(marker);
                                }
                            }
                        }
                        else{
                            Log.d("Storage get", "Could not retrieve data", task.getException());
                        }
                    }
                }));









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

    //if a marker is at the same point
    //a small offset needs to be set to distinguish each value
    private boolean samePointMarkerHandler(LatLng point){

        for(Marker marker : markers){
            if(point.equals(marker.getPosition())){
                return true;

            }
        }
        return false;
    }

}