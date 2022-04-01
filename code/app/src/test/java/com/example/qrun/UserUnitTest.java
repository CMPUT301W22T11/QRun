package com.example.qrun;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.location.Location;

import org.junit.jupiter.api.Test;

public class UserUnitTest {

    Location loc = new Location("test");
    User testUser = new User(loc,"test");
    QRGame qrGame = new QRGame("Text","test",null,null,null);
    @Test
    public void testAddQr(){
        assertEquals(0, testUser.getTotalQR());
        testUser.addQR(qrGame);
        assertEquals(1, testUser.getTotalQR());
    }
    @Test
    public void testRemoveQr(){
        assertEquals(0, testUser.getTotalQR());
        testUser.addQR(qrGame);
        assertEquals(1, testUser.getTotalQR());
        testUser.removeQR(qrGame);
        assertEquals(0, testUser.getTotalQR());
    }
    @Test
    public void getUser(){
        assertEquals(testUser, testUser.getUser());
    }

    @Test
    public void setGetGameStatus(){
        testUser.setQrGameStatus(qrGame);
        assertEquals(testUser.getQRGameStatus(), qrGame);
    }
    @Test
    public void getUsernameTest(){
        assertEquals(testUser.getUsername(), "test");
    }
    @Test
    public void setGetUsernameTest(){
        assertEquals(testUser.getUsername(), "test");
        testUser.setUsername("test2");
        assertEquals(testUser.getUsername(), "test2");
    }


}
