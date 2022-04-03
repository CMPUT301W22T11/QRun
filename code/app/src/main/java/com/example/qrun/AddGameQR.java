//https://programmer.ink/think/how-to-use-bitmap-to-store-pictures-into-database.html

package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.function.Consumer;

public class AddGameQR extends AppCompatActivity implements MapPointPopup.OnFragmentInteractionListener, LocationListener, Consumer<Location> {
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if (result.getResultCode() == 1) {
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
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        Log.d("QR Locations", String.valueOf(qr.getLat()) + " " + String.valueOf(qr.getLon()));
                        Log.d("----------------------------------------------------------------------------------------------------------------\n", qr.getPath());
                        if (qr != null) {
                            addQR(qr, imageBitmap);
                        } else {
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
    private Double lat = null; //TODO: modify this when we have an actual map
    private Double lon = null; //TODO: modify this when we have an actual map
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
        ctx = this;
        cancelbut = findViewById(R.id.cancelbutton_id);
        addbut = findViewById(R.id.addbutton_id);
        scanbut = findViewById(R.id.scanningbutton);
        hashId = findViewById(R.id.hashholder);
        pointsId = findViewById(R.id.pointholder);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("userName");
        }
        cancelbut.setOnClickListener((l) -> {
            Intent intent = new Intent();
            setResult(3, intent);
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
        qr.setLat(null);
        qr.setLon(null);
        qr.setPath(null);
        if(qr != null) {
            addQR(qr, null);
        }
        else {
            ErrorFinish();
        }
    }
    private void addQR(QRGame qr, Bitmap bitmap) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", qr.getUsername());
        data.put("hexString", qr.getHexString());
        data.put("points", qr.getPoints());
        data.put("latitude", qr.getLat());
        data.put("longitude", qr.getLon());
        data.put("PicPath", qr.getPath());
        QRStorage storage = new QRStorage(FirebaseFirestore.getInstance());
        UserStorage user = new UserStorage(FirebaseFirestore.getInstance());
        storage.getCol().whereEqualTo("username", qr.getUsername())
                .whereEqualTo("hexString", qr.getHexString())
                .get()
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if(document.size() == 0) {
                            // upload image
                            if(bitmap != null) {
                                String id = storage.getCol().document().getId();
                                StorageReference ref = FirebaseStorage.getInstance().getReference();
                                StorageReference imagesRef = ref.child(id + ".jpg");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                new UserStorage(FirebaseFirestore.getInstance()).getCol()
                                        .whereEqualTo("isOwner", true)
                                        .get().addOnSuccessListener((data1) -> {
                                            int compressionVal = (int)(100 - (long)data1.getDocuments().get(0).get("Compression"));
                                            Log.d("Compression Getter", "Compression Value is: " + String.valueOf(compressionVal));
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, compressionVal, baos);
                                            byte[] dataImg = baos.toByteArray();
                                            UploadTask uploadTask = imagesRef.putBytes(dataImg);
                                            uploadTask.addOnCompleteListener((l) -> {
                                                data.put("PicPath", id + ".jpg");
                                                storage.add(id, data, (isComplete) -> {
                                                    if(isComplete) {
                                                        user.updateUser(qr.getUsername(), storage, (isSuccess) -> {
                                                            if(isSuccess) {
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
                                        });

                                }).addOnFailureListener((l) -> ErrorFinish());
                            }
                            else {
                                storage.add(data, (isComplete) -> {
                                    if(isComplete) {
                                        user.updateUser(qr.getUsername(), storage, (isSuccess) -> {
                                            if(isSuccess) {
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

                        }
                        else {
                            ErrorFinish();
                        }
                    }
                });
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        position = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            //Start your code
        } else {
            //Show snackbar
        }
    }

    @Override
    public void accept(Location location) {
        position = new LatLng(location.getLatitude(), location.getLongitude());
    }
}