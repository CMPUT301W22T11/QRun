package com.example.qrun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class is similar to the PlayerProfile, however this is meant for displaying user profile
 * after searching from user listing activity
 */
public class UserProfileExternal extends AppCompatActivity {
    private String userName, actualUsername;
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
    private FloatingActionButton deleteAdmin;
    ListView QRList;
    User user;
    private Context ctx;
    ArrayAdapter<QRGame> qrAdapter;
    ArrayList<QRGame> qrDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_external);
        ctx = this;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName = extras.getString("externalUsername");
            actualUsername = extras.getString("userName");
        }
        initTV();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserStorage userStorage = new UserStorage(db);
        userStorage.get(actualUsername, (data) -> {
            if(data != null) {
                Object temp = data.get("isOwner");
                if(temp != null) {
                    boolean isOwner = (boolean) temp;
                    if(isOwner) {
                        deleteAdmin.setVisibility(View.VISIBLE);
                        deleteAdmin.setOnClickListener((l) ->{
                            AdminController controller = new AdminController();
                            if(user != null) {
                                controller.deleteUser(user, isSuccess -> {
                                    if(isSuccess) {
                                        Toast.makeText(ctx, "User Deletion Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(ctx, "User Deletion Failed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        userStorage.get(userName, (data)->{
            if(data != null){
                usernameTV.setText(userName);
                user = new User(userName, data);
                nameTV.setText(user.getName());
                emailTV.setText(user.getEmail());
                telTV.setText(user.getPhoneNumber());
                maxQRDisp.setText("Unique QR max: " + String.valueOf(user.getUniqueqr()));
                totalNumQRTV.setText("Total # of QR codes: "+String.valueOf(user.getTotalscannedqr()));
                totalSumQRTV.setText("Total Sum of QR codes: "+String.valueOf(user.getTotalsum()));
                userStorage.getCol().addSnapshotListener((task, error) -> {
                    if(task != null) {
                        QuerySnapshot snapshot = task;
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
                        uniqueQRRankTV.setText("Unique QR Rank: "+ String.valueOf(rankQR));
                        totalNumRank.setText("Total Scanned Rank: "+ String.valueOf(rankScanned));
                        totalSumRank.setText("Total Sum Rank: " + String.valueOf(rankSum));
                    }
                });
            }
        });
    }
    private void initTV(){
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
        deleteAdmin = findViewById(R.id.deleteUserAdmin);
//        streetAddressTV= findViewById(R.id.streetAddressTV);
        nameTV.setText("Gewgwegwegweg");
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
