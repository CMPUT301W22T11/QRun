//https://programmer.ink/think/how-to-use-bitmap-to-store-pictures-into-database.html

package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddGameQR extends AppCompatActivity implements MapPointPopup.OnFragmentInteractionListener, LocationListener {
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

    private ActivityResultLauncher<Intent> cam = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        qr.setPath(bitmapToString(imageBitmap));
                        Log.d("QR Locations", String.valueOf(qr.getLat()) + " " + String.valueOf(qr.getLon()));
                        Log.d("----------------------------------------------------------------------------------------------------------------\n", qr.getPath());
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
                                else {
                                    ErrorFinish();
                                }
                            });
                        }
                        else {
                            ErrorFinish();
                        }


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
    private LocationManager locationManager;
    LatLng position;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_qr);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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

            new MapPointPopup().newInstance().show(getSupportFragmentManager(), "Map");


        });

    }
    private void ErrorFinish() {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    @Override
    public void onOkPressed() {
        qr.setLat(position.latitude);
        qr.setLon(position.longitude);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cam.launch(takePictureIntent);


    }

    @Override
    public void onDiscard() {
        qr.setLat(-1);
        qr.setLon(-1);

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
            Log.d("QR Locations", String.valueOf(qr.getLat()) + " " + String.valueOf(qr.getLon()));
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
                else {
                    ErrorFinish();
                }
            });
        }
        else {
            ErrorFinish();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        position = new LatLng(location.getLatitude(), location.getLongitude());
    }


    public String bitmapToString(Bitmap bitmap){
        //The pictures uploaded by the user in the activity are converted into String for storage
        String string;

        if(bitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();// Convert to byte array
            string= Base64.encodeToString(bytes,Base64.DEFAULT);
            return string;
        }
        else{
            return "";
        }
    }



}