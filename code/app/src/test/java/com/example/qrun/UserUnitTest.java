package com.example.qrun;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.location.Location;

import org.junit.jupiter.api.Test;

public class UserUnitTest {


    User testUser = new User("test");
    QRGame qrGame = new QRGame("Text","test",null,null,null);
    @Test
    public void testGetSetEmail(){
        String email = "test@test.com";
        testUser.setEmail(email);
        assertEquals(email, testUser.getEmail());
    }


    @Test
    public void testGetSetName(){
        String name = "test2";
        testUser.setName(name);
        assertEquals(name, testUser.getName());
    }
    @Test
    public void testGetSetPhone(){
        String phoneNum = "123123";
        testUser.setPhoneNumber(phoneNum);
        assertEquals(phoneNum, testUser.getPhoneNumber());
    }
    @Test
    public void testGetSetTotalScannedQR(){
        long scannedQR = 3;
        testUser.setTotalscannedqr(scannedQR);
        assertEquals(scannedQR, testUser.getTotalscannedqr());
    }
    @Test
    public void testGetSetTotalSum(){
        long totalSum = 2;
        testUser.setTotalsum(totalSum);
        assertEquals(totalSum, testUser.getTotalsum());
    }
    @Test
    public void testGetSetUniqueQR(){
        long uniqueQR = 4;
        testUser.setUniqueqr(uniqueQR);
        assertEquals(uniqueQR, testUser.getUniqueqr());
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
