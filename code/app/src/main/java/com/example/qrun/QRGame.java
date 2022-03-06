package com.example.qrun;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Class to represent Scanned QRGame codes as a form of
 * Text, bytes, hexString and the points of the QRGame code
 */
public class QRGame extends QR implements Serializable {



    private long points;
    private double lat, lon;
    private String path;

    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param Text String that the QRGame code represents
     */
    public QRGame(String Text, String username, double lat, double lon, String path){
        super(Text, username);
        this.points = QRCalculation.calcScore(this.getHexString());
        this.lat = lat;
        this.lon = lon;
        this.path = path;

    }
    public QRGame(String id) {
        super(null, null);
        QRStorage storage = new QRStorage(FirebaseFirestore.getInstance());
        storage.get(id, (l) -> {
            if(l != null) {
                String username = (String)l.get("username");
                String hexString = (String)l.get("hexString");
                lat = Double.valueOf((String)l.get("latitude"));
                lon = Double.valueOf((String)l.get("longitude"));
                path = (String)l.get("PicPath");
                points = Long.valueOf((String)l.get("points"));
                this.hexString = hexString;
                this.username = username;
            }
        });
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return long: points of the code
     */
    public long getPoints(){
        return this.points;
    }


}
