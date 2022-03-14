package com.example.qrun;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QRTest {

    @Test
    public void testEquals(){
        QR qr1 = new QR("Tahiat", "tg123");
        QR qr2 = new QR("Goni", "tg123");
        QR qr3 = new QR("Tahiat", "cat");

        assertTrue(qr1.equals(qr3));
        assertFalse(qr1.equals(qr2));
    }

    @Test
    public void testQRGamePoints(){
        QRGame qr = new QRGame("TestQr1\n", "tahiat", 10.0, 11.0, "");
        assertEquals(39, qr.getPoints());

    }


}