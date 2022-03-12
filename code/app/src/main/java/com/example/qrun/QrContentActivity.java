package com.example.qrun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import android.widget.Toast;

import com.example.qrun.Fragment.AddCommentFragment;
import com.example.qrun.Fragment.CommentViewFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrContentActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener, CommentViewFragment.OnFragmentInteractionListener {
    private TextView commentTextView;
    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentContentList;
    String QRid;
    FirebaseFirestore db;
//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    FirebaseUser user = mAuth.getCurrentUser();
    String uid ;//use user email as reference for now



    Comment tempComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commentList = findViewById(R.id.comment_list);
        commentContentList = new ArrayList<>();
        commentAdapter = new CustomComment(this, commentContentList);
        commentList.setAdapter(commentAdapter);
        db = FirebaseFirestore.getInstance();
        DocumentReference QrRef = db.collection("User").document(uid).collection("QrWallet").document(QRid);//use subcollection to store owned qid
        Map<String, ArrayList<String>> comments = new HashMap<>();
        final FloatingActionButton addCommentButton = findViewById(R.id.add_comment_button);//float button to add comment

        addCommentButton.setOnClickListener(new View.OnClickListener() {//pop up dialog when float button pressed
            @Override
            public void onClick(View view) {
                DialogFragment addComment = new AddCommentFragment();
                Bundle currentQr = new Bundle();//use bundle to pass current Q hash into add comment fragment to generate comment instance
                currentQr.putString("currentQr",QRid);
                addComment.setArguments(currentQr);//send current qid into addCommentFragment
                addComment.show(getSupportFragmentManager(),"add_comment");

            }
        });
        QrRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {//query Firestore and update local view
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                commentContentList.clear();                     //clear local data list
                ArrayList<String> commentText = new ArrayList<>();
                commentText = (ArrayList<String>) snapshot.getData().get("comments");//get data from cloud
                for(int i=0;i<commentText.size();i++){
                    Comment comment = new Comment(QRid, uid,commentText.get(i));
                    commentContentList.add(comment);                    //append cloud data into local data list
                }
                commentAdapter.notifyDataSetChanged();                  //update array adapter
            }
        });


        commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {      //set up long click to delete selected comment
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int item, long l) {
                new AlertDialog.Builder(QrContentActivity.this)                         //When long click, pop up a dialog to prompt delete
                        .setMessage("Do you want to delete the String?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (uid != commentContentList.get(item).getUid()) {             //check the whether user is trying to delete others' comment
                                    Toast.makeText(getApplicationContext(), "You can't delete other's comment", Toast.LENGTH_SHORT).show();
                                } else {
                                    ArrayList<String> tempCommentList = new ArrayList<>();      //a temporary container for comment text
                                    commentContentList.remove(item);//remove selected item form data list
                                    for(int j=0;j<commentContentList.size();j++){               //use for loop to append all remaining comment text into temp container
                                        tempCommentList.add(commentContentList.get(j).getComment());
                                    }
                                    comments.put("comments", tempCommentList);               //put into hash map
                                    QrRef
                                            .set(comments)//update firebase
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("comments updated", "Document is successfully added!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.w("comments update failed", "Error updating document", e);
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("No" , null).show();             //cancel deletion when No clicked
                return true;
            }
        });
        /**
         * This listener will bring up a dialog contains the comment user want to view
         * @author: Lucheng Zhou
         */
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //show a dialog with comment text in it when short clicked
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tempComment = commentAdapter.getItem(i);
                DialogFragment viewComment = new CommentViewFragment();
                Bundle commentBundle = new Bundle();        //bundle selected comment text
                commentBundle.putString("Comment", tempComment.getComment());
                viewComment.setArguments(commentBundle);    //these function will send selected comment text to view fragment
                viewComment.show(getSupportFragmentManager(),"view_comment");
            }
        });


    }

    @Override
    public void onOkPressed(Comment newComment) {
        ArrayList<String> tempCommentList = new ArrayList<>();          //set up a temp container for comment text
        commentContentList.add(newComment);                             //add new comment object into local data list
        for(int i=0;i<commentContentList.size();i++){                   //use for loop to extract comment text into temp container
            tempCommentList.add(commentContentList.get(i).getComment());
        }
        Map<String, ArrayList<String>> comments = new HashMap<>();
        comments.put("comments",tempCommentList);                   //put comment text into hashmap
        DocumentReference QrRef = db.collection("User").document(uid).collection("QrWallet").document(QRid);
        QrRef                                                           //push data into cloud
                .set(comments)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("comments updated", "Document is successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.w("comments update failed", "Error updating document", e);
                    }
                });
    }

    @Override
    public void onCancelPressed() {}


    @Override
    public void onFinishPressed() {

    }
}