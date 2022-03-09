package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainScreen extends AppCompatActivity {
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Log.d("add QR()", "ADD QR Successfully");
                    }
                    else {
                        Log.d("add QR()", "Failed to Add QR");
                    }
                }
            }
    );
    private Button showProfileBut; // TODO: Remove this when Zi's part complete
    ImageButton cameraBut;
    Button mapsButton;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();
        cameraBut = findViewById(R.id.cameraButton);
        showProfileBut = findViewById(R.id.temp); // TODO: Remove this when Zi's part complete
        showProfileBut.setText("Remove This!!!!");// TODO: Remove this when Zi's part complete
        if(extras != null){
            userName = extras.getString("userName");
            Log.d("xx",userName);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage userStorage = new UserStorage(db);
        userStorage.get(userName, (data)->{
            if(data!=null){
                String x = (String) data.get("email");
                Log.d("xx",x);
            }
        });
        cameraBut.setOnClickListener((l) -> {
            Intent intent = new Intent(this, AddGameQR.class);
            intent.putExtra("userName", userName);
            ac.launch(intent);
        });
        showProfileBut.setOnClickListener((l) -> {
            Intent intent = new Intent(this, QRGameListActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.profileBut:
                Intent intent = new Intent(this, PlayerProfile.class);
                intent.putExtra("userName",userName);
                startActivity(intent);

        }
        return  super.onOptionsItemSelected(item);
    }
}