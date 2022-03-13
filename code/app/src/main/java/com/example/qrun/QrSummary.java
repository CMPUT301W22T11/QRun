package com.example.qrun;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrun.databinding.ActivityQrSummaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QrSummary extends AppCompatActivity {
    private ActivityQrSummaryBinding binding;
    private FirebaseFirestore QrRun;
    private ArrayAdapter<String> sharedQrAdapter;
    private ArrayAdapter<String> commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String hexString = "";
        ArrayList<String> sharedUsers = new ArrayList<String>();
        QrRun.collection("QR").whereEqualTo("hexString", hexString ).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                if(document.exists()){
                                    String user = (String) document.getData().get("username");
                                    sharedUsers.add(user);
                                } else {
                                    Log.d("Storage get()", "No such document");
                                }
                            }
                        }else {
                            Log.d("Storage get()", "get failed with", task.getException());
                        }
                    }
                });

        sharedQrAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, sharedUsers);
        binding.sharedQrList.setAdapter(sharedQrAdapter);

        ArrayList commentList = new ArrayList<String>();
        commentAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, commentList);
        binding.commentList.setAdapter(commentAdapter);

//        TODO merge comment fragment
//        binding.addComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //add comment frag
//            }
//        });

//        binding.deleteQr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //delete functionality
//            }
//        });
    }


}