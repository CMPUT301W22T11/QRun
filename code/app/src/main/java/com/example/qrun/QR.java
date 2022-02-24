package com.example.qrun;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class QR implements Serializable {

    private String hexString;
    private byte[] bytes;


    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param Text String that the QRGame code represents
     */
    QR(String Text){

        try{
            this.bytes = QRCalculation.getSHA(Text);
        }
        catch(NoSuchAlgorithmException e){
            //need to add error handling
        }
        this.hexString = QRCalculation.toHexString(this.bytes);
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
