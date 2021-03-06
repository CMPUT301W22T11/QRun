//links to used resources : https://www.youtube.com/watch?v=drH63NpSWyk

package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

/**
 * This is the actual Scanning Activity, this will scan the QR Code, then send the raw String back
 * to the previous Activity
 */
public class ScanningActivity extends AppCompatActivity {

    public static int SCAN_MODE_POINTS = 1;
    public static int SCAN_MODE_USER = 2;
    public static int SCAN_MODE_STATUS = 3;

    private static int CAMERA_REQUEST_CODE = 101;

    private CodeScanner codeScanner;


    private int scanMode;


    private CodeScannerView scannerView;

    private TextView codeText;
    private ImageView qrImage;
    private Context ctx;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1011: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Here user granted the permission
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ScanningActivity.this, "Permission denied to read your Camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        ctx = this;

        //default scan mode is to scan for points
//        scanMode = (int) getIntent().getIntExtra(MainActivity.SCAN_MODE_KEY, SCAN_MODE_POINTS);

        scannerView = (CodeScannerView) findViewById(R.id.scanner_view);
//        codeText = findViewById(R.id.code_text);
//        qrImage = findViewById(R.id.qr_code);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setFlashEnabled(false);
        this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                codeScanner.setScanMode(ScanMode.PREVIEW);
                Intent intent = new Intent();
                intent.putExtra("QR", result.getText());
                setResult(1, intent);
                finish();
            }
        });

        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}