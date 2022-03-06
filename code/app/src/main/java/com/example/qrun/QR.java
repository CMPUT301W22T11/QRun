package com.example.qrun;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class QR implements Serializable {

    String hexString;
    private byte[] bytes;
    String username;

    /**
     * Constructor for QRGame takes in the text that the QRGame represents
     * The constructor uses this text to calculate the points of the QRGame
     * @param Text String that the QRGame code represents
     */
    QR(String Text, String username){

        try{
            this.bytes = QRCalculation.getSHA(Text);
        }
        catch(NoSuchAlgorithmException e){
            //need to add error handling
        }
        this.hexString = QRCalculation.toHexString(this.bytes);
        this.username = username;
    }
    QR(String documentName) {

    }

    public void setHexString(String hexString) {
        this.hexString = hexString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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