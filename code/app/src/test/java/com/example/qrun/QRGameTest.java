package com.example.qrun;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QRGameTest {

    @Test
    public void testEquals(){
        QRGame qr1 = new QRGame("Tahiat", "tg123", 100.0, 200.6, "path");
        QRGame qr2 = new QRGame("Goni", "123", 10.1, 200.7, null);
        QRGame qr3 = new QRGame("Tahiat", "tg", 10.1, 200.7, null);

        assertTrue(qr1.equals(qr3));
        assertFalse(qr1.equals(qr2));
    }

    @Test
    public void testQRGameGetSets(){
        QRGame qr = new QRGame("TestQr1\n", "tahiat", 10.0, 11.0, "");
        assertEquals(39, qr.getPoints());

        assertEquals((Double) 10.0,  qr.getLat());
        assertEquals((Double) 11.0,  qr.getLon());

        qr.setLat(50.0);

        assertEquals((Double) 50.0, qr.getLat());

        qr.setPoints(50);

        assertNotEquals(39, qr.getPoints());
    }
}
