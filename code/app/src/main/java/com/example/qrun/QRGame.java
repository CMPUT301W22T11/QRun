package com.example.qrun;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Class to represent Scanned QRGame codes as a form of
 * Text, bytes, hexString and the points of the QRGame code
 */
public class QRGame extends QR implements Serializable {



    private long points;


    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param Text String that the QRGame code represents
     */
    QRGame(String Text){

        super(Text);

        this.points = QRCalculation.calcScore(this.getHexString());
    }



    /**
     *
     * @return long: points of the code
     */
    public long getPoints(){
        return this.points;
    }


}
