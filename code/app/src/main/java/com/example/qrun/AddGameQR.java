package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddGameQR extends AppCompatActivity {
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        String rawQR = (String) intent.getSerializableExtra("QR");
                        qr = new QRGame(rawQR, username, lat, lon, path);
                        String hash = "QR Hex: " + qr.getHexString();
                        hashId.setText(hash);
                        pointsId.setText("Total Points: " + String.valueOf(qr.getPoints()));
                    }
                }
            }
    );
    QRGame qr = null;
    private Button cancelbut, addbut, scanbut;
    private TextView hashId, pointsId;
    private String path = ""; // TODO: modify this when we can take picture
    private double lat = 0.0; //TODO: modify this when we have an actual map
    private double lon = 0.0; //TODO: modify this when we have an actual map
    private String username;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isExist = false;
        setContentView(R.layout.activity_add_game_qr);
        ctx = this;
        cancelbut = findViewById(R.id.cancelbutton_id);
        addbut = findViewById(R.id.addbutton_id);
        scanbut = findViewById(R.id.scanningbutton);
        hashId = findViewById(R.id.hashholder);
        pointsId = findViewById(R.id.pointholder);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }
        cancelbut.setOnClickListener((l) -> {
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
        });
        scanbut.setOnClickListener((l) -> {
            Intent intent = new Intent(ctx, ScanningActivity.class);
            ac.launch(intent);
        });
        addbut.setOnClickListener((l) -> {
            if(qr != null) {
                QRStorage storage = new QRStorage(FirebaseFirestore.getInstance());
                UserStorage user = new UserStorage(FirebaseFirestore.getInstance());
                HashMap<String, Object> data = new HashMap<>();
                data.put("username", qr.getUsername());
                data.put("hexString", qr.getHexString());
                data.put("points", qr.getPoints());
                data.put("latitude", qr.getLat());
                data.put("longitude", qr.getLon());
                data.put("PicPath", qr.getPath());
                storage.getCol().whereEqualTo("hexString", qr.getHexString())
                        .get().addOnCompleteListener((callback) -> {
                            if(callback.getResult() == null) {
                                storage.add(data, (isSuccess) -> {
                                    if(isSuccess) {
                                        Log.d("QRGame", "Created Successfully");
                                        user.get(username, (userData) -> {
                                            if(userData != null) {
                                                long totalPoints, totalScanned;
                                                Object temp = userData.get("totalpoints");
                                                if(temp == null) {
                                                    totalPoints = qr.getPoints();
                                                }
                                                else {
                                                    totalPoints = (long)temp;
                                                    totalPoints += qr.getPoints();
                                                }
                                                temp = userData.get("totalscannedqr");
                                                if(temp == null) {
                                                    totalScanned = 1;
                                                }
                                                else {
                                                    totalScanned = (long)temp;
                                                    totalScanned++;
                                                }
                                                userData.put("totalscannedqr", totalScanned);
                                                userData.put("totalpoints", totalPoints);
                                                user.update(username, userData, (isUserSuccess) -> {
                                                    if(isUserSuccess) {
                                                        Intent intent = new Intent();
                                                        setResult(1, intent);
                                                        finish();
                                                    }
                                                    else {
                                                        ErrorFinish();
                                                    }
                                                });
                                            }
                                            else {
                                                ErrorFinish();
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                ErrorFinish();
                            }
                        });
            }
            else {
                ErrorFinish();
            }
        });

    }
    private void ErrorFinish() {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }
}