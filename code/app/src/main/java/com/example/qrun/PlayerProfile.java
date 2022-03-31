package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


/**
 * User Profile Screen
 */
public class PlayerProfile extends AppCompatActivity {
    private String userName;
    private String email;
    private String phone;
    private String name;
    private TextView usernameTV;
    private TextView uniqueQRRankTV;
    private TextView totalNumQRTV;
    private TextView totalSumQRTV;
    private TextView totalNumRank, maxQRDisp, totalSumRank;
    private TextView nameTV;
    private TextView emailTV;
    private TextView telTV;
    private ImageView qrCodeImage;
    private long rankQR = 1, rankSum = 1, rankScanned = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initTV();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("userName");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage userStorage = new UserStorage(db);
        QRGenerator qrCodeGen = new QRGenerator();
        Bitmap qrGen= qrCodeGen.generateQRBitmap(userName,this);
        qrCodeImage=(ImageView) findViewById(R.id.qrCodeIV);
        qrCodeImage.setImageBitmap(qrGen);
        userStorage.get(userName, (data)->{
            if(data!=null){
                User user = new User(userName, data);
                name = user.getName();
                email = user.getEmail();
                phone = user.getPhoneNumber();
                usernameTV.setText(userName);
                nameTV.setText(name);
                emailTV.setText(email);
                telTV.setText(phone);
                maxQRDisp.setText("Unique QR max: " + String.valueOf(user.getUniqueqr()));
                totalNumQRTV.setText("Total # of QR codes: "+String.valueOf(user.getTotalscannedqr()));
                totalSumQRTV.setText("Total Sum of QR codes: "+String.valueOf(user.getTotalsum()));
                userStorage.getCol().get().addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        ArrayList<User> dataRanking = new ArrayList<>();
                        if(snapshot != null) {
                            for (QueryDocumentSnapshot document : snapshot) {
                                dataRanking.add(new User(document));
                            }
                        }
                        Collections.sort(dataRanking, (f1, f2) -> Long.compare(f2.getTotalscannedqr(), f1.getTotalscannedqr()));
                        for(User u : dataRanking) {
                            if(u.getUsername().compareTo(userName) == 0) {
                                break;
                            }
                            rankScanned++;
                        }
                        Collections.sort(dataRanking, (f1, f2) -> Long.compare(f2.getTotalsum(), f1.getTotalsum()));
                        for(User u : dataRanking) {
                            if(u.getUsername().compareTo(userName) == 0) {
                                break;
                            }
                            rankSum++;
                        }
                        Collections.sort(dataRanking, (f1, f2) -> Long.compare(f2.getUniqueqr(), f1.getUniqueqr()));
                        for(User u : dataRanking) {
                            if(u.getUsername().compareTo(userName) == 0) {
                                break;
                            }
                            rankQR++;
                        }
                        uniqueQRRankTV.setText("Unique QR Ranking: "+String.valueOf(rankQR));
                        totalNumRank.setText("Total Scanned Ranking: "+ String.valueOf(rankScanned));
                        totalSumRank.setText("Total Sum Ranking: " + String.valueOf(rankSum));
                    }
                });
            }
        }
        );
    }
    private void initTV (){
        usernameTV = findViewById(R.id.userNameTV);
        maxQRDisp = findViewById(R.id.QRCodeRank);
        totalNumQRTV = findViewById(R.id.numQRCode);
        totalSumQRTV = findViewById(R.id.totalSumQR);
        totalSumRank = findViewById(R.id.totalSumQR2);
        totalNumRank = findViewById(R.id.numQRCode2);
        uniqueQRRankTV = findViewById(R.id.QRCodeRank2);
        nameTV = findViewById(R.id.nameTV);
        emailTV= findViewById(R.id.emailTV);
        telTV= findViewById(R.id.teleTV);
        nameTV.setText("Gewgwegwegweg");
    }
}