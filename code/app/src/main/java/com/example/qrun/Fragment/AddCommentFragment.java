package com.example.qrun.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrun.Comment;
import com.example.qrun.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddCommentFragment extends DialogFragment {
//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    FirebaseUser user = mAuth.getCurrentUser();
    String uid;
    String QHash;
    private EditText QrComment;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Comment newComment);
        void onCancelPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_comment_fragment,null);
        QrComment =view.findViewById(R.id.comment_text);
        Bundle bundle = getArguments(); // receiving current qHash and user from QR content activity
        String[] info = bundle.getStringArray("uidQid");
        uid = info[0];
        QHash = info[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add your comment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String commentText = QrComment.getText().toString();
                        if (commentText.length()==0){
                            Toast.makeText(getActivity(), "Please input valid words", Toast.LENGTH_LONG).show();
                            listener.onCancelPressed();
                        }else{
                            Comment comment = new Comment(QHash, uid,commentText);
                            listener.onOkPressed(comment);
                        }
                    }}).create();
    }
}
