package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Main Screen after login
 */
public class MainScreen extends AppCompatActivity {
    private final static int SUCCESS = 0;
    MenuItem item;
    SharedPreferences prefs;
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Log.d("add QR()", "Add QR Successfully");
                        Toast.makeText(ctx, "Add QR Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("add QR()", "Failed to Add QR");
                        Toast.makeText(ctx, "Failed to Add QR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            //Start your code
        } else {
            //Show snackbar
        }
    }
    ImageButton cameraBut;
    Button mapsButton;
    String userName;
    ImageView qrCodeImage;
    Context ctx;
    UserStorage userStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, SUCCESS);
        setContentView(R.layout.activity_main_screen);
        ctx = this;
        item = findViewById(R.id.adminBut);
        qrCodeImage =  (ImageView) findViewById(R.id.qrCodeImage);
        Bundle extras = getIntent().getExtras();
        cameraBut = findViewById(R.id.cameraButton);
        prefs = getApplicationContext().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE); // Get the Shared preferences
        userName = prefs.getString("usrName", null);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userStorage = new UserStorage(db);

        QRGenerator qrCodeGen = new QRGenerator();
        Bitmap qrGen= qrCodeGen.generateQRBitmap(userName,this);
        qrCodeImage=(ImageView) findViewById(R.id.qrCodeImage);
        qrCodeImage.setImageBitmap(qrGen);
        mapsButton = findViewById(R.id.mapButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * The callback for camera button
     * @param view
     */
    public void cameraButton(View view){
        cameraBut.setOnClickListener((l) -> {
            Intent intent = new Intent(this, AddGameQR.class);
            intent.putExtra("userName", userName);
            ac.launch(intent);
        });
    }
    @Override
    public void onBackPressed() {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        item = menu.findItem(R.id.adminBut);
        userStorage.get(userName, (data) -> {
            if(data != null) {

                Object temp = data.get("isOwner");
                if (temp != null) {
                    boolean isOwner = (boolean) temp;
                    if (isOwner) {
                        item.setVisible(true);
                    } else {
                        item.setVisible(false);
                    }
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.socialBut: {
                Intent intent = new Intent(this, UserListingActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
                break;
            }

            case R.id.profileBut: {
                Intent intent = new Intent(this, PlayerProfile.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
                break;
            }
            case R.id.collectionBut: {
                Intent intent = new Intent(this, QRGameListActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            }
            case R.id.logoutBut: {
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("usrName");
                editor.commit();
                finish();
                break;
            }
            case R.id.adminBut: {
                Intent intent = new Intent(this, ImageCompressionActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}