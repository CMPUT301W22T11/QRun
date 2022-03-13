package com.example.qrun;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.qrun.databinding.ActivityQrSummaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QrSummary extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener,
CommentViewFragment.OnFragmentInteractionListener {
    private ActivityQrSummaryBinding binding;
    private ArrayAdapter<String> sharedQrAdapter;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentContentList;
    private String hexString;
    private String username;
    private Comment tempComment;
    private CommentStorage commentStorage;
    private QRGame tempQR = null;
    private Context ctx = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrSummaryBinding.inflate(getLayoutInflater());
        commentStorage = new CommentStorage(FirebaseFirestore.getInstance());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            hexString = extras.getString("hexString");
            username = extras.getString("username");
        }
        ctx = this;
        ArrayList<String> sharedUsers = new ArrayList<>();
        sharedQrAdapter = new ArrayAdapter<String>(this, R.layout.list_layout, sharedUsers);
        binding.sharedQrList.setAdapter(sharedQrAdapter);
        commentContentList = new ArrayList<>();
        commentAdapter = new CustomComment(this, commentContentList);
        binding.commentList.setAdapter(commentAdapter);
        QRStorage QrRun = new QRStorage(FirebaseFirestore.getInstance());
        QrRun.getCol().whereEqualTo("hexString", hexString).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            sharedUsers.clear();
                            for(DocumentSnapshot document : task.getResult()){
                                String points = "Points: " + String.valueOf((long)document.getData().get("points"));
                                binding.points.setText(points);
                                if(document.exists()){
                                    String user = (String) document.getData().get("username");
                                    if(user.compareTo(username) != 0) {
                                        sharedUsers.add(user);
                                    }
                                } else {
                                    Log.d("Storage get()", "No such document");
                                }
                            }
                            sharedQrAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Storage get()", "get failed with", task.getException());
                        }
                    }
                });
        commentStorage.getCol().whereEqualTo("qrCommented",hexString)
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
        binding.commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //show a dialog with comment text in it when short clicked
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
        binding.addComment.setOnClickListener(new View.OnClickListener() {//pop up dialog when float button pressed
            @Override
            public void onClick(View view) {
                DialogFragment addComment = new AddCommentFragment();
                Bundle bundle = new Bundle();//use bundle to pass current Q hash and uid  into add comment fragment to generate comment instance
                String[] uidQid = {username,hexString};
                bundle.putStringArray("uidQid",uidQid);
                //send current uid and qid into addCommentFragment
                addComment.setArguments(bundle);
                addComment.show(getSupportFragmentManager(),"add_comment");

            }
        });
        QRStorage qrStorage = new QRStorage(FirebaseFirestore.getInstance());
        qrStorage.getCol().whereEqualTo("hexString", hexString)
                .whereEqualTo("username", username)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.size() > 0) {
                        tempQR = new QRGame(queryDocumentSnapshots.getDocuments().get(0));
                        binding.deleteQr.setVisibility(View.VISIBLE);
                        binding.deleteQr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                qrStorage.delete(tempQR, isSuccess -> {
                                    Toast.makeText(ctx, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                            }
                        });
                    }

                });

    }
    public void onOkPressed(Comment newComment) {
        commentContentList.add(newComment);                             //add new comment object into local data list
        HashMap<String, Object> comments = new HashMap<>();
        comments.put("commentDate", newComment.getDate());
        comments.put("qrCommented", newComment.getQid());
        comments.put("commenter", newComment.getUid());
        comments.put("comment", newComment.getComment());

        commentStorage.add(comments, (isDone) -> {
            if(isDone) {
                Toast.makeText(this, "Comment Added!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Unable to add Comment", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCancelPressed() {}


    @Override
    public void onFinishPressed() {

    }
}