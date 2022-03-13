package com.example.qrun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CommentViewFragment extends DialogFragment {
    private TextView currentComment;
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener {
        void onFinishPressed();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommentViewFragment.OnFragmentInteractionListener){
            listener = (CommentViewFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.comment_view_fragment,null);
        Bundle commentBundle = getArguments();
        String viewingComment = commentBundle.getString("Comment");
        currentComment =view.findViewById(R.id.current_comment_text);
        currentComment.setText(viewingComment);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Comment")
                .setNegativeButton("Finish", null)
                .create();
    }
}