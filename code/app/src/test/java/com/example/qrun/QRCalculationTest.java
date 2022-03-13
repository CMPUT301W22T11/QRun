package com.example.qrun;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QRCalculationTest {
    @Test
    public void testCalc() {
        long result = QRCalculation.calcScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, result);
        result = QRCalculation.calcScore("5d1291c0f67f819de7b7f9dba0be01216ce8f7d50a77159109ff9699dfe510a9");
        assertEquals(31, result);
    }
    @Test
    public void testHash() {
        try {
            String result = QRCalculation.toHexString(QRCalculation.getSHA("BFG5DGW54\n"));
            assertEquals(result, "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
            result = QRCalculation.toHexString(QRCalculation.getSHA("mldang\n"));
            assertEquals(result, "5d1291c0f67f819de7b7f9dba0be01216ce8f7d50a77159109ff9699dfe510a9");
        }
        catch (Exception e) {
            assertTrue(false);
        }
    }
}
