package com.example.qrun;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserStorageUnitTest {
    FirebaseFirestore firestore;
    @BeforeEach
    public void setup() {
//        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("localhost", 8080);
    }
    @Test
    public void add() {
        UserStorage storage = new UserStorage(firestore);
        HashMap<String, Object> testData = new HashMap();
        testData.put("UUID", "1234567");
        storage.add("mldang", testData, (boolean isTrue) -> {
            assertTrue(isTrue);
        });
    }
}
