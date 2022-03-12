package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ShowPlayerProfile extends AppCompatActivity {
    private TextView usernameText, profileText, statusText;
    private String username;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showplayerprofile);
        usernameText = findViewById(R.id.username);
        profileText = findViewById(R.id.profilethings);
        statusText = findViewById(R.id.statusprof);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }
        usernameText.setText(username);
        UserStorage userStorage = new UserStorage(FirebaseFirestore.getInstance());
        userStorage.get(username, (data) -> {
            if(data != null) {
                String profile = "Name: " + (String)data.get("name") + "\n" +
                                "Email: " + (String)data.get("email") + "\n" +
                                "Tel: " + (String)data.get("phone") + "\n";
                profileText.setText(profile);
                long totalPoints = 0;
                long totalScanned = 0;
                Object temp = data.get("totalpoints");
                if(temp != null) {
                    totalPoints = (long)temp;
                }
                temp = data.get("totalscannedqr");
                if(temp != null) {
                    totalScanned = (long)temp;
                }
                String status = "Unique QR Ranking: " + "TODO" + "\n" + // TODO: get the ranking of the QR
                                "Total Points: " + String.valueOf(totalPoints) + "\n" +
                                "Total QR Scanned: " + String.valueOf(totalScanned);
                statusText.setText(status);
            }
        });
    }
}