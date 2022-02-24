package com.example.qrun;

import java.io.Serializable;

public class QRUser extends QR implements Serializable {

    private String hexString;
    private byte[] bytes;
    private String Text;

    QRUser(String Text){
        super(Text);
        this.Text = Text;

    }


    /**
     *
     * @return String: QRcode text
     */
    public String getCodeText() {
        return Text;
    }
}
