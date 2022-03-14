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
    ListView commentList;
    ArrayAdapter<Comment> commentAdapter;
    ArrayList<Comment> commentContentList;
    String QRid;
    FirebaseFirestore db;
    //    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    FirebaseUser user = mAuth.getCurrentUser();
    String uid;



    Comment tempComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_content);
        commentList = findViewById(R.id.comment_list);
        commentContentList = new ArrayList<>();
        commentAdapter = new CustomComment(this, commentContentList);
        commentList.setAdapter(commentAdapter);
        db = FirebaseFirestore.getInstance();
        CollectionReference CommentCol = db.collection("Comments");
        final FloatingActionButton addCommentButton = findViewById(R.id.add_comment_button);//float button to add comment

        addCommentButton.setOnClickListener(new View.OnClickListener() {//pop up dialog when float button pressed
            @Override
            public void onClick(View view) {
                DialogFragment addComment = new AddCommentFragment();
                Bundle bundle = new Bundle();//use bundle to pass current Q hash and uid  into add comment fragment to generate comment instance
                String[] uidQid = {uid,QRid};
                bundle.putStringArray("uidQid",uidQid);
                //send current uid and qid into addCommentFragment
                addComment.setArguments(bundle);
                addComment.show(getSupportFragmentManager(),"add_comment");

            }
        });

        /**
         * Add a snapshot listener to query comments related to this QR
         */
        CommentCol
                .whereEqualTo("qrCommented",QRid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //add a snapshot tp query all comments related to current QRid
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        commentContentList.clear();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {//get data related to this QRid and construct them as comment object and add to local data list
                            String uid = (String) doc.getData().get("commenter");
                            String qid = (String) doc.getData().get("qrCommented");
                            String commentText = (String) doc.getData().get("comment");
                            String commentId = doc.getId();
                            commentContentList.add(new Comment(qid,uid,commentText,commentId));
                        }
                        commentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                    }
                });


        commentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {      //set up long click to delete selected comment
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int item, long l) {
                new AlertDialog.Builder(QrContentActivity.this)                         //When long click, pop up a dialog to prompt delete
                        .setMessage("Do you want to delete the Comment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                Comment selectedComment = commentContentList.get(item);
                                DocumentReference cmtRef = CommentCol.document(selectedComment.getCommentId());
                                commentContentList.remove(item);//remove selected item form data list
                                commentAdapter.notifyDataSetChanged();
                                cmtRef
                                        .delete()//delete document
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
                Log.d("UID", tempComment.getCommentId());
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
        commentContentList.add(newComment);                             //add new comment object into local data list
        Map<String, Object> comments = new HashMap<>();
        comments.put("commentDate",newComment.getDate());
        comments.put("qrCommented",newComment.getQid());
        comments.put("commenter",newComment.getUid());
        comments.put("comment",newComment.getComment());
        DocumentReference cmtRef = db.collection("Comments").document(newComment.getCommentId());
        cmtRef                                                           //push data into cloud
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