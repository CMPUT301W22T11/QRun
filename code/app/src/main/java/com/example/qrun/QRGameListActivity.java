package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the screen to display all QR Game list for a user
 */
public class QRGameListActivity extends AppCompatActivity {
    ListView qrList;
    ArrayAdapter<QRGame> qrAdapter;
    ArrayList<QRGame> qrDataList;
    String username;
    Button isUpButton;
    boolean isUp = false;

    /**
     * get data and add it to the QR list, then notify the adapter
     * @param queryDocumentSnapshots taken from the Firestore
     * @param qrDataList the array lists contains the QR code
     * @param qrAdapter
     */
    public static void getData(QuerySnapshot queryDocumentSnapshots, ArrayList<QRGame> qrDataList,
                                ArrayAdapter<QRGame> qrAdapter) {
        qrDataList.clear();
        if(queryDocumentSnapshots != null) {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                boolean isExist = false;
                String hex = (String) document.get("hexString");
                for(QRGame game : qrDataList) {
                    if(game.getHexString().equals(hex)) {
                        isExist = true;
                        break;
                    }
                }
                if(!isExist) {
                    qrDataList.add(new QRGame(document));
                }
            }
            qrAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUp = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgame_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }
        qrList = findViewById(R.id.qrgamelist);
        isUpButton = findViewById(R.id.sortingbut);
        qrDataList = new ArrayList<>();
        qrAdapter = new QRGameCustomList(this, qrDataList);
        qrList.setAdapter(qrAdapter);
        isUpButton.setText("Descending");
        isUpButton.setOnClickListener((l) -> {
            QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
            if(isUp) {
                isUp = false;
                isUpButton.setText("Ascending");
                qrStorage.getCol().whereEqualTo("username", username)
                        .orderBy("points", Query.Direction.ASCENDING)
                        .addSnapshotListener((queryDocumentSnapshots, error) -> {
                            getData(queryDocumentSnapshots, qrDataList, qrAdapter);
                        });
            }
            else {
                isUp = true;
                isUpButton.setText("Descending");
                qrStorage.getCol().whereEqualTo("username", username)
                        .orderBy("points", Query.Direction.DESCENDING)
                        .addSnapshotListener((queryDocumentSnapshots, error) -> {
                            getData(queryDocumentSnapshots, qrDataList, qrAdapter);
                        });
            }
        });
        qrList.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(this, QrSummary.class);
            intent.putExtra("hexString", qrDataList.get(i).getHexString());
            intent.putExtra("username", username);
            startActivity(intent);
        });
        QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
        qrStorage.getCol().whereEqualTo("username", username)
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    getData(queryDocumentSnapshots, qrDataList, qrAdapter);
                });
    }
}