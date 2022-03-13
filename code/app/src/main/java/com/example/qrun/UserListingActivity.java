package com.example.qrun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserListingActivity extends AppCompatActivity {
    private EditText searchBar;
    private Button searchbut;
    private ListView userList;
    private ArrayAdapter<User> userAdapter;
    private ArrayList<User> userDataList;
    private UserStorage storage;
    private String username;
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
        searchbut = findViewById(R.id.searchbut);
        searchBar = findViewById(R.id.searchplayer);
        userList = findViewById(R.id.playerlist);
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
    }
}