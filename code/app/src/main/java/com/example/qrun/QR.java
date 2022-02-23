package com.example.qrun;

import com.google.common.primitives.Bytes;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Class to represent Scanned QR codes as a form of
 * Text, bytes, hexString and the points of the QR code
 */
public class QR implements Serializable {

    private String Text;
    private String hexString;
    private byte[] bytes;
    private long points;


    /**
     * Constructor for QR takes in the text that the QR represents
     * The constructor uses this text to calculate the points of the QR
     * @param Text String that the QR code represents
     */
    QR(String Text){
        this.Text = Text;
        try{
            this.bytes = QRCalculation.getSHA(Text);
        }
        catch(NoSuchAlgorithmException e){
            //need to add error handling
        }
        this.hexString = QRCalculation.toHexString(this.bytes);

        this.points = QRCalculation.calcScore(hexString);
    }


    /**
     *
     * @return String: QRcode text
     */
    public String getCodeText() {
        return Text;
    }

    /**
     *
     * @return long: points of the code
     */
    public long getPoints(){
        return this.points;
    }

    /**
     *
     * @return String: hexString of the code
     */
    public String getHexString(){
        return this.hexString;
    }



    @Override
    public boolean equals(Object object){
        QR rhs = (QR) object;

        if(this.hexString.equals(rhs.getHexString())){
            return true;
        }

        return false;
    }

}
