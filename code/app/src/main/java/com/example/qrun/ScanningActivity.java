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

    private static int CAMERA_REQUEST_CODE = 101;

    private CodeScanner codeScanner;



    private CodeScannerView scannerView;

    private TextView codeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

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
                QR qr = new QR(result.getText());
                new AddQRPopup().newInstance(qr).show(getSupportFragmentManager(), "Add QR");

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