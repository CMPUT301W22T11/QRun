package com.example.qrun;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Class to represent Scanned QRGame codes as a form of
 * Text, bytes, hexString and the points of the QRGame code
 */
public class QRGame extends QR implements Serializable {

    private long points;
    private Double lat, lon;
    private String path;
    private DocumentSnapshot snapshot;
    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param Text String that the QRGame code represents
     */
    public QRGame(String Text, String username, @Nullable Double lat, @Nullable Double lon,@Nullable String path){
        super(Text, username);
        this.points = QRCalculation.calcScore(this.getHexString());
        this.lat = lat;
        this.lon = lon;
        this.path = path;

    }
    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param id the Queried Document from Firestore
     */
    public QRGame(DocumentSnapshot id) {
        super("", null);
        snapshot = id;
        Map<String, Object> l = id.getData();
        this.username = (String)l.get("username");
        this.hexString = (String)l.get("hexString");
        this.lat = (Double) l.get("latitude");
        this.lon = (Double)l.get("longitude");
        this.path = (String)l.get("PicPath");
        this.points = (long)l.get("points");
    }
    public void setPoints(long points) {
        this.points = points;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(@Nullable Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(@Nullable Double lon) {
        this.lon = lon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(@Nullable String path) {
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
