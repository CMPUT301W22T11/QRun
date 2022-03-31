package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is an activity to display the sorted qr within 1km from user specified location
 * @author: lucheng
 */
public class SearchActivity extends AppCompatActivity {
    Double lat;
    Double lon;
    ListView qrList;
    ArrayAdapter<QRDist> qrAdapter;
    ArrayList<QRDist> qrDisList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        double[] position = getIntent().getExtras().getDoubleArray("position");//get specified location by user search
        lat = position[0];
        lon = position[1];
        //Log.d("check", lon.toString());
        qrList = findViewById(R.id.qr_list);
        qrDisList = new ArrayList<QRDist>();
        qrAdapter = new CustomSortedQr(this, qrDisList);
        qrList.setAdapter(qrAdapter);
        db = FirebaseFirestore.getInstance();
        CollectionReference QRef = db.collection("QR");
        QRef
                .whereNotEqualTo("latitude",null)//skip test qr without lat&lon
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
            qrDisList.clear();
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots){//query through QR data base
                String qid = doc.getId();
                Long point = (Long) doc.getData().get("points");
                Double longitude = (Double) doc.getData().get("longitude");
                Double latitude = (Double) doc.getData().get("latitude");
                Double distance = distConverter(lat,lon,latitude,longitude);
                if(distance < 1000){//display only qr within 1km from specified location
                    qrDisList.add(new QRDist(qid,distance,point,latitude,longitude));
                }
            }
            Collections.sort(qrDisList, new Comparator<QRDist>() {//sort QRs in ascending orders by their distance from location entered
                @Override
                public int compare(QRDist o1, QRDist o2) {
                    return o1.getDistance().compareTo(o2.getDistance());
                }
            });
            qrAdapter.notifyDataSetChanged();
        });
        qrList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent returnIntent = new Intent(this,MapsActivity.class);
            returnIntent.putExtra("selectedQr",qrDisList.get(i).getqHash());//sent preferred location back to map
            startActivity(returnIntent);
        });
    }
    /**
     * This function will give us the distance in meters between two geo position.
     * source: https://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi
     * original author: https://stackoverflow.com/users/5446/sean
     * modified by: lucheng
     */
    public static double distConverter(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // km ( 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c * 1000;

        return dist;
    }
}