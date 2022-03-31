package com.example.qrun;

/**
 * this class creates an QR object for sorting only
 * @author lucheng
 */
public class QRDist {
    private String qHash;
    private Double distance;
    private Long point;
    private Double lat;
    private Double lon;

    public QRDist(String qHash, Double distance, Long point, Double lat, Double lon) {
        this.qHash = qHash;
        this.distance = distance;
        this.point = point;
        this.lat = lat;
        this.lon = lon;
    }

    public String getqHash() {
        return qHash;
    }

    public Double getDistance() {
        return distance;
    }

    public Long getPoint() {
        return point;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}
