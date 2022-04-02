package com.example.qrun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * This class is similar to the PlayerProfile, however this is meant for displaying user profile
 * after searching from user listing activity
 */
public class UserProfileExternal extends AppCompatActivity {
    String userName, actualUsername;
    String email;
    String phone;
    String name;
    TextView usernameTV;
    TextView uniqueQRRankTV;
    TextView totalNumQRTV;
    TextView totalSumQRTV;
    TextView nameTV;
    TextView emailTV;
    TextView telTV;
    TextView streetAddressTV;
    ListView QRList;
    ArrayAdapter<QRGame> qrAdapter;
    ArrayList<QRGame> qrDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_external);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("externalUsername");
            actualUsername = extras.getString("userName");
        }
        initTV();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage userStorage = new UserStorage(db);
        userStorage.get(userName, (data)->{
            if(data!=null){
                name = (String) data.get("name");
                Log.d("ffwfwf",name);
                email = (String) data.get("email");
                phone = (String) data.get("phone");
                usernameTV.setText(userName);
                nameTV.setText(name);
                emailTV.setText(email);
                telTV.setText(phone);
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
                totalNumQRTV.setText("Total # of QR codes Ranking: "+String.valueOf(totalScanned));
                totalSumQRTV.setText("Total Sum of QR codes Ranking: "+String.valueOf(totalPoints));
            }
        }
        );
    }
    private void initTV (){
        usernameTV = findViewById(R.id.userNameTV);
        uniqueQRRankTV = findViewById(R.id.QRCodeRank) ;
        totalNumQRTV = findViewById(R.id.numQRCode) ;
        totalSumQRTV = findViewById(R.id.totalSumQR) ;
        nameTV = findViewById(R.id.nameTV);
        emailTV= findViewById(R.id.emailTV);
        telTV= findViewById(R.id.teleTV);
        nameTV.setText("Sample Name");
        QRList = findViewById(R.id.qrlist);
        qrDataList = new ArrayList<>();
        qrAdapter = new QRGameCustomList(this, qrDataList);
        QRList.setAdapter(qrAdapter);
        QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
        qrStorage.getCol().whereEqualTo("username", userName)
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    QRGameListActivity.getData(queryDocumentSnapshots, qrDataList, qrAdapter);
                });
        QRList.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(this, QrSummary.class);
            intent.putExtra("hexString", qrDataList.get(i).getHexString());
            intent.putExtra("username", actualUsername);
            startActivity(intent);
        });
    }
}
