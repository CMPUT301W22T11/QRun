package com.example.qrun;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QRCalculation {
    /**
     * Hash the strings
     * Reference: https://www.baeldung.com/sha-256-hashing-java
     * @param input the raw string
     * @return the raw bytes after hashed
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes());
    }

    /**
     * Convert Byte to hex String
     * Reference: https://www.baeldung.com/sha-256-hashing-java
     * @param hash the raw hash data
     * @return the String modified hash
     */
    public static String toHexString(byte[] hash)
    {
        StringBuilder result = new StringBuilder();
        for (byte aByte : hash) {
            int decimal = (int) aByte & 0xff;               // bytes widen to int, need mask, prevent sign extension
            // get last 8 bits
            String hex = Integer.toHexString(decimal);
            if (hex.length() % 2 == 1) {                    // if half hex, pad with zero, e.g \t
                hex = "0" + hex;
            }
            result.append(hex);
        }
        return result.toString();
    }

    /**
     * Faster Power
     * @param x
     * @param n
     * @return
     */
    private static long myPow(int x, int n) {
        // return pow(x, n);
        long res = 1;
        int power = n;
        if(power == 0) return 1;
        while(power > 0) {
            if(power % 2 == 0) {
                x = x * x;
                power /= 2;
            }
            else {
                res = x * res;
                power--;
            }
        }
        return res;
    }

    /**
     * calculate the score based on hashed String
     * @param hexString hashed String
     * @return the actual point of the hashed string
     */
    public static long calcScore(String hexString) {
        long point = 0;
        char before = hexString.charAt(0);
        int noCount = 0;
        for(int i = 1; i < hexString.length(); i++) {
            if(before == hexString.charAt(i)) {
                noCount++;
            }
            else if(noCount > 0) {
                int num = Integer.parseInt(Character.toString(before), 16);
                if(num == 0) {
                    num = 20;
                }
                point += myPow(num, noCount);
                noCount = 0;
            }
            before = hexString.charAt(i);
        }
        return point;
    }

}
