package com.example.qrun;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.location.Location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UserClassUnitTest {
    Location TestLocation = new Location("TestLocation");
    User TestUser = new User(TestLocation, "TestUser");
    QR TestQR=new QR("TestQR");
    @BeforeEach
    public void settings() {
        TestUser.addQR(TestQR);

    }
    @Test
    public void testAddQR(){
        assertEquals(1,TestUser.getTotalQR());
    }
    @Test
    public void testGetStoredQr(){
        ArrayList<QR> temp=new ArrayList<>();
        temp=TestUser.getStoredQr();
        assertEquals(true,temp.get(0).equals(TestQR));
    }
    @Test
    public void testRemoveQR(){
        TestUser.removeQR(TestQR);
        assertEquals(0,TestUser.getTotalQR());
    }
    @Test
    public void TestGetUser(){
        assertEquals("TestUser",TestUser.getUsername());
    }
    @Test
    public void getQRGameStatus(){
        TestUser.setQrGameStatus(TestQR);
        assertEquals(TestQR,TestUser.getQRGameStatus());
    }
    @Test
    public void TestSetQrGameStatus(){
        QR testQR2=new QR("TestQR2");
        TestUser.setQrGameStatus(testQR2);
        assertEquals(testQR2,TestUser.getQRGameStatus());
    }
    @Test
    public void TestGetLocation(){
        assertTrue(TestLocation.equals(TestUser.getLocation()));
    }
    @Test
    public void TestSetLocation(){
        Location Location2=new Location("TestLocation2");
        TestUser.setLocation(Location2);
        assertEquals(Location2,TestUser.getLocation());
    }
    @Test
    public void TestGetUserName(){
        assertEquals("TestUser",TestUser.getUsername());
    }
    @Test
    public void TestSetUserName(){
        TestUser.setUsername("TestUser2");
        assertEquals("TestUser2",TestUser.getUsername());
    }
}
