//https://pragmaapps.com/add-custom-image-in-google-maps-marker-android/

package com.example.qrun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.qrun.databinding.ActivityMapsBinding;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, ImagePopup.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private ArrayList<QRGame> points = new ArrayList<>();
    private HashMap<Marker, String> images = new HashMap<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    FirebaseFirestore QrRun;
    Marker preferredQrMarker;
    Button searchBut;
    private EditText locationSearch;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QrRun = FirebaseFirestore.getInstance();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationSearch = (EditText) findViewById(R.id.location_text);

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
                marker.showInfoWindow();
                return true;
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
                                    Log.d("Storage get", "-------------------------------------------------------" + String.valueOf(qr.getPoints()));
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

    /**
     * move the camera to the location specified by user showing QRs nearby
     * code source: https://www.javatpoint.com/android-google-map-search-location-using-geocodr
     * @param view
     * @author lucheng
     */
    public void searchLocation(View view) {
        String location = locationSearch.getText().toString();
        List<Address> addressList = new ArrayList<>();

        Geocoder geocoder = new Geocoder(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


        try {
            addressList.addAll(geocoder.getFromLocationName(location, 1));
            assert addressList != null;
            Log.d("TAG", String.valueOf(addressList.size()));
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(MapsActivity.this)
                    .setMessage("Stay for the view or go for the treasure hunt?")
                    .setPositiveButton("GO!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                            double[] latLon = {address.getLatitude(),address.getLongitude()};
                            intent.putExtra("position",latLon);
//                            MapsActivity.this.startActivityForResult(intent,101);
                            startActivityForResult(intent,222);

                        }
                    })
                    .setNegativeButton("Sure" , null).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
            }
        },1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==222){
            String received  = data.getStringExtra("selectedQr");
            Toast.makeText(MapsActivity.this, " "+received, Toast.LENGTH_SHORT).show();
            DocumentReference docRef = QrRun.collection("QR").document(received);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            QRGame selectedQr = new QRGame(document);
                            LatLng selectedPosition = new LatLng(selectedQr.getLat(),selectedQr.getLon());
                            preferredQrMarker = mMap.addMarker(new MarkerOptions()
                                    .position(selectedPosition)
                                    .title(String.valueOf(selectedQr.getPoints()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            images.put(preferredQrMarker, selectedQr.getPath());
                            preferredQrMarker.showInfoWindow();
                            markers.add(preferredQrMarker);
                        }
                    }

                });
        }
    }
}