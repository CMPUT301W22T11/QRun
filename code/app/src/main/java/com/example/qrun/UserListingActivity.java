package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


/**
 * This is the user listing activity to display the user, as well as searching for the user
 */
public class UserListingActivity extends AppCompatActivity {
    private EditText searchBar;
    private Button searchbut;
    private ListView userList;
    private ArrayAdapter<User> userAdapter;
    private ArrayList<User> userDataList;
    private UserStorage storage;
    private String username;
    private FloatingActionButton camBut;
    private Context ctx;
    private Spinner choosingStuff;
    private int choose;
    private ListenerRegistration listener;
    private ActivityResultLauncher<Intent> ac = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is 1, then update the list
                    if(result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        String userName = (String) intent.getSerializableExtra("QR");
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        UserStorage store = new UserStorage(db);
                        store.get(userName, (check) -> {
                            if(check != null) {
                                Intent transfer = new Intent(ctx, UserProfileExternal.class);
                                transfer.putExtra("userName", username);
                                transfer.putExtra("externalUsername", userName);
                                startActivity(transfer);
                            }
                        });
                    }
                }
            }
    );
    private void getData(QuerySnapshot queryDocumentSnapshots, int ranking) {
        userDataList.clear();
        if(queryDocumentSnapshots != null) {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if(document.getId().compareTo(username) != 0) {
                    userDataList.add(new User(document));
                }
            }
            switch(ranking) {
                case 0:
                    Collections.sort(userDataList, (f1, f2) -> Long.compare(f2.getUniqueqr(), f1.getUniqueqr()));
                    break;
                case 1:
                    Collections.sort(userDataList, (f1, f2) -> Long.compare(f2.getTotalsum(), f1.getTotalsum()));
                    break;
                case 2:

                    Collections.sort(userDataList, (f1, f2) -> Long.compare(f2.getTotalscannedqr(), f1.getTotalscannedqr()));
                    break;
            }
            userAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);
        ctx = this;
        searchbut = findViewById(R.id.searchbut);
        searchBar = findViewById(R.id.searchplayer);
        userList = findViewById(R.id.playerlist);
        camBut = findViewById(R.id.camera_button);
        choosingStuff = findViewById(R.id.rankingId);
        ArrayAdapter<CharSequence> choosingAdapter = ArrayAdapter.createFromResource(this,
                R.array.rank, android.R.layout.simple_spinner_item);
        choosingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choosingStuff.setAdapter(choosingAdapter);
        choosingStuff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position,
                                       long id) {
                // TODO Auto-generated method stub
                choose = position;
                if(listener != null) listener.remove();
                listener = storage.getCol().addSnapshotListener((task, error) -> {
                    if(error != null) return;
                    getData(task, position);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        userDataList = new ArrayList<>();
        userAdapter = new UserCustomList(this, userDataList);
        userList.setAdapter(userAdapter);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }

        storage = new UserStorage(FirebaseFirestore.getInstance());
        // if search button is clicked
        searchbut.setOnClickListener((l) -> {
             String userName = searchBar.getText().toString();
             if(userName.compareTo("") == 0) {
                 if(listener != null) listener.remove();
                 listener = storage.getCol().addSnapshotListener((task, error) -> {
                     if(error != null) return;
                     getData(task, choose);
                 });
             }
             else {
                 if(listener != null) listener.remove();
                 listener = storage.getCol().document(userName).addSnapshotListener((task, error) -> {
                     if(error != null) return;
                     userDataList.clear();
                     if(task != null && task.exists()) {
                         userDataList.add(new User(task));
                     }
                     userAdapter.notifyDataSetChanged();
                 });
             }
        });
        userList.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(this, UserProfileExternal.class);
            intent.putExtra("userName", username);
            String userName = userDataList.get(i).getUsername();
            intent.putExtra("externalUsername", userName);
            startActivity(intent);
        });
        camBut.setOnClickListener((l) -> {
            Intent intent = new Intent(ctx, ScanningActivity.class);
            ac.launch(intent);
        });
    }
}