package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerProfile extends AppCompatActivity {
    String userName;
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
    ImageView qrCodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initTV();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("userName");
//            Log.d("xx",userName);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage userStorage = new UserStorage(db);

        QRGenerator qrCodeGen = new QRGenerator();
        Bitmap qrGen= qrCodeGen.generateQRBitmap(userName,this);
        qrCodeImage=(ImageView) findViewById(R.id.qrCodeIV);
        qrCodeImage.setImageBitmap(qrGen);

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
    public void initTV (){
        usernameTV = findViewById(R.id.userNameTV);

        uniqueQRRankTV = findViewById(R.id.QRCodeRank) ;
        totalNumQRTV = findViewById(R.id.numQRCode) ;
        totalSumQRTV = findViewById(R.id.totalSumQR) ;
        nameTV = findViewById(R.id.nameTV);
        emailTV= findViewById(R.id.emailTV);
        telTV= findViewById(R.id.teleTV);
        streetAddressTV= findViewById(R.id.streetAddressTV);
        nameTV.setText("Gewgwegwegweg");
    }
}