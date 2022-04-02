package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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
    Spinner isAdmin;
    boolean isUp = false;
    QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
    ListenerRegistration registration;
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
        isAdmin = findViewById(R.id.adminChoice);
        ArrayAdapter<CharSequence> choosingAdapter = ArrayAdapter.createFromResource(this,
                R.array.isAdmin, android.R.layout.simple_spinner_item);
        choosingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isAdmin.setAdapter(choosingAdapter);
        qrDataList = new ArrayList<>();
        qrAdapter = new QRGameCustomList(this, qrDataList);
        qrList.setAdapter(qrAdapter);
        new UserStorage(FirebaseFirestore.getInstance()).get(username, data -> {
            if(data != null) {
                Object temp = data.get("isOwner");
                if(temp != null) {
                    boolean isOwner = (boolean)temp;
                    if(isOwner) {
                        isAdmin.setVisibility(View.VISIBLE);
                        isAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v, int position,
                                                       long id) {
                                switch(position) {
                                    case 0:
                                        UserOperation();
                                        break;
                                    case 1:
                                        AdminOperation();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }
                        });
                    }
                    else {
                        UserOperation();
                    }
                }
                else {
                    UserOperation();
                }
            }
        });
        qrList.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(this, QrSummary.class);
            intent.putExtra("hexString", qrDataList.get(i).getHexString());
            intent.putExtra("username", username);
            startActivity(intent);
        });

    }
    private void UserOperation() {
        isUpButton.setOnClickListener((l) -> {
            if(isUp) {
                isUp = false;
                isUpButton.setText("Ascending");
                if(registration != null) registration.remove();
                registration = qrStorage.getCol().whereEqualTo("username", username)
                        .orderBy("points", Query.Direction.ASCENDING)
                        .addSnapshotListener((task, error) -> {
                            getData(task, qrDataList, qrAdapter);
                        });
            }
            else {
                isUp = true;
                isUpButton.setText("Descending");
                if(registration != null) registration.remove();
                registration = qrStorage.getCol().whereEqualTo("username", username)
                        .orderBy("points", Query.Direction.DESCENDING)
                        .addSnapshotListener((data, error) -> {
                            getData(data, qrDataList, qrAdapter);
                        });
            }
        });
        if(isUp) {
            isUpButton.setText("Descending");
            if(registration != null) registration.remove();
            registration = qrStorage.getCol().whereEqualTo("username", username)
                    .orderBy("points", Query.Direction.DESCENDING)
                    .addSnapshotListener((data, error) -> {
                        getData(data, qrDataList, qrAdapter);
                    });
        }
        else {
            isUpButton.setText("Ascending");
            if(registration != null) registration.remove();
            registration = qrStorage.getCol().whereEqualTo("username", username)
                    .orderBy("points", Query.Direction.ASCENDING)
                    .addSnapshotListener((data, error) -> {
                        getData(data, qrDataList, qrAdapter);
                    });
        }
    }

    private void AdminOperation() {
        isUpButton.setOnClickListener((l) -> {
            if(isUp) {
                isUp = false;
                isUpButton.setText("Ascending");
                if(registration != null) registration.remove();
                registration = qrStorage.getCol().orderBy("points", Query.Direction.ASCENDING)
                        .addSnapshotListener((task, error) -> {
                    getData(task, qrDataList, qrAdapter);
                });
            }
            else {
                isUp = true;
                isUpButton.setText("Descending");
                if(registration != null) registration.remove();
                registration = qrStorage.getCol().orderBy("points", Query.Direction.DESCENDING)
                        .addSnapshotListener((task, error) -> {
                    getData(task, qrDataList, qrAdapter);
                });
            }
        });

        if(isUp) {
            isUpButton.setText("Descending");
            if(registration != null) registration.remove();
            registration = qrStorage.getCol().orderBy("points", Query.Direction.DESCENDING)
                    .addSnapshotListener((data, error) -> {
                        getData(data, qrDataList, qrAdapter);
                    });
        }
        else {
            isUpButton.setText("Ascending");
            if(registration != null) registration.remove();
            registration = qrStorage.getCol().orderBy("points", Query.Direction.ASCENDING)
                    .addSnapshotListener((data, error) -> {
                        getData(data, qrDataList, qrAdapter);
                    });
        }
    }
}