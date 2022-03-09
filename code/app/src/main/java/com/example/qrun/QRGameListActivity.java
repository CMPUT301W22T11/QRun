package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class QRGameListActivity extends AppCompatActivity {
    ListView qrList;
    ArrayAdapter<QRGame> qrAdapter;
    ArrayList<QRGame> qrDataList;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgame_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }
        qrList = findViewById(R.id.qrgamelist);
        qrDataList = new ArrayList<>();
        qrAdapter = new QRGameCustomList(this, qrDataList);
        qrList.setAdapter(qrAdapter);
        QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
        qrStorage.getCol().whereEqualTo("username", username)
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
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
                });
    }
}