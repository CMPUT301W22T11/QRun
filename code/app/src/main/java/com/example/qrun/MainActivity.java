package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static String SCAN_MODE_KEY = "com.example.qrun.SCAN_MODE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ScanningActivity.class);
        intent.putExtra(SCAN_MODE_KEY, ScanningActivity.SCAN_MODE_POINTS);
        startActivity(intent);
    }
}