package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


/**
 * User Profile Screen
 */
public class PlayerProfile extends AppCompatActivity {
    private String userName;
    private String email;
    private String phone;
    private String name;
    private Integer totalQrRank;
    private Integer uniqueQrRank;
    private Integer sumQrRank;
    private User curUser;
    private UserStorage storage;
    private TextView usernameTV;
    private TextView uniqueQRRankTV;
    private TextView totalNumQRRankTV;
    private TextView totalSumQRTV;
    private TextView nameTV;
    private TextView emailTV;
    private TextView telTV;
    private ImageView qrCodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        getRanking();
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
                curUser = (User) data;
                name = (String) data.get("name");
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
                uniqueQRRankTV.setText("Unique Qr scanned: "+ String.valueOf(totalPoints)+" Ranking: "+String.valueOf(uniqueQrRank));
                totalNumQRRankTV.setText("# of Qr scanned: "+ String.valueOf(totalScanned)+" Ranking: "+String.valueOf(totalQrRank));
                totalSumQRTV.setText("Sum of QR codes scanned: "+ String.valueOf(totalPoints)+" Ranking: "+String.valueOf(sumQrRank));
            }
            else{Log.d("PlayerProfile class","Retrieval failure");}
        }
        );
    }


    private void getRanking(){
        storage.sortBy("totalpoints", (data)->{
            if(data!=null){
                totalQrRank = data.indexOf(curUser);
            }
            else{Log.d("PlayerProfile class","Retrieval failure");}
            }
        );

        storage.sortBy("totalscannedqr", (data)->{
            if(data!=null){
                sumQrRank = data.indexOf(curUser);
            }
            else{Log.d("PlayerProfile class","Retrieval failure");}
            }
        );

        storage.sortBy("totalscannedqr", (data)->{
            if(data!=null){
                uniqueQrRank = data.indexOf(curUser);
            }
            else{Log.d("PlayerProfile class","Retrieval failure");}
            }
        );

    }

    private void initTV (){
        usernameTV = findViewById(R.id.userNameTV);
        uniqueQRRankTV = findViewById(R.id.QRCodeRank) ;
        totalNumQRRankTV = findViewById(R.id.numQRCode) ;
        totalSumQRTV = findViewById(R.id.totalSumQR) ;
        nameTV = findViewById(R.id.nameTV);
        emailTV= findViewById(R.id.emailTV);
        telTV= findViewById(R.id.teleTV);
        nameTV.setText("Sample Name");
    }
}