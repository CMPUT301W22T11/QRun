package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qrun.Fragment.AddCommentFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.qrun.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrContentActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    private TextView commentTextView;
    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentContentList;
    String QRid = "1231456";
    FirebaseFirestore db;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String userEmail = user.getEmail();//use user email as reference for now
    Map<String, Object> comments = new HashMap<>();
    DocumentReference QrRef = db.collection("QR").document(QRid);

    Comment tempComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_content);
        commentList = findViewById(R.id.comment_list);
        commentContentList = new ArrayList<>();
        commentAdapter = new CustomComment(this, commentContentList);
        db = FirebaseFirestore.getInstance();
        final CollectionReference qrContentReference = db.collection("QR");
        final FloatingActionButton addCommentButton = findViewById(R.id.add_city_button);

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddCommentFragment().show(getSupportFragmentManager(), "ADD_CITY");

            }
        });
        commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(QrContentActivity.this)
                        .setMessage("Do you want to delete the String?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                tempComment=commentContentList.get(i);
                                commentContentList.remove(i);
                                comments.put("comments",commentContentList);
                                QrRef
                                        .update(comments)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("comments updated", "Document is successfully added!");
                                                commentAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.w("comments update failed", "Error updating document", e);
                                                commentContentList.add(tempComment);
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No" , null).show();
                return true;
            }
        });


    }

    @Override
    public void onOkPressed(Comment newComment) {
        tempComment = newComment;
        commentContentList.add(newComment);
        comments.put("comments",commentContentList);
        QrRef
                .update(comments)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("comments updated", "Document is successfully added!");
                        commentAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.w("comments update failed", "Error updating document", e);
                        commentContentList.remove(tempComment);
                    }
                });
    }

    @Override
    public void onCancelPressed() {}
}