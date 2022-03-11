package com.example.qrun;

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
    ImageButton cameraBut;
    Button mapsButton;
    String userName;
    Boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();
        isAdmin = true;//put the admin checking condition here
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
                }

                );


    }

    public void cameraButton(View view){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        if(isAdmin == false){
             MenuItem adminButton = menu.findItem(R.id.adminBut);
             adminButton.setVisible(false);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.profileBut:
                Intent intent = new Intent(this, PlayerProfile.class);
                intent.putExtra("userName",userName);
                startActivity(intent);

            case R.id.adminBut:
                Intent intent1 = new Intent(this, AdminActivity.class);
                intent1.putExtra("userName",userName);
                startActivity(intent1);





        }
        return  super.onOptionsItemSelected(item);
    }
}
