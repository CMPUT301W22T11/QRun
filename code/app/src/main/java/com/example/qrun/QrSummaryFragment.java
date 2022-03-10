package com.example.qrun;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qrun.databinding.QrSummaryFragmentBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class QrSummaryFragment extends Fragment {

    private QrSummaryFragmentBinding binding;

    private FirebaseFirestore QrRun;
    private Storage QrStore;
    private ArrayAdapter<String> sharedQrAdapter;
    private ArrayAdapter<String> commentAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        QrStore = new Storage(QrRun, "QR");

        binding = QrSummaryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            ArrayList sharedQrList = new ArrayList<String>();
            ArrayList commentList = new ArrayList<String>();

            sharedQrAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, sharedQrList);
            commentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_layout, commentList);

            binding.sharedQrList.setAdapter(sharedQrAdapter);
            binding.commentList.setAdapter(commentAdapter);

           binding.addComment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   // add comment fragment/activity
               }
           });

           binding.deleteQr.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //add delete stuff here
               }
           });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}