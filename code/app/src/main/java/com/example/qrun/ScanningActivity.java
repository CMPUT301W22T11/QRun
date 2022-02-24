//links to used resources : https://www.youtube.com/watch?v=drH63NpSWyk

package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;


public class ScanningActivity extends AppCompatActivity implements AddQRPopup.OnFragmentInteractionListener{

    public static int SCAN_MODE_POINTS = 1;
    public static int SCAN_MODE_USER = 2;
    public static int SCAN_MODE_STATUS = 3;

    private static int CAMERA_REQUEST_CODE = 101;

    private CodeScanner codeScanner;


    private int scanMode;


    private CodeScannerView scannerView;

    private TextView codeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        //default scan mode is to scan for points
        scanMode = (int) getIntent().getIntExtra(MainActivity.SCAN_MODE_KEY, SCAN_MODE_POINTS);

        scannerView = (CodeScannerView) findViewById(R.id.scanner_view);
        codeText = findViewById(R.id.code_text);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setFlashEnabled(false);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                codeScanner.setScanMode(ScanMode.PREVIEW);
                if(scanMode == SCAN_MODE_POINTS) {
                    QR qr = new QR(result.getText());
                    new AddQRPopup().newInstance(qr).show(getSupportFragmentManager(), "Add QR");
                }
                else if(scanMode == SCAN_MODE_USER) {
                    QR qr = new QR(result.getText());

                    //add more code here for handling user
                    codeText.setText("Username : " + String.valueOf(qr.getCodeText()));

                }
                else if(scanMode == SCAN_MODE_STATUS) {
                    QR qr = new QR(result.getText());

                    //add more code here for handling status
                    codeText.setText("User Status : " + String.valueOf(qr.getCodeText()));

                }

            }
        });

        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {

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
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onOkPressed(QR qr) {
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.startPreview();
        codeText.setText(String.valueOf(qr.getPoints()));
    }

    @Override
    public void onDiscard() {
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.startPreview();
    }


}