package com.example.qrun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


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
    private void getData(QuerySnapshot queryDocumentSnapshots) {
        userDataList.clear();
        if(queryDocumentSnapshots != null) {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if(document.getId() != username) {
                    userDataList.add(new User(document));
                }
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
        userDataList = new ArrayList<>();
        userAdapter = new UserCustomList(this, userDataList);
        userList.setAdapter(userAdapter);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("userName");
        }

        storage = new UserStorage(FirebaseFirestore.getInstance());
        storage.getCol().addSnapshotListener((queryDocumentSnapshots, error) ->
        {
            getData(queryDocumentSnapshots);
        });
        // if search button is clicked
        searchbut.setOnClickListener((l) -> {
             String userName = searchBar.getText().toString();
             if(userName.compareTo("") == 0) {
                 storage.getCol().addSnapshotListener((queryDocumentSnapshots, error) -> {
                    getData(queryDocumentSnapshots);
                 });
             }
             else {
                 storage.getCol().document(userName).get().addOnCompleteListener((task) -> {
                     userDataList.clear();
                     if(task.isSuccessful()) {
                         userDataList.add(new User(task.getResult()));
                     }
                     userAdapter.notifyDataSetChanged();
                 });
             }
        });
        userList.setOnItemClickListener((adapter, view, i, l) -> {
            Intent intent = new Intent(this, UserProfileExternal.class);
            intent.putExtra("userName", username);
            intent.putExtra("externalUsername", userDataList.get(i).getUsername());
            startActivity(intent);
        });
        camBut.setOnClickListener((l) -> {
            Intent intent = new Intent(ctx, ScanningActivity.class);
            ac.launch(intent);
        });
    }
}